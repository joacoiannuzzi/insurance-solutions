import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Vehicle} from "../../../../shared/models/vehicle";
import {VehicleService} from "../../../../shared/services/vehicle.service";
import {ConfirmDialogComponent} from "../../../components/confirm-dialog/confirm-dialog.component";
import {DrivingProfilesComponent} from "../driving-profiles/driving-profiles.component";
import {VehicleUpdateComponent} from "../vehicle-update/vehicle-update.component";
import {MonitoringSystemAssignationComponent} from "../monitoring-system-assignation/monitoring-system-assignation.component";
import {Category} from "../../../../shared/models/category";
import {MonitoringSystemService} from "../../../../shared/services/monitoring-system.service";

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
              public monitoringService: MonitoringSystemService
  ) {
  }

  ngOnInit(): void {
  }

  seeProfiles() {
    this.dialogRef.close();
    this.openProfiles(this.vehicle);
  }

  private openProfiles(vehicle: Vehicle): void {
    this.dialog.open(DrivingProfilesComponent, {
      width: '800px',
      data: vehicle
    });
  }

  closeDetails() {
    this.dialogRef.close();
  }

  deleteVehicle() {
    this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro de que desea eliminar al vehículo de patente" + this.vehicle?.licensePlate + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.vehicleService.delete(this.vehicle).subscribe((res) => {
            this.dialogRef.close(res);
          });
        }
      })
  }

  updateVehicle() {
    const dialogRef = this.dialog.open(VehicleUpdateComponent, {
      width: '800px',
      data: this.vehicle
    });
    dialogRef.afterClosed().subscribe();
  }

  assignMonitoringSystem() {
    const dialogRef = this.dialog.open(MonitoringSystemAssignationComponent, {
      width: '800px',
      data: this.vehicle
    });
    dialogRef.afterClosed().subscribe((result) => {
      this.vehicle.monitoringSystem = result;
    });
  }

  unassignMonitoringSystem() {
    this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro de que desea desasignar al servicio de monitoreo " + this.vehicle.monitoringSystem.name + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.vehicleService.unassignMonitoringSystem(this.vehicle.id).subscribe(res => {
            this.vehicle.monitoringSystem = undefined;
            this.monitoringService.findAll().subscribe();
          });
        }
      })
  }

  categoryToString(category: string | Category) {
    return Vehicle.categoryToString(category);
  }
}
