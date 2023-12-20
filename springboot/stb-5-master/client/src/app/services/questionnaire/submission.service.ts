import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SubmissionService {
  private static BASE_URL: string = `${environment.apiUrl}/answer`

  constructor(private httpClient:HttpClient) { }

  submit(score: number, userId: number, questionnaireId: number): Promise<any> {
    let submission = {
      score: score.toFixed(1),
      questionnaireId,
    }
    const config = {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    }
    return this.httpClient.post(SubmissionService.BASE_URL, submission, config).toPromise();
  }
}
