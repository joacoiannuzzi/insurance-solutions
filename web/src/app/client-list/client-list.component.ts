import { Component, OnInit } from '@angular/core';
import {ClientService} from "../../shared/services/client.service";
import {Client} from "../../shared/models/client";

@Component({
  selector: 'app-user-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.scss']
})
export class ClientListComponent implements OnInit {
  displayedColumns: string[] = ['Nombre', 'Apellido', 'DNI', 'Telefono', 'Mail', 'Opciones'];
  columnsToDisplay: string[] = this.displayedColumns.slice();
  clients: Client[];

  constructor(private userService: ClientService) {}

  ngOnInit(): void {
    this.userService.findAll().subscribe((data) => {
      this.clients = data;
    });
  }
}
