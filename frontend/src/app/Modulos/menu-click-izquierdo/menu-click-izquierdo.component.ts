import { Component, EventEmitter,Input, Output  } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ModalArchivosComponent} from '../ModalArchivos/modal-archivos.component';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../Utilidades/Usuario';
import { ArchivoService } from '../../Utilidades/Archivos';
@Component({
  selector: 'app-menu-click-izquierdo',
  standalone: true,
  imports: [CommonModule, ModalArchivosComponent],
  templateUrl: './menu-click-izquierdo.component.html',
  styleUrl: './menu-click-izquierdo.component.css'
})
export class MenuClickIzquierdoComponent {
  isVisible: boolean = false;
  isModalOpen: boolean = false;
  Moviendo: boolean = false;
  CrearTexto:boolean = false;
  @Input() Compartiendo: boolean = false;
  @Input() idPadre:string = "";
  @Output() Actualizar = new EventEmitter<boolean>();
  archivoSeleccionado: any;
  archivoAMover: any;
  contextType: 'contenedor' | 'carpeta' | null = null;
  position = { x: 0, y: 0 };
  constructor(private http: HttpClient, private UserService:UserService , private ArchivoService:ArchivoService  ) { }

  openContextMenu(event: MouseEvent, type: 'contenedor' | 'carpeta', archivo: any) {
    this.contextType = type;
    this.isVisible = true;
    this.position = { x: event.clientX, y: event.clientY };
    this.archivoSeleccionado = archivo;
  }

  opcionSeleccionada(opcion: string) {
    this.isVisible = false;
    if (opcion === 'Nueva Carpeta'){
      this.Compartiendo = false;
      this.abrirModalCrear(opcion);
    }else if(opcion === 'Eliminar'){
      this.DesactivarCarpeta();
    }else if(opcion === 'Mover'){
      this.Moviendo = true;
      this.archivoAMover = this.archivoSeleccionado;
    }else if(opcion === 'Colocar'){
      if(this.archivoAMover != null){
        this.MoverCarpeta();
      }
    }else if(opcion === 'Refrescar'){
      this.Actualizar.emit(true);
    }else if(opcion === 'Texto'){
      this.Compartiendo = false;
      this.CrearTexto = true;
      this.abrirModalCrear(opcion);

    }else if(opcion === 'Compartir'){
      this.Compartiendo = true;
      this.abrirModalCrear(this.archivoSeleccionado);
    }
  }
  cerrarMenu(){
    this.isVisible = false;
  }
  abrirModalCrear(archivo: any) {
      this.archivoSeleccionado = archivo; // Almacena el archivo seleccionado
      this.isModalOpen = true; // Abre el modal
  }
  cerrarModal() {
    this.isModalOpen = false; // Método para cerrar el modal
    this.archivoSeleccionado = null; // Reiniciar archivo seleccionado si es necesario
  }
  manejarActualizar(event: boolean) {
    console.log("emitio el evento");
    this.Actualizar.emit(event); // Reenvía el evento al Abuelo
  }
  DesactivarCarpeta() {
    const url = 'http://localhost:8080/GraFiles/ArchivosServlet?accion=Desactivar&IdArchivo='+this.archivoSeleccionado._id;
    const body = new URLSearchParams();
    body.set('IdArchivo', this.archivoSeleccionado._id);
    body.set('accion', "Desactivar");

    const options = {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    };

    this.http.put(url, body.toString(), options).subscribe(
        (response: any) => {
            // Este bloque se ejecutará si la solicitud fue exitosa (estado 200)
            this.Actualizar.emit(true);
            console.log("Emitió actualizar");
        },
        (error) => {
            // Manejo del error basado en el código de estado
            if (error.status === 400) {
                alert('No se desactivó la carpeta correctamente');
            } else {
                console.error('Error en la solicitud:', error);
            }
        }
    );
}
MoverCarpeta() {
  const url = 'http://localhost:8080/GraFiles/ArchivosServlet?accion=MoverCarpeta&IdArchivo='+this.archivoAMover._id+'&IdMover='+this.idPadre;
  const body = new URLSearchParams();
  body.set('IdArchivo', this.archivoAMover._id);
  body.set('accion', "Desactivar");
  const options = {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  };
  this.http.put(url, body.toString(), options).subscribe(
      (response: any) => {
          this.archivoAMover._id = null;
          this.Actualizar.emit(true);
      },
      (error) => {
          if (error.status === 400) {
              alert('No se movio la carpeta correctamente');
          } else {
              console.error('Error en la solicitud:', error);
          }
      }
  );
}

}
