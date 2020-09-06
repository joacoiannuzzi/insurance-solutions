import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Client} from "../../shared/models/client";

@Component({
  selector: 'app-client-vehicles',
  templateUrl: './client-vehicles.component.html',
  styleUrls: ['./client-vehicles.component.scss']
})
export class ClientVehiclesComponent implements OnInit {
  displayedColumns: string[] = ['vehicle','firstName','options'];

  constructor(public dialogRef: MatDialogRef<ClientVehiclesComponent>,
              @Inject(MAT_DIALOG_DATA) public client: Client) { }

  ngOnInit(): void {
  }

  closeVehicles() {
    this.dialogRef.close();
  }

  addVehicle() {
    //Add vehicle
  }

  deleteVehicle(element: any) {
    //Delete vehicle
  }
}
