package mobile.bambu.consinaclick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by bambumobile on 10/02/16.
 */
public class AceptarOrden extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detalles_activity,container, false);





        return view;
    }

    private void suportToolBar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_custom);
        toolbar.setTitle("");
        TextView titleBat = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleBat.setGravity(Gravity.LEFT);
        titleBat.setText("Orden");
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
