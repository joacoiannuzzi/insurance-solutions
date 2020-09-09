import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Client} from "../../shared/models/client";
import {Vehicle} from "../../shared/models/vehicle";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";
import {VehicleAssignationComponent} from "../vehicle-assignation/vehicle-assignation.component";

@Component({
  selector: 'app-client-vehicles',
  templateUrl: './client-vehicles.component.html',
  styleUrls: ['./client-vehicles.component.scss']
})
export class ClientVehiclesComponent implements OnInit {
  displayedColumns: string[] = ['vehicle','firstName','options'];

  constructor(public dialogRef: MatDialogRef<ClientVehiclesComponent>,
              @Inject(MAT_DIALOG_DATA) public client: Client,
              public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  closeVehicles() {
    this.dialogRef.close();
  }

  addVehicle() {
    this.dialog.open(VehicleAssignationComponent, {
      width: '3290px',
      data: this.client
    })
  }

  deleteVehicle(element: Vehicle) {
    this.dialog.open(ConfirmDialogComponent, {
      width: '3290px',
      data: "¿Esta seguro de que desea eliminar el vehiculo dominio " + element.licensePlate + " del cliente " + this.client.firstName + " " + this.client.lastName + "?"
    })
      .afterClosed()
      .subscribe((confirmed: Boolean) => {
        if (confirmed) {
          //DELETE VEHICLE
        }
      });
  }
}
