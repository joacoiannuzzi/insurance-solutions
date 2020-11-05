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
import {JwtModule} from "@auth0/angular-jwt";
import {AuthService} from "../shared/auth/auth.service";

const routes: Routes = [
  {
    path: 'login', children: [
      {path: '', component: LoginComponent}, // login
    ]
  },
  {
    path: 'clients', children: [
      {path: '', component: ClientListComponent}, // users/
    ], canActivate: [AuthGuard]
  },
  {
    path: 'vehicles', children: [
      {path: '', component: VehicleListComponent}
    ], canActivate: [AuthGuard]
  },
  {
    path: 'monitoring-systems', children: [
      {path: '', component: MonitoringSystemListComponent}
    ], canActivate: [AuthGuard]
  },
  {
    path: 'insurance-companies', children: [
      {path: '', component: InsuranceCompanyListComponent}
    ], canActivate: [RoleGuard],
    data: {
      expectedRole: 'admin'
    }
  },
  {
    path: 'users', children: [
      {path: '', component: UserListComponent}
    ], canActivate: [RoleGuard],
    data: {
      expectedRole: 'admin'
    }
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
