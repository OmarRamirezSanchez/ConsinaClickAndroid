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
 *
 * Created by Recknier on 23/04/2015.
 *
 */

public class InformaicionFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View view = inflater.inflate(R.layout.informacion_fragment,
                container, false);

        suportToolBar();
        return view;
    }

    public void suportToolBar(){
        Toolbar toolbar= (Toolbar)getActivity().findViewById(R.id.tool_bar_custom);
        toolbar.setTitle("");
        TextView titleBat = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleBat.setGravity(Gravity.LEFT);
        titleBat.setText("Información");

        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
