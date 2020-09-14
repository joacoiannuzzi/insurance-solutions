import {Component, OnInit, ViewChild} from '@angular/core';
import {ClientService} from "../../../../shared/services/client.service";
import {Client} from "../../../../shared/models/client";
import {MatDialog} from '@angular/material/dialog';
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import { ClientDetailsComponent } from '../client-details/client-details.component';
import { ConfirmDialogComponent } from '../../../components/confirm-dialog/confirm-dialog.component';
import { ClientUpdateComponent } from '../client-update/client-update.component';
import {ClientAddComponent} from "../client-add/client-add.component";

@Component({
  selector: 'app-user-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.scss']
})
export class ClientListComponent implements OnInit {
  displayedColumns: string[] = ['firstName', 'lastName', 'dni', 'phoneNumber', 'mail', 'options'];
  clients: Client[];
  dataSource: MatTableDataSource<Client>;
  loading: boolean = true;

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(private clientService: ClientService, public dialog: MatDialog) { }

  ngOnInit(): void {
    this.getClients();
  }

  getClients() {
    this.loading = true;
    this.clientService.clients.subscribe((data) => {
      this.clients = data;
      this.loading = false;
      this.dataSource = new MatTableDataSource<Client>(this.clients);
      this.dataSource.sort = this.sort;
    });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(ClientAddComponent, {
      width: '800px',
      data: new Client(null,"","","","","")
    });

    dialogRef.afterClosed().subscribe(result => {
      // this.clients.push(result);
      // this.dataSource._updateChangeSubscription();
      this.getClients();
    });
  }

  deleteClient(client: Client) {
    this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro de que desea eliminar al cliente " + client.firstName + " " + client.lastName + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        console.log(confirmed)
        if (confirmed) {
          this.clientService.delete(client).subscribe((res) => {
            this.clients = res;
            this.getClients();
          });
        }
      })
  }

  updateClient(client: Client)  {
   const dialogRef = this.dialog.open(ClientUpdateComponent, {
      width: '800px',
      data: client
    });
    dialogRef.afterClosed()
      .subscribe((confirmed: Boolean) => {
        if (confirmed) {
        this.clientService.update(client).subscribe(res => {
          this.getClients();
        })
      }
    })
  }

  openClientDetails(element: Client): void {
    this.dialog.open(ClientDetailsComponent, {
      width: '800px',
      data: element
    });
  }
}
