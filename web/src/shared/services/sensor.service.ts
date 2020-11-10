import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {MatSnackBar} from "@angular/material/snack-bar";
import {environment} from "../../environments/environment";
import {Sensor} from "../models/sensor";

@Injectable()
export class SensorService {

  private readonly senosrsUrl: string;
  private sensorsList: Sensor[];

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {
    this.senosrsUrl = `${environment.url}/sensors`;
  }

  public save(sensor: Sensor) {
    return this.http.post<Sensor>(this.senosrsUrl + "/create", sensor).pipe(
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
    return this.http.get(this.senosrsUrl + '/get-all').pipe(
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


}
