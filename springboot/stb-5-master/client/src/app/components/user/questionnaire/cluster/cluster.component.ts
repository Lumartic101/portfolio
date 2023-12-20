import {Component, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Cluster} from "../../../../models/questionnaire/cluster.model";
import {GoalComponent} from "../goal/goal.component";
import {Subquestion} from "../../../../models/questionnaire/subquestion.model";

@Component({
  selector: 'app-questionnaire-cluster',
  templateUrl: './cluster.component.html',
  styleUrls: ['./cluster.component.scss']
})
export class ClusterComponent implements OnInit {
  @Input()
  cluster: Cluster
  @Input()
  clusterIndex: number

  @ViewChildren(GoalComponent)
  private goalComponents: QueryList<GoalComponent>

  constructor() { }

  ngOnInit(): void {
  }
  
  wasAnswered():boolean {
    for (const goalComponent of this.goalComponents)
      if (!goalComponent.wasAnswered())
        return false
    return true
  }

}
