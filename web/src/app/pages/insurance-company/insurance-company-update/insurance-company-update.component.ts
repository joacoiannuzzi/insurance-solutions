import {Component, Inject, OnInit} from '@angular/core';
import {InsuranceCompany} from "../../../../shared/models/insuranceCompany";
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {InsuranceCompanyService} from "../../../../shared/services/insurance-company.service";
import {MonitoringSystem} from "../../../../shared/models/monitoringSystem";

@Component({
  selector: 'app-insurance-company-update',
  templateUrl: './insurance-company-update.component.html',
  styleUrls: ['./insurance-company-update.component.scss']
})
export class InsuranceCompanyUpdateComponent implements OnInit {
  insuranceCompany: InsuranceCompany;
  insuranceCompanyForm: FormGroup;
  insuranceCompanyNames: InsuranceCompany[] = [];

  constructor(private dialogRef: MatDialogRef<InsuranceCompanyUpdateComponent>,
              @Inject(MAT_DIALOG_DATA) private data: InsuranceCompany,
              private icService: InsuranceCompanyService,) {
    this.insuranceCompany = {...data};
  }

  ngOnInit(): void {
    this.getNames();

    function nameExistsValidator(names: InsuranceCompany[]): ValidatorFn {
      return (control: AbstractControl): {[key: string]: any} | null => {
        if(names.find(l => control.value === l.name)) {
          return {'nameExistsValidator': {value: control.value}}
        }
        return null;
      };
    }

    this.insuranceCompanyForm = new FormGroup({
      name: new FormControl(this.insuranceCompany.name, [
        Validators.required,
        nameExistsValidator(this.insuranceCompanyNames)
      ])
    });
  }

  private getNames() {
    this.icService.insuranceCompanies.subscribe((res) => {
      this.insuranceCompanyNames = res;
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

      this.icService.update(this.insuranceCompany).subscribe(res => {
        this.dialogRef.close(res);
      })
    }
  }

}
