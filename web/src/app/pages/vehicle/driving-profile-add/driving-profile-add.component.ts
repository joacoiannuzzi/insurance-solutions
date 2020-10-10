import { DrivingProfileService } from './../../../../shared/services/driving-profile.service';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { DrivingProfile } from './../../../../shared/models/drivingProfile';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, OnInit, Inject } from '@angular/core';

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
  ) { }

  ngOnInit() {
    this.drivingProfileForm = new FormGroup({
      avgDailyDrivingTime: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.pattern("^[0-9]*$")
      ]),
      avgSpeed: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.pattern("^[0-9]*$")
      ]),

      finishDate: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.pattern("^(((0[1-9]|[12][0-9]|30)[-/]?(0[13-9]|1[012])|31[-/]?(0[13578]|1[02])|(0[1-9]|1[0-9]|2[0-8])[-/]?02)[-/]?[0-9]{4}|29[-/]?02[-/]?([0-9]{2}(([2468][048]|[02468][48])|[13579][26])|([13579][26]|[02468][048]|0[0-9]|1[0-6])00))$")
      ]),

      maxSpeed: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.pattern("^[0-9]*$")
      ]),

      minSpeed: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.pattern("^[0-9]*$")
      ]),
      startDate: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.pattern("^(((0[1-9]|[12][0-9]|30)[-/]?(0[13-9]|1[012])|31[-/]?(0[13578]|1[02])|(0[1-9]|1[0-9]|2[0-8])[-/]?02)[-/]?[0-9]{4}|29[-/]?02[-/]?([0-9]{2}(([2468][048]|[02468][48])|[13579][26])|([13579][26]|[02468][048]|0[0-9]|1[0-6])00))$")
      ]),
      totalDrivingTime: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.pattern("^[0-9]*$")
      ]),
    })
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

  saveDrivingProfile() {
    if (this.drivingProfileForm.valid) {
      Object.keys(this.drivingProfileForm.value).map((key) =>
        this.data[key] = this.drivingProfileForm.value[key]);
      this.drivingProfileService.save(this.data).subscribe(res => {
        this.dialogRef.close(res);
      })
    }
  }
}
