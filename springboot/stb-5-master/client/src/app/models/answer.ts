import {Questionnaire} from "./questionnaire/questionnaire.model";
import {User} from "./user";

export class Answer {
  private _user: User
  private _questionnaire: Questionnaire;
  private _score: number
  private _submitDate: Date

  constructor(user: User, questionnaire: Questionnaire, score: number, submitDate: Date) {
    this._user = user;
    this._questionnaire = questionnaire;
    this._score = score;
    this._submitDate = submitDate;
  }

  get user(): User {
    return this._user;
  }

  set user(value: User) {
    this._user = value;
  }

  get questionnaire(): Questionnaire {
    return this._questionnaire;
  }

  set questionnaire(value: Questionnaire) {
    this._questionnaire = value;
  }

  get score(): number {
    return this._score;
  }

  set score(value: number) {
    this._score = value;
  }

  get submitDate(): Date {
    return this._submitDate;
  }

  set submitDate(value: Date) {
    this._submitDate = value;
  }
}
