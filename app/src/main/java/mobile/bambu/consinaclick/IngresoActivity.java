package mobile.bambu.consinaclick;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gcm.GCMRegistrar;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;

import me.drakeet.materialdialog.MaterialDialog;
import mobile.bambu.consinaclick.Extras.InternalStorage;
import mobile.bambu.consinaclick.Extras.KEYS;
import mobile.bambu.consinaclick.Modelo.Platillo;
import mobile.bambu.consinaclick.Modelo.PlatillosArrayList;
import mobile.bambu.consinaclick.Modelo.Usuario;

/**
 * Created by Recknier on 23/04/2015.
 */

public class IngresoActivity extends ActionBarActivity implements View.OnClickListener {

    EditText correo, password;
    TextView ayuda,terminos_condiciones;
    Button ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_de_session);

        correo = (EditText) findViewById(R.id.email_user);
        password = (EditText) findViewById(R.id.pass_word_user);

        ingresar = (Button) findViewById(R.id.ingresar);
        ingresar.setOnClickListener(this);

        ayuda = (TextView) findViewById(R.id.ayuda);
        ayuda.setOnClickListener(this);

        terminos_condiciones = (TextView)findViewById(R.id.terminos_condiciones);
        terminos_condiciones.setOnClickListener(this);
        suportToolBar();
        asignarCorreoSistema();
    }


    public void asignarCorreoSistema() {
        String possibleEmail = "";
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
            }
        }
        if (!possibleEmail.equals("") && !possibleEmail.equals(null)) {
            correo.setText(possibleEmail);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ingresar) {
            if (validarDatosContraseña(password)&&validarDatosMail(correo)){
                iniciarSession(correo.getText().toString(), password.getText().toString());
            }
        }
        if (v.getId() == R.id.ayuda) {
            //TODO Cambiar la ruta por el de producción
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_ayuda)));
            startActivity(browserIntent);
        }

        if (v.getId() ==  R.id.terminos_condiciones){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_registrarse)));
            startActivity(browserIntent);
        }
    }

    public boolean validarDatosContraseña(EditText editText) {
        if (editText.getText().toString().length() <= 0) {
            editText.setError(getString(R.string.pass_clear));
            return false;
        }
        return true;
    }

    public boolean validarDatosMail(EditText editText) {
        if (editText.getText().toString().length() <= 0) {
            editText.setError(getString(R.string.mail_clear));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches()) {
            editText.setError(getString(R.string.mail_format));
            return false;
        }
        return true;
    }

    public void transicion_Lista_Perdidos(Usuario usuario) {
        Intent lista_pedidos = new Intent(getApplicationContext(), FragmenMainManager.class);
        lista_pedidos.putExtra("Usuario",usuario);
        //TODO Descoment for sucribe a Restaurant
        FirebaseMessaging.getInstance().subscribeToTopic(usuario.getId_restaurante());
        startActivity(lista_pedidos);
        finish();
    }

    public void suportToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_custom);
        toolbar.setTitle("");
        TextView titleBat = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleBat.setText(R.string.inicia_sesion);
        titleBat.setGravity(Gravity.CENTER);
        setSupportActionBar(toolbar);
    }

    public void instanceAlert(String title, String body) {
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setTitle(title);
        materialDialog.setMessage(body)
                .setPositiveButton(android.R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                    }
                });
        materialDialog.show();
    }


    public void iniciarSession(final String email_user, final String pass) {

        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setTitle(getString(R.string.inicia_sesion));
        materialDialog.setMessage(getString(R.string.inicar_sesion_ayuda));
        materialDialog.show();
        materialDialog.setCanceledOnTouchOutside(false);
        String  regID="";

        try {
            GCMRegistrar.checkDevice(IngresoActivity.this);
            GCMRegistrar.checkManifest(IngresoActivity.this);
            regID = GCMRegistrar.getRegistrationId(IngresoActivity.this);
            if (regID.equals("")||regID.equals(null)) {
                Log.e("PushRegister","Usuario no registrado ");
                GCMRegistrar.register(IngresoActivity.this, "965593285934");
                regID = GCMRegistrar.getRegistrationId(IngresoActivity.this);
                Log.e("PushRegister","Token de Registro: "+regID);
            } else {
                Log.i("PushRegister", "USUARIO YA REGISTRADO regId : " + regID);
            }
        }catch (Exception e){
            Log.e("PushRegister", "No se pudo registrar "+e.toString());
        }


        String url =getString(R.string.url_base)+getString(R.string.url_login)+regID;
        Log.e("Ingreso","URL: "+url);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        materialDialog.dismiss();
                        Log.e("responce", "Responce : " + response);
                        try {
                            JSONObject persona = new JSONObject(response);
                            Usuario usuario = null;
                            switch (persona.getInt("status")) {
                                case 0:
                                    instanceAlert(getString(R.string.datos_invalidos_titulo),getString(R.string.datos_invalidos_cuerpo));
                                    break;
                                case 1:
                                     usuario = new Usuario(correo.getText().toString(),
                                             password.getText().toString(),
                                            persona.getString("puesto"),
                                             persona.getString("restaurant_id"));
                                    saveUser(usuario);
                                    transicion_Lista_Perdidos(usuario);

                                    break;
                                case 2:
                                    instanceAlert(getString(R.string.correo_invalido_titulo),getString(R.string.correo_invalido_cuerpo));
                                    break;
                                case 3:
                                    usuario = new Usuario(correo.getText().toString(),
                                            password.getText().toString(),
                                            persona.getString("puesto"),
                                            persona.getString("restaurant_id"));
                                    saveUser(usuario);
                                    transicion_Lista_Perdidos(usuario);
                                    break;
                                case 4:
                                    instanceAlert(getString(R.string.correo_invalido_titulo),getString(R.string.usuario_no_verificado));
                                    break;
                                default:
                                    instanceAlert(getString(R.string.conexion_fallida_titulo),getString(R.string.conexion_fallida_cuerpo));
                                    break;
                            }
                        } catch (Exception e) {
                            materialDialog.dismiss();
                            instanceAlert(getString(R.string.conexion_fallida_titulo),getString(R.string.conexion_fallida_cuerpo));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        materialDialog.dismiss();
                        Log.e("IngresoActivity", "Error : "+error.toString());
                        instanceAlert(getString(R.string.conexion_fallida_titulo),getString(R.string.conexion_fallida_cuerpo));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", email_user, pass).getBytes(),
                                Base64.DEFAULT))
                );
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void saveUser(Usuario usuario){
        try {
        InternalStorage.writeObject(getApplicationContext(),KEYS.KEY_USER_DABE,usuario);
        }catch (Exception e){
            Log.e("IngresoActivity","No su puedo guardar el usuario : "+e.toString());
        }
    }
}
