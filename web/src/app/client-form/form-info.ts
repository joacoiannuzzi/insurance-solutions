import { Component, Inject } from '@angular/core';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import {Client} from '../../shared/models/client'
import { ClientService } from '../../shared/services/client.service';

@Component({
  selector: 'form-info',
  templateUrl: 'form-info.html',
})

export class FormInfo {
  constructor(
    public dialogRef: MatDialogRef<FormInfo>,
    @Inject(MAT_DIALOG_DATA) public data: Client,
    public clientService: ClientService,

  ) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

  saveClient() {
    console.log(this.data)
    this.clientService.save(this.data).subscribe(res => {
      this.dialogRef.close(res);

    })
  }

  updateClient() {
    console.log(this.data)
    this.clientService.update(this.data).subscribe(res => {
      this.dialogRef.close(res);
    })
  }

}
