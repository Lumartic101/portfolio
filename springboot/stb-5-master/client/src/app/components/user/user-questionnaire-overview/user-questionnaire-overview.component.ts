import { Component, OnInit } from '@angular/core';
import {QuestionnaireService} from "../../../services/questionnaire/questionnaire.service";
import {Questionnaire} from "../../../models/questionnaire/questionnaire.model";

@Component({
  selector: 'app-welcome-logged-in',
  templateUrl: './user-questionnaire-overview.component.html',
  styleUrls: ['./user-questionnaire-overview.component.scss']
})
export class UserQuestionnaireOverview implements OnInit {
  questionnaires: Questionnaire[]

  constructor(private questionnaireService: QuestionnaireService){

  }

  ngOnInit(): void {
    this.questionnaireService.getUserQuestionnairePreviews()
      .subscribe(questionnaires => this.questionnaires = questionnaires)
  }
}
