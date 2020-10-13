import { Injectable } from '@angular/core';
import {InsuranceCompany} from "../models/insuranceCompany";
import {HttpClient} from "@angular/common/http";
import {MatSnackBar} from "@angular/material/snack-bar";
import {environment} from "../../environments/environment";
import {catchError, map} from "rxjs/operators";
import {Observable} from "rxjs";

@Injectable()
export class InsuranceCompanyService {

  private readonly insuranceCompaniesUrl: string;
  private insuranceCompaniesList: InsuranceCompany[];

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {
    this.insuranceCompaniesUrl = environment.url + '/insurance-companies';
  }

  private findAll(): Observable<InsuranceCompany[]> {
    return this.http.get(this.insuranceCompaniesUrl).pipe(
      map((res: any) => {
        this.insuranceCompaniesList = res.map((insuranceCompany) => InsuranceCompany.fromJsonObject(insuranceCompany));
        return this.insuranceCompaniesList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al traer las empresas aseguradoras.', '', {
          duration: 2000,
        });
        return this.insuranceCompanies;
      })
    );
  }

  delete(insuranceCompany: InsuranceCompany) {
    return this.http.delete<InsuranceCompany>(this.insuranceCompaniesUrl + "/" + insuranceCompany.id).pipe(
      map(() => {
        let auxInsuranceCompaniesList: InsuranceCompany[] = [...this.insuranceCompaniesList];
        auxInsuranceCompaniesList.splice(this.insuranceCompaniesList.findIndex(c => c.id === insuranceCompany.id), 1);
        this.insuranceCompaniesList = [...auxInsuranceCompaniesList];
        this.snackBar.open('La empresa aseguradora fue eliminada con éxito.', '', {
          duration: 2000,
        });
        return this.insuranceCompaniesList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al eliminar la empresa aseguradora.', '', {
          duration: 2000,
        });
        return this.insuranceCompanies;
      })
    )
  }

  get insuranceCompanies(): Observable<InsuranceCompany[]> {
    return this.insuranceCompaniesList
      ? new Observable<InsuranceCompany[]>((subscriber) =>
        subscriber.next(this.insuranceCompaniesList)
      )
      : this.findAll();
  }
}
