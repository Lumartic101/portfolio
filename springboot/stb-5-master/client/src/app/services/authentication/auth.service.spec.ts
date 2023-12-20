import {async, getTestBed, TestBed} from '@angular/core/testing';

import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {AuthService} from "./auth.service";
import {User} from "../../models/user";
import {environment} from "../../../environments/environment";
import {HttpResponse} from "@angular/common/http";

describe('Auth service login', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let BASE_URL: string = `${environment.apiUrl}`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('Example 05: should return a valid authorizated user object', () => {
    // setting the dummy data
    const dummyData = [
      {
        "_id": 5,
        "_email": "luka@gmail.com",
        "_password": "123",
        "_firstName": "Luka",
        "_lastName": "Martinovic",
        "_education": "Phylosofiy",
        "_education_id": 1,
        "_role": 1
      }
    ];
    // doing the auth of the service and checking if has all the values
    service.auth("luka@gmail.com", "123").subscribe((response: HttpResponse<User>) => {
      console.log(response.body[0]._password);
      expect(response.body[0]._email).toEqual("luka@gmail.com");
      expect(response.body[0]._lastName).toEqual("Martinovic");
    });

    const req = httpMock.expectOne(
      BASE_URL + '/authentication');
    expect(req.request.method).toBe('POST');
    // Flushed the call with dummydata
    req.flush(dummyData);
  });

  it('Example 06: should generate an exception due to wrong argument', () => {
    // setting the dummy data
    const dummyData = [];

    // doing the auth of the service and checking if has a error
    service.auth('keesvanderspek@gmail.com', 'kees').subscribe( (response: HttpResponse<User>) => {}, (err) => {
      expect(err).toBeTruthy();
    });

    const req = httpMock.expectOne(
      BASE_URL + '/authentication');
    expect(req.request.method).toBe('POST');
    
    // flush with a error
    req.flush({
      type: 'ERROR',
      status: 400
    });
  });
});
