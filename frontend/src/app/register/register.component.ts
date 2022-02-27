import { Component, OnInit } from '@angular/core';
import { AuthService } from '../_services/auth.service';
import { NgModule } from '@angular/core';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  form: any = {
    username: null,
    firstName: null,
    lastName: null,
    email: null,
    password: null,
  };
  isSuccessful = false;
  isSignUpFailed = false;
  message = '';

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    const { username, firstName, lastName, email, password } = this.form;

    this.authService.register(username, firstName, lastName, email, password).subscribe({
      next: data => {
        this.isSuccessful = true;
        this.isSignUpFailed = false;
        this.message=data.message;
      },
      error: err => {
        this.message = err.message;
        this.isSignUpFailed = true;
      }
    });
  }
}

