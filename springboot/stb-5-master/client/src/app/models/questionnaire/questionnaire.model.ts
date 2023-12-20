import {Id} from "../../interfaces/id";
import {Pillar} from "./pillar.model";
import {Status} from "./status";

export class Questionnaire implements Id{
  constructor(
    public id: number,
    public name: string,
    public startDate: Date,
    public finishDate: Date,
    public pillar: Pillar,
    public status: Status
  ) { }
}
