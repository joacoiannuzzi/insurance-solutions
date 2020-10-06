import { MonitoringSystem } from './../models/monitoringSystem';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Vehicle } from '../models/vehicle';
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable()
export class VehicleService {

  private readonly vehiclesUrl: string;
  private vehiclesList: Vehicle[];

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {
    this.vehiclesUrl = 'http://localhost:8080/vehicles';
  }

  public getClientLess(): Observable<Vehicle[]> {
    return this.http.get(this.vehiclesUrl + "/clientless").pipe(
      map((res: any) => {
        return res.map((vehicle) => Vehicle.fromJsonObject(vehicle));
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al traer los vehículos.', '', {
          duration: 2000,
        });
        return this.vehicles;
      })
    );
  }

  public findAll(): Observable<Vehicle[]> {
    return this.http.get(this.vehiclesUrl).pipe(
      map((res: any) => {
        this.vehiclesList = res.map((vehicle) => Vehicle.fromJsonObject(vehicle));
        return this.vehiclesList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al traer los vehículos.', '', {
          duration: 2000,
        });
        return this.vehicles;
      })
    );
  }

  public save(vehicle: Vehicle) {
    return this.http.post<Vehicle>(this.vehiclesUrl + "/create", vehicle).pipe(

      map((res: any) => {
        this.vehiclesList.push(Vehicle.fromJsonObject(res));
        this.snackBar.open('El vehículo fue guardado con éxito.', '', {
          duration: 2000,
        });
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al guardar el vehículo.', '', {
          duration: 2000,
        });
        return [];
      })
    );
  }

  public update(vehicle: Vehicle) {
    return this.http.put<Vehicle>(this.vehiclesUrl + "/update/" + vehicle.id , vehicle).pipe(
      map((res: Vehicle) => {
        let i = this.vehiclesList.findIndex(c => c.id === vehicle.id);
        this.vehiclesList[i] = res;
        this.snackBar.open('El vehículo fue actualizado con éxito.', '', {
          duration: 2000,
        });
        return res;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al actualizar el vehículo.', '', {
          duration: 2000,
        });
        return this.vehicles;
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
    return this.http.delete<Vehicle>(this.vehiclesUrl + "/" + user.id).pipe(
      map(() => {
        this.vehiclesList.splice(this.vehiclesList.findIndex(c => c.id === user.id))
        this.snackBar.open('El vehículo fue eliminado con éxito.', '', {
          duration: 2000,
        });
        return this.vehiclesList;
      }), catchError( () => {
        this.snackBar.open('Hubo un error al eliminar el vehículo.', '', {
          duration: 2000,
        });
        return this.vehicles;
      }
    ))
  }

  deleteDrivingProfile(vehicleId: number, drivingProfileId: number) {
    return this.http.delete(this.vehiclesUrl + "/" + vehicleId + "/delete-driving-profile/" + drivingProfileId).pipe(
      map(() => {
        this.findAll();//Reload the list of vehicles to account for changes
        this.snackBar.open('El perfil de conducción fue eliminado con éxito.', '', {
          duration: 2000,
        });
        return this.vehiclesList;
      }), catchError( () => {
          this.snackBar.open('Hubo un error al eliminar el perfil de conducción.', '', {
            duration: 2000,
          });
          return this.vehicles;
        })
    );
  }

  getMonitoringSystemLess() {
    return this.http.get(this.vehiclesUrl + "/without-monitoring-system").pipe(
      map((res: any) => {
        return res.map((vehicle) => Vehicle.fromJsonObject(vehicle));
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al traer los vehículos.', '', {
          duration: 2000,
        });
        return this.vehicles;
      })
    );
  }

  assignVehicle(monitoringSystemId: number, vehicleId: number) {
    return this.http.put(this.vehiclesUrl + '/' + vehicleId + '/set-monitoring-system/' + monitoringSystemId, {}).pipe(
      map((res: Vehicle) => {
        let i = this.vehiclesList.findIndex(c => c.id === vehicleId);
        this.vehiclesList[i] = res;
        this.snackBar.open('El servicio de monitoreo fue asignado al vehículo con éxito.', '', {
          duration: 2000,
        });
        return res;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al asignar el servicio de monitoreo al vehículo.', '', {
          duration: 2000,
        });
        return this.vehicles;
      })
    );
  }

  unassignVehicle(vehicleId: number) {
    return this.http.delete(this.vehiclesUrl + "/" + vehicleId + "/remove-monitoring-system").pipe(
      map((res: Vehicle) => {
        this.snackBar.open('El servicio de monitoreo fue desasignado del vehículo con éxito.', '', {
          duration: 2000,
        });
        return res;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al desasignar el servicio de monitoreo del vehículo', '', {
          duration: 2000,
        });
        return this.vehicles;
      })
    );
    
  }

}
