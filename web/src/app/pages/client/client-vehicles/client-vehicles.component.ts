import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Client} from "../../../../shared/models/client";
import {Vehicle} from "../../../../shared/models/vehicle";
import {ConfirmDialogComponent} from "../../../components/confirm-dialog/confirm-dialog.component";
import {VehicleAssignationComponent} from "../vehicle-assignation/vehicle-assignation.component";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {ClientService} from "../../../../shared/services/client.service";
import {VehicleService} from "../../../../shared/services/vehicle.service";

@Component({
  selector: 'app-client-vehicles',
  templateUrl: './client-vehicles.component.html',
  styleUrls: ['./client-vehicles.component.scss']
})
export class ClientVehiclesComponent implements OnInit {
  displayedColumns: string[] = ['vehicle', 'firstName', 'options'];
  vehicles: Vehicle[];
  dataSource: MatTableDataSource<Vehicle>;
  loading: boolean = true;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(public dialogRef: MatDialogRef<ClientVehiclesComponent>,
              @Inject(MAT_DIALOG_DATA) public client: Client,
              public dialog: MatDialog,
              private clientService: ClientService,
              private vehicleService: VehicleService,
  ) {
  }

  ngOnInit(): void {
    this.getVehicles();
  }

  getVehicles() {
    this.loading = true;
    this.clientService.vehicles(this.client).subscribe((data: Vehicle[]) => {
      this.vehicles = data;
      this.loading = false;
      this.dataSource = new MatTableDataSource<Vehicle>(this.vehicles);
      this.dataSource.sort = this.sort;
    });
  }

  closeVehicles() {
    this.dialogRef.close();
  }

  addVehicle() {
    const addVehicleRef = this.dialog.open(VehicleAssignationComponent, {
      width: '800px',
      data: this.client
    });

    addVehicleRef.afterClosed().subscribe(() => {
      this.vehicleService.vehicles;
      this.getVehicles()
    });
  }

  deleteVehicle(element: Vehicle) {
    this.dialog.open(ConfirmDialogComponent, {
      width: '800px',
      data: "¿Esta seguro de que desea eliminar el vehiculo dominio " + element.licensePlate + " del cliente " + this.client.firstName + " " + this.client.lastName + "?"
    })
      .afterClosed()
      .subscribe((confirmed: Boolean) => {
        if (confirmed) {
          this.clientService.deleteVehicle(this.client.id, element.id).subscribe(() => {
            this.vehicleService.vehicles;
            this.getVehicles();
          })
        }
      });
  }
}
