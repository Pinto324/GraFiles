import { Component, Output, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../Utilidades/Usuario';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  @Output() loginEvent = new EventEmitter<string>();
  username: string = "";
  password: string = "";
  errorMessage: string = "";

  constructor(private http: HttpClient, private userService: UserService) {}

  login(username: string, password: string) {
    const url = 'http://localhost:8080/GraFiles/UsuarioServlet?accion=login';
    const body = new URLSearchParams();
    body.set('Usuario', username);
    body.set('Password', password);
    body.set('accion', "login");
    const options = {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    };

    this.http.post(url, body.toString(), options).subscribe(
      (response: any) => {
        if (response.valid) {
          this.userService.setUser(response.username, response.tipo, response.id);
          this.loginEvent.emit(response.tipo); 
        } else {
          alert('Nombre de usuario o contraseña incorrectos.');
        }
      },
      (error) => {
        console.error(error);
        this.errorMessage = 'Ocurrió un error al iniciar sesión. Por favor, inténtalo de nuevo más tarde.';
      }
    );
  }
}

