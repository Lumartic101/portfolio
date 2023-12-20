import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {QuestionOverviewComponent} from './components/admin/questionnaire-overview/question-overview.component'
import { UsersComponent } from './components/admin/users/users.component';

import { WelcomeComponent } from './components/user/welcome/welcome.component';
import { NavBarComponent } from './components/user/nav-bar/nav-bar.component';
import { UserQuestionnaireOverview } from './components/user/user-questionnaire-overview/user-questionnaire-overview.component';
import {SidebarComponent} from "./components/admin/sidebar/sidebar.component";
import {CreateQuestionnaireComponent} from "./components/admin/questionnaire-crud/create-questionnaire-component/create-questionnaire.component";
import { DashboardComponent } from './components/admin/dashboard/dashboard.component';
import { ErrorPageNotFoundComponent } from './components/error-page-not-found/error-page-not-found.component';
import { DashboardHomeComponent } from './components/admin/dashboard-home/dashboard-home.component';
import {ClusterComponent} from "./components/user/questionnaire/cluster/cluster.component";
import {AnswerQuestionnaireComponent} from "./components/user/questionnaire/answer-questionnaire/answer-questionnaire.component";
import {QuestionComponent} from "./components/user/questionnaire/question/question.component";
import {QuestionnaireService} from "./services/questionnaire/questionnaire.service";
import {FormsModule} from "@angular/forms";
import {EditQuestionnaireComponent} from "./components/admin/questionnaire-crud/edit-questionnaire-component/edit-questionnaire.component";
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import {NgChartsModule} from "ng2-charts";
import 'chartjs-adapter-moment';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ChartComponent} from "./components/admin/chart/chart.component";
import {CommonModule} from "@angular/common";
import { GoalComponent } from './components/user/questionnaire/goal/goal.component';
import {AuthInterceptorService} from "./services/authentication/auth-interceptor.service";
import { ForbiddenComponent } from './components/forbidden/forbidden.component';

@NgModule({
    declarations: [
        AppComponent,
        UsersComponent,
        WelcomeComponent,
        NavBarComponent,
        UserQuestionnaireOverview,
        QuestionOverviewComponent,
        CreateQuestionnaireComponent,
        SidebarComponent,
        AnswerQuestionnaireComponent,
        QuestionComponent,
        ClusterComponent,
        DashboardComponent,
        ErrorPageNotFoundComponent,
        DashboardHomeComponent,
        EditQuestionnaireComponent,
        HomeComponent,
        LoginComponent,
        HomeComponent,
        ChartComponent,
        ForbiddenComponent,
        GoalComponent
    ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,
    FormsModule,
    NgChartsModule,
    HttpClientModule
  ],
  providers: [
    QuestionnaireService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
