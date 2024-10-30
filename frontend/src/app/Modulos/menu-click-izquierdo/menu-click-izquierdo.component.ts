import { Component, EventEmitter,Input, Output  } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ModalArchivosComponent} from '../ModalArchivos/modal-archivos.component';
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
  @Input() idPadre:string = "";
  @Output() Actualizar = new EventEmitter<boolean>();
  archivoSeleccionado: any;
  contextType: 'contenedor' | 'carpeta' | null = null;
  position = { x: 0, y: 0 };

  openContextMenu(event: MouseEvent, type: 'contenedor' | 'carpeta') {
    this.contextType = type;
    this.isVisible = true;
    this.position = { x: event.clientX, y: event.clientY };
  }

  opcionSeleccionada(opcion: string) {
    this.isVisible = false;
    if (opcion === 'Nueva Carpeta'){
      this.abrirModalCrear(opcion);
    }
    
  }
  cerrarMenu(){
    this.isVisible = false;
  }
  abrirModalCrear(archivo: any) {
      this.archivoSeleccionado = archivo; // Almacena el archivo seleccionado
      console.log(this.archivoSeleccionado);
      this.isModalOpen = true; // Abre el modal
  }
  cerrarModal() {
    this.isModalOpen = false; // Método para cerrar el modal
    this.archivoSeleccionado = null; // Reiniciar archivo seleccionado si es necesario
  }
  manejarActualizar(event: boolean) {
    this.Actualizar.emit(event); // Reenvía el evento al Abuelo
  }
}
