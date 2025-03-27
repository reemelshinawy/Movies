import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'jwt'; 
  constructor(private http: HttpClient) {}

  login(credentials: any) {
    return this.http.post<any>('http://localhost:9090/api/auth/login', credentials);
  }

  signup(user: any) {
    return this.http.post<any>('http://localhost:9090/api/auth/signup', user);
  }

  setToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken() {
    return localStorage.getItem(this.tokenKey);
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
  }
}

