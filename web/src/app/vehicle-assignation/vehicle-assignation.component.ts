import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Client} from "../../shared/models/client";

@Component({
  selector: 'app-vehicle-assignation',
  templateUrl: './vehicle-assignation.component.html',
  styleUrls: ['./vehicle-assignation.component.scss']
})
export class VehicleAssignationComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<VehicleAssignationComponent>,
              @Inject(MAT_DIALOG_DATA) public client: Client,
              public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  cancel() {
    this.dialogRef.close();
  }

  assignVehicle() {
    this.dialogRef.close();
  }

  closeDetails() {
    this.dialogRef.close();
  }
}
