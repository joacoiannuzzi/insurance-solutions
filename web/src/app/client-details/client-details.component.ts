import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef } from "@angular/material/dialog";
import { Client } from "../../shared/models/client";

@Component({
  selector: 'app-client-details',
  templateUrl: './client-details.component.html',
  styleUrls: ['./client-details.component.scss']
})
export class ClientDetailsComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ClientDetailsComponent>,
              @Inject(this.data) public client: Client) { }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
