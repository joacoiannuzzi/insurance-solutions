import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Client } from '../models/client';
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable()
export class ClientService {

  private readonly usersUrl: string;
  private clientsList: Client[];

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/clients';
  }

  private findAll(): Observable<any> {
    return this.http.get(this.usersUrl).pipe(
      map((res: any) => {
        this.clientsList = res.map((client) => Client.fromJsonObject(client));
        return this.clientsList;
      })
    );
  }

  public save(user: Client) {
    return this.http.post<Client>(this.usersUrl, user);
  }

  get clients(): Observable<Client[]> {
    return this.clientsList
      ? new Observable<Client[]>((subscriber) =>
        subscriber.next(this.clientsList)
      )
      : this.findAll();
  }
}
/*

get clients(): Observable<Client[]> {
  return this.clientsList
    ? new Observable<Client[]>((subscriber) =>
      subscriber.next(this.clientsList)
    )
    : this.getAllClients();
}
getAllClients(): Observable<Client[]> {
  return this.http.get(this.url + 'GetAll').pipe(
    map((res: any) => {
      this.clientsList = res.data.map((url, index) => {
        return new Client(index, url);
      });
      return this.clientsList;
    })
  );
}*/
