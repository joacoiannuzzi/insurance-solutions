import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {MatSnackBar} from "@angular/material/snack-bar";
import {environment} from "../../environments/environment";
import {Sensor} from "../models/sensor";

@Injectable()
export class SensorService {

  private readonly sensorsUrl: string;
  private sensorsList: Sensor[];

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {
    this.sensorsUrl = `${environment.url}/sensors`;
  }

  public save(sensor: Sensor) {
    return this.http.post<Sensor>(`${this.sensorsUrl}/create`, sensor).pipe(
      map((res: any) => {
        this.sensorsList = [...this.sensorsList, Sensor.fromJsonObject(res)];
        this.snackBar.open('El sensor fué guardado con éxito.', '', {
          duration: 2000,
        });
        return res;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al guardar el sensor.', '', {
          duration: 2000,
        });
        return this.sensors;
      })
    );
  }

  get sensors(): Observable<Sensor[]> {
    return this.sensorsList
      ? new Observable<Sensor[]>((subscriber) =>
        subscriber.next(this.sensorsList)
      )
      : this.findAll();
  }

  private findAll(): Observable<Sensor[]> {
    return this.http.get(`${this.sensorsUrl}/get-all`).pipe(
      map((res: any) => {
        this.sensorsList = res.map(Sensor.fromJsonObject);
        return this.sensorsList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al traer los sensores.', '', {
          duration: 2000,
        });
        return [];
      })
    );
  }


  public delete(sensor: Sensor): Observable<Sensor[]> {
    return this.http.delete<Sensor>(`${this.sensorsUrl}/delete/${sensor.id}`).pipe(
      map(() => {
        this.sensorsList.splice(this.sensorsList.findIndex(u => u.id === sensor.id), 1);
        this.snackBar.open('El sensor fue eliminado con éxito.', '', {
          duration: 2000,
        });
        return this.sensorsList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al eliminar el sensor.', '', {
          duration: 2000,
        });
        return this.sensors;
      })
    )
  }
}
