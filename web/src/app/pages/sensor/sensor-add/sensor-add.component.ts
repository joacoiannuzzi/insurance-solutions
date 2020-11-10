import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Sensor} from "../../../../shared/models/sensor";
import {SensorService} from "../../../../shared/services/sensor.service";
import {alreadyExistsValidator} from "../../../../shared/directives/alreadyExistsValidator.directive";
import {InsuranceCompany} from "../../../../shared/models/insuranceCompany";


@Component({
  selector: 'app-sensor-add',
  templateUrl: './sensor-add.component.html',
  styleUrls: ['./sensor-add.component.scss']
})
export class SensorAddComponent implements OnInit {

  sensorForm: FormGroup;
  sensorList: Sensor[] = [];

  constructor(public dialogRef: MatDialogRef<SensorAddComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Sensor,
              public sensorService: SensorService,
  ) {
  }

  ngOnInit(): void {
    this.getSensors();
    this.createForm()
  }

  private getSensors() {
    this.sensorService.sensors.subscribe((res) => {
      this.sensorList = res;
    })
  }

  get name() {
    return this.sensorForm.get('name')
  }

  get model() {
    return this.sensorForm.get('model');
  }

  get invalid() {
    return this.sensorForm.invalid
  }

  close(): void {
    this.dialogRef.close();
  }

  saveSensor() {
    if (this.sensorForm.valid) {
      // Se mapea todos los values del form al objeto user
      Object.keys(this.sensorForm.value).map((key) => this.data[key] = this.sensorForm.value[key]);

      this.sensorService.save(this.data).subscribe(
        () => this.dialogRef.close(),
        () => {
          // Solo catchea el error. No hace nada mas ya que no quiero que cierre el dialogo
          // en caso de error. El mismo service muestra la snackbar de error
        }
      )
    }
  }


  private createForm() {
    this.sensorForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.minLength(2),
        Validators.pattern('^[a-zA-Z0-9]*$'),
      ]),
      model: new FormControl('', [
        Validators.required,
        //Minimum eight characters, at least one letter, one number and one special character
        Validators.pattern('^[a-zA-Z0-9]*$')
      ]),
    });
  }

}
