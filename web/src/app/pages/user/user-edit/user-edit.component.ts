import { AfterContentInit, Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { User } from '../../../../shared/models/user';
import { UserService } from '../../../../shared/services/user.service';
import { alreadyExistsValidator } from '../../../../shared/directives/alreadyExistsValidator.directive';
import { Observable } from 'rxjs';
import { InsuranceCompanyService } from '../../../../shared/services/insurance-company.service';
import { Role } from '../../../../shared/models/role';
import { map, startWith } from 'rxjs/operators';
import { InsuranceCompany } from '../../../../shared/models/insuranceCompany';
import { checkExistsValidator } from '../../../../shared/directives/checkExistsValidator.directive';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.scss']
})
export class UserEditComponent implements OnInit, AfterContentInit {
  userForm: FormGroup;
  userList: User[] = [];

  // types: Role[] = [Role.BASE, Role.ADMIN];
  typeLabels: string[] = ['Base', 'Admin'];
  filteredOptions: Observable<InsuranceCompany[]>;
  insuranceCompanyList: InsuranceCompany[] = [];
  loading = true;

  constructor(public dialogRef: MatDialogRef<UserEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: User,
    public userService: UserService,
    public insuranceCompanyService: InsuranceCompanyService) {
  }
  ngAfterContentInit(): void {
      this.createForm();
      this.createFilteredOptions();
      this.loading = false;
  }

  ngOnInit(): void {
    this.getUsers();
    this.getAllOtherUsers();
    this.getInsuranceCompanies();
  }

  private getUsers() {
    this.userService.users.subscribe((res) => {
      this.userList = res;
    });
  }

  public getAllOtherUsers() {
    const user = this.userList.findIndex(
      u => u.username === this.data.username);
    this.userList.splice(user, 1);
  }

  get username() {
    return this.userForm.get('username');
  }

  get password() {
    return this.userForm.get('password');
  }

  get type() {
    return this.userForm.get('type');
  }

  get email() {
    return this.userForm.get('email');
  }

  get insuranceCompany() {
    return this.userForm.get('insuranceCompany');
  }

  get invalid() {
    return this.userForm.invalid;
  }

  close(): void {
    this.dialogRef.close();
  }

  saveUser() {
    if (this.userForm.valid) {
      // Se mapea todos los values del form al objeto user
      Object.keys(this.userForm.value).map((key) => this.data[key] = this.userForm.value[key]);

      this.userService.update(this.data).subscribe(
        (newUser: any) => {
          // el save no asigna a la compañia. Hay que hacerlo en una ruta aparte.
          this.userService.assignInsuranceCompany(newUser.id, this.userForm.value.insuranceCompany.id).subscribe(res => {
           this.dialogRef.close(res)
          });

        },
        () => {
          // Solo catchea el error. No hace nada mas ya que no quiero que cierre
          // el dialogo en caso de error. El mismo service muestra la snackbar de error
        });
    }
  }

  getInsuranceCompanies() {
    this.insuranceCompanyService.insuranceCompanies.subscribe((res: InsuranceCompany[]) => {
      this.insuranceCompanyList = [...res];
    });

  }

  private createFilteredOptions() {
    this.filteredOptions = this.insuranceCompany.valueChanges
      .pipe(
        startWith(''),
        map(value => {
          return this._filter(value?.name ? value.name : value);
        })
      );
  }

  private createForm() {
    this.userForm = new FormGroup({
      username: new FormControl(this.data.username, [
        Validators.required,
        Validators.maxLength(20),
        Validators.minLength(2),
        Validators.pattern('^[a-zA-Z0-9]*$'),
        alreadyExistsValidator(this.userList, 'username')
      ]),
      password: new FormControl(undefined, [
        Validators.minLength(8),
        // Minimum eight characters, at least one letter, one number and one special character
        Validators.pattern('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).*$')
      ]),
      type: new FormControl(this.data.role, [
        Validators.required
      ]),
      email: new FormControl(this.data.email, [
        Validators.required,
        Validators.email
      ]),
      insuranceCompany: new FormControl(this.data.insuranceCompany ? this.data.insuranceCompany : '')
    });
  }

  private _filter(value: string): InsuranceCompany[] {
    return this.insuranceCompanyList.filter(option => option.name.includes(value));
  }

  displayOption(option: InsuranceCompany) {
    return option.name;
  }

}
