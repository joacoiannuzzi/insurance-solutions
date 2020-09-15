import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {ClientService} from "../../../../shared/services/client.service";
import {Client} from "../../../../shared/models/client";
import {MatDialog} from '@angular/material/dialog';
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import {ClientDetailsComponent} from '../client-details/client-details.component';
import {ConfirmDialogComponent} from '../../../components/confirm-dialog/confirm-dialog.component';
import {ClientUpdateComponent} from '../client-update/client-update.component';
import {ClientAddComponent} from "../client-add/client-add.component";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-user-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.scss']
})
export class ClientListComponent implements OnInit, AfterViewInit  {
  displayedColumns: string[] = ['firstName', 'lastName', 'dni', 'phoneNumber', 'mail', 'options'];
  clients: Client[];
  dataSource: MatTableDataSource<Client> = new MatTableDataSource<Client>([]);
  loading: boolean = true;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private clientService: ClientService, public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.getClients();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  getClients() {
    this.loading = true;
    this.clientService.clients.subscribe((data) => {
      this.clients = data;
      this.loading = false;
      this.dataSource.data = this.clients;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(ClientAddComponent, {
      width: '800px',
      data: new Client(null, "", "", "", "", "")
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getClients();
    });
  }

  deleteClient(client: Client) {
    this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro de que desea eliminar al cliente " + client.firstName + " " + client.lastName + " con dni " + client.dni + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.clientService.delete(client).subscribe((res) => {
            this.getClients();
          });
        }
      })
  }

  updateClient(client: Client) {
    const dialogRef = this.dialog.open(ClientUpdateComponent, {
      width: '800px',
      data: client
    });
    dialogRef.afterClosed().subscribe((confirmed) => {
      if (confirmed) {
        this.getClients();
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
