import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {MonitoringSystem} from "../../../../shared/models/monitoringSystem";
import {map, startWith} from "rxjs/operators";
import {Vehicle} from "../../../../shared/models/vehicle";
import {MonitoringSystemService} from "../../../../shared/services/monitoring-system.service";

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
              @Inject(MAT_DIALOG_DATA) public monitoringSystem: MonitoringSystem,
              public vehicle: Vehicle,
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
          return this._filter(value?.serviceName ? value.serviceName : value);
        })
      );
  }

  private _filter(value: string): MonitoringSystem[] {
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.serviceName.toLowerCase().includes(filterValue));
  }

  get invalid() {
    return this.myControl.invalid
  }

  displayOption(option: MonitoringSystem) {
    return option.serviceName;
  }

  cancel() {
    this.dialogRef.close();
  }

  assignMonitoringSystem() {
    if (this.myControl.valid) {
      this.monitoringSystemService.assignVehicle(this.monitoringSystem.id, this.myControl.value?.id).subscribe(() => {
        this.dialogRef.close();
      });
    }
  }

  closeDetails() {
    this.dialogRef.close();
  }
}
