package mobile.bambu.consinaclick;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONObject;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;
import mobile.bambu.consinaclick.Dialogos.PedidoDetallesDialogo;
import mobile.bambu.consinaclick.Extras.KEYS;
import mobile.bambu.consinaclick.Modelo.Complemento;
import mobile.bambu.consinaclick.Modelo.Pedido;
import mobile.bambu.consinaclick.Modelo.Platillo;

/**
 * Created by Recknier on 09/04/2015.
 */
public class OrdenFragmet extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {


    Pedido pedido;
    TextView tv_nombrecliente, tv_total, tv_horapedido, tv_status, tv_comentario;

    TextView tv_ac_llamar, tv_ac_ubicacion, tv_ac_correo;
    ImageView imagen_tipo_de_pedido;

    LinearLayout ly_pedidos;

    RippleView rpv_aceptar, rpv_rechazar;

    MaterialDialog maDiag;
    MaterialDialog error;

    FragmenMainManager mainManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View view = inflater.inflate(R.layout.detalles_activity,
                container, false);
        mainManager = (FragmenMainManager) getActivity();
        pedido = (Pedido) getArguments().getSerializable(KEYS.KEY_PEDIDO);
        long timeElapsed = pedido.tiempo;
        int hours = (int) (timeElapsed / 3600000);
        int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
        int seconds = (int) (timeElapsed - hours * 3600000 - minutes * 60000) / 1000;
        int total_seconds = (minutes * 60) + seconds + ((hours * 60) * 60);

        pedido.tiempo = total_seconds;

        this.instansElementos(view);
        this.asignarInformacion();
        this.asignar_estado();
        this.incertPlatillos();
        suportToolBar();
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rpv_aceptar:
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true);
                tpd.setMinTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
                tpd.setOnTimeSetListener(this);
                tpd.show(mainManager.getFragmentManager(), "TimePiker");

                break;
            case R.id.rpv_rechazar:
                updateEstado(pedido.id, "3", "&de_time=0");
                break;

            case R.id.tv_llamar:
                hacerLlammada();
                break;
            case R.id.tv_correo:
                if (valiadar_Sting(pedido.email)) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", pedido.email.trim(), null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contacto Restaurante");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
                break;
            case R.id.tv_ubicacion:
                this.verUbicacion();
                break;
        }
    }

    private void verUbicacion() {
        Log.e("OrdenFragment", "Entro a ala Ubicación");
        if (!pedido.longitud.trim().toString().equals("0") && !pedido.latitud.trim().toString().equals("0")) {

            error = new MaterialDialog(getActivity());
            error.setTitle("Localización");
            error.setMessage("No se agrego una ubicación \n ¿ Quieres llamar al cliente ?");
            error.setPositiveButton("Llamar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hacerLlammada();
                    error.dismiss();
                }
            });
            error.setNegativeButton("Cancelar", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    error.dismiss();
                }
            });
            error.show();
        } else {
            double latitude = Double.parseDouble(pedido.latitud);
            double longitude = Double.parseDouble(pedido.longitud);
            Log.e("Ubicar","Ubicando en "+latitude+":"+longitude);
            String label = "ABC Label";
            String uriBegin = "geo:" + latitude + "," + longitude;
            String query = latitude + "," + longitude + "(" + label + ")";
            String encodedQuery = Uri.encode(query);
            String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
            Uri uri = Uri.parse(uriString);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void hacerLlammada() {
        if (valiadar_Sting(pedido.numero_telefonico)) {
            String number = "tel:" + pedido.numero_telefonico.trim();
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
            startActivity(callIntent);
        }
    }

    private void asignar_estado() {
        switch (pedido.estado) {
            case 1:
                this.rpv_rechazar.setEnabled(true);
                this.rpv_aceptar.setEnabled(true);
                break;
            case 2:
                this.rpv_rechazar.setEnabled(false);
                this.rpv_aceptar.setEnabled(false);
                break;
            case 3:
                this.rpv_rechazar.setEnabled(false);
                this.rpv_aceptar.setEnabled(false);
                break;
            default:
                this.rpv_rechazar.setEnabled(true);
                this.rpv_aceptar.setEnabled(true);
                break;
        }
    }

    private void showCustomActualization() {
        maDiag = new MaterialDialog(getActivity());
        View view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.progres_bar_item, null);
        maDiag.setTitle(getString(R.string.act_pedido));
        maDiag.setView(view).show();
    }

    private void showErrorAlert(String body) {
        getActivity().onBackPressed();
    }

    private void instansElementos(View view) {

        this.tv_nombrecliente = (TextView) view.findViewById(R.id.tv_nombre_cliente);
        this.tv_total = (TextView) view.findViewById(R.id.tv_total);
        this.tv_horapedido = (TextView) view.findViewById(R.id.tv_hora_orden);
        this.tv_status = (TextView) view.findViewById(R.id.tv_estado_orden);
        this.tv_comentario = (TextView) view.findViewById(R.id.tv_comentarios);

        this.imagen_tipo_de_pedido = (ImageView) view.findViewById(R.id.im_tipo);

        this.tv_ac_llamar = (TextView) view.findViewById(R.id.tv_llamar);
        this.tv_ac_llamar.setOnClickListener(this);
        this.tv_ac_ubicacion = (TextView) view.findViewById(R.id.tv_ubicacion);
        this.tv_ac_ubicacion.setOnClickListener(this);
        this.tv_ac_correo = (TextView) view.findViewById(R.id.tv_correo);
        this.tv_ac_correo.setOnClickListener(this);

        this.rpv_aceptar = (RippleView) view.findViewById(R.id.rpv_aceptar);
        this.rpv_rechazar = (RippleView) view.findViewById(R.id.rpv_rechazar);

        this.ly_pedidos = (LinearLayout) view.findViewById(R.id.ly_platillos);

        if (pedido.tipo_entrega.toString().trim().equals("0")) {
            this.imagen_tipo_de_pedido.setImageResource(R.mipmap.pickup_icon);
        } else {
            this.imagen_tipo_de_pedido.setImageResource(R.mipmap.delivery_icon);
        }
        
        if (mainManager.usuario.getPuesto().equals("cocinero")|| this.pedido.estado+"" != "") {
            this.rpv_aceptar.setEnabled(false);
            this.rpv_rechazar.setEnabled(false);
        }

    }

    private void asignarInformacion() {
        this.tv_total.setText("Total: $" + this.pedido.total);
        this.tv_nombrecliente.setText(this.pedido.nombre_persona);
        this.tv_horapedido.setText("Ordeno desde : " + this.pedido.hora_de_creacion);
        this.tv_comentario.setText(this.pedido.detalles_orden);
        this.tv_status.setText(this.pedido.distancia);
        this.rpv_aceptar.setOnClickListener(this);
        this.rpv_rechazar.setOnClickListener(this);
    }

    private void incertPlatillos() {
        Log.e("OrdenFragment", "No. Platillos: " + this.pedido.platillos.size());

        Log.e("OrdenFragment", "Inicio : " + this.ly_pedidos.getChildCount());
        for (int i = 0; i < this.pedido.platillos.size(); i++) {
            Platillo platillo = this.pedido.platillos.get(i);

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_platillo, null);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ((TextView) view.findViewById(R.id.tv_item_nombre)).setText(platillo.nombre);
            ((TextView) view.findViewById(R.id.tv_item_costo)).setText("$" + platillo.costo);
            ((TextView) view.findViewById(R.id.tv_item_cantidad)).setText("x"+platillo.porcion);
            Log.e("OrdenFragment", "NombrePlatillo : " + platillo.nombre);
            this.ly_pedidos.addView(view);

            for (Complemento c : platillo.complementos) {

                View complemento = inflater.inflate(R.layout.item_platillo, null);
                complemento.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)complemento.getLayoutParams();
                lp.setMargins(50,0,0,0);
                complemento.setLayoutParams(lp);

                TextView complemento_name = (TextView) complemento.findViewById(R.id.tv_item_nombre);
                complemento_name.setTextSize(13);
                complemento_name.setPadding(10, 0, 0, 0);
                complemento_name.setText(c.nombre);

                TextView complemento_cost = (TextView) complemento.findViewById(R.id.tv_item_costo);
                complemento_cost.setTextSize(13);
                complemento_cost.setText("$"+c.precio);

                TextView complemento_cant = (TextView) complemento.findViewById(R.id.tv_item_cantidad);
                complemento_cant.setTextSize(13);
                complemento_cant.setText("x1");
                this.ly_pedidos.addView(complemento);
            }
        }
        Log.e("OrdenFragment", "Final : " + this.ly_pedidos.getChildCount());


    }


    private void suportToolBar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_custom);
        toolbar.setTitle("");
        TextView titleBat = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleBat.setGravity(Gravity.LEFT);
        titleBat.setText("Orden");
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean valiadar_Sting(String dato) {
        if (dato.equals("No Espesificado")) {
            return false;
        }
        return true;
    }

    private void updateEstado(String id, String status, String tiempo) {
        showCustomActualization();
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            String url = getString(R.string.url_base) + getString(R.string.url_update_pedido) + id + "?status_id=" + status + tiempo;
            Log.wtf("UpdateEstado", "URL : " + url);
            StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.wtf("UpdateEstado", "status : " + response);
                            maDiag.dismiss();
                            try {

                                JSONObject estado = new JSONObject(response);

                                switch (estado.getInt("status")) {
                                    case 1:
                                        showErrorAlert("Tu ordenfue actualizada");
                                        break;
                                    case 2:
                                        showErrorAlert("No se puedo actualizar esta orden");
                                        break;
                                    case 3:
                                        showErrorAlert("La Orden ya fue actualizada");
                                        break;
                                }
                            } catch (Exception e) {
                                Log.wtf("UpdateEstado", "Erro en el JSON " + e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showErrorAlert("No se puedo actualizar esta orden");
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    maDiag.dismiss();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(
                            "Authorization",
                            String.format("Basic %s", Base64.encodeToString(
                                    String.format("%s:%s", mainManager.usuario.getEmail(), mainManager.usuario.getPass()).getBytes(),
                                    Base64.DEFAULT))
                    );
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            maDiag.dismiss();
            Log.e("ERROR", "Murio");
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        Calendar now = Calendar.getInstance();
        int minutos = ( ((hourOfDay * 60) + minute) - ((now.get(Calendar.HOUR_OF_DAY) * 60) + now.get(Calendar.MINUTE)));
        Log.e("Actualizado", "Operación : " + (now.get(Calendar.HOUR_OF_DAY) * 60)+" + "+(now.get(Calendar.MINUTE)) +" : "+((hourOfDay * 60) + minute));
        Log.e("Actualizado", "Operación : " + minutos);
        updateEstado(pedido.id, "2", "&de_time=" + minutos);
    }
}
