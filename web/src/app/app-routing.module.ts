import {MonitoringSystemListComponent} from './pages/monitoring-system/monitoring-system-list/monitoring-system-list.component';
import {VehicleListComponent} from './pages/vehicle/vehicle-list/vehicle-list.component';
import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {ClientListComponent} from "./pages/client/client-list/client-list.component";
import {InsuranceCompanyListComponent} from "./pages/insurance-company/insurance-company-list/insurance-company-list.component";
import {UserListComponent} from "./pages/user/user-list/user-list.component";
import {AuthGuard} from "../shared/auth/auth.guard";
import {RoleGuardService as RoleGuard} from "../shared/auth/role-guard.service";
import {LoginComponent} from "./pages/login/login.component";
import {ReverseAuthGuard} from "../shared/auth/reverse-auth.guard";
import {AuthAdminComponent} from "./components/auth-admin/auth-admin.component";
import {AuthBaseComponent} from "./components/auth-base/auth-base.component";

const routes: Routes = [
  {
    path: 'login', children: [
      {path: '', component: LoginComponent}, // login
    ], canActivate: [ReverseAuthGuard]
  },
  {
    path: '', component: AuthAdminComponent, canActivate: [RoleGuard],
    data: {
      expectedRole: 'admin'
    },
    children: [
      {path: 'insurance-companies', component: InsuranceCompanyListComponent},
      {path: 'users', component: UserListComponent},
      {path: 'clients', component: ClientListComponent},
      {path: 'vehicles', component: VehicleListComponent},
      {path: 'monitoring-systems', component: MonitoringSystemListComponent}
    ]
  },
  {
    path: '', component: AuthBaseComponent, canActivate: [AuthGuard],
    children: [
      {path: 'clients', component: ClientListComponent},
      {path: 'vehicles', component: VehicleListComponent},
      {path: 'monitoring-systems', component: MonitoringSystemListComponent}
    ]
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
