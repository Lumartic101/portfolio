import { Injectable } from '@angular/core';
import {MockService} from "../../models/service.model";
import {Subquestion} from "../../models/questionnaire/subquestion.model";
import {Cluster} from "../../models/questionnaire/cluster.model";
import {Questionnaire} from "../../models/questionnaire/questionnaire.model";
import {Progress} from "../../models/questionnaire/progress.model";

@Injectable({
  providedIn: 'root'
})
export class QuestionService extends MockService<Subquestion> {

  constructor() {
    super()
  }

  createQuestion(title: string, description: string, desc1: Progress, desc2: Progress, desc3: Progress, desc4: Progress, desc5: Progress, desc6: Progress, questionnaire: Questionnaire = null, cluster: Cluster = null): number {
    //Create new id which is stored for later reference
    let id = super.nextId();
    //Create new question with newly created id and the details the user entered
    let newQuestion = new Subquestion(id, title, description, null, [desc1, desc2, desc3, desc4, desc5, desc6], questionnaire, cluster);
    //Adds newly created cluster to the cluster array.
    super.add(newQuestion);

    //Return index of created cluster for other methods to use
    return id;
  }
}
