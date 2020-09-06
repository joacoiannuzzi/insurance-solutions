import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Client} from "../../shared/models/client";
import {ClientVehiclesComponent} from "../client-vehicles/client-vehicles.component";

@Component({
  selector: 'app-client-details',
  templateUrl: './client-details.component.html',
  styleUrls: ['./client-details.component.scss']
})
export class ClientDetailsComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ClientDetailsComponent>,
              @Inject(MAT_DIALOG_DATA) public client: Client,
              public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  seeVehicles() {
    this.dialogRef.close();
    this.openVehicles(this.client);
  }

  private openVehicles(client: Client): void {
    this.dialog.open(ClientVehiclesComponent, {
      width: '3290px',
      data: client
    });
  }

  editClientDetails() {
    this.dialogRef.close();
    //Edit client details
  }

  deleteClientDetails() {
    this.dialogRef.close();
    //Detele client details
  }

  closeDetails() {
    this.dialogRef.close();
  }
}
