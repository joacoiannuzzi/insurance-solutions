import {Component, Inject, OnInit} from '@angular/core';
import {InsuranceCompany} from "../../../../shared/models/insuranceCompany";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {InsuranceCompanyService} from "../../../../shared/services/insurance-company.service";

@Component({
  selector: 'app-insurance-company-update',
  templateUrl: './insurance-company-update.component.html',
  styleUrls: ['./insurance-company-update.component.scss']
})
export class InsuranceCompanyUpdateComponent implements OnInit {
  insuranceCompany: InsuranceCompany;
  insuranceCompanyForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<InsuranceCompanyUpdateComponent>,
              @Inject(MAT_DIALOG_DATA) private data: InsuranceCompany,
              private icService: InsuranceCompanyService,) {
    this.insuranceCompany = {...data};
  }

  ngOnInit(): void {
    this.insuranceCompanyForm = new FormGroup({
      firstName: new FormControl(this.insuranceCompany.name, [
        Validators.required
      ])
    });
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
