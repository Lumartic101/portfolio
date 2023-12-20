import {Component, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Goal} from "../../../../models/questionnaire/goal.model";
import {QuestionComponent} from "../question/question.component";
import {Subquestion} from "../../../../models/questionnaire/subquestion.model";

@Component({
  selector: 'app-goal',
  templateUrl: './goal.component.html',
  styleUrls: ['./goal.component.scss']
})
export class GoalComponent implements OnInit {
  @Input()
  goal: Goal
  @Input()
  clusterIndex: number
  @Input()
  goalIndex: number

  @ViewChildren(QuestionComponent)
  private questionComponents: QueryList<QuestionComponent>

  constructor() { }

  ngOnInit(): void {
  }

  wasAnswered():boolean {
    for (const questionComponent of this.questionComponents)
      if (!questionComponent.answered)
        return false
    return true
  }
}
