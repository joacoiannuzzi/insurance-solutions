import {Component, Inject, OnInit} from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import {Client} from '../../../../shared/models/client'
import {ClientService} from '../../../../shared/services/client.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'client-update.component',
  templateUrl: 'client-update.component.html',
  styleUrls: ['./client-update.component.scss']
})
export class ClientUpdateComponent implements OnInit{
  client: Client;
  clientForm: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<ClientUpdateComponent>,
    @Inject(MAT_DIALOG_DATA) private data: Client,
    private clientService: ClientService,
  ) {
    this.client = {...data};
  }

  ngOnInit(): void {
    this.clientForm = new FormGroup({
      firstName: new FormControl(this.client.firstName, [
        Validators.required,
        Validators.minLength(2),
        //  To accept only alphabets and space.
        Validators.pattern('^[a-zA-Z ]*$')
      ]),
      lastName: new FormControl(this.client.lastName, [
        Validators.required,
        Validators.minLength(2),
        //  To accept only alphabets and space.
        Validators.pattern('^[a-zA-Z ]*$')
      ]),
      dni: new FormControl(this.client.dni, [
        Validators.required,
        Validators.maxLength(9),
        Validators.minLength(7)
      ]),
      phoneNumber: new FormControl(this.client.phoneNumber, [
        Validators.required,
      ]),
      mail: new FormControl(this.client.mail, [
        Validators.required,
        Validators.email,
      ]),
    });
  }

  get firstName() { return this.clientForm.get('firstName'); }

  get lastName() { return this.clientForm.get('lastName'); }

  get dni() { return this.clientForm.get('dni'); }

  get phoneNumber() { return this.clientForm.get('phoneNumber'); }

  get mail() { return this.clientForm.get('mail'); }

  get invalid() { return this.clientForm.invalid }

  close(): void {
    this.dialogRef.close();
  }

  updateClient() {
    if (this.clientForm.valid) {
      // Se mapea todos los values del form al objeto client
      Object.keys(this.clientForm.value).map((key) => this.client[key] = this.clientForm.value[key]);

      this.clientService.update(this.client).subscribe(res => {
        this.dialogRef.close(res);
      })
    }
  }

}
