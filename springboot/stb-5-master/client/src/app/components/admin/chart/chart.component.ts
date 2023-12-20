import {Component, OnInit, ViewChild} from '@angular/core';
import { ChartConfiguration, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import {ChartService} from "../../../services/chart/chart.service";
import {Dataset} from "../../../models/dataset";
import {Datapoint} from "../../../models/datapoint";
import {FacultySbService} from "../../../services/users/faculty-sb.service";
import {Faculty} from "../../../models/faculty";
import {HttpErrorResponse} from "@angular/common/http";
import {PillarService} from "../../../services/questionnaire/pillar.service";

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss'],
})
export class ChartComponent implements OnInit {
  public selectedPillar: string = "All";
  public selectedFaculty: string = "All";

  public errorMessage: any;

  private HVA_COLOR_HEX: string = '#25167A';

  // all the info regarding the selected filters
  public info: any = {}

  get pillars() {
    return this.pillarService.getAll()
  }

  get faculties(): Faculty[] {
    return this.facultyService.findAll()
  }

  // Set the available chart types here in order for them to be an option in the selection of chart types
  public chartTypes = [
    { name: "line" },
    { name: "bar" },
  ]

  // Setup and define chart configuration
  public chartData: ChartConfiguration<"line", Datapoint[]>['data'] = {
      // Use this empty structure as a placeholder for dynamic structuring.
      labels: [],
      datasets: []
  };

  // Setup and define chart plugins, the way the chart is being displayed and behaves
  public chartOptions: ChartConfiguration['options'] = {
    elements: {
      line: {
        tension: 0.25 // creates smoother lines
      }
    },
    scales: {
      x: {
        display: true,
        title: {
          display: true,
          text: 'Year',
          color: this.HVA_COLOR_HEX,
          font: {
            size: 15,
            weight: 'bold'
          }
        },
        type: 'time',
        time: {
          unit: 'year'
        }
      },
      y: {
        display: true,
        title: {
          display: true,
          text: 'Score',
          color: this.HVA_COLOR_HEX,
          font: {
            size: 15,
            weight: 'bold'
          }
        },
        max: 5.0,
        min: 0.0,
        beginAtZero: true
      }
    }
  };

  public chartType: ChartType = 'line';

  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;

  constructor(private facultyService: FacultySbService, private pillarService: PillarService, private questionnaireResultsServiceSb: ChartService) {}

  ngOnInit(): void {
    // on initial render plot the chart by retrieving data from the service and then plotting it to the chart
    this.questionnaireResultsServiceSb.plot({})
      .subscribe(
        responseData => this.updateChartWithData(responseData),
        error => this.errorMessageHandling(error)
      )
  }

  public filterChart(info: { pillar?: string, faculty?: string }): void {
    // update the chart title to the chosen pillar and/or faculty
    this.selectedPillar = info.pillar != undefined ? info.pillar.charAt(0).toUpperCase() + info.pillar.substring(1) : "All";
    this.selectedFaculty = info.faculty != undefined ? info.faculty.toUpperCase() : "All";

    // plot the chart with the retrieved data from service using the given pillar
    this.questionnaireResultsServiceSb.plot(info)
      .subscribe(
        responseData => this.updateChartWithData(responseData),
        error => this.errorMessageHandling(error)
      )
  }

  public updateChartWithData(data: Dataset[]): void {
    const dates = this.getDatesFromDataSet(data);

    let averageDataSet = []
    // create a average line from using the dates from the dataset, and the original data
    for (let i = 0; i < dates.length; i++) {
      averageDataSet.push({x: dates[i], y: this.calculateAveragePerDataSet(Object.values(data), i)})
    }

    this.chartData.datasets = Object.values(data); // add the retrieved data to the Chart dataset
    this.chartData.labels = dates // add the dates to the Chart labels
    // add the average data to the Chart dataset
    this.createAverageLine(averageDataSet, this.info.faculty == undefined || this.info.faculty == "all" ? "HvA" : this.info.faculty.toUpperCase());
    this.chart?.update(); // update the chart in order to set the new data in chart
  }

  private calculateAveragePerDataSet(data: Dataset[], index: number): number {
    const ONE_DECIMAL_MULTIPLIER: number = 10;
    let totalScorePerSet: number = 0.0;
    let totalValuesPerSet: number = 0;

    data.map((set: Dataset) => {
      for (const key in set.data) {
        // if the value is valid (not undefined / NaN) then add it to the totals
        if(!isNaN(set.data[index]?.y)) {
          totalValuesPerSet++;
          totalScorePerSet += set.data[index]?.y;  // add the y value to the total
        }
      }
    })

    // calculate the average which is the total amount of value divided by the amount of values
    // set the average, and round it to one digit after the decimal
    return Math.round(totalScorePerSet / totalValuesPerSet * ONE_DECIMAL_MULTIPLIER) / ONE_DECIMAL_MULTIPLIER;
  }

  private getDatesFromDataSet(dataSet: { data: Datapoint[] }[]): string[] {
    let dates: string[] = []

    // push the date from each data point into the dates array
    Object.values(dataSet)[0].data.map(data => dates.push(data.x))

    return dates;
  }

  private createAverageLine(dataSet: Datapoint[], label: string): void {
    this.chartData.datasets.push(
      { data: dataSet,
        label: label,
        borderColor: this.HVA_COLOR_HEX,
        backgroundColor: this.HVA_COLOR_HEX,
        pointBackgroundColor: this.HVA_COLOR_HEX,
        pointHoverBorderColor: this.HVA_COLOR_HEX,
        hoverBackgroundColor: this.HVA_COLOR_HEX,
        borderDash: [5] }
      );
  }

  public changeChartType(type: string): void {
    switch (type) {
      case "line":
        this.chartType = 'line'
        break;
      case "bar":
        this.chartType = 'bar'
        break;
      default:
        this.chartType = 'line'
        break;
    }

    this.chart?.update(); // update the chart in order to set the new chart type in chart
  }

  errorMessageHandling(error: HttpErrorResponse) {
    this.errorMessage = `Something went wrong!<br>`

    switch (error.status) {
      case 500:
        this.errorMessage += 'Please try again later'
        break;
    }
  }

}
