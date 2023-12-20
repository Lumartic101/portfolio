import { TestBed } from '@angular/core/testing';

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { QuestionnaireService } from "./questionnaire.service";
import { Questionnaire } from "../../models/questionnaire/questionnaire.model";
import { environment } from "../../../environments/environment";

describe('QuestionnaireService', () => {
  let service: QuestionnaireService;
  let httpMock: HttpTestingController;

  let BASE_URL: string = `${environment.apiUrl}/questionnaire`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(QuestionnaireService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('01: Should return a valid questionnaire object', () => {

    const dummyData = [
      {
        "id": 1,
        "name": "DRAFT Dashboard onderwijs 0.9",
        "startDate": "2022-02-15T00:00:00.000+00:00",
        "finishDate": "2023-03-15T00:00:00.000+00:00",
        "status": "CONCEPT",
        "pillar": {
          "id": 1,
          "name": "Onderwijs"
        }
      }
    ];

    service.getById(1).subscribe((response: Questionnaire) => {
      expect(response.id).toEqual(1);
      expect(response.pillar.name).toEqual('Onderwijs');
    });

    const request = httpMock.expectOne(
      BASE_URL+"/1");
    expect(request.request.method).toBe('GET');

    request.flush(dummyData);
  });

  it('02: Should throw an exception because there is no questionnaire with said id', () => {

    const dummyData = [];

    service.getById(0).subscribe( (response: Questionnaire) => {}, (error) => {
      expect(error).toBeTruthy();
    });

    const request = httpMock.expectOne(
      BASE_URL+"/0");
    expect(request.request.method).toBe('GET');

    request.flush({
      type: 'ERROR',
      status: 400
    });
  });
});
