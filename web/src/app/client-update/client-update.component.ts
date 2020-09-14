import { Component, Inject } from '@angular/core';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import {Client} from '../../shared/models/client'
import { ClientService } from '../../shared/services/client.service';

@Component({
  selector: 'client-update.component',
  templateUrl: 'client-update.component.html',
})
export class ClientUpdateComponent {
  constructor(
    public dialogRef: MatDialogRef<ClientUpdateComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Client,
    public clientService: ClientService,

  ) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

  updateClient() {
    console.log(this.data)
    this.clientService.update(this.data).subscribe(res => {
      this.dialogRef.close(res);
    })
  }

}
