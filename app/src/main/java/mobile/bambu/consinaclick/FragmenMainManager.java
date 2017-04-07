package mobile.bambu.consinaclick;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.materialdialog.MaterialDialog;
import mobile.bambu.consinaclick.Extras.InternalStorage;
import mobile.bambu.consinaclick.Extras.KEYS;
import mobile.bambu.consinaclick.Extras.PlayAudio;

import mobile.bambu.consinaclick.Modelo.Pedido;
import mobile.bambu.consinaclick.Modelo.PedidosArrayList;
import mobile.bambu.consinaclick.Modelo.Usuario;

/**
 * Created by omarsanchez on 28/04/15.
 */

public class FragmenMainManager extends ActionBarActivity {


    public Boolean is_in_main_view;
    public TextView cargando_text;
    public Usuario usuario;
    MaterialDialog alertAutentificacion = new MaterialDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        instanceView(savedInstanceState);
    }
    private void instanceView(Bundle savedInstanceState) {
        this.usuario = loadUser();
        Log.e("FragmentManager", "Usuario 2 : " + usuario.toString());
        is_in_main_view = true;
        cargando_text = (TextView) findViewById(R.id.text_cargando);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragmet, new ListaOrdenes(), "ListaOrdenes")
                    .commit();
        }
    }

    public Usuario loadUser() {
        Usuario usuario_aux = null;
        try {
            usuario_aux = (Usuario) InternalStorage.readObject(getApplicationContext(), KEYS.KEY_USER_DABE);
        } catch (Exception e) {
            Log.e("FragmentMain", "No se pudo recuperar al Usuario " + e.toString());
        }
        return usuario_aux;
    }

    public void sendAlertView() {
        View view = LayoutInflater.from(this).inflate(R.layout.progres_bar_item, null);
        alertAutentificacion.setView(view).show();
    }

    public void dismis() {
        if (!alertAutentificacion.equals(null)) {
            alertAutentificacion.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!usuario.getPuesto().equals(getString(R.string.manager))) {
            Intent objIntent = new Intent(this, PlayAudio.class);
            stopService(objIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!usuario.getPuesto().equals(getString(R.string.manager))) {
            Intent objIntent = new Intent(this, PlayAudio.class);
            stopService(objIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!usuario.getPuesto().equals(getString(R.string.manager))) {
            Intent objIntent = new Intent(this, PlayAudio.class);
            startService(objIntent);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!is_in_main_view)
            menu.setGroupVisible(R.id.grup_menu, false);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //TODO quitarke el gone a los items para que se vean
            case R.id.menu_mi_menu:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.entrar_izquierda, R.anim.salir_derecha)
                        .replace(R.id.container_fragmet, new MenuFragmet())
                        .commit();
                is_in_main_view = false;
                return true;

            case R.id.menu_acerca:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.entrar_izquierda, R.anim.salir_derecha)
                        .replace(R.id.container_fragmet, new AcercaDeFragment())
                        .commit();
                is_in_main_view = false;
                return true;

            case R.id.menu_informacion:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.entrar_izquierda, R.anim.salir_derecha)
                        .replace(R.id.container_fragmet, new InformaicionFragment())
                        .commit();
                is_in_main_view = false;
                return true;

            case R.id.menu_session:
                try {
                    GCMRegistrar.unregister(FragmenMainManager.this);
                    try {
                        InternalStorage.writeObject(getApplicationContext(), KEYS.KEY_USER_DABE, null);
                    } catch (Exception e) {
                        Log.e("IngresoActivity", "No su puedo guardar el usuario : " + e.toString());
                    }
                    Log.i("FragmetMain", "No registrado");
                } catch (Exception e) {
                    Log.i("FragmetMain", "No se pudo des registrar");
                }
                Intent login = new Intent(getApplicationContext(), IngresoActivity.class);
                startActivity(login);
                finish();
                return true;

            case android.R.id.home:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.entrar_derecha, R.anim.salir_izquerda)
                        .replace(R.id.container_fragmet, new ListaOrdenes())
                        .commit();
                is_in_main_view = true;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!is_in_main_view) {
            is_in_main_view = true;
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.entrar_derecha, R.anim.salir_izquerda)
                    .replace(R.id.container_fragmet, new ListaOrdenes())
                    .commit();
        } else {
            super.onBackPressed();
        }
    }
}
