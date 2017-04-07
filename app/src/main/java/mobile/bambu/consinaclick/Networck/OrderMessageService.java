package mobile.bambu.consinaclick.Networck;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import mobile.bambu.consinaclick.Extras.KEYS;
import mobile.bambu.consinaclick.FragmenMainManager;
import mobile.bambu.consinaclick.R;

/**
 * Created by Omar on 06/04/2017.
 */

public class OrderMessageService extends FirebaseMessagingService {
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    final  int notification_id=0;


    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        this.sendNotification("Pedido","Tienes un nuevo pedido");
        this.updateOrders();
    }

    private void updateOrders(){
        Intent intent = new Intent(KEYS.KEY_UPDATE_ORDERS);
        LocalBroadcastManager.getInstance( this.getApplicationContext()).sendBroadcast(intent);
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
