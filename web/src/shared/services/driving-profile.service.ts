import { catchError } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { DrivingProfile } from '../models/drivingProfile';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Vehicle } from '../models/vehicle';

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


}
