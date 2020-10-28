import { MonitoringSystemListComponent } from './pages/monitoring-system/monitoring-system-list/monitoring-system-list.component';
import { VehicleListComponent } from './pages/vehicle/vehicle-list/vehicle-list.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ClientListComponent} from "./pages/client/client-list/client-list.component";
import {InsuranceCompanyListComponent} from "./pages/insurance-company/insurance-company-list/insurance-company-list.component";
import {UserListComponent} from "./pages/user/user-list/user-list.component";

const routes: Routes = [
  { path: 'clients', children: [
      { path: '', component: ClientListComponent }, // users/
    ]
  },
  {
    path: 'vehicles', children: [
      { path: '', component: VehicleListComponent }
    ]
  },
  {
    path: 'monitoring-systems', children: [
      { path: '', component: MonitoringSystemListComponent}
    ]
  },
  {
    path: 'insurance-companies', children: [
      { path: '', component: InsuranceCompanyListComponent}
    ]
  },
  {
    path: 'users', children: [
      { path: '', component: UserListComponent}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
