import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../shared/auth/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;

  constructor(private authService: AuthService) {
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
        res => {
          // next
        }, error => {
          // err
        }
      )
    }
  }
}
