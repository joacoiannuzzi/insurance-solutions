import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {Vehicle} from "../../../../shared/models/vehicle";
import {Observable} from "rxjs";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {MonitoringSystem} from "../../../../shared/models/monitoringSystem";
import {VehicleService} from "../../../../shared/services/vehicle.service";
import {map, startWith} from "rxjs/operators";
import {MonitoringSystemService} from "../../../../shared/services/monitoring-system.service";

@Component({
  selector: 'app-monitoring-system-vehicle-assignation',
  templateUrl: './monitoring-system-vehicle-assignation.component.html',
  styleUrls: ['./monitoring-system-vehicle-assignation.component.scss']
})
export class MonitoringSystemVehicleAssignationComponent implements OnInit {
  myControl: FormControl;
  options: Vehicle[] = [];
  filteredOptions: Observable<Vehicle[]>;

  constructor(public dialogRef: MatDialogRef<MonitoringSystemVehicleAssignationComponent>,
              @Inject(MAT_DIALOG_DATA) public monitoringSystem: MonitoringSystem,
              public vehiclesService: VehicleService,
              public monitoringSystemService: MonitoringSystemService,
              public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.myControl = new FormControl('', [Validators.required]);

    this.getVehicles();
  }

  getVehicles() {
    this.vehiclesService.getMonitoringSystemLess().subscribe((res: Vehicle[]) => {
      this.options = [...res];
    })
    this.filteredOptions = this.myControl.valueChanges
      .pipe(
        startWith(''),
        map(value => {
          return this._filter(value?.licensePlate ? value.licensePlate : value);
        })
      );
  }

  private _filter(value: string): Vehicle[] {
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.licensePlate.toLowerCase().includes(filterValue));
  }

  get invalid() {
    return this.myControl.invalid
  }

  displayOption(option: Vehicle) {
    return option.licensePlate;
  }

  assignVehicle() {
    if (this.myControl.valid) {
      this.vehiclesService.assignVehicle(this.monitoringSystem.id, this.myControl.value?.id).subscribe(() => {
        this.monitoringSystem.assigned = true;
        this.dialogRef.close(this.monitoringSystem);
      });
    }
  }

  closeDetails() {
    this.dialogRef.close();
  }
}
