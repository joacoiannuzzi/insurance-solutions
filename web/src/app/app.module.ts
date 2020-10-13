import { MonitoringSystemUpdateComponent } from './pages/monitoring-system/monitoring-system-update/monitoring-system-update.component';
import { MonitoringSystemAddComponent } from './pages/monitoring-system/monitoring-system-add/monitoring-system-add.component';
import { MonitoringSystemService } from '../shared/services/monitoring-system.service';
import { VehicleListComponent } from './pages/vehicle/vehicle-list/vehicle-list.component';
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
import { VehicleAddComponent } from './pages/vehicle/vehicle-add/vehicle-add.component';
import {MatSelectModule} from "@angular/material/select";
import { DrivingProfilesComponent } from './pages/vehicle/driving-profiles/driving-profiles.component';
import { VehicleDetailsComponent } from './pages/vehicle/vehicle-details/vehicle-details.component';
import { VehicleUpdateComponent } from './pages/vehicle/vehicle-update/vehicle-update.component';
import { MonitoringSystemListComponent } from './pages/monitoring-system/monitoring-system-list/monitoring-system-list.component';
import { MonitoringSystemDetailsComponent } from './pages/monitoring-system/monitoring-system-details/monitoring-system-details.component';
import { MonitoringSystemVehicleAssignationComponent } from './pages/monitoring-system/monitoring-system-vehicle-assignation/monitoring-system-vehicle-assignation.component';
import { MonitoringSystemAssignationComponent } from './pages/vehicle/monitoring-system-assignation/monitoring-system-assignation.component';
import { InsuranceCompanyListComponent } from './pages/insurance-company/insurance-company-list/insurance-company-list.component';
import { InsuranceCompanyAddComponent } from './pages/insurance-company/insurance-company-add/insurance-company-add.component';
import {InsuranceCompanyService} from "../shared/services/insurance-company.service";

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    ClientListComponent,
    ClientAddComponent,
    ClientDetailsComponent,
    ClientVehiclesComponent,
    ClientUpdateComponent,
    VehicleListComponent,
    VehicleAssignationComponent,
    ConfirmDialogComponent,
    ClientUpdateComponent,
    VehicleAddComponent,
    DrivingProfilesComponent,
    VehicleAddComponent,
    VehicleDetailsComponent,
    VehicleUpdateComponent,
    MonitoringSystemListComponent,
    MonitoringSystemUpdateComponent,
    MonitoringSystemDetailsComponent,
    MonitoringSystemVehicleAssignationComponent,
    MonitoringSystemAssignationComponent,
    MonitoringSystemAddComponent,
    InsuranceCompanyListComponent,
    InsuranceCompanyAddComponent
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
    MatSnackBarModule,
    MatSelectModule
  ],
  providers: [ClientService, VehicleService, MonitoringSystemService, InsuranceCompanyService],
  bootstrap: [AppComponent]
})
export class AppModule { }
