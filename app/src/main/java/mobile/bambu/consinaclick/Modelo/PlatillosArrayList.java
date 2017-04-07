package mobile.bambu.consinaclick.Modelo;

import android.content.Context;

import java.util.ArrayList;

import mobile.bambu.consinaclick.Extras.InternalStorage;
import mobile.bambu.consinaclick.Extras.KEYS;

/**
 * Created by omarsanchez on 03/05/15.
 */
public class PlatillosArrayList extends ArrayList<Platillo> {


    public PlatillosArrayList(){
        super();

    }

    @Override
    public boolean add(Platillo object) {
        return super.add(object);
    }

    public void actulizar_platillo(Platillo platillo,Context context){
        for (int i=0;i<size();i++){
            if (platillo.id.equals(get(i).id)){
                get(i).disponibilidad=platillo.disponibilidad;
                break;
            }
        }
        try {
            InternalStorage.writeObject(context, KEYS.KEY_MENU,this);

        }catch (Exception e){

        }
    }
}
