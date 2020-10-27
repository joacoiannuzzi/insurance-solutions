import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {User} from "../../../../shared/models/user";
import {UserService} from "../../../../shared/services/user.service";
import {alreadyExistsValidator} from "../../../../shared/directives/alreadyExistsValidator.directive";
import {Observable} from "rxjs";
import {InsuranceCompanyService} from "../../../../shared/services/insurance-company.service";
import {Type} from "../../../../shared/models/type";
import {map, startWith} from "rxjs/operators";
import {InsuranceCompany} from "../../../../shared/models/insuranceCompany";

@Component({
  selector: 'app-user-add',
  templateUrl: './user-add.component.html',
  styleUrls: ['./user-add.component.scss']
})
export class UserAddComponent implements OnInit {
  userForm: FormGroup;
  userList: User[] = [];
  types: Type[] = [Type.BASE, Type.ADMIN];
  typeLabels: string[] = ['Base', 'Admin'];
  filteredOptions: Observable<InsuranceCompany[]>;
  insuranceCompanyList = [];

  constructor(public dialogRef: MatDialogRef<UserAddComponent>,
              @Inject(MAT_DIALOG_DATA) public data: User,
              public userService: UserService,
              public insuranceCompanyService: InsuranceCompanyService) { }

  ngOnInit(): void {
    this.getUsers();

    this.userForm = new FormGroup({
      username: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.minLength(2),
        Validators.pattern('^[a-zA-Z0-9]*$'),
        alreadyExistsValidator(this.userList, 'username')
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        //Minimum eight characters, at least one letter, one number and one special character
        Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).*$')
      ]),
      type: new FormControl('', [
        Validators.required
      ]),
      insuranceCompany: new FormControl('', [])
    });
    this.getInsuranceCompanies();
  }

  private getUsers() {
    this.userService.users.subscribe((res) => {
      this.userList = res;
    })
  }

  get username() { return this.userForm.get('username'); }

  get password() { return this.userForm.get('password'); }

  get type() { return this.userForm.get('type'); }

  get insuranceCompany() { return this.userForm.get('insuranceCompany'); }

  get invalid() { return this.userForm.invalid }

  close(): void {
    this.dialogRef.close();
  }

  saveUser() {
    if (this.userForm.valid) {
      // Se mapea todos los values del form al objeto user
      Object.keys(this.userForm.value).map((key) => this.data[key] = this.userForm.value[key]);

      //Aca habria que hacer un assign insuranceCompany al user. Depende de como lo manejen en el back

      this.userService.save(this.data).subscribe(() => {
        this.dialogRef.close();
      })
    }
  }

  getInsuranceCompanies() {
    this.insuranceCompanyService.insuranceCompanies.subscribe((res: InsuranceCompany[]) => {
      this.insuranceCompanyList = [...res];
    })
    this.filteredOptions = this.insuranceCompany.valueChanges
      .pipe(
        startWith(''),
        map(value => {
          return this._filter(value?.name ? value.name : value);
        })
      );
  }

  private _filter(value: string): InsuranceCompany[] {
    return this.insuranceCompanyList.filter(option => option.name.includes(value));
  }

  displayOption(option: InsuranceCompany) {
    return option.name;
  }

}
