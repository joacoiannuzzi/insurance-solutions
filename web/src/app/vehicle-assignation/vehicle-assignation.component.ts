import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Client} from "../../shared/models/client";
import {FormControl} from "@angular/forms";
import {Observable} from "rxjs";
import {map, startWith} from "rxjs/operators";

@Component({
  selector: 'app-vehicle-assignation',
  templateUrl: './vehicle-assignation.component.html',
  styleUrls: ['./vehicle-assignation.component.scss']
})
export class VehicleAssignationComponent implements OnInit {
  myControl = new FormControl();
  options: string[] = ['One', 'Two', 'Three'];
  filteredOptions: Observable<string[]>;

  constructor(public dialogRef: MatDialogRef<VehicleAssignationComponent>,
              @Inject(MAT_DIALOG_DATA) public client: Client,
              public dialog: MatDialog) { }

  ngOnInit(): void {
    this.filteredOptions = this.myControl.valueChanges
      .pipe(
        startWith(''),
        map(value => this._filter(value))
      );
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  cancel() {
    this.dialogRef.close();
  }

  assignVehicle() {
    this.dialogRef.close();
    //DO VEHICLE ASSIGNATION
  }

  closeDetails() {
    this.dialogRef.close();
  }
}
