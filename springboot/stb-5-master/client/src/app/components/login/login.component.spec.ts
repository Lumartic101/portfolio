import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {FormsModule} from '@angular/forms';
import {HttpClientModule, HttpResponse} from '@angular/common/http';
import {LoginComponent} from "./login.component";
import {RouterTestingModule} from "@angular/router/testing";
import {AuthService} from "../../services/authentication/auth.service";
import {observable, of} from "rxjs";


describe('Login component', () => {
  let component: LoginComponent;
  let componentHtml: HTMLElement;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoginComponent ],
      imports: [HttpClientModule, FormsModule, RouterTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    componentHtml = fixture.debugElement.nativeElement;
    fixture.detectChanges();
  });

  /**
   * Example of an integration test. Note that the component is using the real service.
   */
  it( 'Example 01: search input should update component property',  () => {

    // Arrange (getting UI components)
    const emailInput: HTMLInputElement = componentHtml.querySelector('#email');

    // Act: Filling in the email input
    emailInput.value = 'luka@gmail.com';
    emailInput.dispatchEvent(new Event('input'));
    fixture.detectChanges(); // Angular should be updated

    // Assert: Check if the property was updated
    expect(component.email).toEqual(emailInput.value);
  });

  it( 'Example 02: search input should update component property',  () => {

    // Arrange (getting UI components)
    const passInput: HTMLInputElement = componentHtml.querySelector('#password');

    // Act: Filling in the password input
    passInput.value = '123';
    passInput.dispatchEvent(new Event('input'));
    fixture.detectChanges(); // Angular should be updated

    // Assert: Check if the property was updated
    expect(component.pass).toEqual(passInput.value);
  });

  /**
   * Example of an integration test. Note that the component is using the real service.
   */
  it( 'Example 03: should get logged in as a user (mocking a service with a spy)',  (done) => {

    // Arrange (getting UI components)
    const emailInput: HTMLInputElement = componentHtml.querySelector('#email');
    const passInput: HTMLInputElement = componentHtml.querySelector('#password');

    const loginButton: HTMLButtonElement = componentHtml.querySelector('#login');

    // Act: Performing login
    emailInput.value = 'luka@gmail.com';
    emailInput.dispatchEvent(new Event('input'));
    passInput.value = '123';
    passInput.dispatchEvent(new Event('input'));
    fixture.detectChanges(); // Angular should be updated

    expect(component.email).toEqual(emailInput.value);
    expect(component.pass).toEqual(passInput.value);

    // Creating a spy to intecept calls and return a dummy object
    const authservice = fixture.debugElement.injector.get(AuthService);
    const spy = spyOn(authservice, 'auth').and.returnValue( of({ email: 'luka@gmail.com', password: "123", admin: false, education: "Technology"}));

    loginButton.click();

    fixture.detectChanges(); // Angular should be updated

    // Assert: checking if the UI was updated, getting the right values back
    spy.calls.mostRecent().returnValue.subscribe((data) => {
      const admin: boolean = data.admin;
      const nameFaculty: string = data.education;
      fixture.detectChanges();
      expect(nameFaculty).toEqual("Technology");
      expect(admin).toEqual(false);
      done();
    });
  });

  /**
   * Mocking a service using a spy
   */
  it( 'Example 04: should show a wrong message (mocking a service with a spy)',  (done) => {

    // Arrange (getting UI components)
    const emailInput: HTMLInputElement = componentHtml.querySelector('#email');
    const passInput: HTMLInputElement = componentHtml.querySelector('#password');

    // Creating a spy to intecept calls and return a dummy object
    const authService = fixture.debugElement.injector.get(AuthService);
    const spy = spyOn(authService, 'auth').and.returnValue( of({ email: 'Testing05@gmail.com', password: "123"}));

    const loginButton: HTMLButtonElement = componentHtml.querySelector('#login');

    // Act: Performing login
    loginButton.click();
    fixture.detectChanges(); // Angular should be updated

    // Assert: checking if the login gives a error
    spy.calls.mostRecent().returnValue.subscribe(error => {
      component.errorMessageHandling(error);
      fixture.detectChanges();
      expect(component.errorMessage).toContain('Something went wrong');
      done();
    })
  });
});
