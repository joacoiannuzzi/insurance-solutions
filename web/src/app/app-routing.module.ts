import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ClientListComponent} from "./client-list/client-list.component";

const routes: Routes = [
  { path: 'clients', children: [
      { path: '', component: ClientListComponent }, // users/
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
