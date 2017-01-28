package mobile.bambu.consinaclick.Modelo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class Platillo implements Serializable {

    public String porcion;
    public String id;
    public String nombre;
    public String categoria;
    public String costo;
    public String descripcion;
    public String tamanio;
    public ArrayList<Complemento> complementos;
    public boolean disponibilidad;

    public Platillo(String porcion, String nombre, String costo) {
        this.costo = costo;
        this.nombre = nombre;
        this.porcion = porcion;
        this.descripcion = "";
        this.tamanio = "";
        this.complementos = new ArrayList<>();
    }

    public Platillo(String porcion, String nombre, String costo, ArrayList<Complemento> complmentos) {
        this.costo = costo;
        this.nombre = nombre;
        this.porcion = porcion;
        this.descripcion = "";
        this.tamanio = "";
        this.complementos = complmentos;
    }

    public Platillo() {
        this.id = "";
        this.costo = "";
        this.tamanio = "";
        this.nombre = "";
        this.porcion = "";
        this.categoria = "";
        this.descripcion = "";
        this.disponibilidad = true;
        this.complementos = new ArrayList<>();
    }

    public void setPorcion(String porcion) {
        if (porcion.equals(null) || porcion.trim().equals("")) {
            this.porcion = "No Espesificado";
        }
        this.porcion = porcion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        if (nombre.equals(null) || nombre.trim().equals("") || nombre.trim().equals("null")) {
            this.nombre = "No Espesificado";
        } else {
            this.nombre = nombre;
        }
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCosto(String costo) {
        if (costo.equals(null) || costo.trim().equals("") || costo.trim().equals("null")) {
            this.costo = "-1";
        }
        this.costo = costo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public void setComplementos(ArrayList<Complemento> complementos) {
        this.complementos = complementos;
    }


    public void setComplementos(JSONArray complementosJSON) {

        try {
            for (int k = 0; k < complementosJSON.length(); k++) {
                JSONObject ingreditesJSON = complementosJSON.getJSONObject(k);
                JSONArray arrayIngredientes = ingreditesJSON.getJSONArray("orderIngredients");
                for (int i = 0; i < arrayIngredientes.length(); i++) {
                    Complemento complemento = new Complemento();
                    JSONObject complementoJSON = arrayIngredientes.getJSONObject(i);
                    JSONObject complementoExtra = complementoJSON.getJSONObject("ingredient");
                    complemento.setPrecio(complementoExtra.getString("price"));
                    complemento.setNombre(complementoExtra.getString("name"));
                    this.complementos.add(complemento);
                }
            }
        } catch (Exception e) {
            Log.e("Platillo", "Murio al crear los complementos : " + e.toString());
        }
    }


    public void setTamanio(String tamanio) {
        if (tamanio.equals(null) || tamanio.trim().equals("")) {
            this.tamanio = "";
        } else {
            this.tamanio = tamanio;
        }
    }


    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}
