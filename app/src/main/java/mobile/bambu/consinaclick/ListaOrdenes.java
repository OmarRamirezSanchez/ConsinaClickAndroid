package mobile.bambu.consinaclick;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.materialdialog.MaterialDialog;
import mobile.bambu.consinaclick.Extras.InternalStorage;
import mobile.bambu.consinaclick.Extras.KEYS;
import mobile.bambu.consinaclick.Modelo.Pedido;
import mobile.bambu.consinaclick.Modelo.PedidosArrayList;
import mobile.bambu.consinaclick.Modelo.Usuario;
import mobile.bambu.consinaclick.Networck.GlobalNetwork;
import mobile.bambu.consinaclick.Tabs_Class.SlidingTabLayout;
import mobile.bambu.consinaclick.Tabs_Class.Tab_Orden;
import mobile.bambu.consinaclick.Tabs_Class.ViewPagerAdapter;

/**
 * Created by Recknier on 09/04/2015.
 */
public class ListaOrdenes extends Fragment {



    FragmenMainManager mainManager;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"PENDIENTES","ACEPTADOS","RECHAZADOS","TODOS"};
    int Numboftabs=4;


    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ordenes_activity,container, false);
        mainManager = (FragmenMainManager) getActivity();
        mainManager.usuario.setPedidos(new PedidosArrayList());
        cargarListaDeOrdenes();
        suportToolBar();
        GlobalNetwork.getInstance().setContext(getContext());
        GlobalNetwork.getInstance().sucribeToRestaurant(mainManager.usuario.id_restaurante);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mOrderReceiver, new IntentFilter(KEYS.KEY_UPDATE_ORDERS));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mOrderReceiver);
    }

    private BroadcastReceiver mOrderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            cargarListaDeOrdenes();
        }
    };
   public void suportToolBar(){
        Toolbar toolbar= (Toolbar)getActivity().findViewById(R.id.tool_bar_custom);
        TextView titleBat = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleBat.setText("Lista de Pedidos");
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void cargarListaDeOrdenes() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = getString(R.string.url_base)+getString(R.string.url_pedidos);
        Log.e("ListaOrdenesRes", "URL : " + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ListaOrdenesRes", "Responce" + response.toString());
                          try {
                            JSONArray json_pedidos = new JSONArray(response);
                            if (json_pedidos.length()<1) {
                                    instanceAlert("Ordenes","No tienes ordenes pendientes");
                                    mainManager.cargando_text.setText(getString(R.string.alert_emoy_ordenes_body));
                            }else {
                                for (int i = json_pedidos.length()-1; i >= 0 ; i--) {
                                    mainManager.usuario.pedidos.addJSON(json_pedidos.getJSONObject(i));
                                }
                               instanceView(getView());
                            }
                        }catch (Exception e){
                            Log.wtf("ListaOrdenes", "JSON_error => " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        instanceAlert("Upps....","No pudimos comunicarnos con el servidor trata mas tarde");
                        Log.d("ERROR","HTTP error => "+error.toString());
                    }
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s",mainManager.usuario.getEmail(),mainManager.usuario.getPass()).getBytes(),
                                Base64.DEFAULT))
                );
                return params;
            }
        };
        queue.add(postRequest);
   }

    public void instanceView(View view){
        adapter =  new ViewPagerAdapter(getFragmentManager(),Titles,Numboftabs);
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
    }

    private void instanceAlert(String title,String body){
        try {
        final MaterialDialog materialDialog = new MaterialDialog(getActivity());
        materialDialog.setTitle(title);
        materialDialog.setMessage(body)
                .setPositiveButton(android.R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                    }
                });
        materialDialog.show();
        }catch (Exception e){
            Log.e("ListaOrdenes","No se puedo cargar la notificación");
        }
    }

}
