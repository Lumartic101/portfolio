import {Injectable} from '@angular/core';
import {User, UserRole} from "../../models/user";

@Injectable({
  providedIn: 'root'
})

export class  UsersService {
  private lastId: number = 0;
  private _users: User[] = [];

  constructor() {
    // initialize the initial list of users from the constructor
    // this._users.push(
    //   new User(this.nextId(), "admin", "admin", "Konrad", "Sokolowski", UserRole.ADMIN_USER),
    //   new User(this.nextId(), "user", "user", "Konrad", "Sokolowski", UserRole.GENERAL_USER),
    //
    //   new User(this.nextId(), "k.sokolowski@hva.nl", "test123", "Konrad", "Sokolowski", UserRole.ADMIN_USER),
    //   new User(this.nextId(), "t.vanlinden@hva.nl", "test123", "Troes", "van Linden", UserRole.GENERAL_USER,"FBSV", "Physical Education"),
    //   new User(this.nextId(), "g.abbot@hva.nl", "test123", "Gregg", "Abbot", UserRole.GENERAL_USER,"FBE", "Business and Economics"),
    //   new User(this.nextId(), "b.singh@hva.nl", "test123", "Brian", "Singh", UserRole.GENERAL_USER, "FG", "Nursing"),
    //   new User(this.nextId(), "m.elengard@hva.nl", "test123", "Maries", "Elengard", UserRole.GENERAL_USER, "FMR", "Legal Studies"),
    //   new User(this.nextId(), "a.groen@hva.nl", "test123", "Alex", "Groen", UserRole.GENERAL_USER,"FDMCI", "Software Engineering"),
    //   new User(this.nextId(), "d.rovenburg@hva.nl", "test123", "Dries", "Rovenburg", UserRole.GENERAL_USER,"FOO", "Pedagogics"),
    //   new User(this.nextId(), "r.vanbaren@hva.nl", "test123", "Robbie", "van Baren", UserRole.GENERAL_USER,"FT", "Technology")
    // );
  }

  // retrieves the list of all users
  public getAll(): User[] {
    return this._users;
  }

  // deletes the user identified by the given id
  public deleteById(id: number): void {
    let chosenUser = this._users.map(user => {
      return user.id
    }).indexOf(id);

    this._users.splice(chosenUser, 1)
  }

  // saves a new or changed user and returns the updated instance.
  // (New users may get id=0 at creation and shall be given their
  // true, final, unique id by this save method.)
  save(user: User): User {
    // if the user is not already in the list save a new user to the array
    if (!this.userWithIdAlreadyExistsInArray(user.id)) {
      user.id = this.nextId();
      this._users.push(user)
    }
    // the user is already in the array so update it's value
    else {
      // first find the index of the user in the array
      let indexOfUserToBeUpdated = this._users.findIndex((obj => obj.id == user.id));

      // update the user at the given index to the new user
      this._users[indexOfUserToBeUpdated] = user;
    }

    console.log(this._users)
    return user;
  }

  private userWithIdAlreadyExistsInArray(id: number) {
    // This method tests whether at least one element in the array passes the test.
    // It returns true if, in the array, it finds an element for which the provided function returns true; otherwise it returns false.
    return this._users.some(user => {
      return user.id === id;
    });
  }

  // changes user role, for the user identified by the given id
  public changeUserRoleById(id: number): void {
    let user = this.findById(id);

    if (user.role == UserRole.GENERAL_USER)
      user.role = UserRole.ADMIN_USER // if the user is a regular change user role to admin role
    else
      user.role = UserRole.GENERAL_USER // if the user is already an admin change user role to regular user
  }

  // retrieves one user, identified by a given id
  public findById(id: number): User {
    return this._users.find(user => user.id == id)
  }

  // track the id-s of new users
  private nextId(): number {
    return this.lastId++;
  }

}
