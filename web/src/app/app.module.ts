import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {ClientService} from "../shared/services/client.service";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatTableModule} from '@angular/material/table';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatSortModule} from "@angular/material/sort";
import {MatDialogModule } from '@angular/material/dialog';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {ClientListComponent} from "./pages/client/client-list/client-list.component";
import {NavbarComponent} from "./components/navbar/navbar.component";
import {ClientDetailsComponent} from "./pages/client/client-details/client-details.component";
import {ClientVehiclesComponent} from "./pages/client/client-vehicles/client-vehicles.component";
import {VehicleAssignationComponent} from "./pages/client/vehicle-assignation/vehicle-assignation.component";
import {ConfirmDialogComponent} from "./components/confirm-dialog/confirm-dialog.component";
import {ClientUpdateComponent} from "./pages/client/client-update/client-update.component";
import {ClientAddComponent} from "./pages/client/client-add/client-add.component";
import {VehicleService} from "../shared/services/vehicle.service";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatSnackBarModule} from '@angular/material/snack-bar';

@NgModule({
  declarations: [
    AppComponent,
    ClientListComponent,
    NavbarComponent,
    ClientAddComponent,
    ClientDetailsComponent,
    ClientVehiclesComponent,
    VehicleAssignationComponent,
    ConfirmDialogComponent,
    ClientUpdateComponent
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
    ReactiveFormsModule,
    MatPaginatorModule,
    MatGridListModule,
    MatSnackBarModule
  ],
  providers: [ClientService, VehicleService],
  bootstrap: [AppComponent]
})
export class AppModule { }
