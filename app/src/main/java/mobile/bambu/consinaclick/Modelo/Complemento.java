package mobile.bambu.consinaclick.Modelo;

import java.io.Serializable;

/**
 * Created by bambumobile on 28/08/15.
 */
public class Complemento implements Serializable{
    public String nombre;
    public String precio;

    public Complemento() {
        this.nombre = "No espesificado";
        this.precio = "0";
    }

    public Complemento(String nombre, String precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public void setNombre(String nombre) {
        if (nombre.equals(null) || nombre.trim().equals("")) {
            this.nombre = "No especificado";
        } else {
            this.nombre = nombre;
        }
    }

    public void setPrecio(String precio) {
        if (precio.equals(null) || precio.trim().equals("")) {
            this.precio = "-1";
        } else {
            this.precio = precio;
        }
    }
}
