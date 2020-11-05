import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DrivingProfile } from 'src/shared/models/drivingProfile';
import { DrivingProfileService } from 'src/shared/services/driving-profile.service';
import { VehicleService } from 'src/shared/services/vehicle.service';
import { DrivingProfileUpdateComponent } from '../driving-profile-update/driving-profile-update.component';
@Component({
  selector: 'app-driving-profile-details',
  templateUrl: './driving-profile-details.component.html',
  styleUrls: ['./driving-profile-details.component.scss']
})
export class DrivingProfileDetailsComponent implements OnInit {
  drivingProfileForm: FormGroup;
  driPro: DrivingProfile;


constructor(
    public dialogRef: MatDialogRef<DrivingProfileDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DrivingProfile,
    public drivingProfileService: DrivingProfileService,
    public dialog: MatDialog,
    public vehicleService: VehicleService
  ) {
    this.driPro = {...data};
   }

  ngOnInit(): void {
  }

  get avgDailyDrivingTime() { return this.data.avgDailyDrivingTime; }
  get avgSpeed() { return this.data.avgSpeed; }
  get startDate() { return this.data.startDate; }
  get finishDate() { return this.data.finishDate; }
  get maxSpeed() { return this.data.maxSpeed; }
  get minSpeed() { return this.data.minSpeed; }
  get totalDrivingTime() { return this.data.totalDrivingTime; }

  close(): void {
    this.dialogRef.close();
  }


 update(driPro: DrivingProfile) {
    const dialogRef = this.dialog.open(DrivingProfileUpdateComponent, {
      width: '800px',
      data: driPro
    });
    dialogRef.afterClosed ().subscribe((res) => {
    });
  }
}
