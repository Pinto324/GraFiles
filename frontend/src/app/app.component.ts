import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoginComponent } from './Modulos/login/login.component'; 
import { UserService } from './Utilidades/Usuario';
import { Router } from '@angular/router';
import { MenuEmpleadoComponent } from './Modulos/menus/menu-empleado.component';
import { MenuAdminComponent } from './Modulos/menu-admin/menu-admin.component';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoginComponent,MenuEmpleadoComponent,CommonModule,HttpClientModule,MenuAdminComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Frontend';
  Login = true;
  Empleado = false;
  Admin = false;

  constructor(private userService: UserService, private router: Router) {}
  onLogin(tipo: string) {
    this.Login = false;  // Oculta el componente de login
    if (tipo === 'Empleado') {
      this.Empleado = true;  // Muestra el componente de Empleado
    } else if (tipo === 'Admin') {
      this.Admin = true;  // Aquí puedes activar un componente o lógica para Admin
    }
  }
  onLogout() {
    this.Login = true;
    this.Empleado = false; 
    this.Admin = false;  
    this.userService.setUser("","","");
  }
}
