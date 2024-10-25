/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objeto;

/**
 *
 * @author branp
 */
public class Usuario {
    private String Usuario;
    private String Password;
    private boolean EsUsuario;

    public Usuario(String Usuario, String Password, boolean EsUsuario) {
        this.Usuario = Usuario;
        this.Password = Password;
        this.EsUsuario = EsUsuario;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public boolean isEsUsuario() {
        return EsUsuario;
    }

    public void setEsUsuario(boolean EsUsuario) {
        this.EsUsuario = EsUsuario;
    }
    
}
