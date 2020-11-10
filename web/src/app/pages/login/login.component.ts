import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../shared/auth/auth.service";
import decode from 'jwt-decode';
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;

  constructor(private authService: AuthService, private router: Router, private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.createForm();
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  get invalid() {
    return this.loginForm.invalid
  }

  private createForm() {
    this.loginForm = new FormGroup({
      username: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        Validators.pattern('^[a-zA-Z0-9]*$'),
      ]),
      password: new FormControl('', [
        Validators.required
      ])
    });
  }

  login() {
    if (this.loginForm.valid) {
      this.authService.login(this.username.value, this.password.value).subscribe(
        () => {
          // login successful
          // Check role to see which page to redirect to
          const role = sessionStorage.getItem('role');

          if (role !== 'ROLE_ADMIN') {
            this.router.navigate(['/clients']);
          } else {
            this.router.navigate(['/insurance-companies']);
          }
        }, () => {
          this.snackBar.open('Hubo un error. Verifíque los datos e inténtelo de nuevo.', '', {
            duration: 2000,
          });
        }
      );
    }
  }
}
