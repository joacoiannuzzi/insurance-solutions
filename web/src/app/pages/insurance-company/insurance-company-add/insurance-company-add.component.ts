import {Component, Inject, OnInit} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {InsuranceCompany} from "../../../../shared/models/insuranceCompany";
import {InsuranceCompanyService} from "../../../../shared/services/insurance-company.service";
import {alreadyExistsValidator} from "../../../../shared/directives/alreadyExistsValidator.directive";

@Component({
  selector: 'app-insurance-company-add',
  templateUrl: './insurance-company-add.component.html',
  styleUrls: ['./insurance-company-add.component.scss']
})
export class InsuranceCompanyAddComponent implements OnInit {
  insuranceCompanyForm: FormGroup;
  insuranceCompanyList: InsuranceCompany[] = [];

  constructor(public dialogRef: MatDialogRef<InsuranceCompanyAddComponent>,
              @Inject(MAT_DIALOG_DATA) public data: InsuranceCompany,
              public insuranceCompanyService: InsuranceCompanyService
  ) {
  }

  ngOnInit(): void {
    this.getInsuranceCompanies();

    this.insuranceCompanyForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        alreadyExistsValidator(this.insuranceCompanyList, 'name')
      ]),
    });
  }

  private getInsuranceCompanies() {
    this.insuranceCompanyService.insuranceCompanies.subscribe((res) => {
      this.insuranceCompanyList = res;
    })
  }

  get name() { return this.insuranceCompanyForm.get('name'); }

  get invalid() { return this.insuranceCompanyForm.invalid }

  close(): void {
    this.dialogRef.close();
  }

  saveInsuranceCompany() {
    if (this.insuranceCompanyForm.valid) {
      // Se mapea todos los values del form al objeto client
      Object.keys(this.insuranceCompanyForm.value).map((key) => this.data[key] = this.insuranceCompanyForm.value[key]);

      this.insuranceCompanyService.save(this.data).subscribe(() => {
        this.dialogRef.close();
      })
    }
  }

}
