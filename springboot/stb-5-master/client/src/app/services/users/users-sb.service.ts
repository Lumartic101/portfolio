import { Injectable } from '@angular/core';
import {Observable, throwError} from "rxjs";
import {User} from "../../models/user";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {map} from "rxjs/operators";
import {environment} from "../../../environments/environment";
import {log10} from "chart.js/helpers";
import {ErrorHandling} from "../util/ErrorHandling";

@Injectable({
  providedIn: 'root'
})
export class UsersSbService {
  private users: User[] = [];
  private BASE_URL: string = `${environment.apiUrl}`;

  constructor(private httpClient: HttpClient) {
    this.restGetUsers().subscribe(
      responseData => this.users = responseData,
      error => ErrorHandling.handleError(error)
    );
  }

  restGetAcc(email: String, pass: String): Observable<User[]> {
    return this.httpClient.get<User[]>(`${this.BASE_URL}/login/${email}/${pass}`);
  }

  private restGetUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(`${this.BASE_URL}/users`)
      .pipe(
        map(responseData => {
          console.log(responseData)
          const usersArray: User[] = [];
          for (const key in responseData) {
            // change the userRole enum to typescript format
            responseData[key].role = UsersSbService.convertToTypeScriptEnum(responseData[key]);
            responseData[key].education = responseData[key]["education"]
            // if(responseData[key].education.name != null){
            //   responseData[key].education.name = "heeft geen education"
            // }
            usersArray.push(responseData[key]);  // add the user to the list of users
          }

          console.log(usersArray[0])
          console.log("initial list", usersArray)
          return usersArray;
        })
      );
  }

  private restPostUser(user: User) {
    console.log(user.education)
    const url = `${this.BASE_URL}/users`
    const body = {
      "id": user.id,
      "email": user.email,
      "password": user.password,
      "firstName": user.firstName,
      "lastName": user.lastName,
      "department": user.education,
      "education_id": user.education_id,
      "role": UsersSbService.convertToJavaEnum(user)
    }

    const config = {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    }

    return this.httpClient.post(url, body, config).toPromise();
  }

  private restPutUser(user: User) {
    const url = `${this.BASE_URL}/users/${user.id}`
    const body = {
      "id": user.id,
      "email": user.email,
      "password": user.password,
      "firstName": user.firstName,
      "lastName": user.lastName,
      "department": user.education,
      "education_id": user.education_id,
      "role": UsersSbService.convertToJavaEnum(user)
    }

    const config = {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    }

    return this.httpClient.put(url, body, config).toPromise();
  }

  private restDeleteUser(userId: number) {
    const url = `${this.BASE_URL}/users/${userId}`
    return this.httpClient.delete(url).toPromise();
  }

  findAll(): User[] {
    return this.users;
  }

  save(user: User): User {
    // if the user is not already in the users list save a new user
    if (!this.userAlreadyExistsInArray(user.id)) {
      user.education.id
      this.restPostUser(user).then(r => this.users.push(user));
    }
    else {
      // the user is already in the array so update it's values
      // first find the index of the user in the array
      this.restPutUser(user).then(r => {
        let indexOfUserToBeUpdated = this.users.findIndex((obj => obj.id == user.id));

        // update the user at the given index to the new user
        this.users[indexOfUserToBeUpdated] = user;
      })
    }

    return user;
  }

  // retrieves one user, identified by a given id
  findById(id: number): User {
    console.log(id)
    return this.users.find(user => user.id == id)
  }


  deleteById(id: number): void {
    let toBeDeletedUser = this.users.map(user => {
      return user.id
    }).indexOf(id);

    console.log(toBeDeletedUser)

    // retrieve the response then splice the array to exclude the response
    this.restDeleteUser(id).then(r => this.users.splice(toBeDeletedUser, 1))
  }

  // helper method to convert enums from the back-end into TS enum value (number)
  private static convertToTypeScriptEnum(user: any): number {
    if (user.role == "ADMIN") return 0;
    else return 1;
  }

  // helper method to convert enums from the front-end into JAVA enum value (string)
  private static convertToJavaEnum(user: User): string {
    if (user.role == 0) return "ADMIN";
    else return "USER";
  }

  // helper method to check if a user is already in the array
  private userAlreadyExistsInArray(id: number) {
    return this.users.some(user => { return user.id === id });
  }
}
