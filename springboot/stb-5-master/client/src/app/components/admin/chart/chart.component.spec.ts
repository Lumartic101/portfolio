import {ComponentFixture, TestBed} from "@angular/core/testing";
import {HttpClientModule} from "@angular/common/http";
import {ChartComponent} from "./chart.component";
import {NgChartsModule} from "ng2-charts";
import {FormsModule} from "@angular/forms";

import 'chartjs-adapter-moment';
import {FacultySbService} from "../../../services/users/faculty-sb.service";
import {Faculty} from "../../../models/faculty";
import {PillarService} from "../../../services/questionnaire/pillar.service";
import {Pillar} from "../../../models/questionnaire/pillar.model";
import {ChartService} from "../../../services/chart/chart.service";
import {Dataset} from "../../../models/dataset";
import {Datapoint} from "../../../models/datapoint";
import {of} from "rxjs";

describe('ChartComponent', () => {
  let component: ChartComponent;
  let componentHtml: HTMLElement;
  let fixture: ComponentFixture<ChartComponent>;

  let fakeFacultyService: FacultySbService;
  let fakePillarService: PillarService;
  let fakeChartService: ChartService;

  beforeEach(async () => {
    const faculties: Faculty[] = [
      new Faculty(1, "FBSV"),
      new Faculty(2, "FBE"),
      new Faculty(3, "FG"),
      new Faculty(4, "FMR"),
      new Faculty(5, "FDMCI"),
      new Faculty(6, "FOO"),
      new Faculty(7, "FT"),
    ]

    const pillars: Pillar[] = [
      new Pillar(1, "Onderwijs", null),
      new Pillar(1, "Bedrijfsvoering", null)
    ]

    const datasets: Dataset[] = [
      new Dataset("FBE", [new Datapoint("2022", 3.5), new Datapoint("2023", 4)] ),
      new Dataset("FBSV", [new Datapoint("2022", 1.5)] ),
      new Dataset("FDMCI", [new Datapoint("2022", 3)] ),
      new Dataset("FG", [new Datapoint("2022", 2)] ),
      new Dataset("FMR", [new Datapoint("2022", 4)] ),
      new Dataset("FOO", [new Datapoint("2022", 2)] ),
      new Dataset("FT", [new Datapoint("2022", 3)] ),
    ]

    // create the fake service, with all of the methods used by this component
    fakeFacultyService = jasmine.createSpyObj<FacultySbService>('FacultySbService',
      { findAll: faculties }
    );

    // create the fake service, with all of the methods used by this component
    fakePillarService = jasmine.createSpyObj<PillarService>('PillarService',
      { getAll: pillars }
    );

    // create the fake service, with all of the methods used by this component
    fakeChartService = jasmine.createSpyObj<ChartService>('ChartService',
      { plot: of(datasets) }  // use 'of' because we want to return an Observable
    );

    await TestBed.configureTestingModule({
      declarations: [ ChartComponent ],
      // use fake services instead of original services
      providers: [
        { provide: FacultySbService, useValue: fakeFacultyService },
        { provide: PillarService, useValue: fakePillarService },
        { provide: ChartService, useValue: fakeChartService }
      ],
      // configure all the dependencies that need to be injected in order for this component to work as intended
      imports: [
        HttpClientModule,
        NgChartsModule,
        FormsModule
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChartComponent);  // create the chart component
    component = fixture.componentInstance;              // store the instance of the root component class
    componentHtml = fixture.debugElement.nativeElement; // store the DOM element at the root of the component

    fixture.detectChanges();  // wait for initialization to complete
  });

  afterEach(() => {
    expect(fakeChartService.plot).toHaveBeenCalled();
  })

  it('should display the faculty in the chart title when selecting from dropdown', () => {
    // get the UI properties from the DOM (Arrange)
    const dropdownToggle: HTMLButtonElement = componentHtml.querySelector('#facultiesDropdownMenu');
    const radioButtonInput: HTMLInputElement = componentHtml.querySelector('#radio-faculty-5');
    const selectedFaculty: HTMLElement = componentHtml.querySelector('#selectedFaculty');

    // select the option from the dropdown (Act)
    dropdownToggle.click(); // open the dropdown
    fixture.detectChanges();  // action to DOM should be completed
    radioButtonInput.dispatchEvent(new Event('change'));  // change the value of radio button
    fixture.detectChanges();  // action to DOM should be completed

    // check if the title, and component values were updated correctly (Assert)
    expect(fakeFacultyService.findAll).toHaveBeenCalled();                  // check if the service has been called
    expect(selectedFaculty.textContent).toEqual('Faculty: FDMCI');  // check the title content
    expect(component.selectedFaculty).toEqual('FDMCI');             // check the component value
    expect(component.info).toEqual({ faculty: 'FDMCI'} );           // check the if value was updated with selection
  });

  it('should display the pillar in the chart title when selecting from dropdown', () => {
    // get the UI properties from the DOM (Arrange)
    const dropdownToggle: HTMLButtonElement = componentHtml.querySelector('#pillarsDropdownMenu');
    const radioButtonInput: HTMLInputElement = componentHtml.querySelector('#radio-pillar-1');
    const selectedPillar: HTMLElement = componentHtml.querySelector('#selectedPillar');

    // select the option from the dropdown (Act)
    dropdownToggle.click(); // open the dropdown
    fixture.detectChanges();  // action to DOM should be completed
    radioButtonInput.dispatchEvent(new Event('change'));  // change the value of radio button
    fixture.detectChanges();  // action to DOM should be completed

    // check if the title, and component values were updated correctly (Assert)
    expect(fakePillarService.getAll).toHaveBeenCalled();                      // check if the service has been called
    expect(selectedPillar.textContent).toEqual('Pillar: Onderwijs');  // check the title content
    expect(component.selectedPillar).toEqual('Onderwijs');            // check the component value
    expect(component.info).toEqual({ pillar: 'Onderwijs'} );          // check the if value was updated with selection
  });

  it('should change chart type when selecting from dropdown', () => {
    // get the UI properties from the DOM (Arrange)
    const dropdownToggle: HTMLButtonElement = componentHtml.querySelector('#chartTypesDropdownMenu');
    const radioButtonInput: HTMLInputElement = componentHtml.querySelector('#radio-chart-bar');

    // select the option from the dropdown (Act)
    dropdownToggle.click(); // open the dropdown
    fixture.detectChanges();  // action to DOM should be completed
    radioButtonInput.dispatchEvent(new Event('change'));  // change the value of radio button
    fixture.detectChanges();  // action to DOM should be completed

    // check if the chart type has changed (Assert)
    expect(component.chartType).toEqual('bar'); // check the component value
  });

  it('should have data displayed within the chart', () => {
    // check if the service has been called and if data that is present (Assert)
    // only Assert because data has already been arranged in beforeEach() and Act has been performed on initial render.
    expect(fakePillarService.getAll).toHaveBeenCalled();                // check if the service has been called
    expect(component.chart.datasets).not.toEqual(null);         // check if the datasets are present in chart
    expect(component.chart.labels).not.toEqual(null);           // check if the year labels are present in chart
  });

});
