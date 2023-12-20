import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {environment} from '../../../environments/environment';
import {Dataset} from "../../models/dataset";
import {Datapoint} from "../../models/datapoint";
import { createHttpParams } from "../util/http-params";

@Injectable({
  providedIn: 'root'
})

export class ChartService {
  constructor(private _http: HttpClient) {}

  public plot(filter?: { pillar?: string, faculty?: string } ): Observable<Dataset[]> {
    const params: HttpParams = createHttpParams({ pillar: filter.pillar, faculty: filter.faculty });

    return this._http.get(
      `${environment.apiUrl}/answer`,
      { params }
    ).pipe(
      map(
          responseData => this.convertAnswersToChartData(responseData)),  // transform data when there is a proper return
                catchError(this.handleError)  // catch error when it occurs
    );
  }

  // converts the response (answers) from API call into chart data, by converting it to Datasets with Datapoints
  private convertAnswersToChartData(responseData: {}) {
    let convertedResponseData: Dataset[] = []

    for (const key in responseData) {
      const answer = (responseData as any)[key]
      const score: number = answer[0]
      const date: string = parseInt(answer[1]).toString() // use parseInt to only get the first part of the date (year)
      const name: string = answer[2]

      // if there is no Dataset created for the given name create one
      if (convertedResponseData[name] == undefined) convertedResponseData[name] = new Dataset(name, [])

      // add all of the answers to the corresponding name
      convertedResponseData[name].data.push(new Datapoint(date, score))

      // group the data so that all the scores from the same year are grouped
      const groupedDataSet: {} = this.groupDataPoints(convertedResponseData[name].data)

      // set the data for the given faculty to the averaged data set
      convertedResponseData[name].data = this.createAverageDataSet(groupedDataSet)
    }

    return convertedResponseData;
  }

  // groups the datapoint(s) in a dataset together and calculates the sums while tracking a count
  private groupDataPoints(data: Datapoint[]) {
    return data.reduce((merger: {}, datapoint: Datapoint) => {
      // if the x value is not present create a new object with that x value
      if (!merger[datapoint.x]) {
        // create a new object that has a count included
        merger[datapoint.x] = {...datapoint, count: 1}
        return merger
      }

      merger[datapoint.x].y += datapoint.y    // sum the y values of the given Datapoint
      merger[datapoint.x].count += 1          // keep track of the amount of times the Datapoint(s) have been grouped

      return merger // return the transformed object
      }, {} // empty in order to pick first value in array as the initial value
    )
  }

  // computes the average for the datapoint(s) y values for a given dataset
  private createAverageDataSet(data: {}) {
    return Object.keys(data).map(k => new Datapoint(data[k].x, data[k].y / data[k].count))
  }

  private handleError(error: HttpErrorResponse) {
    let message: string;
    console.log(error);
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      message = error.error.message;
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      if(error.status === 0) {
        message = 'backend error: status: ' + error.message;
      } else {
        message = 'backend error: status: ' + error.status + ' - ' + error.statusText;
      }
    }
    // return an observable with a user-facing error message
    return throwError(message);
  };


}
