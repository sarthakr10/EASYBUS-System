import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AllServiceService } from '../../services/all-service.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-contact-us',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './contact-us.component.html',
  styleUrl: './contact-us.component.css',
})
export class ContactUsComponent {
  contactForm: FormGroup;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private allService: AllServiceService,
    private toastr: ToastrService // Inject the service
  ) {
    this.contactForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      subject: ['', Validators.required],
      message: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.contactForm.valid) {
      this.isLoading = true;
      const contactData = this.contactForm.value;

      this.allService.sendContactForm(contactData).subscribe({
        next: () => {
          this.isLoading = false;
          alert('Message sent successfully');
          this.toastr.info('Your query will be solved as soon as possible');
          this.contactForm.reset();
        },
        error: () => {
          this.isLoading = false;
          alert('Failed to send message');
        },
      });
    }
  }
}
