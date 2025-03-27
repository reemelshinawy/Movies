import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
  imports: [CommonModule, FormsModule,RouterLink]
})
export class SignupComponent {
  user = {
    name: '',
    email: '',
    password: ''
  };

  actionText = 'Sign Up';
  title = 'Create an Account';
  isSignup = true;

  constructor(private authService: AuthService, private router: Router) {}

  submit() {
    this.authService.signup(this.user).subscribe(() => {
      this.authService.login({
        email: this.user.email,
        password: this.user.password
      }).subscribe(res => {
        this.authService.setToken(res.jwt);
        if (res.userRole === 'ADMIN') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/user']);
        }
      });
    });
  }
}  
