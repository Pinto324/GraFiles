import { Component, Input,Output, EventEmitter  } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../Utilidades/Usuario';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms'; 
@Component({
  selector: 'app-modal-archivos',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './modal-archivos.component.html',
  styleUrl: './modal-archivos.component.css'
})
export class ModalArchivosComponent {
  NuevaCarpeta = false;
  valorInput: string = '';
  @Input() isOpen: boolean = false; // Propiedad para controlar la visibilidad del modal
  @Input() archivoSeleccionado: any;
  @Input() IdPadre: any;
  @Output() close = new EventEmitter<void>();
  @Output() Actualizar = new EventEmitter<boolean>();
  constructor(private http: HttpClient, private UserService:UserService  ) { }
  closeModal() {
    this.close.emit(); // Emitir el evento de cierre
  }
  CrearCarpeta() {
    if (this.valorInput.trim() === '') {
      alert('El campo no puede estar vacío.');
    } else {
      const url = 'http://localhost:8080/GraFiles/ArchivosServlet';
      const body = new URLSearchParams();
      body.set('NombreCarpeta', this.valorInput);
      body.set('IdPadre', this.IdPadre);
      body.set('IdCreador', this.UserService.getId());
      body.set('accion', "IngresarCarpeta");
      const options = {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      };
      this.http.post(url, body.toString(), options).subscribe(
        (response: any) => {
          // Este bloque se ejecutará solo si la solicitud fue exitosa (estado 200)
            this.Actualizar.emit(true);
            this.close.emit();
        },
        (error) => {
          // Manejo del error basado en el código de estado
          if (error.status === 400) {
            alert('Error al crear la carpeta. no puedes tener el mismo nombre');
          } 
        }
      );
    }      
  }
}
