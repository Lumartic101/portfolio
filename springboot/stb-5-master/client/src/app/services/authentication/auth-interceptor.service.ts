import { Injectable } from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {AuthService} from "./auth.service";
import {Router} from "@angular/router";
import {catchError, switchMap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  constructor(private authService: AuthService, private router: Router) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // do not intercept authentication attempts, because this URL is white listed from filter in the back-end
    if (request.url.includes("/authentication")) {
      return next.handle(request);
    } else {
      // add the token header if there is a token in the current session
      if (this.authService.currentToken) {
        request = this.addToken(request, this.authService.currentToken);
      }
      return next.handle(request).pipe(
        catchError((error: HttpErrorResponse) => {
          // if the response code is 401 (unauthorised)
          if (error && error.status === 401) {
            // force authentication if it was a failed attempt to refresh the token
            if (request.url.includes('/refresh-token')) {
              this.forceLogoff();
              return throwError(error);
            } else {
              // try to refresh the token
              return this.authService.refreshToken().pipe(
                switchMap(data => {
                  // getting the returned token
                  const token = data['headers'].get('Authorization');
                  // trying again with the returned token
                  return next.handle(this.addToken(request, data['headers'].get('Authorization')));
                })
              ) /* pipe */
            }
          } else {
            return throwError(error);
          }
        }
        ) /* catchError */
      ) /* pipe */
    }
  }

  // forces the user to logoff and navigate to login screen
  private forceLogoff() {
    this.authService.logout();
    this.router.navigate(['login'], { queryParams: { msg: 'session expired'}} );
  }

  /**
   * Add the token header to the request. Since HttpRequests are immutable a clone is created
   * @param request
   * @param token
   * @private
   */
  private addToken(request: HttpRequest<any>, token: string) {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

}
