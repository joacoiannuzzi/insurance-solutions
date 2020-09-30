import { MonitoringSystem } from '../models/monitoringSystem';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Client} from "../models/client";

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
  deleteMonitoringSystem(monitoringSystemId: number) {
    return this.http.delete<Client>(this.monitoringSystemsUrl + "/delete/" + monitoringSystemId).pipe(
      map(() => {
        let monitoringSystemList: MonitoringSystem[] = [...this.monitoringSystemsList];
        monitoringSystemList.splice(this.monitoringSystemsList.findIndex(c => c.id === monitoringSystemId), 1);
        this.monitoringSystemsList = [...monitoringSystemList];
        this.snackBar.open('El sistema de monitoreo fué eliminado con éxito.', '', {
          duration: 2000,
        });
        return this.monitoringSystemsList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al eliminar el sistema de monitoreo.', '', {
          duration: 2000,
        });
        return this.monitoringSystems;
      })
    )
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
  public save(moSys: MonitoringSystem) {
    return this.http.post(this.monitoringSystemsUrl + "/create", moSys).pipe(
      map((res: any) => {
        this.monitoringSystemsList.push(MonitoringSystem.fromJsonObject(res));
        this.snackBar.open('El servicio de monitoreo fue guardado con éxito.', '', {
          duration: 2000,
        });
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al guardar el vehículo.', '', {
          duration: 2000,
        });
        return [];
      })
    )

  }

  getVehicleLess() {
    return this.http.get(this.monitoringSystemsUrl + "/without-vehicle").pipe(
      map((res: any) => {
        return res.map((monitoringSystem) => MonitoringSystem.fromJsonObject(monitoringSystem));
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al traer los sistemas de monitoreo.', '', {
          duration: 2000,
        });
        return this.monitoringSystems;
      })
    );
  }

  unassignVehicle(MonitoringSystemId: number) {
    //Back not implemented yet
  }
}
