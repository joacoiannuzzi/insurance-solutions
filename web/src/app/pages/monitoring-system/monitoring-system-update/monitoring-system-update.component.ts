import { MonitoringSystemService } from './../../../../shared/services/monitoring-system.service';
import { MonitoringSystem } from './../../../../shared/models/monitoringSystem';
import {Component, Inject, OnInit} from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import {FormControl, FormGroup, Validators} from "@angular/forms";


@Component({
  selector: 'monitoring-system-update',
  templateUrl: './monitoring-system-update.component.html',
  styleUrls: ['./monitoring-system-update.component.scss']
})

export class MonitoringSystemUpdateComponent implements OnInit {
  moSys: MonitoringSystem;
  monitoringSystemForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<MonitoringSystemUpdateComponent>,
    @Inject(MAT_DIALOG_DATA) public data: MonitoringSystem,
    public monitoringSystemService: MonitoringSystemService,
  ) {
    this.moSys = { ...data };
   }


  ngOnInit() {
    this.monitoringSystemForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        Validators.pattern('^[a-zA-Z ]*$')
      ]),
      sensor: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        Validators.pattern('^[a-zA-Z ]*$')
      ]),
      monitoringCompany: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        Validators.pattern('^[a-zA-Z ]*$')
      ]),

    })
  }
  get name() { return this.monitoringSystemForm.get('name');}
  get sensor() { return this.monitoringSystemForm.get('sensor');}
  get monitoringCompany() { return this.monitoringSystemForm.get('monitoringCompany');}
  get invalid() { return this.monitoringSystemForm.invalid }

  close(): void {
    this.dialogRef.close();
  }

  updateMonitoringSystem() {
    if (this.monitoringSystemForm.valid){
      Object.keys(this.monitoringSystemForm.value).map((key) => this.data[key] = this.monitoringSystemForm.value[key]);
      this.monitoringSystemService.save(this.data).subscribe(res => {
        this.dialogRef.close(res);
        this.monitoringSystemService.monitoringSystems.subscribe();
      })
    }
  }


}
