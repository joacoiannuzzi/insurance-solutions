import { MonitoringSystem } from '../models/monitoringSystem';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable()
export class MonitoringSystemService {

  private readonly monitoringSystemsUrl: string;
  private monitoringSystemsList: MonitoringSystem[];

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {
    this.monitoringSystemsUrl = 'http://localhost:8080/monitoring-systems';
  }

  private findAll(): Observable<MonitoringSystem[]> {
    return this.http.get(this.monitoringSystemsUrl + "/get-all").pipe(
      map((res: any) => {
        this.monitoringSystemsList = res.map((monitoringSystem) => MonitoringSystem.fromJsonObject(monitoringSystem));
        return this.monitoringSystemsList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al traer los servicios de monitoreo.', '', {
          duration: 2000,
        });
        return this.monitoringSystems;
      })
    );
  }

  deleteMonitoringSystem() {
    //Back not implemented yet
  }

  get monitoringSystems(): Observable<MonitoringSystem[]> {
    return this.monitoringSystemsList
      ? new Observable<MonitoringSystem[]>((subscriber) =>
        subscriber.next(this.monitoringSystemsList)
      )
      : this.findAll();
  }

  assignVehicle(MonitoringSystemId: number, vehicleId: number) {
    /*return this.http.put<Client>(`${this.clientsUrl}/${clientId}/add-vehicle/${vehicleId}`, {}).pipe(
      map((res: any) => {
        this.vehicleList = [...this.vehicleList, Vehicle.fromJsonObject(res)];
        this.snackBar.open('El cliente fué actualizado con éxito.', '', {
          duration: 2000,
        });
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al actualizar el cliente.', '', {
          duration: 2000,
        });
        return this.clients;
      })
    );*/
  }

}
