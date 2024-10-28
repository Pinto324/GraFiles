import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
interface Archivo {
  NombreCarpeta: string;
  extension: string;
}

@Component({
  selector: 'app-menu-empleado',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './menu-empleado.component.html',
  styleUrl: './menu-empleado.component.css'
})
export class MenuEmpleadoComponent implements OnInit{
  archivos: Archivo[] = [];
  capa = 0;
  NombreArchivo: string = 'empleado';
  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.cargarArchivos();
  }

  cargarArchivos() {
    const url = 'http://localhost:8080/GraFiles/ArchivoServlet?accion=cargarArchivos'; // Cambia esto a tu URL
    this.http.get<Archivo[]>(url).subscribe(
      (response) => {
        this.archivos = response;
      },
      (error) => {
        console.error('Error al cargar archivos', error);
      }
    );
  }

  getIconPath(extension: string): string {
    // Devuelve la ruta del ícono según la extensión del archivo
    switch (extension) {
      case '.folder':
        return '../../../Recursos/folder.png';
      case '.png':
      case '.jpg':
        return '../../../Recursos/image.png';
      case '.txt':
        return '../../../Recursos/txt.png';
      case '.html':
        return '../../../Recursos/html.png';
      default:
        return '../../../Recursos/txt.png'; // Ícono por defecto
    }
  }
}
