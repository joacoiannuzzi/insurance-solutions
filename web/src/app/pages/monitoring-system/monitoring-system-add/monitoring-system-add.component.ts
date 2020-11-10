import {MonitoringSystem} from '../../../../shared/models/monitoringSystem';
import {Validators} from '@angular/forms';
import {MonitoringSystemService} from '../../../../shared/services/monitoring-system.service';
import {FormGroup, FormControl} from '@angular/forms';
import {Component, OnInit, Inject, AfterContentInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {alreadyExistsValidator} from "../../../../shared/directives/alreadyExistsValidator.directive";
import {SensorService} from "../../../../shared/services/sensor.service";
import {Sensor} from "../../../../shared/models/sensor";
import {Observable} from "rxjs";
import {map, startWith} from "rxjs/operators";
import {checkExistsValidator} from "../../../../shared/directives/checkExistsValidator.directive";
import {InsuranceCompany} from "../../../../shared/models/insuranceCompany";
import {InsuranceCompanyService} from "../../../../shared/services/insurance-company.service";

@Component({
  selector: 'app-monitoring-system-add',
  templateUrl: './monitoring-system-add.component.html',
  styleUrls: ['./monitoring-system-add.component.scss']
})
export class MonitoringSystemAddComponent implements OnInit, AfterContentInit {
  monitoringSystemForm: FormGroup;
  monitoringSystemList: MonitoringSystem[] = [];
  sensorList: Sensor[] = [];
  insuranceCompanyList: InsuranceCompany[] = [];
  filteredSensors: Observable<Sensor[]>;
  filteredInsuranceCompanies: Observable<InsuranceCompany[]>
  loading;

  constructor(
    public dialogRef: MatDialogRef<MonitoringSystemAddComponent>,
    @Inject(MAT_DIALOG_DATA) public data: MonitoringSystem,
    public monitoringSystemService: MonitoringSystemService,
    public sensorService: SensorService,
    public insuranceCompanyService: InsuranceCompanyService
  ) {
    this.loading = true;
  }


  ngOnInit() {
    this.getMonitoringSystems();
    this.getSensors();
    this.getInsuranceCompanies();
  }

  ngAfterContentInit(): void {
    this.createForm();
    this.createFilteredSensors();
    this.createFilteredInsuranceCompanies();
    this.loading = false;
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

  saveMonitoringSystem() {
    if (this.monitoringSystemForm.valid) {

      Object.keys(this.monitoringSystemForm.value).map((key) => this.data[key] = this.monitoringSystemForm.value[key]);

      this.monitoringSystemService.save(this.data).subscribe(res => {
        this.dialogRef.close(res);
      });
    }
  }

  private getSensors() {
    this.sensorService.sensors.subscribe(data => {
      this.sensorList = data;
    })
  }

  private getInsuranceCompanies() {
    this.insuranceCompanyService.insuranceCompanies.subscribe(data => {
      this.insuranceCompanyList = data;
    })
  }

  private getSensorsAndInsuranceCompanies() {
    //Con promises

    // let sensorPromise = this.sensorService.findAll().toPromise();
    // let insuranceCompanyPromise = this.insuranceCompanyService.findAll().toPromise();
    //
    // Promise.all([sensorPromise, insuranceCompanyPromise]).then((values) => {
    //   this.sensorList = [...values[0]];
    //   this.insuranceCompanyList = [...values[1]];
    //
    // })

    //Alternativa con subscribers pero deprecated

    // let sensorPromise = this.sensorService.sensors
    // let insuranceCompanyPromise = this.insuranceCompanyService.insuranceCompanies
    //
    // combineLatest(insuranceCompanyPromise, sensorPromise).subscribe((data) => {
    //   this.insuranceCompanyList = [...data[0]];
    //   this.sensorList = [...data[1]];
    //   this.createForm();
    //   this.createFilteredSensors();
    //   this.createFilteredInsuranceCompanies();
    //   this.loading = false;
    // })
  }

  private createForm() {
    this.monitoringSystemForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        alreadyExistsValidator(this.monitoringSystemList, 'name')
      ]),
      sensor: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        checkExistsValidator(this.sensorList, 'name')
      ]),
      monitoringCompany: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        checkExistsValidator(this.insuranceCompanyList, 'name')
      ]),
    })
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

  displaySensor(option: Sensor) {
    return option.name;
  }

  displayInsuranceCompany(option: InsuranceCompany) {
    return option.name;
  }

  private createFilteredInsuranceCompanies() {
    this.filteredInsuranceCompanies = this.monitoringCompany.valueChanges
      .pipe(
        startWith(''),
        map(value => {
          return this._filterInsuranceCompanies(value?.name ? value?.name : value);
        })
      );
  }

  private _filterInsuranceCompanies(value: string): InsuranceCompany[] {
    return this.insuranceCompanyList.filter(option => option.name.includes(value));
  }
}
