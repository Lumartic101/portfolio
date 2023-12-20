import {Component, OnInit} from '@angular/core';
import {Questionnaire} from "../../../models/questionnaire/questionnaire.model";
import {QuestionnaireService} from "../../../services/questionnaire/questionnaire.service";
import {PillarService} from "../../../services/questionnaire/pillar.service";
import {Pillar} from "../../../models/questionnaire/pillar.model";
import {Router} from "@angular/router";
import {Status} from "../../../models/questionnaire/status";

@Component({
  selector: 'app-question-overview',
  templateUrl: './question-overview.component.html',
  styleUrls: ['./question-overview.component.scss']
})
export class QuestionOverviewComponent implements OnInit {
  constructor(private questionnaireService: QuestionnaireService, private pillarService: PillarService, private router: Router) {}

  questionnaires: Questionnaire[]

  ngOnInit(): void {
    this.questionnaireService.getAdminPreview().subscribe(q => this.questionnaires = q)
  }

  getPillar(id: number): Pillar {
    return this.pillarService.getById(id);
  }

  compareDate(startDate: Date, endDate: Date): boolean{
    let now = new Date();
    return startDate < now && endDate > now
  }

  onCreateNew() {
    this.router.navigate(['/dashboard/edit-questionnaire/000']);
  }

  onOpen(id: number) {
    if (this.questionnaires.find(q => q.id == id).status != Status.OPEN) {
      this.router.navigateByUrl('/dashboard/edit-questionnaire/' + id);
    } else alert("Questionnaires which are open for users to answer can't be edited in order to maintain data integrity.")
  }
}
