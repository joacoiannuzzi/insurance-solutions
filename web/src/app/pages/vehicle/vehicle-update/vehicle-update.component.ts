import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Vehicle} from "../../../../shared/models/vehicle";
import {VehicleService} from "../../../../shared/services/vehicle.service";
import {Category} from "../../../../shared/models/category";
import {alreadyExistsValidator} from "../../../../shared/directives/alreadyExistsValidator.directive";


@Component({
  selector: 'app-vehicle-update',
  templateUrl: './vehicle-update.component.html',
  styleUrls: ['./vehicle-update.component.scss']
})
export class VehicleUpdateComponent implements OnInit {
  vehicleForm: FormGroup;
  categories: Category[] = [Category.CAR, Category.TRUCK, Category.VAN, Category.MOTORCYCLE];
  categoryLabels: string[] = ['Automóvil', 'Camión', 'Camioneta', 'Moto'];
  vehicleList = [];

  constructor(
    private dialogRef: MatDialogRef<VehicleUpdateComponent>,
    @Inject(MAT_DIALOG_DATA) private vehicle: Vehicle,
    private vehicleService: VehicleService,
  ) {
  }

  ngOnInit(): void {
    this.getVehicles();
    this.vehicleForm = new FormGroup({
      licensePlate: new FormControl(this.vehicle.licensePlate, [
        Validators.required,
        Validators.minLength(6),
        //  To accept license plates from 1994-2016 (argentine format) and 2016-present (mercosur format).
        Validators.pattern('(([A-Z]){2}([0-9]){3}([A-Z]){2})|(([A-Z]){3}([0-9]){3})|(([a-z]){3}([0-9]){3})'),
        alreadyExistsValidator(this.vehicleList, 'licensePlate')
      ]),
      brand: new FormControl(this.vehicle.brand, [
        Validators.required,
        Validators.minLength(1),
        Validators.pattern('([A-Z,a-z, ,-,_,0-9])\\w+')
      ]),
      model: new FormControl(this.vehicle.model, [
        Validators.required,
        Validators.minLength(1),
        Validators.pattern('([A-Z,a-z, ,-,_,0-9])\\w+')
      ]),
      category: new FormControl(this.categories[Vehicle.categoryToInt(this.vehicle.category)], [
        Validators.required
      ]),
    });
  }

  private getVehicles() {
    this.vehicleService.vehicles.subscribe((res) => {
      this.vehicleList = [...res];
      this.vehicleList.splice(this.vehicleList.findIndex(v => v.id === this.vehicle.id), 1)
    })
  }

  get licensePlate() {
    return this.vehicleForm.get('licensePlate');
  }

  get category() {
    return this.vehicleForm.get('category');
  }

  get brand() {
    return this.vehicleForm.get('brand');
  }

  get model() {
    return this.vehicleForm.get('model');
  }

  get invalid() {
    return this.vehicleForm.invalid
  }

  close(): void {
    this.dialogRef.close();
  }

  updateVehicle() {
    if (this.vehicleForm.valid) {
      // Maps all values from form to vehicle object
      Object.keys(this.vehicleForm.value).map((key) => this.vehicle[key] = this.vehicleForm.value[key]);

      this.vehicleService.update(this.vehicle).subscribe(res => {
        this.dialogRef.close(res);
      })
    }
  }
}
