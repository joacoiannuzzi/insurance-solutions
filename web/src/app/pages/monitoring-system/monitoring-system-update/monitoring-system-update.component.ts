import {MonitoringSystemService} from '../../../../shared/services/monitoring-system.service';
import {MonitoringSystem} from '../../../../shared/models/monitoringSystem';
import {Component, Inject, OnInit} from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {alreadyExistsValidator} from "../../../../shared/directives/alreadyExistsValidator.directive";


@Component({
  selector: 'monitoring-system-update',
  templateUrl: './monitoring-system-update.component.html',
  styleUrls: ['./monitoring-system-update.component.scss']
})

export class MonitoringSystemUpdateComponent implements OnInit {
  moSys: MonitoringSystem;
  monitoringSystemForm: FormGroup;
  monitoringSystemList: MonitoringSystem[] = [];

  constructor(
    public dialogRef: MatDialogRef<MonitoringSystemUpdateComponent>,
    @Inject(MAT_DIALOG_DATA) public data: MonitoringSystem,
    public monitoringSystemService: MonitoringSystemService,
  ) {
    this.moSys = {...data};
  }


  ngOnInit() {
    this.getMonitoringSystems();

    this.monitoringSystemForm = new FormGroup({
      name: new FormControl(this.moSys.name, [
        Validators.required,
        Validators.minLength(2),
        alreadyExistsValidator(this.monitoringSystemList, 'name')
      ]),
      sensor: new FormControl(this.moSys.sensor, [
        Validators.required,
        Validators.minLength(2),
        Validators.pattern('^[a-zA-Z ]*$')
      ]),
      monitoringCompany: new FormControl(this.moSys.monitoringCompany, [
        Validators.required,
        Validators.minLength(2),
        Validators.pattern('^[a-zA-Z ñ]*$')
      ]),

    })
  }

  private getMonitoringSystems() {
    this.monitoringSystemService.monitoringSystems.subscribe((res) => {
      this.monitoringSystemList = res;
    })
  }

  get name() {
    return this.monitoringSystemForm.get('name');
  }

  get sensor() {
    return this.monitoringSystemForm.get('sensor');
  }

  get monitoringCompany() {
    return this.monitoringSystemForm.get('monitoringCompany');
  }

  get invalid() {
    return this.monitoringSystemForm.invalid
  }

  close(): void {
    this.dialogRef.close();
  }

  updateMonitoringSystem() {
    if (this.monitoringSystemForm.valid) {
      Object.keys(this.monitoringSystemForm.value).map((key) => this.data[key] = this.monitoringSystemForm.value[key]);
      this.monitoringSystemService.update(this.data).subscribe(res => {
        this.dialogRef.close(res);
      })
    }
  }


}
