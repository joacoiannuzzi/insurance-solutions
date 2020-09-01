import { Component, OnInit } from '@angular/core';
import {ClientService} from "../../shared/services/client.service";
import {Client} from "../../shared/models/client";

@Component({
  selector: 'app-user-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.scss']
})
export class ClientListComponent implements OnInit {
  displayedColumns: string[] = ['firstName', 'lastName', 'dni', 'phoneNumber', 'mail', 'options'];
  clients: Client[];
  loading: boolean = true;

  constructor(private userService: ClientService) {}

  ngOnInit(): void {
    this.userService.clients.subscribe((data) => {
      this.clients = data;
      this.loading = false;
    });
  }
}
