import {Id} from "../../interfaces/id";

export class Progress implements Id{
  constructor(
    public id: number,
    public title: string,
    public description: string
  ) { }
}
