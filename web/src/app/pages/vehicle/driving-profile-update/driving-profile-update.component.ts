import {DrivingProfileService} from '../../../../shared/services/driving-profile.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {DrivingProfile} from '../../../../shared/models/drivingProfile';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Component, Inject, OnInit} from '@angular/core';
import {VehicleService} from 'src/shared/services/vehicle.service';

@Component({
  selector: 'app-driving-profile-update',
  templateUrl: './driving-profile-update.component.html',
  styleUrls: ['./driving-profile-update.component.scss']
})
export class DrivingProfileUpdateComponent implements OnInit {
  drivingProfileForm: FormGroup;
  driPro: DrivingProfile;


  constructor(
    public dialogRef: MatDialogRef<DrivingProfileUpdateComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DrivingProfile,
    public drivingProfileService: DrivingProfileService,
    public vehicleService: VehicleService
  ) {
    this.driPro = {...data};
  }

  ngOnInit(): void {

    let startDate: string = this.data.startDate.split('T')[0];
    let finishDate: string = this.data.finishDate.split('T')[0];

    this.drivingProfileForm = new FormGroup({
      avgDailyDrivingTime: new FormControl(this.data.avgDailyDrivingTime, [
        Validators.required,
        Validators.min(0),
        Validators.minLength(1),
        Validators.pattern('^[0-9]*$')
      ]),
      avgSpeed: new FormControl(this.data.avgSpeed, [
        Validators.required,
        Validators.min(0),
        Validators.minLength(1),
        Validators.pattern('^[0-9]*$')
      ]),
      finishDate: new FormControl(finishDate, [
        Validators.required,
      ]),
      maxSpeed: new FormControl(this.data.maxSpeed, [
        Validators.required,
        Validators.minLength(1),
        Validators.min(0),
        Validators.pattern('^[0-9]*$')
      ]),
      minSpeed: new FormControl(this.data.minSpeed, [
        Validators.required,
        Validators.minLength(1),
        Validators.min(0),
        Validators.pattern('^[0-9]*$')
      ]),
      startDate: new FormControl(startDate, [
        Validators.required,
      ]),
      totalDrivingTime: new FormControl(this.data.totalDrivingTime, [
        Validators.required,
        Validators.minLength(1),
        Validators.min(0),
        Validators.pattern('^[0-9]*$')
      ]),
    });
  }

  get avgDailyDrivingTime() {
    return this.drivingProfileForm.get('avgDailyDrivingTime');
  }

  get avgSpeed() {
    return this.drivingProfileForm.get('avgSpeed');
  }

  get startDate() {
    return this.drivingProfileForm.get('startDate');
  }

  get finishDate() {
    return this.drivingProfileForm.get('finishDate');
  }

  get maxSpeed() {
    return this.drivingProfileForm.get('maxSpeed');
  }

  get minSpeed() {
    return this.drivingProfileForm.get('minSpeed');
  }

  get totalDrivingTime() {
    return this.drivingProfileForm.get('totalDrivingTime');
  }

  get invalid() {
    return this.drivingProfileForm.invalid
  }

  close(): void {
    this.dialogRef.close();
  }

  updateDrivingProfile() {
    if (this.drivingProfileForm.valid) {
      Object.keys(this.drivingProfileForm.value).map((key) =>
        this.data[key] = this.drivingProfileForm.value[key]);
      this.vehicleService.vehicles.subscribe(res => {
        const vehicle = res.find(v => v.monitoringSystem?.id === this.data.monitoringSystemId);
        this.drivingProfileService.save(this.data, vehicle).subscribe(res => {
          this.dialogRef.close(res);
        });
      });

    }
  }

  get today() {
    let date = new Date();
    let month = date.getMonth() + 1;
    return date.getFullYear() + '-' + month + '-' + date.getDate();
  }
}
