import {Injectable} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly loginUrl: string;
  private jwtHelper: JwtHelperService;

  constructor(private http: HttpClient) {
    this.loginUrl = environment.url + '/login';
    this.jwtHelper = new JwtHelperService();
  }

  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
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
            sessionStorage.setItem('username', username);
            //TODO DEBUG VARIABLES DON'T FORGET TO DELETE LATER
            const headerz = res.headers.keys();
            const tok = res.headers.get('authorization');
            sessionStorage.setItem('token', tok);
          }
          return res;
        }),
        catchError(err => {
          return err.error;
        })
      );
  }

  //TODO preguntar sobre el logout (?)
}
