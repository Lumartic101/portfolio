import {Id} from "../../interfaces/id";
import {Cluster} from "./cluster.model";

export class Pillar implements Id {

  id:number
  name:string
  clusters: Array<Cluster>

  constructor(id:number, name:string, clusters: Array<Cluster>) {
    this.id = id
    this.name = name
    this.clusters = clusters;
  }
}
