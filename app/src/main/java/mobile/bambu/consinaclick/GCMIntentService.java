package mobile.bambu.consinaclick;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import mobile.bambu.consinaclick.FragmenMainManager;
import mobile.bambu.consinaclick.R;

public class GCMIntentService extends GCMBaseIntentService {

    public GCMIntentService() {
        super("165251260760");
    }

    @Override
    protected void onError(Context context, String errorId) {
        Log.d("GCMTest", "REGISTRATION: Error -> " + errorId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String msg = intent.getExtras().getString("msg");
        Log.d("GCMTest", "Mensaje: " + msg);
        String data = intent.getExtras().getString("data");
        String mensaje = intent.getExtras().getString("mensaje");
        String sub = intent.getExtras().getString("subtitulo");
        Log.d("GCMTest", "data: " + data);
        Log.d("GCMTest", "msg: " + mensaje);
        Log.d("GCMTest", "msg: " + mensaje);
        //mostrarNotificacion(context, mensaje,sub);
        sendNotification(mensaje, sub);
    }

    @Override
    protected void onRegistered(Context context, String regId) {
        Log.d("GCMTest", "REGISTRATION: Registrado OK.");

        SharedPreferences prefs =
                context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        String usuario = prefs.getString("usuario", "por_defecto");

//    	registroServidor(usuario, regId);
    }

    @Override
    protected void onUnregistered(Context context, String regId) {
        Log.d("GCMTest", "REGISTRATION: Desregistrado OK.");
    }


    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    private void sendNotification(String title, String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,
                new Intent(this, FragmenMainManager.class), 0);

        builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(msg);
//        .setStyle(new NotificationCompat.BigTextStyle()
//        .bigText(msg))


        builder.setDefaults(NOTIFICATION_ID);
        builder.setAutoCancel(true);


        builder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
