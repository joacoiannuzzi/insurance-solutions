import {Component, OnInit, ViewChild} from '@angular/core';
import {ClientService} from "../../shared/services/client.service";
import {Client} from "../../shared/models/client";
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { FormInfo } from '../client-form/form-info';
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import { ClientDetailsComponent } from '../client-details/client-details.component';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-user-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.scss']
})
export class ClientListComponent implements OnInit {
  displayedColumns: string[] = ['firstName', 'lastName', 'dni', 'phoneNumber', 'mail', 'options'];
  clients: Client[];
  dataSource: MatTableDataSource<Client>
  loading: boolean = true;

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(private clientService: ClientService, public dialog: MatDialog) { }

  ngOnInit(): void {
    this.clientService.clients.subscribe((data) => {
      this.clients = data;
      this.loading = false;
      this.dataSource = new MatTableDataSource<Client>(this.clients);
      this.dataSource.sort = this.sort;
    });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(FormInfo, {
      width: '3290px',
      data: new Client(null,"","","","","")

    });

    dialogRef.afterClosed().subscribe(result => {
      this.clients.push(result)
    });
  }

  deleteClient(client: Client) {
    this.dialog.open(ConfirmDialogComponent, {
      data: "Está seguro de que desea eliminar al cliente " + client.firstName + " " + client.lastName + "?"
    })
      .afterClosed()
      .subscribe((confirmed: Boolean) => {
        if (confirmed) {
          this.clientService.delete(client)
        }
        })
  }

  openClientDetails(element: Client): void {
    this.dialog.open(ClientDetailsComponent, {
      width: '3290px',
      data: element
    });
  }
}
