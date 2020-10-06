import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Client} from "../../../../shared/models/client";
import {ClientVehiclesComponent} from "../client-vehicles/client-vehicles.component";
import {ConfirmDialogComponent} from "../../../components/confirm-dialog/confirm-dialog.component";
import {ClientUpdateComponent} from "../client-update/client-update.component";
import {ClientService} from "../../../../shared/services/client.service";

@Component({
  selector: 'app-client-details',
  templateUrl: './client-details.component.html',
  styleUrls: ['./client-details.component.scss']
})
export class ClientDetailsComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ClientDetailsComponent>,
              @Inject(MAT_DIALOG_DATA) public client: Client,
              public dialog: MatDialog,
              public clientService: ClientService,
  ) {
  }

  ngOnInit(): void {
  }

  seeVehicles() {
    this.dialogRef.close();
    this.openVehicles(this.client);
  }

  private openVehicles(client: Client): void {
    this.dialog.open(ClientVehiclesComponent, {
      width: '800px',
      data: client
    });
  }

  closeDetails() {
    this.dialogRef.close();
  }

  deleteClient() {
    this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro de que desea eliminar al cliente " + this.client.firstName + " " + this.client.lastName + " con dni " + this.client.dni + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        console.log(confirmed)
        if (confirmed) {
          this.clientService.delete(this.client).subscribe();
        }
      })
  }

  updateClient() {
    const dialogRef = this.dialog.open(ClientUpdateComponent, {
      width: '800px',
      data: this.client
    });
    dialogRef.afterClosed().subscribe((res)=>{
      this.client = res;
    });
  }
}
