import { Component, OnInit } from '@angular/core';
import {UsersService} from "../../../services/users/users.service";
import {User, UserRole} from "../../../models/user";
import {UsersSbService} from "../../../services/users/users-sb.service";
import {EducationSbService} from "../../../services/users/education-sb.service";
import {Education} from "../../../models/education";
import {FacultySbService} from "../../../services/users/faculty-sb.service";
import {Faculty} from "../../../models/faculty";
import {parse} from "@angular/compiler/src/render3/view/style_parser";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {
  constructor(private usersService: UsersSbService, private educationService: EducationSbService,
              private facultyService: FacultySbService) {}

  get users(): User[] {
    return this.usersService.findAll();
  }

  get educations(): Education[]{
    return this.educationService.findAll()
  }

  ngOnInit(): void {}

  email: string = '';
  password: string = '';
  firstName: string = '';
  lastName: string = '';
  education: string;
  admin: boolean = false;

  public createUser(): void {
    const array = this.education.split(" ")
    const id = parseInt(array[0])
    const name = array[1]
    const newEducation = new Education(id, name)
    const newUser = new User(
      null,
      this.email,
      this.password,
      this.firstName,
      this.lastName,
      newEducation,
      id,
      this.admin ? UserRole.ADMIN_USER : UserRole.GENERAL_USER
    )

    console.log(newUser)
    // save the user using the service
    this.usersService.save(newUser);
    // $(`#test`).modal('hide')
  }

  loadFacultyInSelect(){

  }


  deleteUser(id: number): void {
    console.log(id)
    const userToBeDeleted: User = this.usersService.findById(id);
    if (confirm(`Are you sure you want to delete ${userToBeDeleted.firstName} ?`)) {
      this.usersService.deleteById(id)

    }
  }

  changeUserRole(id: number): void {
    const userToBeUpdated: User = this.usersService.findById(id);
    if (confirm(`Are you sure you want to change the role of ${userToBeUpdated.firstName} ?`)) {
      // change the role to other role
      userToBeUpdated.role = userToBeUpdated.role == 0 ? 1 : 0;
      this.usersService.save(userToBeUpdated)
    }
  }

}
