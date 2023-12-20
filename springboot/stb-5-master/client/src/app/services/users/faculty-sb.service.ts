import { Injectable } from '@angular/core';
import {Faculty} from "../../models/faculty";
import {environment} from "../../../environments/environment";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {ErrorHandling} from "../util/ErrorHandling";

@Injectable({
  providedIn: 'root'
})
export class FacultySbService {

  private faculties: Faculty[] =[]
  private BASE_URL: string = `${environment.apiUrl}`;

  constructor(private httpClient: HttpClient) {
    this.restGetFaculties().subscribe(
      responseDate => this.faculties = responseDate,
      error => ErrorHandling.handleError(error)
    )
  }

  private restGetFaculties(): Observable<Faculty[]>{
    return this.httpClient.get<Faculty[]>(`${this.BASE_URL}/faculty`)
  }

  findAll(): Faculty[] {
    return this.faculties
  }

}
