package mobile.bambu.consinaclick.Modelo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Recknier on 20/05/2015.
 */
public class AlarmReciver extends WakefulBroadcastReceiver {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent servicio = new Intent(context,ServerPeticiones.class);
        startWakefulService(context,servicio);
    }

    public void setAlerm(Context context){
        alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmReciver.class);
        pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        int minutes = 60000;
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+minutes,minutes,pendingIntent);
    }

}
