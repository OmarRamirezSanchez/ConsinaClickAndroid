package mobile.bambu.consinaclick.Modelo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import mobile.bambu.consinaclick.Extras.InternalStorage;
import mobile.bambu.consinaclick.Extras.KEYS;
import mobile.bambu.consinaclick.R;

/**
 * Created by omarsanchez on 03/05/15.
 */
public class Item_menu extends ArrayAdapter {

    ViewHolder holder;

    public Item_menu(Context context,PlatillosArrayList menu){
        super(context, 0, menu);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_producto,
                    parent, false);

            holder=new ViewHolder();
            holder.textView=(TextView)convertView.findViewById(R.id.nombre_platillo);
            holder.check=(CheckBox)convertView.findViewById(R.id.platillo_disponible);
            TextView title = (TextView) convertView
                    .findViewById(R.id.item_nombre);
        }

        //No regresar la inscancia de la vista y regresar la que estoy creando


        //Platillo platillo = (Platillo)getItem(position);

        TextView nombre_producto =(TextView)convertView.findViewById(R.id.nombre_platillo);
        holder=(ViewHolder) convertView.getTag();
        EachRow row= getItem(position);
        Log.d("size", row.text);
        holder.textView.setText("asdasd");
        holder.check.setChecked(false);
;

        return convertView;
    }
    @Override
    public EachRow getItem(int position) {
        return getItem(position);
    }
    private class EachRow
    {
        String text;
        boolean checkBool;
    }
    private class ViewHolder
    {
        TextView textView;

        CheckBox check;
    }



}
