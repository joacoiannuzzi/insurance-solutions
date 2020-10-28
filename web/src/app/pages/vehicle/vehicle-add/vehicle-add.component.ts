import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {VehicleService} from "../../../../shared/services/vehicle.service";
import {Vehicle} from "../../../../shared/models/vehicle";
import {ClientService} from "../../../../shared/services/client.service";
import {Category} from "../../../../shared/models/category";
import {Client} from "../../../../shared/models/client";
import {Observable} from "rxjs";
import {map, startWith} from "rxjs/operators";
import {alreadyExistsValidator} from "../../../../shared/directives/alreadyExistsValidator.directive";
import {checkExistsValidator} from "../../../../shared/directives/checkExistsValidator.directive";

@Component({
  selector: 'app-vehicle-add',
  templateUrl: './vehicle-add.component.html',
  styleUrls: ['./vehicle-add.component.scss']
})
export class VehicleAddComponent implements OnInit {
  vehicleForm: FormGroup;
  categories: Category[] = [Category.CAR, Category.TRUCK, Category.VAN, Category.MOTORCYCLE];
  categoryLabels: string[] = ['Automóvil', 'Camión', 'Camioneta', 'Moto'];
  clients: Client[] = [];
  filteredOptions: Observable<Client[]>;
  vehicleList = [];
  loading = true;

  constructor(
    public dialogRef: MatDialogRef<VehicleAddComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Vehicle,
    public vehicleService: VehicleService,
    public clientService: ClientService
  ) {
  }

  ngOnInit(): void {
    this.getVehicles();
    this.getClients();
  }

  private createForm() {
    this.vehicleForm = new FormGroup({
      licensePlate: new FormControl('', [
        Validators.required,
        Validators.minLength(6),
        //  To accept license plates from 1994-2016 (argentine format) and 2016-present (mercosur format).
        Validators.pattern('(([A-Z]){2}([0-9]){3}([A-Z]){2})|(([A-Z]){3}([0-9]){3})|(([a-z]){3}([0-9]){3})'),
        alreadyExistsValidator(this.vehicleList, 'licensePlate')
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
      client: new FormControl('', [
        checkExistsValidator(this.clients, 'dni')
      ])
    });
  }

  private getVehicles() {
    this.vehicleService.vehicles.subscribe((res) => {
      this.vehicleList = res;
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

  get client() {
    return this.vehicleForm.get('client');
  }

  get invalid() {
    return this.vehicleForm.invalid
  }

  close(): void {
    this.dialogRef.close();
  }

  saveVehicle() {
    if (this.vehicleForm.valid) {
      // Se mapea todos los values del form al objeto vehicle
      Object.keys(this.vehicleForm.value).map((key) => this.data[key] = this.vehicleForm.value[key]);

      this.vehicleService.save(this.data).subscribe(res => {
        //The 'empty' Client passed in constructor has an id of -1.
        // So if that Client has not been modified, it doesn't execute the following if statement.
        if (res && this.data.client?.id !== -1 && this.data.client?.id !== undefined) {
          this.clientService.assignVehicle(this.data.client.id, res.id).subscribe(() => {
            this.dialogRef.close(res);
          })
        } else {
          this.dialogRef.close(res);
        }
      });
    }
  }

  getClients() {
    this.clientService.clients.subscribe((res: Client[]) => {
      this.clients = [...res];
      this.createForm();
      this.createFilteredOptions();
      this.loading = false;
    })
  }

  private createFilteredOptions() {
    this.filteredOptions = this.client.valueChanges
      .pipe(
        startWith(''),
        map(value => {
          return this._filter(value?.lastName ? value.lastName : value);
        })
      );
  }

  private _filter(value: string): Client[] {
    return this.clients.filter(option => option.dni.includes(value));
  }

  displayOption(option: Client) {
    return option.dni;
  }
}
