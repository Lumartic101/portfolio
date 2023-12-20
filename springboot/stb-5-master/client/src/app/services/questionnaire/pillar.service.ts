import {Injectable} from '@angular/core';
import {MockService} from "../../models/service.model";
import {Pillar} from "../../models/questionnaire/pillar.model";
import {ClusterService} from "./cluster.service";
import {ClusterComponent} from "../../components/user/questionnaire/cluster/cluster.component";
import {Cluster} from "../../models/questionnaire/cluster.model";
import {Observable} from "rxjs";
import {Questionnaire} from "../../models/questionnaire/questionnaire.model";
import {map} from "rxjs/operators";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../../models/user";
import {environment} from "../../../environments/environment";
import {UsersSbService} from "../users/users-sb.service";
import {ErrorHandling} from "../util/ErrorHandling";
import {response} from "express";

@Injectable({
  providedIn: 'root'
})
export class PillarService extends MockService<Pillar> {

  private pillars: Pillar[] = [];
  private pillarsByYear: Pillar[] = [];
  private BASE_URL: string = `${environment.apiUrl}`;
  loaded: Promise<void>

  constructor(private clusterService: ClusterService, private httpClient: HttpClient) {
    super()
    this.loaded = new Promise<void>((resolve) => {
      this.restGetPillars().subscribe(
        (responseData) => {
          this.pillars = responseData;
          resolve()
        },
        (error) => ErrorHandling.handleError(error),
      )
    })
  }

  addClusterToPillar(p: number, c: Cluster) {
    //Get right pillar and add cluster
    this.getById(p).clusters.push(c)
  }

  getAll(): Pillar[] {
    return this.pillars;
  }

  getAllByYear(): Pillar[] {
    return this.pillarsByYear;
  }

  save(pillar: Pillar): Pillar {

    this.restPostPillar(pillar).then(r => this.pillars.push(pillar));

    return pillar;
  }

  private restPostPillar(pillar: Pillar) {

    const url = `${this.BASE_URL}/pillar/add`
    const body = {
      "id": pillar.id,
      "name": pillar.name,
      "clusters": pillar.clusters
    }

    const config = {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    }

    return this.httpClient.post(url, body, config).toPromise();
  }

  public restGetById(id: number): Observable<Pillar[]> {
    return this.httpClient.get<Pillar[]>(`${this.BASE_URL}/pillar/${id}`)
  }

  public restGetPillars(): Observable<Pillar[]> {
    return this.httpClient.get<Pillar[]>(`${this.BASE_URL}/pillar/all`)
      .pipe(map
      (responseData => {
          this.pillars = [];
          for (const key in responseData) {
            this.pillars.push(responseData[key])
          }
          return this.pillars
        }
      ));
  }

  public restGetPillarsByYear(selectedYear: number): Observable<Pillar[]> {
    return this.httpClient.get<any[]>(`${this.BASE_URL}/pillar/year/${selectedYear}`)
      .pipe(map
      (responseData => {
          this.pillars = [];
          for (const key in responseData) {
            for (const cluster of responseData[key].clusters) {
              for (let i = 0; i < responseData[key].clusters; i++) {
                cluster.id = responseData[key].clusters[i].id;
                cluster.name = responseData[key].clusters[i].name
              }
            }
            this.pillars.push(responseData[key])
          }
          return this.pillars;
        }
      ));
  }


  // retrieves one pillar, identified by a given id
  findById(id: number): Pillar {
    return this.pillars.find(pillar => pillar.id == id)
  }

}
