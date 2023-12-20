import {Education} from "./education";

export enum UserRole {
  ADMIN_USER,
  GENERAL_USER
}

export class User {
  private _id: number;
  private _email: string;
  private _password: string;
  private _firstName: string;
  private _lastName: string;
  private _education?: Education;
  private _education_id: number;
  private _role: UserRole;
  private _exp: number;


  constructor(id?: number, email?: string, password?: string, firstName?: string, lastName?: string, education?: Education, education_id?: number, role?: UserRole) {
    this._id = id;
    this._email = email;
    this._password = password;
    this._firstName = firstName;
    this._lastName = lastName;
    this._education = education;
    this._education_id = education_id;
    this._role = role;
  }

  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  get password(): string {
    return this._password;
  }

  set password(value: string) {
    this._password = value;
  }

  get firstName(): string {
    return this._firstName;
  }

  set firstName(value: string) {
    this._firstName = value;
  }

  get lastName(): string {
    return this._lastName;
  }

  set lastName(value: string) {
    this._lastName = value;
  }

  get role(): UserRole {
    return this._role;
  }

  set role(value: UserRole) {
    this._role = value;
  }

  get exp(): number {
    return this._exp;
  }

  set exp(value: number) {
    this._exp = value;
  }

  get education(): Education {
    return this._education;
  }

  set education(value: Education) {
    this._education = value;
  }

  get education_id(): number {
    return this._education_id;
  }

  set education_id(value: number) {
    this._education_id = value;
  }

  // using = () : string => ensures that 'this' will be what you expect in the toString override
  public toString = () : string => {
    return `id: ${this._id}, email: ${this._email}, name: ${this._firstName} ${this._lastName}, role: ${this._role}, education: ${this._education}`;
  }

}
