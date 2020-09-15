
import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Vehicle} from "../../../../shared/models/vehicle";
import {ConfirmDialogComponent} from "../../../components/confirm-dialog/confirm-dialog.component";
import {VehicleUpdateComponent} from "../vehicle-update/vehicle-update.component";
import {VehicleService} from "../../../../shared/services/vehicle.service";

@Component({
  selector: 'app-vehicle-details',
  templateUrl: './vehicle-details.component.html',
  styleUrls: ['./vehicle-details.component.scss']
})
export class VehicleDetailsComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<VehicleDetailsComponent>,
              @Inject(MAT_DIALOG_DATA) public vehicle: Vehicle,
              public dialog: MatDialog,
              public vehicleService: VehicleService,
  ) {
  }

  ngOnInit(): void {
  }

  // seeVehicles() {
  //   this.dialogRef.close();
  //   this.openVehicles(this.vehicle);
  // }

  // private openVehicles(vehicle: Vehicle): void {
  //   this.dialog.open(VehicleVehiclesComponent, {
  //     width: '800px',
  //     data: vehicle
  //   });
  // }

  closeDetails() {
    this.dialogRef.close();
  }


  updateVehicle() {
    const dialogRef = this.dialog.open(VehicleUpdateComponent, {
      width: '800px',
      data: this.vehicle
    });
    dialogRef.afterClosed().subscribe();
  }
}
