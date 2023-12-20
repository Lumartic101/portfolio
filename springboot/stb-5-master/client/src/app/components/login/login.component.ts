import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {AuthService} from "../../services/authentication/auth.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  email: string;
  pass: string;

  welcomeMessage: string;
  errorMessage: any;
  expectedUrl: string;

  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) {
    this.email = this.email;
    this.expectedUrl = '/';
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(
      (params: Params) => {
        this.welcomeMessage = params.msg;
        if (params.expectedUrl) {
          this.expectedUrl = params.expectedUrl;
        }
      });
  }

  submitLogin() {
    this.authService.auth(this.email, this.pass).subscribe(
      data => this.router.navigate([this.expectedUrl]),
      error => this.errorMessageHandling(error)
    );
  }

  errorMessageHandling(error: HttpErrorResponse) {
    this.errorMessage = `Something went wrong!<br>`

    switch (error.status) {
      case 401:
        this.errorMessage += 'Invalid credentials, please try again'
        break;
      case 500:
        this.errorMessage += 'Something went wrong, please try again later'
        break;
    }

  }
}
