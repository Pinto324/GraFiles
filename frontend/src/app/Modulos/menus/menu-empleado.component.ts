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
  selector: 'app-menu-empleado',
  standalone: true,
  imports: [CommonModule, ModalArchivosComponent, MenuClickIzquierdoComponent],
  templateUrl: './menu-empleado.component.html',
  styleUrl: './menu-empleado.component.css'
})
export class MenuEmpleadoComponent implements OnInit{
  @ViewChild(MenuClickIzquierdoComponent) contextMenu!: MenuClickIzquierdoComponent;
  contextType: 'contenedor' | 'carpeta' | null = null;
  archivos: Archivo[] = [];
  root: string = "ObjectId('726f6f740000000000000000')";
  compartir: string = "ObjectId('507f1f77bcf86cd799439011')";
  padre:string ="ObjectId('726f6f740000000000000000')";
  NombreArchivo: string = '';
  isModalOpen: boolean = false; 
  archivoSeleccionado: any;
  EsRoot: boolean = true;
  ModificarTexto:boolean = false;
  @Output() EmpleadoEvent = new EventEmitter<string>();
  constructor(private http: HttpClient, private UserService:UserService ) { }

  ngOnInit() {
    this.cargarArchivos();
  }
  onRightClickContainer(event: MouseEvent) {
    if(this.EsRoot){
      event.preventDefault();
    event.stopPropagation(); // Evita la propagación del evento
    this.contextType = 'contenedor';
    this.contextMenu.openContextMenu(event, 'contenedor', null);
    }
  }

  onRightClickFolder(event: MouseEvent, archivo: any) {
    event.preventDefault();
    event.stopPropagation(); // Evita la propagación del evento
    this.contextType = 'carpeta';
    this.contextMenu.openContextMenu(event, 'carpeta', archivo);
  }
  //METODOS HTTPS:
//get archivos:
  cargarArchivos() {
    if(this.EsRoot){
    const url = 'http://localhost:8080/GraFiles/ArchivosServlet?accion=cargarArchivos&Id='+ this.UserService.getId()+'&Padre='+this.padre; // Cambia esto a tu URL
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
  }else{
    const url = 'http://localhost:8080/GraFiles/ArchivosServlet?accion=cargarArchivos&Id='+ this.UserService.getId()+'&Padre='+this.compartir; // Cambia esto a tu URL
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
  }
  retroceder() {
    if (this.padre !== this.root) {
      const url = 'http://localhost:8080/GraFiles/ArchivosServlet?accion=retroceder&Id=' + this.UserService.getId() + '&Padre=' + this.padre;
      this.archivos = [];
      this.http.get<Archivo[]>(url).subscribe(
        (response) => {
          if (Array.isArray(response) && response.length > 0) {
            this.archivos = response;
            this.padre = this.archivos[0].PadreId;
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
  }
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////77
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
    }else if(archivo.Extension.toLowerCase() == "txt"||archivo.Extension.toLowerCase() == "html"){
      this.archivoSeleccionado = archivo; 
      this.isModalOpen = true;
    } else{
      this.archivoSeleccionado = archivo; // Almacena el archivo seleccionado
      this.isModalOpen = true; // Abre el modal
    }
  }
  cerrarModal() {
    this.isModalOpen = false; // Método para cerrar el modal
    this.archivoSeleccionado = null; // Reiniciar archivo seleccionado si es necesario
  }
  bloquearRetroceder() {
    if (this.root !== this.padre) {
      this.retroceder();
    }
  }
  //modal:
  abrirClick(archivo: any) {
    this.archivoSeleccionado = archivo;
    this.isModalOpen = true;
  }

  cerrarClick() {
    this.isModalOpen = false;
  }
  //True false:
  manejarActualizar(event: boolean) {
    console.log("entroManejar");
    if(event) {
      this.cargarArchivos();
      console.log("actualizo");
    }
  }
  //deslogueo:
  deslogear(){
    this.EmpleadoEvent.emit(); 
  }
  activarCompartido(){
    this.EsRoot = false;
    this.cargarArchivos();
  }
  activarRoot(){
    this.EsRoot = true;
    this.cargarArchivos();
  }
}
