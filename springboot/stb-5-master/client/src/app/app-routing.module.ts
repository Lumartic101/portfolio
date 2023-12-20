import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeComponent } from "./components/user/welcome/welcome.component";
import { UserQuestionnaireOverview } from "./components/user/user-questionnaire-overview/user-questionnaire-overview.component";
import {UsersComponent} from "./components/admin/users/users.component";
import {QuestionOverviewComponent} from "./components/admin/questionnaire-overview/question-overview.component";
import {DashboardComponent} from "./components/admin/dashboard/dashboard.component";
import {ErrorPageNotFoundComponent} from "./components/error-page-not-found/error-page-not-found.component";
import {DashboardHomeComponent} from "./components/admin/dashboard-home/dashboard-home.component";
import {AnswerQuestionnaireComponent} from "./components/user/questionnaire/answer-questionnaire/answer-questionnaire.component";
import {CreateQuestionnaireComponent} from "./components/admin/questionnaire-crud/create-questionnaire-component/create-questionnaire.component";
import {EditQuestionnaireComponent} from "./components/admin/questionnaire-crud/edit-questionnaire-component/edit-questionnaire.component";
import {HomeComponent} from "./components/home/home.component";
import {LoginComponent} from "./components/login/login.component";
import {ChartComponent} from "./components/admin/chart/chart.component";
import {AuthGuardAdminService} from "./services/authentication/auth-guard-admin.service";
import {AuthGuardLoggedInService} from "./services/authentication/auth-guard-logged-in.service";
import {ForbiddenComponent} from "./components/forbidden/forbidden.component";
import {editQuestionnaireResolver} from "./components/resolvers/edit-questionnaire.resolver";


const appRoutes : Routes = [
  {path: '', redirectTo: '/home/welcome', pathMatch: 'full'},
  {path: 'welcome', component: WelcomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'forbidden', component: ForbiddenComponent},
  {path: 'home', component: HomeComponent,
    children: [
      {path: '', redirectTo: '/home/welcome', pathMatch: 'full'},
      {path: 'welcome', component: WelcomeComponent},
      {path: 'questionnaires', component: UserQuestionnaireOverview , canActivate: [AuthGuardLoggedInService]},
      {path: 'questionnaires/answer/:id', component: AnswerQuestionnaireComponent , canActivate: [AuthGuardLoggedInService]},
    ]
  },
  {path: 'dashboard', component: DashboardComponent,
    children: [
      {path: '', redirectTo: '/dashboard/dashboardHome', pathMatch: 'full'},
      {path: 'dashboardHome', component: DashboardHomeComponent},
      {path: "users", component: UsersComponent},
      {path: "overview", component: QuestionOverviewComponent},
      {path: "create-questionnaire", component: CreateQuestionnaireComponent},
      {
        path: "edit-questionnaire/:id",
        component: EditQuestionnaireComponent,
        resolve: {questionnaire: editQuestionnaireResolver}
      },
      {path: 'overview/edit/:id', component: EditQuestionnaireComponent},
      {path: 'results', component: ChartComponent},
    ], canActivate: [AuthGuardAdminService, AuthGuardLoggedInService]},
    {path: '**', component: ErrorPageNotFoundComponent}
]


@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
