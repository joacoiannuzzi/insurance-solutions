import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Client} from "../../shared/models/client";

@Component({
  selector: 'app-client-details',
  templateUrl: './client-details.component.html',
  styleUrls: ['./client-details.component.scss']
})
export class ClientDetailsComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ClientDetailsComponent>,
              @Inject(MAT_DIALOG_DATA) public client: Client) { }

  ngOnInit(): void {
  }

  seeVehicles() {
    this.dialogRef.close();
    //Open the other one
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
