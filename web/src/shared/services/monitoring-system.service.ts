import { MonitoringSystem } from './../models/monitoringSystem';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Client} from '../models/client';
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {Vehicle} from "../models/vehicle";
import {MatSnackBar} from "@angular/material/snack-bar";
import { throwMatDialogContentAlreadyAttachedError } from '@angular/material/dialog';

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
 

  get monitoringSystems(): Observable<MonitoringSystem[]> {
    return this.monitoringSystemsList
      ? new Observable<MonitoringSystem[]>((subscriber) =>
        subscriber.next(this.monitoringSystemsList)
      )
      : this.findAll();
  }

  public save(moSys: MonitoringSystem) {
    // Acá iría el request
    return this.monitoringSystems;
  }


}
