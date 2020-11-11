import {MonitoringSystemService} from '../../../../shared/services/monitoring-system.service';
import {MonitoringSystem} from '../../../../shared/models/monitoringSystem';
import {AfterContentInit, Component, Inject, OnInit} from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {alreadyExistsValidator} from "../../../../shared/directives/alreadyExistsValidator.directive";
import {Observable} from "rxjs";
import {Sensor} from "../../../../shared/models/sensor";
import {SensorService} from "../../../../shared/services/sensor.service";
import {checkExistsValidator} from "../../../../shared/directives/checkExistsValidator.directive";
import {map, startWith} from "rxjs/operators";


@Component({
  selector: 'monitoring-system-update',
  templateUrl: './monitoring-system-update.component.html',
  styleUrls: ['./monitoring-system-update.component.scss']
})

export class MonitoringSystemUpdateComponent implements OnInit, AfterContentInit {
  moSys: MonitoringSystem;
  monitoringSystemForm: FormGroup;
  monitoringSystemList: MonitoringSystem[] = [];
  sensorList: Sensor[] = [];
  filteredSensors: Observable<Sensor[]>;
  loading;

  constructor(
    public dialogRef: MatDialogRef<MonitoringSystemUpdateComponent>,
    @Inject(MAT_DIALOG_DATA) public data: MonitoringSystem,
    public monitoringSystemService: MonitoringSystemService,
    public sensorService: SensorService
  ) {
    this.moSys = {...data};
    this.loading = true;
  }


  ngOnInit() {
    this.getMonitoringSystems();
    this.getSensors();
  }

  ngAfterContentInit() {
    this.createForm();
    this.createFilteredSensors();
    this.loading = false;
  }

  private createFilteredSensors() {
    this.filteredSensors = this.sensor.valueChanges
      .pipe(
        startWith(''),
        map(value => {
          return this._filterSensors(value?.name ? value?.name : value);
        })
      );
  }

  private _filterSensors(value: string): Sensor[] {
    return this.sensorList.filter(option => option.name.includes(value));
  }

  private createForm() {
    this.monitoringSystemForm = new FormGroup({
      name: new FormControl(this.data.name, [
        Validators.required,
        Validators.minLength(2),
        alreadyExistsValidator(this.monitoringSystemList, 'name')
      ]),
      sensor: new FormControl(this.data?.sensor?.name ? this.data.sensor : '', [
        Validators.required,
        Validators.minLength(2),
        checkExistsValidator(this.sensorList, 'name')
      ]),
      monitoringCompany: new FormControl(this.data.monitoringCompany, [
        Validators.required,
        Validators.minLength(2)
      ]),
    })
  }

  private getSensors() {
    this.sensorService.sensors.subscribe(data => {
      this.sensorList = data;
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

  displaySensor(option: Sensor) {
    return option.name;
  }
}
