package mobile.bambu.consinaclick.Dialogos;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import mobile.bambu.consinaclick.R;

/**
 * Created by Recknier on 23/04/2015.
 */
public class NotaDetallesDialogo extends DialogFragment{

        TextView detalles;
        public String notas_detalles;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogo_notas_detalles, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        detalles = (TextView)view.findViewById(R.id.tv_nota_detalles);
        detalles.setText(notas_detalles);
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
