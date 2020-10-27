import { DrivingProfileUpdateComponent } from './../../app/pages/vehicle/driving-profile-update/driving-profile-update.component';
import { MatDialogRef } from '@angular/material/dialog';
import { catchError } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { DrivingProfile } from './../models/drivingProfile';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Vehicle } from '../models/vehicle';
import { throwToolbarMixedModesError } from '@angular/material/toolbar';

@Injectable()
export class DrivingProfileService {
    private readonly drivingProfilesUrl: string;
    private drivingProfilesList: DrivingProfile[] = [];

    constructor(private http: HttpClient, private snackBar: MatSnackBar) {
        this.drivingProfilesUrl = 'http://localhost:8080/driving-profiles';
    }


    public save(driPro: DrivingProfile, vehicle: Vehicle) {
        return this.http.post<DrivingProfile>(this.drivingProfilesUrl + "/create" + "/" + vehicle.id, driPro).pipe(
            map((res: any) => {
                this.drivingProfilesList.push(DrivingProfile.fromJsonObject(res));
                this.snackBar.open('El perfil de conducción fue guardado con éxito', '', {
                    duration: 2000,
                });
            }),
            catchError(() => {
                this.snackBar.open('Hubo un error al guardar el vehículo', '', {
                    duration: 2000,
                });
                return [];
            })
        );
    }

    public delete(driPro: DrivingProfile) {
        return this.http.delete<DrivingProfile>(this.drivingProfilesUrl + "/delete/" + driPro.id).pipe(
            map(() => {
                let auxDriProList: DrivingProfile[] = [...this.drivingProfilesList];
                auxDriProList.splice(this.drivingProfilesList.findIndex(d => d.id === driPro.id),1);
                this.drivingProfilesList = [...auxDriProList];
                this.snackBar.open("El perfil de manejo fue eliminado con éxito.", '', {
                    duration: 2000,
                });
                return this.drivingProfilesList;
            })
        )
    }

    public update(driPro: DrivingProfile) {
      return this.http.put<DrivingProfile>(this.drivingProfilesUrl + "/update/" + driPro.id, driPro).pipe(
        map((res: DrivingProfile) => {
          let i = this.drivingProfilesList.findIndex(d => d.id === driPro.id);
          let auxDriProList: DrivingProfile[] = [...this.drivingProfilesList];
          auxDriProList[i] = res;
          this.drivingProfilesList = [...auxDriProList];
          this.snackBar.open('El perfil de conducción fue actualizado con éxito', '', {
            duration: 2000,
          });
          return res;
        }),
        catchError((response) => {
          this.snackBar.open('Hubo un error al actualizar el perfil de conducción', '', {
            duration: 2000,
          });
          return [];

        })
      )
    }


}
