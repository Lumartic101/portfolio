import {Id} from "../interfaces/id";

//model for a service using mock data
export abstract class MockService<T extends Id> {
  objects: Array<T>
  lastId: number

  public getAll(): Array<T> {
    return this.objects
  }

  public getById(id: number): T {
    return this.objects.find(o => o.id == id)
  }

  public getSet(ids: Array<number>): Array<T> {
    let result = new Array<T>()
    for (let key in ids) {
      result.push(this.getById(ids[key]))
    }
    return result
  }

  public add(toAdd: T): number {
    let index = this.objects.findIndex(o => o.id == toAdd.id)
    if(index == -1) {
      this.objects.push(toAdd)
    } else {
      this.objects[index] = toAdd
    }
    return index
  }

  public deleteById(id: number): Boolean {
    let index = this.objects.findIndex(q => q.id == id)
    this.objects.splice(index, 1);
    return index != -1
  }

  public nextId(): number {
    let id = this.lastId
    this.lastId++;
    return id;
  }

  protected constructor() {
    this.lastId = 0
    this.objects = new Array<T>()
  }
}
