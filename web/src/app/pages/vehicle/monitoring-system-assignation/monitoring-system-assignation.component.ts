import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {MonitoringSystem} from "../../../../shared/models/monitoringSystem";
import {map, startWith} from "rxjs/operators";
import {Vehicle} from "../../../../shared/models/vehicle";
import {MonitoringSystemService} from "../../../../shared/services/monitoring-system.service";
import {VehicleService} from "../../../../shared/services/vehicle.service";

@Component({
  selector: 'app-monitoring-system-assignation',
  templateUrl: './monitoring-system-assignation.component.html',
  styleUrls: ['./monitoring-system-assignation.component.scss']
})
export class MonitoringSystemAssignationComponent implements OnInit {
  myControl: FormControl;
  options: MonitoringSystem[] = [];
  filteredOptions: Observable<MonitoringSystem[]>;

  constructor(public dialogRef: MatDialogRef<MonitoringSystemAssignationComponent>,
              @Inject(MAT_DIALOG_DATA) public vehicle: Vehicle,
              public vehicleService: VehicleService,
              public monitoringSystemService: MonitoringSystemService,
              public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.myControl = new FormControl('', [Validators.required]);

    this.getMonitoringSystems();
  }

  getMonitoringSystems() {
    this.monitoringSystemService.getVehicleLess().subscribe((res: MonitoringSystem[]) => {
      this.options = [...res];
    })
    this.filteredOptions = this.myControl.valueChanges
      .pipe(
        startWith(''),
        map(value => {
          return this._filter(value?.name ? value.name : value);
        })
      );
  }

  private _filter(value: string): MonitoringSystem[] {
    if(value){
      const filterValue = value.toLowerCase();
      return this.options.filter(option => option.name.toLowerCase().includes(filterValue));
    } else {
      return this.options;
    }
  }

  get invalid() {
    return this.myControl.invalid
  }

  displayOption(option: MonitoringSystem) {
    return option.name;
  }

  cancel() {
    this.dialogRef.close();
  }

  assignMonitoringSystem() {
    if (this.myControl.valid) {
      this.vehicleService.assignMonitoringSystem(this.myControl.value?.id, this.vehicle.id).subscribe((res) => {
        this.dialogRef.close(res);
      });
    }
  }

  closeDetails() {
    this.dialogRef.close();
  }
}
