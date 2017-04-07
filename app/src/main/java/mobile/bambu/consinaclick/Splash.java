package mobile.bambu.consinaclick;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.andexert.library.RippleView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import mobile.bambu.consinaclick.Extras.InternalStorage;
import mobile.bambu.consinaclick.Extras.KEYS;


public class Splash extends Activity implements View.OnClickListener {


    private RippleView iniciar_sesion,registrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
            String   timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
            Log.e("Splash",timeZone.substring(0, 3) + ":"+ timeZone.substring(3, 5));


        registrar = (RippleView) findViewById(R.id.registrar);
        registrar.setOnClickListener(this);
        iniciar_sesion = (RippleView) findViewById(R.id.iniciar_sesion);
        iniciar_sesion.setOnClickListener(this);
        registrar.setVisibility(View.INVISIBLE);
        iniciar_sesion.setVisibility(View.INVISIBLE);

        if (estaRegistrado()){
            Intent main_list = new Intent(getApplicationContext(), FragmenMainManager.class);
            startActivity(main_list);
            finish();
        }else {
            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                registrar.setVisibility(View.VISIBLE);
                iniciar_sesion.setVisibility(View.VISIBLE);
                }
        }, 3000);
        }
    }

    public boolean estaRegistrado(){

        try {


        if(InternalStorage.readObject(getApplicationContext(),KEYS.KEY_USER_DABE).toString() == null){
            return false;
        }
        }catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(R.anim.entrar_derecha, R.anim.salir_izquerda);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.registrar:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cocinaclick.herokuapp.com/"));
                startActivity(browserIntent);
                break;
            case R.id.iniciar_sesion:
                new  Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Splash.this, IngresoActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, 1000);
                break;
        }
    }



}
