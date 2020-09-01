import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {Client} from "../../shared/models/client";
import {ClientService} from "../../shared/services/client.service";

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit {

  newUser: Client = new Client('', '', '');

  @Input()
  users: Client[];

  @Output()
  userCreated = new EventEmitter();

  constructor(private userService: ClientService) { }

  ngOnInit(): void {
  }

  createUser(): void {
    this.saveUser(this.newUser)
  }

  saveUser(user: Client): void {
    this.userService.save(user).subscribe(data => {
      this.users.push(data);
      this.newUser = new Client('', '', '');
      this.userCreated.emit({id: data.id});
    })
  }

}
