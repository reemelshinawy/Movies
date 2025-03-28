import { Injectable } from '@angular/core';

const TOKEN ="token";
const USER="user";

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor() { }

  static saveToken(token: string): void {
    window.localStorage.removeItem("TOKEN");
    window.localStorage.setItem("TOKEN", token);
  } 

  // Save user to local storage
  static saveUser(user: any): void {
    window.localStorage.removeItem("USER");
    window.localStorage.setItem("USER", JSON.stringify(user));
  }

  // Get token from local storage
  static getToken(): string | null {
    return localStorage.getItem("TOKEN");
  }

  static getUser(): any {
    const user = localStorage.getItem("USER");
    return user ? JSON.parse(user) : null;
  }

  // Get user role
  static getUserRole(): string {
    const user = this.getUser();
    if (user === null) return "";
    return user.role;
  }

  // Check if admin is logged in
  static isAdminLoggedIn(): boolean {
    if (this.getToken() === null) return false;
    const role: string = this.getUserRole();
    return role === "ADMIN";
  }

  static isCustomerLoggedin(): boolean {
    if (this.getToken() === null) return false;
    const role: string = this.getUserRole();
    return role === "CUSTOMER";
  }



  static signout(): void {
    window.localStorage.removeItem(TOKEN);
    window.localStorage.removeItem(USER);
  }

  static getUserId(): number | null {
    const user = this.getUser();
    return user ? +user.id : null;
  }
  



}
