import {Component, OnInit, ViewChild} from '@angular/core';
import {ClientService} from "../../shared/services/client.service";
import {Client} from "../../shared/models/client";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import {MatDialog} from "@angular/material/dialog";
import {ClientDetailsComponent} from "../client-details/client-details.component";

@Component({
  selector: 'app-user-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.scss']
})
export class ClientListComponent implements OnInit {
  displayedColumns: string[] = ['firstName', 'lastName', 'dni', 'phoneNumber', 'mail', 'options'];
  clients: Client[];
  dataSource : MatTableDataSource<Client>
  loading: boolean = true;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private clientService: ClientService, public dialog: MatDialog) {}

  ngOnInit(): void {
    this.clientService.clients.subscribe((data) => {
      this.clients = data;
      this.loading = false;
      this.dataSource = new MatTableDataSource<Client>(this.clients);
      this.dataSource.sort = this.sort;
    });
  }

  openClientDetails(element: Client): void {
    const dialogRef = this.dialog.open(ClientDetailsComponent, {
      width: '250px',
      data: {firstName: element.firstName, lastName: element.lastName, dni: element.dni, phoneNumber: element.phoneNumber, mail: element.mail}
    });
  }

}
