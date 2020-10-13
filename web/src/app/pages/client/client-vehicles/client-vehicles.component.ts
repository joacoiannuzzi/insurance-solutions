import {AfterViewInit, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Client} from "../../../../shared/models/client";
import {Vehicle} from "../../../../shared/models/vehicle";
import {ConfirmDialogComponent} from "../../../components/confirm-dialog/confirm-dialog.component";
import {VehicleAssignationComponent} from "../vehicle-assignation/vehicle-assignation.component";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {ClientService} from "../../../../shared/services/client.service";
import {MatPaginator} from "@angular/material/paginator";
import {VehicleService} from "../../../../shared/services/vehicle.service";

@Component({
  selector: 'app-client-vehicles',
  templateUrl: './client-vehicles.component.html',
  styleUrls: ['./client-vehicles.component.scss']
})
export class ClientVehiclesComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['licensePlate', 'firstName', 'options'];
  vehicles: Vehicle[];
  dataSource: MatTableDataSource<Vehicle> = new MatTableDataSource<Vehicle>([]);
  loading: boolean = true;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(public dialogRef: MatDialogRef<ClientVehiclesComponent>,
              @Inject(MAT_DIALOG_DATA) public client: Client,
              public dialog: MatDialog,
              private clientService: ClientService,
              private vehiclesService: VehicleService,
  ) {
  }

  ngOnInit(): void {
    this.getVehicles();
    this.paginator._intl.itemsPerPageLabel = 'Elementos por página';
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  getVehicles() {
    this.loading = true;
    this.clientService.vehicles(this.client).subscribe((data) => {
      this.vehicles = data;
      this.loading = false;
      this.dataSource.data = this.vehicles;
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
    addVehicleRef.afterClosed().subscribe((res) => {
      if (res) {
        this.getVehicles()
      }
    });
  }

  deleteVehicle(element: Vehicle) {
    this.dialog.open(ConfirmDialogComponent, {
      width: '800px',
      data: "¿Esta seguro de que desea eliminar el vehículo dominio " + element.licensePlate + " del cliente " + this.client.firstName + " " + this.client.lastName + "?"
    })
      .afterClosed()
      .subscribe((confirmed: Boolean) => {
        if (confirmed) {
          this.clientService.deleteVehicle(this.client.id, element.id).subscribe((res) => {
            if (res) {
              this.getVehicles();
              this.vehiclesService.findAll().subscribe();
            }
          })
        }
      });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}
