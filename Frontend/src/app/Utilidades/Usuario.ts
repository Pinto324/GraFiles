import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private username: any;
  private tipo: any;
  constructor() { }

  setUser(username: any, tipo: any) {
    this.username = username;
    this.tipo = tipo;
  }

  getUsername() {
    return this.username;
  }
  getTipo(){
    return this.tipo;
  }
}