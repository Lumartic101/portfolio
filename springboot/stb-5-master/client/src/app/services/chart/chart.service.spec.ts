import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

import { ChartService } from './chart.service';
import { Dataset } from '../../models/dataset';

describe('ChartService', () => {
  let service: ChartService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(ChartService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should call http.get with the expected url and query params', () => {
    const dummyData = [
      [
        5.0,
        "2022-12-09T23:00:00.000+00:00",
        "Pabo HvA",
        "Onderwijs"
      ],
      [
        1.0,
        "2022-12-09T23:00:00.000+00:00",
        "Pedagogiek",
        "Onderwijs"
      ],
      [
        2.0,
        "2022-12-09T23:00:00.000+00:00",
        "Leraar",
        "Onderwijs"
      ]
    ];

    service.plot({pillar: "Onderwijs", faculty: "FOO"}).subscribe( (res: Dataset[]) => {
      expect(res[0].data[0].x).toEqual('2022')
      expect(res[0].data[0].y).toEqual(2)
      expect(res[0].label).toEqual('Leraar')

      expect(res[1].data[0].x).toEqual('2022')
      expect(res[1].data[0].y).toEqual(5)
      expect(res[1].label).toEqual('Pabo HvA')

      expect(res[2].data[0].x).toEqual('2022')
      expect(res[2].data[0].y).toEqual(1)
      expect(res[2].label).toEqual('Pedagogiek')
    });

    // check if the correct request was made
    const req = httpMock.expectOne('http://localhost:8080/answer?pillar=Onderwijs&faculty=FOO');

    // check if the correct request was made
    expect(req.request.method).toBe('GET');

    // check if both of the parameters have been passed correctly
    expect(req.request.params.get('pillar')).toEqual('Onderwijs');
    expect(req.request.params.get('faculty')).toEqual('FOO');

    // resolve the request with successful response
    req.flush(dummyData);
  });

  it('should generate an exception due to wrong params', () => {
    service.plot({pillar: "Non-existing pillar"} ).subscribe( (res: Dataset[]) => {}, (err) => {
      expect(err).toBeTruthy(); // we only test for error since we expect an error when passing this filter
    });

    const req = httpMock.expectOne('http://localhost:8080/answer?pillar=Non-existing%20pillar');
    expect(req.request.method).toBe('GET');

    // resolve the request with unsuccessful response
    req.flush({
      type: 'ERROR',
      status: 400
    });
  });

});
