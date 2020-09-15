import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Client} from '../models/client';
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {Vehicle} from "../models/vehicle";

@Injectable()
export class ClientService {

  private readonly clientsUrl: string;
  private clientsList: Client[];
  private vehicleList: Vehicle[];

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

  private getVehicles(id: number): Observable<any> {
    return this.http.get(this.clientsUrl + "/vehicles/" + id).pipe(
      map((res: any) => {
        this.vehicleList = res.map((vehicle) => Vehicle.fromJsonObject(vehicle));
        return this.vehicleList;
      })
    );
  }

  public save(client: Client) {
    return this.http.post<Client>(this.clientsUrl + "/create", client).pipe(
      map((res: any) => {
        this.clientsList = [...this.clientsList, Client.fromJsonObject(res)]
      })
    );
  }

  assignVehicle(clientId: number, vehicleId: number) {
    return this.http.put<Client>(`${this.clientsUrl}/${clientId}/add-vehicle/${vehicleId}`, {}).pipe(
      map((res: any) => {
        this.vehicleList = [...this.vehicleList, Vehicle.fromJsonObject(res)]
      })
    );
  }

  deleteVehicle(clientId: number, vehicleId: number) {
    return this.http.put<Client>(`${this.clientsUrl}/${clientId}/delete-vehicle/${vehicleId}`, {}).pipe(
      map((res: any) => {
        let auxVehicleList: Vehicle[] = [...this.vehicleList];
        auxVehicleList.splice(this.vehicleList.findIndex(c => c.id === vehicleId), 1);
        this.vehicleList = [...auxVehicleList];
        // Snackbar success
        return this.vehicleList;
      })
    );
  }

  public update(client: Client) {
    console.log("HERE on update");
    console.log(client);
    return this.http.put<Client>(this.clientsUrl + "/update/" + client.id, client).pipe(
      map((res: Client) => {
        let i = this.clientsList.findIndex(c => c.id === client.id);
        let auxClientsList: Client[] = [...this.clientsList];
        auxClientsList[i] = res;
        this.clientsList = [...auxClientsList];
        return res;
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

  vehicles(client: Client): Observable<Vehicle[]> {
    return this.vehicleList
      ? new Observable<Vehicle[]>((subscriber) =>
        subscriber.next(this.vehicleList)
      )
      : this.getVehicles(client.id);
  }

  public delete(user: Client) {
    return this.http.delete<Client>(this.clientsUrl + "/" + user.id).pipe(map(() => {
      let auxClientsList: Client[] = [...this.clientsList];
      auxClientsList.splice(this.clientsList.findIndex(c => c.id === user.id), 1);
      this.clientsList = [...auxClientsList];
      // Snackbar success
      return this.clientsList;
    }), catchError(() => {
        console.log("HERE IN ERROR DELETE");
        // Snackbar failure
        return new Observable<Client[]>((subscriber) =>
          subscriber.next(this.clientsList)
        );
      }
    ))
  }
}
