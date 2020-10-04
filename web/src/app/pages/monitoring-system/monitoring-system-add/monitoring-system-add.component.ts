import { MonitoringSystem } from './../../../../shared/models/monitoringSystem';
import { Validators } from '@angular/forms';
import { MonitoringSystemService } from './../../../../shared/services/monitoring-system.service';
import { FormGroup, FormControl } from '@angular/forms';
import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-monitoring-system-add',
  templateUrl: './monitoring-system-add.component.html',
  styleUrls: ['./monitoring-system-add.component.scss']
})
export class MonitoringSystemAddComponent implements OnInit {
  monitoringSystemForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<MonitoringSystemAddComponent>,
    @Inject(MAT_DIALOG_DATA) public data: MonitoringSystem,
    public monitoringSystemService: MonitoringSystemService,
  ) { }


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

  saveMonitoringSystem() {
    if (this.monitoringSystemForm.valid){
      Object.keys(this.monitoringSystemForm.value).map((key) => this.data[key] = this.monitoringSystemForm.value[key]);
      this.monitoringSystemService.save(this.data).subscribe(res => {
        this.dialogRef.close(res);
        this.monitoringSystemService.monitoringSystems.subscribe();
      })
    }
  }


}
