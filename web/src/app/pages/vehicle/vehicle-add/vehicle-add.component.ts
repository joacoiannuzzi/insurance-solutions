import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {VehicleService} from "../../../../shared/services/vehicle.service";
import {Vehicle} from "../../../../shared/models/vehicle";

@Component({
  selector: 'app-vehicle-add',
  templateUrl: './vehicle-add.component.html',
  styleUrls: ['./vehicle-add.component.scss']
})
export class VehicleAddComponent implements OnInit {
  vehicleForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<VehicleAddComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Vehicle,
    public vehicleService: VehicleService,
  ) {
  }

  ngOnInit(): void {
    this.vehicleForm = new FormGroup({
      licensePlate: new FormControl('', [
        Validators.required,
        Validators.minLength(6),
        //  To accept license plates from 1994-2016 (argentine format) and 2016-present (mercosur format).
        Validators.pattern('(([A-Z]){2}([0-9]){3}([A-Z]){2})|(([A-Z]){3}([0-9]){3})')
      ]),
      category: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        //  To accept only alphabets and space.
        Validators.pattern('([A-Z,a-z, ,-,_,0-9])\\w+')
      ]),
      brand: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.pattern('([A-Z,a-z, ,-,_,0-9])\\w+')
      ]),
      model: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.pattern('([A-Z,a-z, ,-,_,0-9])\\w+')
      ]),
    });
  }

  get licensePlate() { return this.vehicleForm.get('licensePlate'); }

  get category() { return this.vehicleForm.get('category'); }

  get brand() { return this.vehicleForm.get('brand'); }

  get model() { return this.vehicleForm.get('model'); }

  get client() { return this.vehicleForm.get('client'); }

  get invalid() { return this.vehicleForm.invalid }

  close(): void {
    this.dialogRef.close();
  }

  saveVehicle() {
    if (this.vehicleForm.valid) {
      // Se mapea todos los values del form al objeto vehicle
      Object.keys(this.vehicleForm.value).map((key) => this.data[key] = this.vehicleForm.value[key]);

      this.vehicleService.save(this.data).subscribe(res => {
        this.dialogRef.close(res);
        this.vehicleService.vehicles.subscribe();
      })
    }
  }

}
