import { Injectable } from '@angular/core';
import {Client} from "../models/client";
import {Vehicle} from "../models/vehicle";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class VehicleService {

  private readonly usersUrl: string;
  private vehicleList: Vehicle[];

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/vehicles';
  }

  private findAll(): Observable<any> {
    return this.http.get(this.usersUrl).pipe(
      map((res: any) => {
        this.vehicleList = res.map((client) => Client.fromJsonObject(client));
        return this.vehicleList;
      })
    );
  }

  public save(vehicle: Vehicle, clientId: string) {
    return this.http.post<Vehicle>(this.usersUrl+ "/" + clientId, vehicle).pipe(

      map((res: any) => {
        this.vehicleList.push(Vehicle.fromJsonObject(res))
      })
    );
  }

  get vehicles(): Observable<Vehicle[]> {
    return this.vehicleList
      ? new Observable<Vehicle[]>((subscriber) =>
        subscriber.next(this.vehicleList)
      )
      : this.findAll();
  }
}
