import { HttpParams } from '@angular/common/http';

// function to set passed in params into the HttpParams, in order to avoid 'undefined' params
export function createHttpParams(params: {}): HttpParams {
  let httpParams: HttpParams = new HttpParams();
  Object.keys(params).forEach(param => {
    if (params[param]) {
      httpParams = httpParams.set(param, params[param]);
    }
  });

  return httpParams;
}
