import { Injectable } from '@angular/core';
import {environment} from "../../../environments/environment";
import {Observable, throwError} from "rxjs";
import {Education} from "../../models/education";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {User} from "../../models/user";
import {map} from "rxjs/operators";
import {ErrorHandling} from "../util/ErrorHandling";

@Injectable({
  providedIn: 'root'
})
export class EducationSbService {
  private educations: Education[] = []
  private BASE_URL: string = `${environment.apiUrl}`;

  constructor(private httpClient: HttpClient) {
    this.restGetEducations().subscribe(
      responseDate => this.educations = responseDate,
      error => ErrorHandling.handleError(error)
    )
  }

  private restGetEducations(): Observable<Education[]>{
    return this.httpClient.get<Education[]>(`${this.BASE_URL}/education`)
      .pipe(map(responseData => {
        console.log(responseData)
        const educationsArray: Education[] = [];
        for (const key in responseData){
          educationsArray.push(responseData[key])
        }
        console.log("initial list", educationsArray)
        return educationsArray;
      })
      );
  }

  findAll(): Education[] {
    return this.educations;
  }
}
