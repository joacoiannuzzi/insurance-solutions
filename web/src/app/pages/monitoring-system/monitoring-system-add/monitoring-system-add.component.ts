import { Validators } from '@angular/forms';
import { MonitoringSystemService } from './../../../../shared/services/monitoring-system.service';
import { FormGroup, FormControl } from '@angular/forms';
import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { inject } from '@angular/core/testing';

@Component({
  selector: 'app-monitoring-system-add',
  templateUrl: './monitoring-system-add.component.html',
  styleUrls: ['./monitoring-system-add.component.scss']
})
export class MonitoringSystemAddComponent implements OnInit {
  monitoringSystemForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<MonitoringSystemAddComponent>,
    @Inject(MAT_DIALOG_DATA) public data: MonitoringSystemAddComponent,
    public monitoringSystemService: MonitoringSystemService,
  ) { }


  ngOnInit() {
    this.monitoringSystemForm = new FormGroup({
      serviceName: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
      ]),

    })
  }
  get serviceName(){return this.monitoringSystemForm.get('serviceName')}

  saveMonitoringService() {
    if (this.monitoringSystemForm.valid){
      Object.keys(this.monitoringSystemForm.value).map((key) => this.data[key] = this.monitoringSystemForm.value[key]);
      this.monitoringSystemService.save(this.data).subscribe(res => {
        this.dialogRef.close(res);
        this.monitoringSystemService.monitoringSystems.subscribe();
      })
}
  }

}
