import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {ClientService} from "../shared/services/client.service";
import { ClientListComponent } from './client-list/client-list.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatTableModule} from '@angular/material/table';
import { NavbarComponent } from './navbar/navbar.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatCardModule} from "@angular/material/card";
import { FormInfo } from "./client-form/form-info";
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatSortModule} from "@angular/material/sort";
import { ClientDetailsComponent } from './client-details/client-details.component';
import {MatDialogModule } from '@angular/material/dialog';
import { ClientVehiclesComponent } from './client-vehicles/client-vehicles.component';
import { VehicleAssignationComponent } from './vehicle-assignation/vehicle-assignation.component';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import {MatAutocompleteModule} from "@angular/material/autocomplete";

import { DeleteConfirmationComponent } from './client-list/delete-confirmation/delete-confirmation.component';
@NgModule({
  declarations: [
    AppComponent,
    ClientListComponent,
    NavbarComponent,
    FormInfo,
    ClientDetailsComponent,
    DeleteConfirmationComponent,
    ClientVehiclesComponent,
    VehicleAssignationComponent,
    ConfirmDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    MatTableModule,
    MatToolbarModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatInputModule,
    MatDialogModule,
    MatIconModule,
    MatSortModule,
    MatAutocompleteModule,
    ReactiveFormsModule
  ],
  providers: [ClientService],
  bootstrap: [AppComponent]
})
export class AppModule { }
