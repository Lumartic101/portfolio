import {Component, Input, OnInit} from '@angular/core';
import {Subquestion} from "../../../../models/questionnaire/subquestion.model";

@Component({
  selector: 'app-questionnaire-question-component',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.scss']
})
export class QuestionComponent implements OnInit {
  @Input()
  question: Subquestion
  @Input()
  clusterIndex: number
  @Input()
  goalIndex: number
  @Input()
  questionIndex: number
  questionLetter: string

  answered: boolean

  constructor() {
    this.answered = false
  }

  ngOnInit(): void {
    this.questionLetter = QuestionComponent.numberToLetter(this.questionIndex)
  }

  //turn a number into a letter of the alphabet, wraps around with numbers > 26
  private static numberToLetter(num: number): string {
    return String.fromCharCode((num % 26) + 97)
  }
}
