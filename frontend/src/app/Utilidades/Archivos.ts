import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ArchivoService {
  private archivoaMover: any;
  constructor() { }

  setUser(archivoaMover: any) {
    this.archivoaMover = archivoaMover;
  }

  getUsername() {
    return this.archivoaMover;
  }
  setArchivo(archivoaMover: any){
    this.archivoaMover = archivoaMover;
  }
}