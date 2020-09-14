import { Component, Inject } from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import {Client} from '../../../../shared/models/client'
import { ClientService } from '../../../../shared/services/client.service';

@Component({
  selector: 'client-add',
  templateUrl: 'client-add.component.html',
  styleUrls: ['./client-add.component.scss']
})

export class ClientAddComponent {
  constructor(
    public dialogRef: MatDialogRef<ClientAddComponent>,
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
      this.clientService.clients.subscribe();
    })
  }

  updateClient() {
    console.log(this.data)
    this.clientService.update(this.data).subscribe(res => {
      this.dialogRef.close(res);
    })
  }

}
