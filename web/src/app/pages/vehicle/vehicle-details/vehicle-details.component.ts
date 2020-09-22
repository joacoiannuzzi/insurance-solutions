import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Vehicle} from "../../../../shared/models/vehicle";
import {VehicleService} from "../../../../shared/services/vehicle.service";
import {ConfirmDialogComponent} from "../../../components/confirm-dialog/confirm-dialog.component";

@Component({
  selector: 'app-vehicle-details',
  templateUrl: './vehicle-details.component.html',
  styleUrls: ['./vehicle-details.component.scss']
})
export class VehicleDetailsComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<VehicleDetailsComponent>,
              @Inject(MAT_DIALOG_DATA) public vehicle: Vehicle,
              public dialog: MatDialog,
              public vehicleService: VehicleService
  ) {}

  ngOnInit(): void {
  }

  seeProfiles() {
    this.dialogRef.close();
    //this.openProfiles(this.vehicle);
  }

  // private openProfiles(vehicle: Vehicle): void {
  //   this.dialog.open(VehicleProfileComponent, {
  //     width: '800px',
  //     data: vehicle
  //   });
  // }

  closeDetails() {
    this.dialogRef.close();
  }

  deleteVehicle() {
    this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro de que desea eliminar al vehículo de patente" + this.vehicle.licensePlate + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.vehicleService.delete(this.vehicle).subscribe();
        }
      })
  }

  updateVehicle() {
    // const dialogRef = this.dialog.open(VehicleUpdateComponent, {
    //   width: '800px',
    //   data: this.vehicle
    // });
    // dialogRef.afterClosed().subscribe();
  }

}
