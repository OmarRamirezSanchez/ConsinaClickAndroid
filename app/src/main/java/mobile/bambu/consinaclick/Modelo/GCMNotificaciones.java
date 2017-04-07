package mobile.bambu.consinaclick.Modelo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import mobile.bambu.consinaclick.FragmenMainManager;
import mobile.bambu.consinaclick.R;

/**
 * Created by bambumobile on 29/05/15.
 */
public class GCMNotificaciones extends GCMBaseIntentService {

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    final  int notification_id=0;

    public GCMNotificaciones(){
        super("965593285934");
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String mensaje = intent.getExtras().getString("mensaje");
        sendNotification("Pedido","Tienes un nuevo pedido");

    }

    @Override
    protected void onError(Context context, String s) {
        Log.wtf("GCMNotificaciones","Error Registracion");
    }

    @Override
    protected void onRegistered(Context context, String s) {
        Log.wtf("GCMNotificaciones","Registrado");
    }

    @Override
    protected void onUnregistered(Context context, String s) {

    }

    public void sendNotification(String title_not,String pedido_body){

        notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent =PendingIntent.getActivity(this,notification_id,new Intent(this, FragmenMainManager.class),0);
        builder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title_not).setContentText(pedido_body);
        builder.setDefaults(notification_id);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(notification_id,builder.build()) ;
    }
}
