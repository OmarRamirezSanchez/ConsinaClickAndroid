package mobile.bambu.consinaclick.Modelo;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Recknier on 23/10/2015.
 */
public class Usuario implements Serializable{

    public String email;
    public String pass;
    public String puesto;
    public String id_restaurante;
    public PedidosArrayList pedidos;


    public Usuario(){
        this.email = "";
        this.pass = "";
        this.puesto = "";
        this.id_restaurante ="";
        this.pedidos =  new PedidosArrayList();
    }

    public Usuario(String email,String pass,String puesto,String restaurante){
        if(email.equals(null)||email.equals("")||
           pass.equals(null)||pass.equals("")||
           puesto.equals("")||puesto.equals(null)||
           restaurante.equals(null)||restaurante.equals("")){
            Log.e("Usuario","El Usuario se Inicializo Vacio");
            this.email = "";
            this.pass = "";
            this.puesto = "";
            this.id_restaurante = "";
            this.pedidos =  new PedidosArrayList();
        }else{
            this.email = email;
            this.pass = pass;
            this.puesto = puesto;
            this.id_restaurante = restaurante;
            this.pedidos =  new PedidosArrayList();
        }
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public void setId_restaurante(String id_restaurante) {
        this.id_restaurante = id_restaurante;
    }

    public void setPedidos(PedidosArrayList pedidos) {
        this.pedidos = pedidos;
    }

    public PedidosArrayList getPedidos() {
        return pedidos;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getPuesto() {
        return puesto;
    }

    public String getId_restaurante() {
        return id_restaurante;
    }

    @Override
    public String toString() {
        return "Usuario " +
                "email      : " + this.email + "\n" +
                "pass       : " + this.pass +"\n" +
                "puesto     : " + this.puesto + "\n" +
                "restaurant : " + this.id_restaurante + "\n" +
                "No.Pedidos : " + this.pedidos.size();
    }
}
