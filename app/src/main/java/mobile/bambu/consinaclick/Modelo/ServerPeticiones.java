package mobile.bambu.consinaclick.Modelo;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Recknier on 20/05/2015.
 */
public class ServerPeticiones extends IntentService {

    public ServerPeticiones(){
        super("ServicoPedidos");

    }
    @Override
    protected void onHandleIntent(Intent intent) {
            //Hacer la peticion
        Toast.makeText(getApplicationContext(),"Hola",Toast.LENGTH_LONG).show();
    }
}
