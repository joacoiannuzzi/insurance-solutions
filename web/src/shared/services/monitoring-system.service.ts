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

  deleteMonitoringSystem(monitoringSystemId: number) {
    //
  }

  get monitoringSystems(): Observable<MonitoringSystem[]> {
    return this.monitoringSystemsList
      ? new Observable<MonitoringSystem[]>((subscriber) =>
        subscriber.next(this.monitoringSystemsList)
      )
      : this.findAll();
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
  public delete(moSys: MonitoringSystem) {
    return this.http.delete<MonitoringSystem>(this.monitoringSystemsUrl + "/delete/" + moSys.id).pipe(
      map(() => {
        let auxmoSysList: MonitoringSystem[] = [...this.monitoringSystemsList];
        auxmoSysList.splice(this.monitoringSystemsList.findIndex(m => m.id == moSys.id), 1);
        this.monitoringSystemsList = [...auxmoSysList];
        this.snackBar.open('El servicio fue eliminado con éxito.', '', {
          duration: 2000,
        });
        return this.monitoringSystemsList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al eliminar el servicio', '', {
          duration: 2000,
        });
        return this.monitoringSystems;
      })
    )
  }
  public update(moSys: MonitoringSystem) {
    return this.http.put<MonitoringSystem>(this.monitoringSystemsUrl + "/update/" + moSys.id, moSys).pipe(
      map((res: MonitoringSystem) => {
        let i = this.monitoringSystemsList.findIndex(m => m.id === moSys.id);
        this.monitoringSystemsList[i] = res;
        this.snackBar.open('El servicio de monitoreo fue actualizado con éxito', '', {
          duration: 2000,
        });
        return res;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al actualizar el servicio de monitoreo', '', {
          duration: 2000,
        });
        return this.monitoringSystems;
      })
    );
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
