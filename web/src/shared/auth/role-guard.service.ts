import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";
import {AuthService} from "./auth.service";
import decode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class RoleGuardService implements CanActivate {

  constructor(public auth: AuthService, public router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    // this will be passed from the route config
    // on the data property
    const expectedRole = route.data.expectedRole;

    const role = sessionStorage.getItem('role');

    if (!this.auth.isAuthenticated()) {
      this.router.navigate(['/login']);
      return false;
    } else if (role !== expectedRole) {
      this.router.navigate(['/clients']);
      return false;
    }
    return true;
  }
}
