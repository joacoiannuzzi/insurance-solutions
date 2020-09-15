import {Component, Inject, OnInit} from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import {Vehicle} from '../../../../shared/models/vehicle'
import {VehicleService} from '../../../../shared/services/vehicle.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'vehicle-update.component',
  templateUrl: 'vehicle-update.component.html',
  styleUrls: ['./vehicle-update.component.scss']
})
export class VehicleUpdateComponent implements OnInit{
  vehicle: Vehicle;
  vehicleForm: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<VehicleUpdateComponent>,
    @Inject(MAT_DIALOG_DATA) private data: Vehicle,
    private vehicleService: VehicleService,
  ) {
    this.vehicle = {...data};
  }

  ngOnInit(): void {
    this.vehicleForm = new FormGroup({
      licensePlate: new FormControl(this.vehicle.licensePlate, [
        Validators.required,
        Validators.minLength(2),
        //  To accept only alphabets and space.
        Validators.pattern('^[a-zA-Z ]*$')
      ]),
      category: new FormControl(this.vehicle.category, [
        Validators.required,
        Validators.minLength(2),
        //  To accept only alphabets and space.
        Validators.pattern('^[a-zA-Z ]*$')
      ]),
      model: new FormControl(this.vehicle.model, [
        Validators.required,
        Validators.maxLength(9),
        Validators.minLength(7)
      ]),
      brand: new FormControl(this.vehicle.brand, [
        Validators.required,
      ]),
    });
  }

  get licensePlate() { return this.vehicleForm.get('firstName'); }

  get category() { return this.vehicleForm.get('lastName'); }

  get model() { return this.vehicleForm.get('dni'); }

  get brand() { return this.vehicleForm.get('phoneNumber'); }

  get client() { return this.vehicleForm.get('mail'); }

  get invalid() { return this.vehicleForm.invalid }

  close(): void {
    this.dialogRef.close();
  }

  updateVehicle() {
    if (this.vehicleForm.valid) {
      // Se mapea todos los values del form al objeto vehicle
      Object.keys(this.vehicleForm.value).map((key) => this.vehicle[key] = this.vehicleForm.value[key]);

      this.vehicleService.update(this.vehicle).subscribe(res => {
        this.dialogRef.close(res);
      })
    }
  }

}
