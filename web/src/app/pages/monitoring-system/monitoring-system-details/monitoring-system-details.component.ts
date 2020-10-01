import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../../../components/confirm-dialog/confirm-dialog.component";
import {MonitoringSystem} from "../../../../shared/models/monitoringSystem";
import {MonitoringSystemService} from "../../../../shared/services/monitoring-system.service";
import {MonitoringSystemAssignationComponent} from "../../vehicle/monitoring-system-assignation/monitoring-system-assignation.component";
import {VehicleService} from "../../../../shared/services/vehicle.service";

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
        /*if (confirmed) {
          this.monitoringSystemService.deleteMonitoringSystem(this.monitoringSystem.id).subscribe(()=>{
            this.dialogRef.close();
          })
        }*/
      })
  }

  updateMonitoringSystem() {
    /*const dialogRef = this.dialog.open(MonitoringSystemUpdateComponent, {
      width: '800px',
      data: this.monitoringSystem
    });
    dialogRef.afterClosed().subscribe();*/
  }

  assignMonitoringSystem() {
    const dialogRef = this.dialog.open(MonitoringSystemAssignationComponent, {
      width: '800px',
      data: this.monitoringSystem
    });
    dialogRef.afterClosed().subscribe();
  }

  unassignMonitoringSystem() {
    this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro de que desea desasignar al servicio de monitoreo " + this.monitoringSystem.name + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        console.log(confirmed)
        if (confirmed) {
          this.monitoringSystemService.unassignVehicle(this.monitoringSystem.id);
        }
      })
  }
}
