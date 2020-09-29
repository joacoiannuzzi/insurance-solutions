import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Client} from '../models/client';
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {Vehicle} from "../models/vehicle";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable()
export class ClientService {

  private readonly clientsUrl: string;
  private clientsList: Client[];
  private vehicleList: Vehicle[];

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {
    this.clientsUrl = 'http://localhost:8080/clients';
  }

  private findAll(): Observable<Client[]> {
    return this.http.get(this.clientsUrl).pipe(
      map((res: any) => {
        this.clientsList = res.map((client) => Client.fromJsonObject(client));
        return this.clientsList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al traer los clientes.', '', {
          duration: 2000,
        });
        return this.clients;
      })
    );
  }

  private getVehicles(id: number): Observable<Vehicle[]> {
    return this.http.get(this.clientsUrl + "/vehicles/" + id).pipe(
      map((res: any) => {
        return res.map((vehicle) => Vehicle.fromJsonObject(vehicle));
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al traer los clientes.', '', {
          duration: 2000,
        });
        return [];
      })
    );
  }

  public save(client: Client) {
    return this.http.post<Client>(this.clientsUrl + "/create", client).pipe(
      map((res: any) => {
        this.clientsList = [...this.clientsList, Client.fromJsonObject(res)];
        this.snackBar.open('El cliente fué guardado con éxito.', '', {
          duration: 2000,
        });
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al guardar el cliente.', '', {
          duration: 2000,
        });
        return this.clients;
      })
    );
  }

  assignVehicle(clientId: number, vehicleId: number) {
    return this.http.put<Client>(`${this.clientsUrl}/${clientId}/add-vehicle/${vehicleId}`, {}).pipe(
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
    );
  }

  deleteVehicle(clientId: number, vehicleId: number) {
    return this.http.put<Client>(`${this.clientsUrl}/${clientId}/delete-vehicle/${vehicleId}`, {}).pipe(
      map(() => {
        let auxVehicleList: Vehicle[] = [...this.vehicleList];
        auxVehicleList.splice(this.vehicleList.findIndex(c => c.id === vehicleId), 1);
        this.vehicleList = [...auxVehicleList];
        this.snackBar.open('El cliente fué actualizado con éxito.', '', {
          duration: 2000,
        });
        return this.vehicleList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al eliminar el cliente.', '', {
          duration: 2000,
        });
        return this.clients;
      })
    );
  }

  public update(client: Client) {
    return this.http.put<Client>(this.clientsUrl + "/update/" + client.id, client).pipe(
      map((res: Client) => {
        let i = this.clientsList.findIndex(c => c.id === client.id);
        let auxClientsList: Client[] = [...this.clientsList];
        auxClientsList[i] = res;
        this.clientsList = [...auxClientsList];
        this.snackBar.open('El cliente fué actualizado con éxito.', '', {
          duration: 2000,
        });
        return res;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al actualizar el cliente.', '', {
          duration: 2000,
        });
        return this.clients;
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
    return this.getVehicles(client.id);
  }

  public delete(user: Client) {
    return this.http.delete<Client>(this.clientsUrl + "/" + user.id).pipe(
      map(() => {
        let auxClientsList: Client[] = [...this.clientsList];
        auxClientsList.splice(this.clientsList.findIndex(c => c.id === user.id), 1);
        this.clientsList = [...auxClientsList];
        this.snackBar.open('El cliente fué eliminado con éxito.', '', {
          duration: 2000,
        });
        return this.clientsList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al eliminar el cliente.', '', {
          duration: 2000,
        });
        return this.clients;
      })
    )
  }
}
