import { DrivingProfileDetailsComponent } from './../driving-profile-details/driving-profile-details.component';
import { DrivingProfileService } from 'src/shared/services/driving-profile.service';
import { AfterViewInit, Component, Inject, OnInit, ViewChild } from '@angular/core';
import { ConfirmDialogComponent } from "../../../components/confirm-dialog/confirm-dialog.component";
import { DrivingProfile } from "../../../../shared/models/drivingProfile";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort } from "@angular/material/sort";
import { MatPaginator } from "@angular/material/paginator";
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from "@angular/material/dialog";
import { Vehicle } from "../../../../shared/models/vehicle";
import { VehicleService } from "../../../../shared/services/vehicle.service";
import { DrivingProfileAddComponent } from '../driving-profile-add/driving-profile-add.component';
import { DrivingProfileUpdateComponent } from '../driving-profile-update/driving-profile-update.component';

@Component({
  selector: 'app-driving-profiles',
  templateUrl: './driving-profiles.component.html',
  styleUrls: ['./driving-profiles.component.scss']
})
export class DrivingProfilesComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = ['drivingProfile', 'options'];
  drivingProfiles: DrivingProfile[];
  dataSource: MatTableDataSource<DrivingProfile> = new MatTableDataSource<DrivingProfile>(this.vehicle.drivingProfiles);
  loading: boolean = true;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  constructor(public dialogRef: MatDialogRef<DrivingProfilesComponent>,
    @Inject(MAT_DIALOG_DATA) public vehicle: Vehicle,
    public dialog: MatDialog,
    public vehicleService: VehicleService,
    public driProService: DrivingProfileService
  ) {
  }

  ngOnInit(): void {
    this.paginator._intl.itemsPerPageLabel = 'Elementos por página';
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  closeDrivingProfiles() {
    this.dialogRef.close();
  }

  deleteDrivingProfile(element: DrivingProfile) {
    this.dialog.open(ConfirmDialogComponent, {
      width: '800px',
      data: "¿Está seguro de que desea eliminar el perfil?"
    })
      .afterClosed()
      .subscribe((confirmed: Boolean) => {
        if (confirmed) {
          this.vehicleService.deleteDrivingProfile(this.vehicle.id, element.id).subscribe(() => {
            this.vehicle.drivingProfiles.splice(this.vehicle.drivingProfiles.findIndex(d => d.id === element.id), 1);
            this.closeDrivingProfiles();
          });
        }
      });
  }

  updateDrivingProfile(driPro: DrivingProfile) {
    const dialogRef = this.dialog.open(DrivingProfileUpdateComponent, {
      width: '800px',
      data: driPro
    });
    dialogRef.afterClosed().subscribe((res) => {
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  viewDrivingProfile(driPro: DrivingProfile) {
    const dialogRef = this.dialog.open(DrivingProfileDetailsComponent, {
      width: '800px',
      data: driPro
    });
    dialogRef.afterClosed().subscribe((res) => {
    });
  }


}
