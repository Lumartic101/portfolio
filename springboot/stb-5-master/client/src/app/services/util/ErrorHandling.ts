import {HttpErrorResponse} from "@angular/common/http";
import {throwError} from "rxjs";

export class ErrorHandling {

  // Method for handling API Errors
  static handleError(error: HttpErrorResponse) {

    let message: string;

    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      message = error.error.message;
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      if (error.status === 0) {
        message = 'backend error: status: ' + error.message;
      } else {
        message = 'backend error: status: ' + error.status + ' - ' + error.error.message;
      }
      console.error(message);
    }
    // return an observable with a user-facing error message
    return throwError(message);
  };
}
