import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { catchError, map } from "rxjs/operators";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Router } from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly loginUrl: string;
  private jwtHelper: JwtHelperService;

  constructor(
    private http: HttpClient,
    private snackBar: MatSnackBar,
    private router: Router,
     ) {
    this.loginUrl = environment.url + '/login';
    this.jwtHelper = new JwtHelperService();
  }

  public isAuthenticated(): boolean {
    const token = sessionStorage.getItem('token');
    // Check whether the token is expired and return
    // true or false
    return !this.jwtHelper.isTokenExpired(token);
  }

  public login(username: string, password: string) {
    return this.http.post(
      this.loginUrl,
      { 'username': username, 'password': password },
      { observe: 'response' }
    )
      .pipe(
        map((res: any) => {
          if (res?.status === 200) {
            const tok = res.headers.get('authorization');
            sessionStorage.setItem('token', tok);
            sessionStorage.setItem('role', res.body?.user?.rol)
          } else if (res?.status === 403) {
          }
          return res;
        }),
      );
  }

  logout(): void {
    sessionStorage.removeItem('token')
    sessionStorage.removeItem('role')
    this.router.navigate(['/login'])
      .then(() => this.http.get(environment.url + '/logout').subscribe());
  }
}
