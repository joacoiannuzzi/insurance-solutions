import { DrivingProfileAddComponent } from './../../vehicle/driving-profile-add/driving-profile-add.component';
import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../../../components/confirm-dialog/confirm-dialog.component";
import {MonitoringSystem} from "../../../../shared/models/monitoringSystem";
import {MonitoringSystemService} from "../../../../shared/services/monitoring-system.service";
import {VehicleService} from "../../../../shared/services/vehicle.service";
import {MonitoringSystemVehicleAssignationComponent} from "../monitoring-system-vehicle-assignation/monitoring-system-vehicle-assignation.component";
import {MonitoringSystemUpdateComponent} from "../monitoring-system-update/monitoring-system-update.component";
import { DrivingProfile } from 'src/shared/models/drivingProfile';

@Component({
  selector: 'app-monitoring-system-details',
  templateUrl: './monitoring-system-details.component.html',
  styleUrls: ['./monitoring-system-details.component.scss']
})
export class MonitoringSystemDetailsComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<MonitoringSystemDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public monitoringSystem: MonitoringSystem,
    public vehicleService: VehicleService,
    public monitoringSystemService: MonitoringSystemService,
    public dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
  }

  closeDetails() {
    this.dialogRef.close();
  }

  deleteMonitoringSystem() {
    this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro de que desea eliminar al servicio de monitoreo " + this.monitoringSystem.name + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        console.log(confirmed)
        if (confirmed) {
          this.monitoringSystemService.delete(this.monitoringSystem).subscribe(()=>{
            this.dialogRef.close();
          })
        }
      })
  }

  updateMonitoringSystem() {
    const dialogRef = this.dialog.open(MonitoringSystemUpdateComponent, {
      width: '800px',
      data: this.monitoringSystem
    });
    dialogRef.afterClosed().subscribe((res) => {
      this.monitoringSystem = res;
    });
  }

  assignMonitoringSystem() {
    const dialogRef = this.dialog.open(MonitoringSystemVehicleAssignationComponent, {
      width: '1000px',
      data: this.monitoringSystem
    });
    dialogRef.afterClosed().subscribe((res)=>{
      this.dialogRef.close(res);
    });
  }

  createDrivingProfile(): void {
     const driPro =  new DrivingProfile(0, 0, 0, "", 0, 0, '', 0)
     driPro.monitoringSystemId = this.monitoringSystem.id
    const dialogRef = this.dialog.open(DrivingProfileAddComponent, {
      data: driPro,
      width: '1000px',
    });
    dialogRef.afterClosed().subscribe(() => {
      this.closeDetails();
    })
  }

  unassignMonitoringSystem() {
    this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro de que desea desasignar al servicio de monitoreo " + this.monitoringSystem.name + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.vehicleService.vehicles.subscribe(res => {
            const vehicle = res.find(v => v.monitoringSystem?.id === this.monitoringSystem.id);
            vehicle && this.vehicleService.unassignMonitoringSystem(vehicle.id).subscribe(() => {
              this.monitoringSystemService.findAll().subscribe();
              this.monitoringSystem.assigned = false;
            });
          })
        }
      })
  }
}
