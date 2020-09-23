import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Vehicle} from "../../../../shared/models/vehicle";
import {VehicleService} from "../../../../shared/services/vehicle.service";
import {category} from "../../../../shared/models/category";


@Component({
  selector: 'app-vehicle-update',
  templateUrl: './vehicle-update.component.html',
  styleUrls: ['./vehicle-update.component.scss']
})
export class VehicleUpdateComponent implements OnInit {
  vehicleForm: FormGroup;
  categories: category[] = [category.CAR,category.TRUCK,category.VAN,category.MOTORCYCLE];
  categoryLabels: string[] = ['Automóvil', 'Camión', 'Camioneta', 'Moto'];

  constructor(
    private dialogRef: MatDialogRef<VehicleUpdateComponent>,
    @Inject(MAT_DIALOG_DATA) private vehicle: Vehicle,
    private vehicleService: VehicleService,
  ) {
  }

  ngOnInit(): void {
    this.vehicleForm = new FormGroup({
      licensePlate: new FormControl('', [
        Validators.required,
        Validators.minLength(6),
        //  To accept license plates from 1994-2016 (argentine format) and 2016-present (mercosur format).
        Validators.pattern('(([A-Z]){2}([0-9]){3}([A-Z]){2})|(([A-Z]){3}([0-9]){3})|(([a-z]){3}([0-9]){3})')
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
      category: new FormControl('', [
        Validators.required
      ]),
    });
  }

  get licensePlate() { return this.vehicleForm.get('licensePlate'); }

  get category() { return this.vehicleForm.get('category'); }

  get brand() { return this.vehicleForm.get('brand'); }

  get model() { return this.vehicleForm.get('model'); }

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