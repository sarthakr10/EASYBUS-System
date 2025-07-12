import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UserRegistration } from '../../models/user-registration';
import { ToastrService } from 'ngx-toastr';
import { ConnectorService } from '../../services/connector.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  userForms: FormGroup = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
    ]),
    email: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern(
        /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$/
      ),
    ]),
    phone: new FormControl('', [
      Validators.required,
      Validators.pattern(/^\d{10}$/),
    ]),
  });

  registeredObject: any;
  registerObj: UserRegistration;
  constructor(
    private toastr: ToastrService,
    private connector: ConnectorService,
    private router: Router
  ) {
    this.registerObj = new UserRegistration('', '', '', '');
  }

  submitHandler() {
    console.log('1');
    if (this.userForms.valid) {
      this.toastr.info('Signing up....', 'Please Wait');
      console.log('3');
      this.registerObj = new UserRegistration(
        this.userForms.get('username')?.value,
        this.userForms.get('email')?.value,
        this.userForms.get('password')?.value,
        this.userForms.get('phone')?.value
      );
      this.connector.doSignUp(this.registerObj).subscribe(
        (val) => {
          console.log('3' + val);
          this.toastr.success(
            'You have been successfully signed up....',
            'Please Log in'
          );
          console.log('4');

          this.registeredObject = val;
          console.log(this.registeredObject);
          this.router.navigateByUrl('/login');
        },
        (error) => {
          if (error.status == '403') {
            this.toastr.error('Invalid credentials', 'Login Error');
          } else {
            this.toastr.error(
              'Failed to login. Please try again later.',
              'Error'
            );
          }
        }
      );
    } else {
      this.toastr.error('Please fill all the fields correctly');
      this.userForms.markAllAsTouched();
    }
  }
}
