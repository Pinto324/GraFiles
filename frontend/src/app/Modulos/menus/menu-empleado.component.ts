import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { UserService } from '../../Utilidades/Usuario';
import {ModalArchivosComponent} from '../ModalArchivos/modal-archivos.component';

interface Archivo {
  _id: string;
  Nombre: string;
  Extension: string;
}

@Component({
  selector: 'app-menu-empleado',
  standalone: true,
  imports: [CommonModule, ModalArchivosComponent],
  templateUrl: './menu-empleado.component.html',
  styleUrl: './menu-empleado.component.css'
})
export class MenuEmpleadoComponent implements OnInit{
  archivos: Archivo[] = [];
  root: string = "ObjectId('726f6f740000000000000000')";
  padre:string ="ObjectId('726f6f740000000000000000')";
  NombreArchivo: string = '';
  isModalOpen: boolean = false; 
  archivoSeleccionado: any;
  constructor(private http: HttpClient, private UserService:UserService  ) { }

  ngOnInit() {
    this.cargarArchivos();
  }
//get archivos:
  cargarArchivos() {
    const url = 'http://localhost:8080/GraFiles/ArchivosServlet?accion=cargarArchivos&Id='+ this.UserService.getId()+'&Padre='+this.padre; // Cambia esto a tu URL
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
  getIconPath(extension: string): string {
    // Ruta del ícono según la extensión del archivo
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
        return 'assets/txt.png'; // Ícono por defecto
    }
  }
  //metodo para la selección de un archivo:
  abrirModal(archivo: any) {
    if(archivo.Extension.toLowerCase() == "carpeta"){
      this.padre = archivo._id;
      this.cargarArchivos();
    }else{
      this.archivoSeleccionado = archivo; // Almacena el archivo seleccionado
      this.isModalOpen = true; // Abre el modal
    } 
  }
  cerrarModal() {
    this.isModalOpen = false; // Método para cerrar el modal
    this.archivoSeleccionado = null; // Reiniciar archivo seleccionado si es necesario
  }
  //metodo para navegar:
  navegar(){
    
  }
}
