import { VehicleListComponent } from './pages/vehicle/vehicle-list/vehicle-list.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ClientListComponent} from "./pages/client/client-list/client-list.component";

const routes: Routes = [
  { path: 'clients', children: [
      { path: '', component: ClientListComponent }, // users/
    ]
  },
  {
    path: 'vehicles', children: [
      { path: '', component: VehicleListComponent }
  ]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
