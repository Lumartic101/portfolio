import {Questionnaire} from "./questionnaire.model";
import {Cluster} from "./cluster.model";
import {Id} from "../../interfaces/id";
import {Progress} from "./progress.model";

export class Subquestion implements Id {
  constructor(
    public id: number,
    public name: string,
    public description: string,
    public rating: number = 0,
    public progresses: Progress[],
    public belongsToQuestionnaire: Questionnaire = null,
    public belongsToCluster: Cluster = null
    ) { }
}
