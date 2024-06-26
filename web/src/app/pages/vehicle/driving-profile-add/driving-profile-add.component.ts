import { DrivingProfileService } from '../../../../shared/services/driving-profile.service';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { DrivingProfile } from '../../../../shared/models/drivingProfile';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, OnInit, Inject } from '@angular/core';
import { VehicleService } from 'src/shared/services/vehicle.service';

@Component({
  selector: 'app-driving-profile-add',
  templateUrl: './driving-profile-add.component.html',
  styleUrls: ['./driving-profile-add.component.scss']
})
export class DrivingProfileAddComponent implements OnInit {
  drivingProfileForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<DrivingProfileAddComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DrivingProfile,
    public drivingProfileService: DrivingProfileService,
    public vehicleService: VehicleService
  ) { }

  ngOnInit() {
    this.drivingProfileForm = new FormGroup({
      avgDailyDrivingTime: new FormControl('', [
        Validators.required,
        Validators.pattern('^[0-9\.]*$')
      ]),
      avgSpeed: new FormControl('', [
        Validators.required,
        Validators.pattern('^[0-9\.]*$')
      ]),

      finishDate: new FormControl('', [
        Validators.required,
      ]),

      maxSpeed: new FormControl('', [
        Validators.required,
        Validators.pattern('^[0-9\.]*$')
      ]),

      minSpeed: new FormControl('', [
        Validators.required,
        Validators.pattern('^[0-9\.]*$')
      ]),
      startDate: new FormControl('', [
        Validators.required,
      ]),
      totalDrivingTime: new FormControl('', [
        Validators.required,
        Validators.pattern('^[0-9\.]*$')
      ]),
    });
  }

  get avgDailyDrivingTime() { return this.drivingProfileForm.get('avgDailyDrivingTime'); }
  get avgSpeed() { return this.drivingProfileForm.get('avgSpeed'); }
  get startDate() { return this.drivingProfileForm.get('startDate'); }
  get finishDate() { return this.drivingProfileForm.get('finishDate'); }
  get maxSpeed() { return this.drivingProfileForm.get('maxSpeed'); }
  get minSpeed() { return this.drivingProfileForm.get('minSpeed'); }
  get totalDrivingTime() { return this.drivingProfileForm.get('totalDrivingTime'); }
  get invalid() { return this.drivingProfileForm.invalid }

  close(): void {
    this.dialogRef.close();
  }

  get today() {
    let date = new Date();
    let month = date.getMonth() + 1;
    return date.getFullYear() + '-' + month + '-' + date.getDate();
  }

  saveDrivingProfile() {
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
}
