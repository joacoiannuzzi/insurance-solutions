import { Injectable } from '@angular/core';
import {User} from "../models/user";
import {HttpClient} from "@angular/common/http";
import {MatSnackBar} from "@angular/material/snack-bar";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly usersUrl: string;
  private usersList: User[];

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {
    this.usersUrl = environment.url + '/users';
  }

  private findAll(): Observable<User[]> {
    return this.http.get(this.usersUrl).pipe(
      map((res: any) => {
        this.usersList = res.map((user) => User.fromJsonObject(user));
        return this.usersList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al traer los usuarios.', '', {
          duration: 2000,
        });
        return this.users;
      })
    );
  }

  get users(): Observable<User[]> {
    return this.usersList
      ? new Observable<User[]>((subscriber) =>
        subscriber.next(this.usersList)
      )
      : this.findAll();
  }

  public save(user: User) {
    // return this.http.post<User>(this.usersUrl + "/create", user).pipe(
    //   map((res: any) => {
    //     this.usersList = [...this.usersList, User.fromJsonObject(res)];
    //     this.snackBar.open('El usuario fue guardado con éxito.', '', {
    //       duration: 2000,
    //     });
    //     return res;
    //   }),
    //   catchError(() => {
    //     this.snackBar.open('Hubo un error al guardar el usuario.', '', {
    //       duration: 2000,
    //     });
    //     return this.users;
    //   })
    // );
  }

  public update(user: User) {
    // return this.http.put<User>(this.usersUrl + "/update/" + user.id, user).pipe(
    //   map((res: User) => {
    //     let i = this.usersList.findIndex(u => u.id === user.id);
    //     this.usersList[i] = res;
    //     this.snackBar.open('El usuario fué actualizado con éxito.', '', {
    //       duration: 2000,
    //     });
    //     return res;
    //   }),
    //   catchError((response) => {
    //     this.snackBar.open(response?.error?.message, '', {
    //       duration: 2000,
    //     });
    //     return this.users;
    //   })
    // );
  }

  public delete(user: User) {
    // return this.http.delete<User>(this.usersUrl + "/" + user.id).pipe(
    //   map(() => {
    //     this.usersList.splice(this.usersList.findIndex(u => u.id === user.id), 1);
    //     this.snackBar.open('El usuario fue eliminado con éxito.', '', {
    //       duration: 2000,
    //     });
    //     return this.usersList;
    //   }),
    //   catchError(() => {
    //     this.snackBar.open('Hubo un error al eliminar el usuario.', '', {
    //       duration: 2000,
    //     });
    //     return this.users;
    //   })
    // )
  }
}
