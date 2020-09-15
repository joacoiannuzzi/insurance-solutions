import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Vehicle } from '../models/vehicle';
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";

@Injectable()
export class VehicleService {

  private readonly vehiclesUrl: string;
  private vehiclesList: Vehicle[];

  constructor(private http: HttpClient) {
    this.vehiclesUrl = 'http://localhost:8080/vehicles';
  }

  public findAll(): Observable<any> {
    return this.http.get(this.vehiclesUrl + "/clientless").pipe(
      map((res: any) => {
        this.vehiclesList = res.map((vehicle) => Vehicle.fromJsonObject(vehicle));
        return this.vehiclesList;
      })
    );
  }

  public save(vehicle: Vehicle) {
    return this.http.post<Vehicle>(this.vehiclesUrl + "/create", vehicle).pipe(

      map((res: any) => {
        this.vehiclesList.push(Vehicle.fromJsonObject(res))
      })
    );
  }

  public update(vehicle: Vehicle) {
    return this.http.put<Vehicle>(this.vehiclesUrl + "/update/" + vehicle.id , vehicle).pipe(
      map((res: Vehicle) => {
        let i = this.vehiclesList.findIndex(c => c.id === vehicle.id);
        this.vehiclesList[i] = res;
        return res;
      }),
      map(() => {
        console.log("ERROR IN UPDATE")
        //TODO snackbar
      })
    );
  }

  get vehicles(): Observable<Vehicle[]> {
    return this.vehiclesList
      ? new Observable<Vehicle[]>((subscriber) =>
        subscriber.next(this.vehiclesList)
      )
      : this.findAll();
  }

  public delete(user: Vehicle) {
    return this.http.delete<Vehicle>(this.vehiclesUrl + "/" + user.id).pipe(map(() => {
        this.vehiclesList.splice(this.vehiclesList.findIndex(c => c.id === user.id))
        // Snackbar success
        return this.vehiclesList;
      }), catchError( () => {
        // Snackbar failure
        return new Observable<Vehicle[]>((subscriber) =>
          subscriber.next(this.vehiclesList)
        );
      }
    ))
  }
}
