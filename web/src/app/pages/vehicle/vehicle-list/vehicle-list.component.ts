import { MatPaginator } from '@angular/material/paginator';
import { VehicleDetailsComponent } from './../vehicle-details/vehicle-details.component';
import { VehicleUpdateComponent } from './../vehicle-update/vehicle-update.component';
import { ConfirmDialogComponent } from './../../../../app/components/confirm-dialog/confirm-dialog.component';
import { Vehicle } from './../../../../shared/models/vehicle';
import { VehicleService } from './../../../../shared/services/vehicle.service';
import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-vehicle-list',
  templateUrl: './vehicle-list.component.html',
  styleUrls: ['./vehicle-list.component.scss']
})
export class VehicleListComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['licensePlate', 'category', 'brand', 'model'];
  vehicles: Vehicle[];
  dataSource: MatTableDataSource<Vehicle> = new MatTableDataSource<Vehicle>();
  loading: boolean = true;

  constructor(private vehicleService: VehicleService, public dialog: MatDialog) { }

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit(): void {
    this.getVehicles();
    
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  getVehicles() {
    this.loading = true;
    this.vehicleService.vehicles.subscribe((data) => {
      this.vehicles = data;
      this.loading = false;
      this.dataSource.data = this.vehicles;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
// openDialog(): void {
//     const dialogRef = this.dialog.open(VehicleAddComponent, {
//       width: '800px',
//       data: new Vehicle(0,"","","","","","")
//     });

  //   dialogRef.afterClosed().subscribe(result => {
  //     this.getVehicles();
  //   });
  // }

  deleteClient(vehicle: Vehicle) {
    this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro de que desea eliminar al vehículo de marca " + vehicle.brand + " y modelo " + vehicle.model + ", con placa" + vehicle.licensePlate + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.vehicleService.delete(vehicle).subscribe((res) => {
            this.getVehicles();
          });
        }
      })
  }

updateVehicle(vehicle: Vehicle){ 
    const dialogRef = this.dialog.open(VehicleUpdateComponent, {
      width: '800px',
      data: vehicle
    });
    dialogRef.afterClosed().subscribe((confirmed) => {
      if (confirmed) {
        this.getVehicles();
      }
    })
  }

  openVehicleDetails(element: Vehicle): void {
    this.dialog.open(VehicleDetailsComponent, {
      width: '800px',
      data: element
    });
  }
}
