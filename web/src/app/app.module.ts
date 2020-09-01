import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {ClientService} from "../shared/services/client.service";
import { ClientListComponent } from './client-list/client-list.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import { UserFormComponent } from './user-form/user-form.component';
import {MatTableModule} from '@angular/material/table';
import { NavbarComponent } from './navbar/navbar.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';

@NgModule({
  declarations: [
    AppComponent,
    ClientListComponent,
    UserFormComponent,
    NavbarComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        BrowserAnimationsModule,
        FormsModule,
        MatTableModule,
        MatToolbarModule,
        MatButtonModule
    ],
  providers: [ClientService],
  bootstrap: [AppComponent]
})
export class AppModule { }
