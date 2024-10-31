import { Component, OnInit, ViewChild, EventEmitter,  Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { UserService } from '../../Utilidades/Usuario';
import {ModalArchivosComponent} from '../ModalArchivos/modal-archivos.component';
import {MenuClickIzquierdoComponent} from '../menu-click-izquierdo/menu-click-izquierdo.component';

interface Archivo {
  _id: string;
  Nombre: string;
  Extension: string;
  PadreId: string;
  FechaCreacion:string;
  FechaModificacion:string;
  Contenido:string;
  PropietarioId:string;
  Habilitado:string;
}

@Component({
  selector: 'app-menu-admin',
  standalone: true,
  imports: [CommonModule, ModalArchivosComponent, MenuClickIzquierdoComponent],
  templateUrl: './menu-admin.component.html',
  styleUrl: './menu-admin.component.css'
})
export class MenuAdminComponent {
  constructor(private http: HttpClient, private UserService:UserService ) { }
  isModalOpen: boolean = false; 
  archivoSeleccionado: any;
  desactivar: boolean=true;
  @Output() AdminEvent = new EventEmitter<string>();
  archivos: Archivo[] = [];
  ngOnInit() {
    this.cargarArchivos();
  }
  cerrarModal() {
    this.isModalOpen = false;
    this.archivoSeleccionado = null;
  }
  deslogear(){
    this.AdminEvent.emit(); 
  }

  abrirModal(archivo: any) {
      this.archivoSeleccionado = archivo;
      this.isModalOpen = true;
  }

  getIconPath(extension: string): string {
    switch (extension) {
      case 'carpeta':
        return 'assets/folder.png';
      case 'png':
      case 'jpg':
        return 'assets/image.png';
      case 'txt':
        return 'assets/txt.png';
      case 'html':
        return 'assets/html.png';
      default:
        return 'assets/txt.png'; 
    }
  }
  cargarArchivos() {
    const url = 'http://localhost:8080/GraFiles/ArchivosServlet?accion=cargarArchivosAdmin'; 
    this.archivos = [];
    this.http.get<Archivo[]>(url).subscribe(
      (response) => {
        if (Array.isArray(response)) {
          this.archivos = response;
          if (this.archivos.length === 0) {
            alert('No se encontraron archivos.');
          }
        } else {
          this.archivos = [];
        }
      },
      (error) => {
        console.error('Error al cargar archivos', error);
      }
    );
  }
  esCrearUsuario(){
    if(this.archivoSeleccionado==="CrearUsuario"){
      return false;
    }else{
      return true;
    }
  }
}
