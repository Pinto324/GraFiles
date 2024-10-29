import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private id: any;
  private username: any;
  private tipo: any;
  constructor() { }

  setUser(username: any, tipo: any, id: any) {
    this.username = username;
    this.tipo = tipo;
    this.id = id;
  }

  getUsername() {
    return this.username;
  }
  getTipo(){
    return this.tipo;
  }
  getId(){
    return this.id;
  }
}