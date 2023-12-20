import {Component, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Questionnaire} from "../../../../models/questionnaire/questionnaire.model";
import {QuestionnaireService} from "../../../../services/questionnaire/questionnaire.service";
import {ActivatedRoute, Router} from "@angular/router";
import {SubmissionService} from "../../../../services/questionnaire/submission.service";
import {Subquestion} from "../../../../models/questionnaire/subquestion.model";
import {ClusterComponent} from "../cluster/cluster.component";
import {AuthService} from "../../../../services/authentication/auth.service";


@Component({
  selector: 'app-answer-questionnaire',
  templateUrl: './answer-questionnaire.component.html',
  styleUrls: ['./answer-questionnaire.component.scss']
})
export class AnswerQuestionnaireComponent implements OnInit {
  questionnaire: Questionnaire
  submissionOngoing: boolean

  @ViewChildren(ClusterComponent)
  private clusterComponents: QueryList<ClusterComponent>

  constructor(
    private route: ActivatedRoute,
    private questionnaireService: QuestionnaireService,
    private submissionService: SubmissionService,
    private router: Router,
    private auth: AuthService
  ) { }

  ngOnInit(): void {
    this.submissionOngoing = false
    let questionnaireId = Number(this.route.snapshot.paramMap.get('id'))
    this.questionnaireService.getByIdForUser(questionnaireId).subscribe(
      q => this.questionnaire = q,
      e => {
        console.log(e)
        alert(`could not load questionnaire, reason: ${e.error.error}\nstatus: ${e.error.status}`)
        this.router.navigate(["/home/questionnaires"])
      }
    )
  }

  onSubmit() {
    if (!confirm('do you want to submit this questionnaire?'))
      return
    //check if all questions have been answered
    if (!this.wasAnswered()) {
      alert('you have not answered all the questions yet!')
      return;
    }
    //set submissionOngoing to true to block the submission button
    this.submissionOngoing = true
    //calculate the average of all questions
    let questions = this.getQuestions()
    let total = 0
    let questionCount = questions.length
    for (const question of questions)
      total += question.rating
    let average = total / questionCount

    //make the submission
    let userId = this.auth.currentUser.id
    let submissionPromise = this.submissionService.submit(average, userId, this.questionnaire.id)
    submissionPromise.then(
      () => {
        alert("submitted!")
        this.router.navigate(["/home/questionnaires"])
      },
      () => {
        this.submissionOngoing = false
        alert("an error occured while submitting")
      }
    )
  }

  getQuestions(): Subquestion[] {
    let questions: Subquestion[] = []
    for (const cluster of this.questionnaire.pillar.clusters)
      for (const goal of cluster.goals)
        for (const question of goal.subquestions)
          questions.push(question)
    return questions
  }

  wasAnswered():boolean {
    for (const clusterComponent of this.clusterComponents)
      if (!clusterComponent.wasAnswered())
        return false
    return true
  }
}
