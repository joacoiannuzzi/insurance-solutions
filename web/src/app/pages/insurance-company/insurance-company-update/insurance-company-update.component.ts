import {Component, Inject, OnInit} from '@angular/core';
import {InsuranceCompany} from "../../../../shared/models/insuranceCompany";
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {InsuranceCompanyService} from "../../../../shared/services/insurance-company.service";
import {MonitoringSystem} from "../../../../shared/models/monitoringSystem";
import {alreadyExistsValidator} from "../../../../shared/directives/alreadyExistsValidator.directive";

@Component({
  selector: 'app-insurance-company-update',
  templateUrl: './insurance-company-update.component.html',
  styleUrls: ['./insurance-company-update.component.scss']
})
export class InsuranceCompanyUpdateComponent implements OnInit {
  insuranceCompany: InsuranceCompany;
  insuranceCompanyForm: FormGroup;
  insuranceCompanyList: InsuranceCompany[] = [];

  constructor(private dialogRef: MatDialogRef<InsuranceCompanyUpdateComponent>,
              @Inject(MAT_DIALOG_DATA) private data: InsuranceCompany,
              private icService: InsuranceCompanyService,) {
    this.insuranceCompany = {...data};
  }

  ngOnInit(): void {
    this.getInsuranceCompanies();

    this.insuranceCompanyForm = new FormGroup({
      name: new FormControl(this.insuranceCompany.name, [
        Validators.required,
        alreadyExistsValidator(this.insuranceCompanyList, 'name')
      ])
    });
  }

  private getInsuranceCompanies() {
    this.icService.insuranceCompanies.subscribe((res) => {
      this.insuranceCompanyList = res;
    })
  }

  get name() {
    return this.insuranceCompanyForm.get('name');
  }

  get invalid() {
    return this.insuranceCompanyForm.invalid
  }

  close(): void {
    this.dialogRef.close();
  }

  updateInsuranceCompany() {
    if (this.insuranceCompanyForm.valid) {
      // Se mapea todos los values del form al objeto client
      Object.keys(this.insuranceCompanyForm.value).map((key) => this.insuranceCompany[key] = this.insuranceCompanyForm.value[key]);

      this.icService.update(this.insuranceCompany, 'La empresa aseguradora fué actualizada con éxito.', 'Hubo un error al actualizar la empresa aseguradora.').subscribe(res => {
        this.dialogRef.close(res);
      })
    }
  }

}
