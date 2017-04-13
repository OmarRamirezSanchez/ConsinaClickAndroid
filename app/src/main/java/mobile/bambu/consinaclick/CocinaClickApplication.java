package mobile.bambu.consinaclick;

import android.app.Application;
import android.util.Log;

import mobile.bambu.consinaclick.Networck.GlobalNetwork;

/**
 * Created by Omar on 12/04/2017.
 */

public class CocinaClickApplication extends Application {

    private static GlobalNetwork globalNetwork;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("IMAXAplication","Init Singleton NetWork");
        globalNetwork = new GlobalNetwork(getApplicationContext());
    }
}