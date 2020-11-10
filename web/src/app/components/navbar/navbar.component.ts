import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../../shared/auth/auth.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  public isAdmin: boolean = false;

  constructor(private auth: AuthService) {

    if (auth.isAuthenticated() && sessionStorage.getItem('role') === 'ROLE_ADMIN') {
      this.isAdmin = true;
    }
  }

  ngOnInit(): void {
  }

  logout() {
    this.isAdmin = false
    this.auth.logout()
  }
}
