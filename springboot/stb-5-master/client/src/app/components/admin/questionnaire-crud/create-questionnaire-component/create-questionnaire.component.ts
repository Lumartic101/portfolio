import {Component, OnInit} from '@angular/core';
import {QuestionnaireService} from "../../../../services/questionnaire/questionnaire.service";
import {Questionnaire} from "../../../../models/questionnaire/questionnaire.model";
import {Router} from "@angular/router";
import {PillarService} from "../../../../services/questionnaire/pillar.service";
import {Pillar} from "../../../../models/questionnaire/pillar.model";
import {Status} from "../../../../models/questionnaire/status";

@Component({
  selector: 'app-create-questionnaire',
  templateUrl: './create-questionnaire.component.html',
  styleUrls: ['./create-questionnaire.component.scss']
})

export class CreateQuestionnaireComponent implements OnInit {

  //The name of the questionnaire
  name: string = null;
  //The date at which the questionnaire becomes available for the public.
  startDate: Date = null;
  //The date at which the questionnaire becomes unavailable again.
  endDate: Date = null;
  //The pillar that this questionnaire will be about
  pillar: Pillar;

  //All available pillats
  pillars: Array<Pillar>;

  constructor(public questionnaireService: QuestionnaireService, public pillarService: PillarService, public router: Router) {
  }

  ngOnInit(): void {
    //Retrieve all pillars and store in pillar array
    this.pillarService.loaded.then(() => this.pillars = this.pillarService.getAll())
  }

  onProceed(): Questionnaire {

    let id = 3000;

    let questionnaire = null;

    let today = new Date();

    //Validation of user data
    if (this.name == null) {
      alert("Please enter a name.");
      return null;
    } else if (this.startDate == null || this.endDate == null) {
      alert("Please select a date at which the questionnaire starts and closes.");
      return null;
    } else if (this.endDate < this.startDate) {
      alert("End date can't be before start date.");
      return null;
    } else if (this.startDate.toString() < today.toISOString().split('T')[0]) {
      alert("Start date must be somewhere in the future.");
      return null;
    } else {
      questionnaire = new Questionnaire(null, this.name, this.startDate, this.endDate, this.pillar, Status.CONCEPT);
      console.log(questionnaire);
      this.questionnaireService.save(questionnaire);
    }

    //this.router.navigate(['/dashboard/edit-questionnaire', questionnaire.id]);

    return questionnaire;

  }

  setSelectedPillar(id: number) {
    console.log(id);
    this.pillar = this.pillarService.findById(id);
  }

}
