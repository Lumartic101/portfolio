import {Subquestion} from "./subquestion.model";
import {Id} from "../../interfaces/id";
import {Goal} from "./goal.model";

export class Cluster implements Id{

  id:number
  name: string
  goals: Goal[]
  belongsToQuestionnaire: number;

  constructor(id: number, title: string, goals: Goal[], belongsToQuestionnaire: number) {
    this.id = id
    this.name = title
    this.goals = goals
    this.belongsToQuestionnaire = belongsToQuestionnaire;
  }
}
