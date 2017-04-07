package mobile.bambu.consinaclick.Modelo;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Recknier on 21/04/2015.
 */
public class PedidosArrayList extends ArrayList<Pedido> {

    public ArrayList<Pedido> pendientes;
    public ArrayList<Pedido> aceptados;
    public ArrayList<Pedido> rechazados;


    public PedidosArrayList() {
        super();
        pendientes = new ArrayList<>();
        aceptados = new ArrayList<>();
        rechazados = new ArrayList<>();
    }

    @Override
    public boolean add(Pedido object) {
        if (!this.contiene(object)) {
            switch (object.estado) {
                case 1:
                    pendientes.add(object);
                    break;
                case 2:
                    aceptados.add(object);
                    break;
                case 3:
                    rechazados.add(object);
                    break;
                default:
                    super.add(object);
                    break;
            }
            return super.add(object);
        }
        return false;
    }

    public boolean addJSON(JSONObject jsonObject) {
        Pedido pedido_res = new Pedido();
        try {
            pedido_res.setId(jsonObject.getString("id"));
            pedido_res.setNombre_persona(jsonObject.getString("cliente"));
            pedido_res.setHora_de_creacion(jsonObject.getString("hora"));
            pedido_res.setTotal(jsonObject.getString("total"));
            pedido_res.setTipo_entrega(jsonObject.getString("tipo"));
            pedido_res.setDistancia(jsonObject.getString("distance"));
            pedido_res.setEstado(jsonObject.getInt("status_id"));
            Log.e("PedidosArrayList","ID :"+jsonObject.getInt("status_id"));
            pedido_res.setTotal(jsonObject.getString("total"));
            pedido_res.setNumero_telefonico(jsonObject.getString("phone"));
            pedido_res.setEmail(jsonObject.getString("email"));
            pedido_res.setDireccion(jsonObject.getString("address"));
            pedido_res.setDetalles_orden(jsonObject.getString("comentario"));
            pedido_res.setPlatillos(jsonObject.getString("orderDishes"));
            pedido_res.setLatLog(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
        } catch (Exception e) {
            Log.e("PedidosArrayList", "Murio al crear el Pedido" + e.toString());
            return this.add(pedido_res);
        }
        Log.i("PedidosArrayList", "Pedido : " + pedido_res.toString() + "");
        return this.add(pedido_res);
    }

    public boolean contiene(Pedido obj) {
        for (int i = 0; i < size(); i++) {
            if (get(i).id.equals(obj.id)) {
                return true;
            }
        }
        return false;
    }

    public void actualizar_estado_rechazado(Pedido pedido) {
        for (int i = 0; i < size(); i++) {
            if (pedido.id.equals(get(i).id)) {
                get(i).estado = 3;
                get(i).tiempo = pedido.tiempo;
                rechazados.add(get(i));
                pendientes.remove(get(i));
            }
        }
    }

    public void actualizar_estado_aceptado(Pedido pedido) {
        for (int i = 0; i < size(); i++) {
            if (pedido.id.equals(get(i).id)) {
                get(i).estado = 2;
                get(i).tiempo = pedido.tiempo;
                aceptados.add(get(i));
                pendientes.remove(get(i));
            }
        }
    }
}
