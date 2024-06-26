import {Injectable} from '@angular/core';
import {InsuranceCompany} from "../models/insuranceCompany";
import {HttpClient} from "@angular/common/http";
import {MatSnackBar} from "@angular/material/snack-bar";
import {environment} from "../../environments/environment";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";

@Injectable()
export class InsuranceCompanyService {

  private readonly insuranceCompaniesUrl: string;
  private insuranceCompaniesList: InsuranceCompany[];

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {
    this.insuranceCompaniesUrl = environment.url + '/insurance-companies';
  }

  findAll(): Observable<InsuranceCompany[]> {
    return this.http.get(this.insuranceCompaniesUrl + "/get-all").pipe(
      map((res: any) => {
        this.insuranceCompaniesList = res.map((insuranceCompany) => InsuranceCompany.fromJsonObject(insuranceCompany));
        return this.insuranceCompaniesList;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al traer las empresas aseguradoras.', '', {
          duration: 2000,
        });
        return [];
      })
    );
  }

  delete(insuranceCompany: InsuranceCompany) {
    return this.http.delete<InsuranceCompany>(this.insuranceCompaniesUrl + "/delete/" + insuranceCompany.id).pipe(
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

  public save(insuranceCompany: InsuranceCompany) {
    return this.http.post<InsuranceCompany>(this.insuranceCompaniesUrl + "/create", insuranceCompany).pipe(
      map((res: any) => {
        this.insuranceCompaniesList = [...this.insuranceCompaniesList, InsuranceCompany.fromJsonObject(res)]
        this.snackBar.open('La compañia fué guardada con éxito.', '', {
          duration: 2000,
        });
        return res;
      }),
      catchError(() => {
        this.snackBar.open('Hubo un error al guardar el cliente.', '', {
          duration: 2000,
        });
        return this.insuranceCompaniesList;
      })
    );
  }

  update(insuranceCompany: InsuranceCompany, successText: string, errorText: string) {
    return this.http.put<InsuranceCompany>(this.insuranceCompaniesUrl + "/update/" + insuranceCompany.id, insuranceCompany).pipe(
      map((res: InsuranceCompany) => {
        let i = this.insuranceCompaniesList.findIndex(c => c.id === insuranceCompany.id);
        this.insuranceCompaniesList[i] = res;
        this.snackBar.open(successText, '', {
          duration: 2000,
        });
        return res;
      }),
      catchError((err) => {
        this.snackBar.open(errorText, '', {
          duration: 2000,
        });
        return throwError(err);
      })
    );
  }
}
