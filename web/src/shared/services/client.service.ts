import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Client } from '../models/client';
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";

@Injectable()
export class ClientService {

  private readonly clientsUrl: string;
  private clientsList: Client[];

  constructor(private http: HttpClient) {
    this.clientsUrl = 'http://localhost:8080/clients';
  }

  private findAll(): Observable<any> {
    return this.http.get(this.clientsUrl).pipe(
      map((res: any) => {
        this.clientsList = res.map((client) => Client.fromJsonObject(client));
        return this.clientsList;
      })
    );
  }

  public save(client: Client) {
    return this.http.post<Client>(this.clientsUrl + "/create", client).pipe(

      map((res: any) => {
        this.clientsList.push(Client.fromJsonObject(res))
      })
    );
  }

  assignVehicle(clientId: number, vehicleId: number) {
    return this.http.get<Client>(`${this.clientsUrl}/${clientId}/add-vehicle/${vehicleId}`).pipe(
      map((res: any) => {
        return res;
      })
    );
  }

  public update(client: Client) {
    return this.http.put<Client>(this.clientsUrl + "/update/" + client.id , client).pipe(
      map((res: Client) => {
        let i = this.clientsList.findIndex(c => c.id === client.id);
        this.clientsList[i] = res;
        return res;
      }),
      map(() => {
        console.log("ERROR IN UPDATE")
        //TODO snackbar
      })
    );
  }

  get clients(): Observable<Client[]> {
    return this.clientsList
      ? new Observable<Client[]>((subscriber) =>
        subscriber.next(this.clientsList)
      )
      : this.findAll();
  }

  public delete(user: Client) {
    return this.http.delete<Client>(this.clientsUrl + "/" + user.id).pipe(map(() => {
        this.clientsList.splice(this.clientsList.findIndex(c => c.id === user.id))
        // Snackbar success
        return this.clientsList;
      }), catchError( () => {
        // Snackbar failure
        return new Observable<Client[]>((subscriber) =>
          subscriber.next(this.clientsList)
        );
      }
    ))
  }
}
