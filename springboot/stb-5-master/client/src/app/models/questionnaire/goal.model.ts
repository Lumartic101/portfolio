import {Subquestion} from "./subquestion.model";
import {Id} from "../../interfaces/id";

export class Goal implements Id{
  constructor(
    public id: number,
    public name: string,
    public subquestions: Subquestion[]
  ){}
}
