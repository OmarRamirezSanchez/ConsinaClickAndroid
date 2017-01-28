package mobile.bambu.consinaclick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Recknier on 23/04/2015.
 */
public class AcercaDeFragment extends Fragment implements View.OnClickListener {


    public Toolbar toolbar;
    public ImageView logo_cocina;
    public TextView title_cocina;

    int count_logo, count_text;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        View view = inflater.inflate(R.layout.creditos_fragment,
                container, false);
        logo_cocina = (ImageView) view.findViewById(R.id.image_fake_logo);
        logo_cocina.setOnClickListener(this);
        title_cocina = (TextView) view.findViewById(R.id.text_view_fake);
        title_cocina.setOnClickListener(this);
        suportToolBar();
        return view;
    }


    public void suportToolBar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_custom);
        toolbar.setTitle("");
        TextView titleBat = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleBat.setGravity(Gravity.LEFT);
        titleBat.setText("Acerca De");
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.image_fake_logo:
                count_logo++;
                count_text=0;
                break;

            case R.id.text_view_fake:
                count_text++;
                if (count_logo != 4){
                    count_logo=0;
                }
                break;
        }
        instanceAnimation();
    }

    private void instanceAnimation(){
        Log.e("AcercaDe","Logo : "+count_logo +" Text : "+count_text);
        if (count_logo==4 && count_text==3){
            Toast.makeText(getContext(),"Animacion",Toast.LENGTH_LONG).show();
            count_logo =0;
            count_text =0;
        }
    }
}
