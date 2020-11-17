import {Component, Inject, OnInit} from '@angular/core';
import {Sensor} from "../../../../shared/models/sensor";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {SensorService} from "../../../../shared/services/sensor.service";

@Component({
  selector: 'app-sensor-update',
  templateUrl: './sensor-update.component.html',
  styleUrls: ['./sensor-update.component.scss']
})
export class SensorUpdateComponent implements OnInit {
  sensor: Sensor;
  sensorForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<SensorUpdateComponent>,
              @Inject(MAT_DIALOG_DATA) private data: Sensor,
              private sensorService: SensorService,) {
    this.sensor = {...data};
  }

  ngOnInit(): void {

    this.sensorForm = new FormGroup({
      name: new FormControl(this.sensor.name, [
        Validators.required,
        Validators.maxLength(20),
        Validators.minLength(2),
        Validators.pattern('^[a-zA-Z0-9 ]*$')
      ]),
      model: new FormControl(this.sensor.model, [
        Validators.required,
        //Minimum eight characters, at least one letter, one number and one special character
        Validators.pattern('^[a-zA-Z0-9 ]*$')
      ]),
    });
  }

  get name() {
    return this.sensorForm.get('name');
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

  updateSensor() {
    if (this.sensorForm.valid) {
      // Se mapea todos los values del form al objeto sensor
      Object.keys(this.sensorForm.value).map((key) => this.sensor[key] = this.sensorForm.value[key]);

      this.sensorService.update(this.sensor).subscribe(
        res => {
          this.dialogRef.close(res);
        },
        () => {
          //No quiero que cierre el modal, asi que no hago nada. El snackbar lo lanza desde el service
        })
    }
  }

}
