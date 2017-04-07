package mobile.bambu.consinaclick.Dialogos;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import mobile.bambu.consinaclick.Modelo.Complemento;
import mobile.bambu.consinaclick.Modelo.PedidosArrayList;
import mobile.bambu.consinaclick.Modelo.Platillo;
import mobile.bambu.consinaclick.Modelo.PlatillosArrayList;
import mobile.bambu.consinaclick.R;


public class PedidoDetallesDialogo extends DialogFragment {


    public Platillo platillo;
    public TextView nombre_costo, title_complementos, nota_tv,title_nota,total;
    public String total_pedido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogo_pedido_detalles, container);
        title_nota = (TextView) view.findViewById(R.id.title_nota);
        title_complementos = (TextView) view.findViewById(R.id.title_complementos);
        nombre_costo = (TextView) view.findViewById(R.id.tv_lista_comidas_nombre_precio);
        nota_tv = (TextView) view.findViewById(R.id.tv_nota_platillo);
        nota_tv.setText(platillo.descripcion);
        total = (TextView)view.findViewById(R.id.total_tv_costo);
        total.setText(total_pedido);

        if (platillo.descripcion.equals(null) || platillo.descripcion.equals("")){
            title_nota.setVisibility(View.INVISIBLE);
        }
        if (platillo.complementos.size() <= 0) {
            nombre_costo.setVisibility(View.INVISIBLE);
            title_complementos.setVisibility(View.INVISIBLE);
        } else {
            for (int i = 0;i< platillo.complementos.size(); i++) {
                Complemento complemento = platillo.complementos.get(i);
                nombre_costo.setText(nombre_costo.getText() + "\n" + complemento.nombre+" : " + complemento.precio);
            }
        }
        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
