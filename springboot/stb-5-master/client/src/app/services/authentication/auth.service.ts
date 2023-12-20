import {Injectable} from '@angular/core';
import {User, UserRole} from "../../models/user";
import {JwtHelperService} from "@auth0/angular-jwt";
import {environment} from "../../../environments/environment";
import {share, shareReplay} from "rxjs/operators";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // base API url
  private BASE_URL: string = environment.apiUrl;

  // user information
  currentUser: User;

  // utility function to decode token
  jwtService = new JwtHelperService();

  constructor(private httpClient: HttpClient) {
    // check if there is some token in the storage and update information
    this.updateUserInformation();
  }

  auth(email: string, password: string): Observable<any> {
    // make a request that returns a full response, including the header in order to gain access to the JWT token auth header
    let observable = this.httpClient.post<HttpResponse<User>>(
      `${this.BASE_URL}/authentication`,
      { email: email, password: password },
      { observe: "response" }
    ).pipe(share());

    observable.subscribe(data => {
      // retrieve the token from the header of request
      let token = data['headers'].get('Authorization');

      // no token present so do not proceed
      if(token == null) {
        throw new Error('token was not present in the response');
      }

      // remove the string part from the token
      token = token.replace('Bearer ', '');

      // add the token to the storage
      sessionStorage.setItem('token', token);

      this.updateUserInformation();
    },
    (err) => {
      this.logout();
    });

    return observable;
  }

  refreshToken(): Observable<any> {
    const observable = this.httpClient.post(
      `${this.BASE_URL}/refresh-token`,
      {},
      { headers: new HttpHeaders({ Authorization: this.currentToken }), observe: 'response'}
    ).pipe(share());

    observable.subscribe(data => {
        // retrieve the token from the header of request
        let refreshedToken = data['headers'].get('Authorization');

        // no token present so do not proceed
        if (refreshedToken == null) {
          throw new Error('token was not present in the response');
        }

        // remove the string part from the token
        refreshedToken = refreshedToken.replace('Bearer ', '');

        // add the token to the storage
        sessionStorage.setItem('token', refreshedToken);

        this.updateUserInformation();
      },
      (err) => {
        this.logout();
      });

    return observable;
  }

    // checks if there is a token present to make sure the user is logged in
  isLoggedIn(): boolean {
    return this.currentToken != null;
  }

  // checks if the user is logged in and has a admin role
  isAdmin(): boolean {
    return this.isLoggedIn() && this.currentUser.role == UserRole.ADMIN_USER;
  }

  // retrieves the token from the storage
  get currentToken(): string {
    return sessionStorage.getItem('token');
  }

  // removes the token from the session storage and updates the user with new info
  logout(): void {
    sessionStorage.removeItem('token');
    this.updateUserInformation();
  }

  // updates the user with the info from the token
  private updateUserInformation(): void {
    if (this.currentToken) {
      // decode the token in order to get all of the data from the token
      const decodedToken = this.jwtService.decodeToken(this.currentToken);

      // create a user and add the data from the token to the user
      this.currentUser = new User();
      this.currentUser.id = decodedToken.sub;
      this.currentUser.role = decodedToken.admin.toLowerCase() === 'true' ? UserRole.ADMIN_USER : UserRole.GENERAL_USER;
      this.currentUser.exp = decodedToken.exp;

    } else {
      this.currentUser = null;
    }
  }

}
