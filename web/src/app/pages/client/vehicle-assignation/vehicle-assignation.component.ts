import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Client} from "../../../../shared/models/client";
import {FormControl, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {map, startWith} from "rxjs/operators";
import {VehicleService} from "../../../../shared/services/vehicle.service";
import {ClientService} from "../../../../shared/services/client.service";
import {Vehicle} from "../../../../shared/models/vehicle";

@Component({
  selector: 'app-vehicle-assignation',
  templateUrl: './vehicle-assignation.component.html',
  styleUrls: ['./vehicle-assignation.component.scss']
})
export class VehicleAssignationComponent implements OnInit {
  myControl: FormControl;
  options: Vehicle[] = [];
  filteredOptions: Observable<Vehicle[]>;

  constructor(public dialogRef: MatDialogRef<VehicleAssignationComponent>,
              @Inject(MAT_DIALOG_DATA) public client: Client,
              public vehiclesService: VehicleService,
              public clientService: ClientService,
              public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.myControl = new FormControl('', [Validators.required]);

    this.getVehicles();
  }

  getVehicles() {
    this.vehiclesService.vehicles.subscribe((res: Vehicle[]) => {
      this.options = res;
    })
    this.filteredOptions = this.myControl.valueChanges
      .pipe(
        startWith(''),
        map(value => this._filter(value))
      );
  }

  private _filter(value: string): Vehicle[] {
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.licensePlate.toLowerCase().includes(filterValue));
  }

  get invalid() {
    return this.myControl.invalid
  }

  displayOption(option: Vehicle) {
    return option.licensePlate;
  }

  cancel() {
    this.dialogRef.close();
    console.log("HERE on add: ", this.options);
  }

  assignVehicle() {
    if (this.myControl.valid) {
      this.clientService.assignVehicle(this.client.id, this.myControl.value?.id).subscribe(() => {
        this.dialogRef.close();
        this.clientService.vehicles(this.client).subscribe(() => {
          this.getVehicles();
        });
      });
    }
  }

  closeDetails() {
    this.dialogRef.close();
  }
}
