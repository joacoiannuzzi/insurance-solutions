import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {User} from "../../shared/models/user";
import {UserService} from "../../shared/services/user.service";

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit {

  newUser: User = new User('', '', '');

  @Input()
  users: User[];

  @Output()
  userCreated = new EventEmitter();

  constructor(private userService: UserService) { }

  ngOnInit(): void {
  }

  createUser(): void {
    this.saveUser(this.newUser)
  }

  saveUser(user: User): void {
    this.userService.save(user).subscribe(data => {
      this.users.push(data);
      this.newUser = new User('', '', '');
      this.userCreated.emit({id: data.id});
    })
  }

}
