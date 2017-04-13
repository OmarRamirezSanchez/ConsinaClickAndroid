package mobile.bambu.consinaclick.Networck;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mobile.bambu.consinaclick.Extras.KEYS;
import mobile.bambu.consinaclick.FragmenMainManager;
import mobile.bambu.consinaclick.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Omar on 12/04/2017.
 */
public class GlobalNetwork {
    Context context;

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    final  int notification_id=0;

    private static GlobalNetwork ourInstance = new GlobalNetwork();
    public static GlobalNetwork getInstance() {
        return ourInstance;
    }

    public GlobalNetwork() {
    }

    public GlobalNetwork(Context context){
        this.context = context;
    }
    public void setContext(Context context){
        this.context = context;
    }
    public void sendNotification(String title_not,String pedido_body){
        Log.e("GlobalNetwork","Sendin Notificacion  . . . . . . . . . . . .  isNull Context : "+(context == null));
        if (context != null){
            notificationManager= (NotificationManager) this.context.getSystemService(NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,notification_id,new Intent(context, FragmenMainManager.class),0);
            builder = new NotificationCompat.Builder(this.context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title_not).setContentText(pedido_body);
            builder.setDefaults(notification_id);
            builder.setAutoCancel(true);
            builder.setContentIntent(pendingIntent);
            notificationManager.notify(notification_id,builder.build()) ;
        }
    }
    public void sucribeToRestaurant(String restaurant){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ordersRestaurant =  firebaseDatabase.getReference().child("Restaurants").child(restaurant).child("orders");
        ordersRestaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent = new Intent(KEYS.KEY_UPDATE_ORDERS);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                sendNotification("Nuevo Pedido","Tienes nuevos pedidos");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
