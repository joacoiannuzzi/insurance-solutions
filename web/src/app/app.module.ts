import {DrivingProfileDetailsComponent} from './pages/vehicle/driving-profile-details/driving-profile-details.component';
import {DrivingProfileUpdateComponent} from './pages/vehicle/driving-profile-update/driving-profile-update.component';
import {DrivingProfileService} from '../shared/services/driving-profile.service';
import {DrivingProfileAddComponent} from './pages/vehicle/driving-profile-add/driving-profile-add.component';
import {MonitoringSystemUpdateComponent} from './pages/monitoring-system/monitoring-system-update/monitoring-system-update.component';
import {MonitoringSystemAddComponent} from './pages/monitoring-system/monitoring-system-add/monitoring-system-add.component';
import {MonitoringSystemService} from '../shared/services/monitoring-system.service';
import {VehicleListComponent} from './pages/vehicle/vehicle-list/vehicle-list.component';
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ClientService} from "../shared/services/client.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatTableModule} from '@angular/material/table';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatSortModule} from "@angular/material/sort";
import {MatDialogModule} from '@angular/material/dialog';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {ClientListComponent} from "./pages/client/client-list/client-list.component";
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
import {VehicleAddComponent} from './pages/vehicle/vehicle-add/vehicle-add.component';
import {MatSelectModule} from "@angular/material/select";
import {DrivingProfilesComponent} from './pages/vehicle/driving-profiles/driving-profiles.component';
import {VehicleDetailsComponent} from './pages/vehicle/vehicle-details/vehicle-details.component';
import {VehicleUpdateComponent} from './pages/vehicle/vehicle-update/vehicle-update.component';
import {MonitoringSystemListComponent} from './pages/monitoring-system/monitoring-system-list/monitoring-system-list.component';
import {MonitoringSystemDetailsComponent} from './pages/monitoring-system/monitoring-system-details/monitoring-system-details.component';
import {MonitoringSystemVehicleAssignationComponent} from './pages/monitoring-system/monitoring-system-vehicle-assignation/monitoring-system-vehicle-assignation.component';
import {MonitoringSystemAssignationComponent} from './pages/vehicle/monitoring-system-assignation/monitoring-system-assignation.component';
import {InsuranceCompanyListComponent} from './pages/insurance-company/insurance-company-list/insurance-company-list.component';
import {InsuranceCompanyAddComponent} from './pages/insurance-company/insurance-company-add/insurance-company-add.component';
import {InsuranceCompanyService} from "../shared/services/insurance-company.service";
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {InsuranceCompanyUpdateComponent} from './pages/insurance-company/insurance-company-update/insurance-company-update.component';
import {UserListComponent} from './pages/user/user-list/user-list.component';
import {UserAddComponent} from './pages/user/user-add/user-add.component';
import {InsuranceCompanyClientsComponent} from './pages/insurance-company/insurance-company-clients/insurance-company-clients.component';
import {LoginComponent} from './pages/login/login.component';
import {AuthBaseComponent} from './components/auth-base/auth-base.component';
import {AuthInterceptor} from "../shared/auth/auth.interceptor";
import {NavbarComponent} from "./components/navbar/navbar.component";
import {SensorAddComponent} from "./pages/sensor/sensor-add/sensor-add.component";
import {SensorListComponent} from "./pages/sensor/sensor-list/sensor-list.component";
import {SensorService} from "../shared/services/sensor.service";
import {UserEditComponent} from "./pages/user/user-edit/user-edit.component";
import { SensorUpdateComponent } from './pages/sensor/sensor-update/sensor-update.component';

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
    InsuranceCompanyAddComponent,
    MonitoringSystemAddComponent,
    DrivingProfileAddComponent,
    SensorAddComponent,
    InsuranceCompanyUpdateComponent,
    DrivingProfileUpdateComponent,
    DrivingProfileDetailsComponent,
    UserListComponent,
    UserAddComponent,
    UserEditComponent,
    SensorListComponent,
    InsuranceCompanyUpdateComponent,
    InsuranceCompanyClientsComponent,
    LoginComponent,
    AuthBaseComponent,
    NavbarComponent,
    SensorUpdateComponent
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
    MatSelectModule,
    MatTooltipModule,
    MatDatepickerModule

  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    ClientService,
    VehicleService,
    MonitoringSystemService,
    InsuranceCompanyService,
    DrivingProfileService,
    SensorService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
