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
  @Input()textoGuardado: string = ''; 
  @Input()Compartir: boolean = false; 
  @Input()CrearTexto: boolean = false;
  @Input()ModificarTexto: boolean = false;  





  nombreArchivo: string = ''; 
  extensionArchivo: string = '';
  extensiones: string[] = ['txt', 'html']; 
  @Input() isOpen: boolean = false; 
  @Input() archivoSeleccionado: any;
  @Input() IdPadre: any;
  @Output() close = new EventEmitter<void>();
  @Output() Actualizar = new EventEmitter<boolean>();
  selectedFile: File | null = null;
  compartir: string = "ObjectId('507f1f77bcf86cd799439011')";

  @Input() desactivado: boolean = false;
  //manejo de usuario nuevo:
  name: string = "";
  username: string = '';
  password: string = '';
  confirmPassword: string = '';
  passwordMismatch: boolean = false;

  constructor(private http: HttpClient, private UserService:UserService  ) { }

  closeModal() {
    this.close.emit(); 
  }
  //HTTP------------------------------------------------------------
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
            this.Actualizar.emit(true);
            this.close.emit();
            this.isOpen = false;
            this.valorInput="";
        },
        (error) => {
          if (error.status === 400) {
            alert('Error al crear la carpeta. no puedes tener el mismo nombre');
          } 
        }
      );
    }      
  }
  CompartirArchivo(){
    if (this.valorInput.trim() === '') {
      alert('El campo no puede estar vacío.');
    } else {
      const url = 'http://localhost:8080/GraFiles/ArchivosServlet';
      const body = new URLSearchParams();
      body.set('IdObjeto', this.archivoSeleccionado._id);
      body.set('IdPadre', "ObjectId('507f1f77bcf86cd799439011')");
      body.set('NombreDeUsuario', this.valorInput);
      body.set('Nombre', this.archivoSeleccionado.Nombre);
      body.set('Extension', this.archivoSeleccionado.Extension);
      body.set('FechaM', this.archivoSeleccionado.FechaModificacion);
      body.set('FechaC', this.archivoSeleccionado.FechaCreacion); 
      body.set('Contenido', this.archivoSeleccionado.Contenido);
      body.set('Habilitado', this.archivoSeleccionado.Habilitado);
      body.set('accion', "CompartirArchivo");
      const options = {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      };
      this.http.post(url, body.toString(), options).subscribe(
        (response: any) => {
            this.Actualizar.emit(true);
            this.close.emit();
            this.isOpen = false;
            this.valorInput="";
        },
        (error) => {
          if (error.status === 400) {
            alert('Error al compartir archivo. no puedes');
          } 
        }
      );
    }      
  }
  
  guardarTexto() {
    if (!this.nombreArchivo || !this.extensionArchivo || !this.textoGuardado) {
        alert('Por favor, completa todos los campos requeridos.');
        return; 
    }  
    const contenidoEscapado = this.textoGuardado.replace(/\n/g, '(*XDXD)'); 
    const url = 'http://localhost:8080/GraFiles/ArchivosServlet';
    const body = new URLSearchParams();
    body.set('Nombre', this.nombreArchivo);
    body.set('IdPadre', this.IdPadre);
    body.set('IdCreador', this.UserService.getId());
    body.set('Extension', this.extensionArchivo);
    body.set('Contenido', contenidoEscapado);
    body.set('accion', "CrearTexto");
    
    const options = {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    };
    
    this.http.post(url, body.toString(), options).subscribe(
        (response: any) => {
            this.Actualizar.emit(true);
            this.close.emit();
            this.isOpen = false;
            this.valorInput = "";
        },
        (error) => {
            // Manejo del error basado en el código de estado
            if (error.status === 400) {
                alert('Error al crear la carpeta. No puedes tener el mismo nombre.');
            }
        }
    );
}
guardarTextoAntiguo() {
  const contenidoEscapado = this.textoGuardado.replace(/\n/g, '(*XDXD)'); 
  const url = 'http://localhost:8080/GraFiles/ArchivosServlet';
  const body = new URLSearchParams();
  body.set('id', this.archivoSeleccionado._id);
  body.set('Nombre', this.archivoSeleccionado.Nombre);
  body.set('Extension', this.archivoSeleccionado.Extension);
  body.set('Contenido', contenidoEscapado);
  body.set('IdPadre', this.archivoSeleccionado.IdPadre);
  body.set('accion', "ActualizarTexto");
  
  const options = {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  };
  this.http.post(url, body.toString(), options).subscribe(
      (response: any) => {
          this.Actualizar.emit(true);
          this.close.emit();
          this.isOpen = false;
          this.valorInput = "";
      },
      (error) => {
          // Manejo del error basado en el código de estado
          if (error.status === 400) {
              alert('Error al crear la carpeta. No puedes tener el mismo nombre.');
          }
      }
  );
}
  crearUsuario() {
    if (this.password !== this.confirmPassword) {
      this.passwordMismatch = true;
      return;
    }
    this.passwordMismatch = false;
    const url = 'http://localhost:8080/GraFiles/UsuarioServlet';
    const body = new URLSearchParams();
    body.set('Usuario', this.username);
    body.set('Password', this.password);
    body.set('Nombre', this.name);
    body.set('accion', "CrearUsuario");
    const options = {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    };
    this.http.post(url, body.toString(), options).subscribe(
      (response: any) => {
          this.Actualizar.emit(true);
          this.close.emit();
          this.isOpen = false;
          this.valorInput="";
          alert('Se creo un nuevo usuario empleado');
      },
      (error) => {
        if (error.status === 400) {
          alert('Error al crear al usuario. no puede tener el mismo nombre');
        } 
      }
    );
    
  }

}
