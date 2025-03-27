import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  imports: [CommonModule, FormsModule,RouterLink]
})
export class LoginComponent {
  user = {
    name: '',           
    email: '',
    password: ''
  };
  

  actionText = 'Login';
  title = 'Welcome Back';
  isSignup = false;

  constructor(private authService: AuthService, private router: Router) {}

  submit() {
    this.authService.login(this.user).subscribe(res => {
      this.authService.setToken(res.jwt);
      if (res.userRole === 'ADMIN') {
        this.router.navigate(['/admin']);
      } else {
        this.router.navigate(['/user']);
      }
    });
  }
  
}
