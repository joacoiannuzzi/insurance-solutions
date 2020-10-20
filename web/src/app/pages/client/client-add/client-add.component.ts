import {Component, Inject, OnInit} from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import {Client} from '../../../../shared/models/client'
import {ClientService} from '../../../../shared/services/client.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {alreadyExistsValidator} from "../../../../shared/directives/alreadyExistsValidator.directive";

@Component({
  selector: 'client-add',
  templateUrl: 'client-add.component.html',
  styleUrls: ['./client-add.component.scss']
})

export class ClientAddComponent implements OnInit {
  clientForm: FormGroup;
  clientList: Client[] = [];

  constructor(
    public dialogRef: MatDialogRef<ClientAddComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Client,
    public clientService: ClientService
  ) {
  }

  ngOnInit(): void {
    this.getClients();

    this.clientForm = new FormGroup({
      firstName: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        //  To accept only alphabets and space.
        Validators.pattern('^[a-zA-Z ]*$')
      ]),
      lastName: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        //  To accept only alphabets and space.
        Validators.pattern('^[a-zA-Z ]*$')
      ]),
      dni: new FormControl('', [
        Validators.required,
        Validators.maxLength(9),
        Validators.minLength(7),
        alreadyExistsValidator(this.clientList, 'dni')
      ]),
      phoneNumber: new FormControl('', [
        Validators.required,
      ]),
      mail: new FormControl('', [
        Validators.required,
        Validators.email,
      ]),
    });
  }

  private getClients() {
    this.clientService.clients.subscribe((res) => {
      this.clientList = res;
    })
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

  saveClient() {
    if (this.clientForm.valid) {
      // Se mapea todos los values del form al objeto client
      Object.keys(this.clientForm.value).map((key) => this.data[key] = this.clientForm.value[key]);

      this.clientService.save(this.data).subscribe(() => {
        this.dialogRef.close();
      })
    }
  }
}
