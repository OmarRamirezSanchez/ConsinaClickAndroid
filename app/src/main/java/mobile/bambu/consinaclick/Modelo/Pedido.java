package mobile.bambu.consinaclick.Modelo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Recknier on 15/04/2015.
 */
public class Pedido implements Serializable, Comparable {


    public String id;
    public String nombre_persona;
    public String distancia;
    public String numero_telefonico;
    public String detalles_orden;
    public String email;
    public String direccion;
    public String tipo_entrega; //0: entrega en restaurante 1: Entrega a domicilio
    public String hora_de_creacion;
    public String total;
    public String latitud;
    public String longitud;

    public PlatillosArrayList platillos;

    private static String NO_ESPESIFICADO = "No Especificado";
    public int tiempo;      // tiempo en el que inicia cada orden
    public int estado;      // 1 si esta pendiente, 2 si esta aceptado, 3 Si esta rechazado
    public int tipo;        // 1 si es entrega adomiciolio--- 2 si es para recojer


    public Pedido() {
        id = "";
        detalles_orden = "Sin comentarios";
        direccion = NO_ESPESIFICADO;
        numero_telefonico = NO_ESPESIFICADO;
        email = NO_ESPESIFICADO;
        tipo_entrega = NO_ESPESIFICADO;
        tiempo = 120;
        nombre_persona = NO_ESPESIFICADO;
        distancia = 0 + " Km";
        estado = 1;
        hora_de_creacion = "00:00:00";
        tipo = randInt(0, 1);
        platillos = new PlatillosArrayList();
        total = "0";
    }

    public static int randInt(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    @Override
    public int compareTo(Object another) {
        int compareage = ((Pedido) another).tipo;
        /* For Ascending order*/
        return this.tipo - compareage;
    }

    @Override
    public String toString() {

        return "Nombre: " + nombre_persona + " Total : " + this.total +
                " Id    : " + id +
                " Email : " + email +
                " Date  : " + tiempo +
                " Estado: " + estado;

    }

    public void setLatLog(double lat,double log){

        if(lat<1||log<1){
            this.latitud = "0";
            this.longitud = "0";
        }else {
            this.latitud = ""+lat;
            this.longitud = ""+log;
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre_persona(String nombre_persona) {
        this.nombre_persona = nombre_persona;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void setDistancia(String distancia) {
        if (distancia.equals(null) || distancia.equals("") || distancia.equals("null")) {
            this.distancia = NO_ESPESIFICADO;
        } else {
            this.distancia = round(Double.parseDouble(distancia), 1) + " Kilometros";
        }
    }

    public void setNumero_telefonico(String numero_telefonico) {
        if (numero_telefonico.equals(null) || numero_telefonico.equals("") || numero_telefonico.equals("null")) {
            this.numero_telefonico = NO_ESPESIFICADO;
        } else {
            this.numero_telefonico = numero_telefonico;
        }
    }

    public void setDetalles_orden(String detalles_orden) {
        if (detalles_orden.equals(null) || detalles_orden.equals("")) {
            this.detalles_orden = NO_ESPESIFICADO;
        } else {
            this.detalles_orden = detalles_orden;
        }
    }

    public void setEmail(String email) {
        if (email.equals(null) || email.equals("")) {
            this.email = NO_ESPESIFICADO;
        } else {
            this.email = email;
        }
    }

    public void setDireccion(String direccion) {

        if (direccion.equals(null) || direccion.trim().equals("")) {
            this.direccion = NO_ESPESIFICADO;
        } else {
            this.direccion = direccion;
        }
    }

    public void setTipo_entrega(String tipo_entrega) {
        if (tipo_entrega.equals(null) || tipo_entrega.equals("")) {
            this.tipo_entrega = NO_ESPESIFICADO;
        } else {
            this.tipo_entrega = tipo_entrega;
        }
    }

    public void setTotal(String total) {
        if (total.equals(null) || total.equals("") || total.length() < 1 || total.equals("null")) {
            this.total = "0";
        } else {
            this.total = total;
        }
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public void setHora_de_creacion(String date_time) {
        try {
            this.hora_de_creacion = date_time+ ":00";
        } catch (Exception e) {
            this.hora_de_creacion = "00:00:00";
            e.printStackTrace();
        }
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setPlatillos(String st_platillos) {
        try {
            JSONArray jsonArray = new JSONArray(st_platillos);
            for (int i = 0; i < jsonArray.length(); i++) {
                Platillo platillo = new Platillo();
                JSONObject json_platillo = jsonArray.getJSONObject(i);
                platillo.setDescripcion(json_platillo.getString("observation"));
                platillo.setPorcion(json_platillo.getString("cantidad"));
                JSONObject jsonSize = json_platillo.getJSONObject("size");
                platillo.setTamanio(jsonSize.getString("name"));
                platillo.setComplementos(json_platillo.getJSONArray("extraOrder"));
                JSONObject jsonDish = json_platillo.getJSONObject("dish");
                JSONObject jsonSizeObj = json_platillo.getJSONObject("size");
                platillo.setNombre(jsonDish.getString("name")+" "+jsonSizeObj.getString("name"));
                platillo.setCosto(jsonSizeObj.getString("price"));
                platillos.add(platillo);
            }
        } catch (Exception e) {
            Log.i("Pedido", "Error al Instanciar pedido " + e.toString());
        }

    }

}
