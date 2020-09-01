import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Client } from '../models/client';
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable()
export class ClientService {

  private readonly usersUrl: string;

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/users';
  }

  public findAll(): Observable<any> {
    return this.http.get<Client[]>(this.usersUrl).pipe(map(data => {
      console.log(data);
    }));
  }

  public save(user: Client) {
    return this.http.post<Client>(this.usersUrl, user);
  }
}
