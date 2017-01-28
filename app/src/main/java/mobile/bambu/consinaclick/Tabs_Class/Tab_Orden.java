    package mobile.bambu.consinaclick.Tabs_Class;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.ListView;

import java.util.ArrayList;

import mobile.bambu.consinaclick.Extras.InternalStorage;
import mobile.bambu.consinaclick.Extras.KEYS;
import mobile.bambu.consinaclick.FragmenMainManager;
import mobile.bambu.consinaclick.Modelo.Pedido;
import mobile.bambu.consinaclick.Modelo.PedidosArrayList;
import mobile.bambu.consinaclick.OrdenFragmet;
import mobile.bambu.consinaclick.R;

/**
 * Created by Recknier on 09/04/2015.
 */
public class Tab_Orden extends Fragment implements AdapterView.OnItemClickListener{

    protected ArrayList<Pedido> pedidos;
    public Item_Orden order;
    public ListView lista_de_Ordenes;
    public int type_of_tab;
    ViewStub bg_view;
    public FragmenMainManager mainManager;
    public boolean is_In_Animation;


    @Override
    public void onStart() {
        super.onStart();
        intanceListView();
    }

     @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_lista_pedidos_activity,container,false);
        bg_view =(ViewStub)v.findViewById(R.id.bg_list_view);
        bg_view.setVisibility(View.VISIBLE);
        is_In_Animation=false;
         mainManager = (FragmenMainManager)getActivity();
        lista_de_Ordenes =(ListView)v.findViewById(R.id.lista_de_pedodos);
        lista_de_Ordenes.setOnItemClickListener(this);
        lista_de_Ordenes.post(new Runnable() {
            @Override
            public void run() {
                lista_de_Ordenes.smoothScrollToPosition(0);
            }
        });
        return v;
    }
    public void intanceListView(){
        try {

            switch (type_of_tab){
                case 0:
                    pedidos= mainManager.usuario.getPedidos().pendientes;
                    break;
                case 1:
                    pedidos=mainManager.usuario.getPedidos().aceptados;
                    break;
                case 2:
                    pedidos=mainManager.usuario.getPedidos().rechazados;
                    break;
                case 3:
                    pedidos=mainManager.usuario.getPedidos();
                    break;
                default:
                    pedidos=mainManager.usuario.getPedidos();
                    break;
            }
        }catch (Exception e){
            pedidos=new ArrayList<>();
        }

        order = new Item_Orden(this.getActivity().getBaseContext(),pedidos);
        lista_de_Ordenes.setAdapter(order);
        if(order.isEmpty()){
            lista_de_Ordenes.setBackgroundColor(Color.TRANSPARENT);
            bg_view.setVisibility(View.VISIBLE);
        }else{
            lista_de_Ordenes.setBackgroundColor(Color.WHITE);
            bg_view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
        if (!is_In_Animation){
           is_In_Animation=true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Chronometer chronometer =(Chronometer)view.findViewById(R.id.cronometro);
                pedidos.get(position).tiempo= (int) (SystemClock.elapsedRealtime()-chronometer.getBase());
                OrdenFragmet ordenFragmet =new OrdenFragmet();
                Bundle args = new Bundle();
                args.putSerializable(KEYS.KEY_PEDIDO,pedidos.get(position));
                ordenFragmet.setArguments(args);

                ((FragmenMainManager)getActivity()).is_in_main_view=false;
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.entrar_izquierda, R.anim.salir_derecha)
                        .replace(R.id.container_fragmet, ordenFragmet)
                        .commit();
            }
        }, 200);
      }
    }

}
