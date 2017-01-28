package mobile.bambu.consinaclick.Tabs_Class;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mobile.bambu.consinaclick.Extras.InternalStorage;
import mobile.bambu.consinaclick.Extras.KEYS;
import mobile.bambu.consinaclick.Modelo.Pedido;
import mobile.bambu.consinaclick.Modelo.PedidosArrayList;
import mobile.bambu.consinaclick.R;

/**
 * Created by Recknier on 09/04/2015.
 */
public class Item_Orden extends ArrayAdapter {

    public Item_Orden(Context context, ArrayList<Pedido> users) {
        super(context, 0, users);

    }

    @Override
    public void insert(Object object, int index) {
        super.insert(object, index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_pedido,
                    parent, false);

            TextView title = (TextView) convertView
                    .findViewById(R.id.item_nombre);
        }

        Pedido pedido = (Pedido)getItem(position);

        TextView nombre =(TextView)convertView.findViewById(R.id.item_nombre);
        TextView distancia =(TextView)convertView.findViewById(R.id.distancia_item);
        TextView telefono =(TextView)convertView.findViewById(R.id.telefono_item);
        ImageView ic_tipo =(ImageView)convertView.findViewById(R.id.indicador_de_tipo);
        ImageView estado = (ImageView)convertView.findViewById(R.id.estado_del_pedido);
        Chronometer chronometer = (Chronometer)convertView.findViewById(R.id.cronometro);

        nombre.setText(pedido.nombre_persona);
        distancia.setText(pedido.distancia);
        telefono.setText("Tel : "+pedido.numero_telefonico);
        switch (pedido.tipo_entrega){
            case "1":
                ic_tipo.setImageResource(R.mipmap.delivery_icon);
                break;
            case "0":
                ic_tipo.setImageResource(R.mipmap.pickup_icon);
                break;
        }

        switch (pedido.estado){
            case 1:
                estado.setImageResource(R.mipmap.pendientes);
                //Asiganamos el tiempo de diferencia de cada pedido asigando el tiempode llegada
                chronometer.setBase(SystemClock.elapsedRealtime() - (diferencia_Segundos(pedido.hora_de_creacion) * 1000));
                chronometer.start();
                break;
            case 2:
                estado.setImageResource(R.mipmap.aceptadas);
                chronometer.setBase(SystemClock.elapsedRealtime() - (diferencia_Segundos(pedido.hora_de_creacion) * 1000));
                chronometer.stop();
                break;
            case 3:
                estado.setImageResource(R.mipmap.rechazadas);
                chronometer.setBase(SystemClock.elapsedRealtime() - (diferencia_Segundos(pedido.hora_de_creacion) * 1000));
                chronometer.stop();
                break;
        }
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        convertView.startAnimation(animation);

        return convertView;
    }

    public long diferencia_Segundos(String hora_de_llegada){

        Calendar calendar_actual =Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date tiempo_de_llegada,tiempo_actual;

        long elapsedSeconds=0;
        long elapsedMinutes=0;
        long elapsedHours=0;
        try {
            tiempo_de_llegada =simpleDateFormat.parse(hora_de_llegada);
            tiempo_actual = simpleDateFormat.parse(calendar_actual.get(Calendar.HOUR_OF_DAY) + ":" + calendar_actual.get(Calendar.MINUTE) + ":" + calendar_actual.get(Calendar.SECOND));
            //milliseconds
            long different = tiempo_actual.getTime() - tiempo_de_llegada.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

             elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            elapsedSeconds = different / secondsInMilli;
        }catch (Exception e){}
        return ((elapsedHours*60)*60)+(elapsedMinutes*60)+elapsedSeconds;
    }
}
