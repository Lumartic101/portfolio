import {Injectable} from '@angular/core';
import {MockService} from "../../models/service.model";
import {Cluster} from "../../models/questionnaire/cluster.model";
import {QuestionService} from "./question.service";
import {Subquestion} from "../../models/questionnaire/subquestion.model";
import {Pillar} from "../../models/questionnaire/pillar.model";
import {Goal} from "../../models/questionnaire/goal.model";

@Injectable({
  providedIn: 'root'
})
export class ClusterService extends MockService<Cluster> {

  constructor() {
    super()
  }

  createCluster(title: string, goals: Goal[], questionnaire: number): number {
    //Create new id which is stored for later reference
    let id = super.nextId();
    //Create new cluster with newly created id and the title and goal the user entered
    let newCluster = new Cluster(id, title, goals, questionnaire);
    //Adds newly created cluster to the cluster array.
    super.add(newCluster);

    //Return index of created cluster for other methods to use
    return id;
  }
}
