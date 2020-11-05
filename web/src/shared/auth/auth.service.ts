import {Injectable} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {catchError, map} from "rxjs/operators";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly loginUrl: string;
  private jwtHelper: JwtHelperService;

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {
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
      {"username": username, "password": password},
      {observe: 'response'})
      .pipe(
        map((res: any) => {
          if (res?.status === 200) {
            const tok = res.headers.get('authorization');
            sessionStorage.setItem('token', tok);
            sessionStorage.setItem('role', res.body?.user?.rol)
          } else if (res?.status === 403) {
            this.snackBar.open('Hubo un error. Verifíque los datos e inténtelo de nuevo.', '', {
              duration: 2000,
            });
          }
          return res;
        }),
        catchError(() => {
          return [];
        })
      );
  }
}
