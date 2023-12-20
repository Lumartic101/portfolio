import {Datapoint} from "./datapoint";

export class Dataset {
  public label: string;
  public data: Datapoint[]

  constructor(label: string, data: Datapoint[]) {
    this.label = label;
    this.data = data;
  }

}
