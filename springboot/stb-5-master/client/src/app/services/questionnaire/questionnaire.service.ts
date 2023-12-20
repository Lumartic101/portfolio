import {Injectable} from '@angular/core';
import {Questionnaire} from "../../models/questionnaire/questionnaire.model";
import {ClusterService} from "./cluster.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {environment} from "../../../environments/environment";
import {Status} from "../../models/questionnaire/status";
import {Progress} from "../../models/questionnaire/progress.model";

@Injectable({
  providedIn: 'root'
})

export class QuestionnaireService {

  private BASE_URL: string = `${environment.apiUrl}/questionnaire`

  constructor(clusterService: ClusterService, private httpClient: HttpClient) {}

  public getUserQuestionnairePreviews(): Observable<Questionnaire[]> {
    return this.httpClient.get<any[]>(`${this.BASE_URL}/user/preview`).pipe(map(data => {
      for (const q of data)
        q.finishDate = new Date(q["finishDate"])
      return data as Questionnaire[]
    }))
  }

  public getAdminPreview(): Observable<Questionnaire[]> {
    return this.httpClient.get<any[]>(`${this.BASE_URL}/admin/preview`).pipe(map(data => {
      for (const q of data) {
        q.finishDate = new Date(q["finishDate"])
        q.startDate = new Date(q["startDate"])
        q.status = Status[q.status]
      }
      return data as Questionnaire[]
    }))
  }

  public getByIdForUser(id: number): Observable<Questionnaire> {
    return this.httpClient.get<any[]>(`${this.BASE_URL}/user/${id}`)
      .pipe(map(responseData => QuestionnaireService.mapToQuestionnaire(responseData)))
  }

  public getById(id: number): Observable<Questionnaire> {
    return this.httpClient.get<any[]>(`${this.BASE_URL}/${id}`)
      .pipe(map(responseData => QuestionnaireService.mapToQuestionnaire(responseData)))
  }

  public getAll(): Observable<Questionnaire[]> {
    return this.httpClient.get<any[]>(`${this.BASE_URL}`)
      .pipe(map(responseData => QuestionnaireService.mapAlltoQuestionnaire(responseData)))
  }

  // sort an array of objects implementing the Id interface
  private static sortIdArray(array: Array<any>) {
    return array.sort((o1, o2) => o1.id - o2.id)
  }

  private static sortProgresses(progresses: Array<Progress>) {
    let result: Progress[] = []
    for (const progress of progresses) {
      switch (progress.title) {
        case "(Nog) niet van toepassing (0)": {
          result[0] = progress
          break;
        }
        case "Incidenteel (1)": {
          result[1] = progress
          break;
        }
        case "Samenhangend (2)": {
          result[2] = progress
          break;
        }
        case "Systematisch (3) (dit is een belangrijk element)": {
          result[3] = progress
          break;
        }
        case "Systematisch en partnergericht (4)": {
          result[4] = progress
          break;
        }
        case "Impactvol (5)": {
          result[5] = progress
          break;
        }
      }
    }
    return result;
  }

  private static mapAlltoQuestionnaire(data: any): Questionnaire[] {
    this.sortIdArray(data)
    for (const questionnaire of data) {
      this.mapToQuestionnaire(questionnaire)
    }
    return data as Questionnaire[]
  }

  private static mapToQuestionnaire(questionnaire: any): Questionnaire {
    //Convert String responses to real Date objects
    questionnaire.startDate = new Date(questionnaire["startDate"])
    questionnaire.finishDate = new Date(questionnaire["finishDate"])
    questionnaire.status = Status[questionnaire.status]
    this.sortIdArray(questionnaire.pillar.clusters)
    for (const cluster of questionnaire.pillar.clusters) {
      this.sortIdArray(cluster.goals)
      for (const goal of cluster.goals) {
        this.sortIdArray(goal.subquestions)
        for (const question of goal.subquestions) {
          question.progresses = this.sortProgresses(question.progresses)
        }
      }
    }
    return questionnaire as Questionnaire
  }


  restPostQuestionnaire(q: Questionnaire): Promise<any> {

    const url = `${this.BASE_URL}`

    q.id = null;

    //Undo front-end id's of all goals and subquestions
    for (let cluster of q.pillar.clusters) {
      delete cluster.belongsToQuestionnaire;
      for (let goal of cluster.goals) {
        goal.id = null;
        for (let sub of goal.subquestions) {
          sub.id = null;
          delete sub.belongsToQuestionnaire;
          delete sub.belongsToCluster;
          delete sub.description;
          delete sub.rating;
        }
      }
    }

    const body = JSON.stringify(q);

    const config = {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    }

    return this.httpClient.post(url, body, config).toPromise();
  }

  restPutQuestionnaire(q: Questionnaire): Promise<any> {

    const url = `${this.BASE_URL}/${q.id}`

    //Remove unnecessary front-end fields which cause circularity
    for (let cluster of q.pillar.clusters) {
      delete cluster.belongsToQuestionnaire;
      for (let goal of cluster.goals) {
        for (let sub of goal.subquestions) {
          delete sub.belongsToQuestionnaire;
          delete sub.belongsToCluster;
          delete sub.description;
          delete sub.rating;
        }
      }
    }

    const body = JSON.stringify(q);

    const config = {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    }

    return this.httpClient.put(url, body, config).toPromise();
  }

  async save(questionnaire: Questionnaire): Promise<any> {

    if (! await this.questionnaireExists(questionnaire.id)) {
      return this.restPostQuestionnaire(questionnaire);
    } else {
      return this.restPutQuestionnaire(questionnaire);
    }
  }

  //Check whether questionnaire already exists
  public async questionnaireExists(id: number): Promise<any> {

    let questionnaires = await this.getAll().toPromise();

    for (let i = 0; i < questionnaires.length; i++) {
      if(questionnaires[i].id == id) {
        return true;
      }
    }
    return false;
  }

}
