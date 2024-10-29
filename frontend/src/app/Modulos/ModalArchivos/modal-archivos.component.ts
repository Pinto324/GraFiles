import { Component, Input,Output, EventEmitter  } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-modal-archivos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './modal-archivos.component.html',
  styleUrl: './modal-archivos.component.css'
})
export class ModalArchivosComponent {
  @Input() isOpen: boolean = false; // Propiedad para controlar la visibilidad del modal
  @Input() archivoSeleccionado: any;
  @Output() close = new EventEmitter<void>();
  closeModal() {
    this.close.emit(); // Emitir el evento de cierre
  }
}
