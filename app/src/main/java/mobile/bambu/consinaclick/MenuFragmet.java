package mobile.bambu.consinaclick;

import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import mobile.bambu.consinaclick.Extras.InternalStorage;
import mobile.bambu.consinaclick.Extras.KEYS;

/**
 * Created by Recknier on 30/04/2015.
 */


public class MenuFragmet extends Fragment implements View.OnClickListener{

    private ListView mainListView ;
    private Button guardar;

    private static String user_email = "";
    private static String pass ="";
    private static Modificados modificados;
    private ArrayList<Platillo> platillos_items;
    private ArrayAdapter<Platillo> listAdapter;

    FragmenMainManager mainFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mis_platillos_fragment,container, false);
        suportToolBar();
        try {
            user_email = (String) InternalStorage.readObject(getActivity().getApplicationContext(), KEYS.KEY_EMAIL);
            pass = (String) InternalStorage.readObject(getActivity().getApplicationContext(),KEYS.KEY_PASS);
        }catch (Exception e){}

        mainFragment = (FragmenMainManager) getActivity();
        platillos_items = new ArrayList<>();
        modificados = new Modificados();
        mainListView = (ListView) rootView.findViewById(R.id.menu_list);
        guardar = (Button) rootView.findViewById(R.id.guardar_menu);
        guardar.setOnClickListener(this);
        listAdapter = new PlanetArrayAdapter(getActivity().getApplicationContext(), platillos_items);
        mainListView.setAdapter(listAdapter);
        donwloadMenu();
        return rootView;
    }


    private void request(String url){
        String url_base = "https://cocina-click.herokuapp.com/api/platillo.json?disponibilidad="+url;
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplication().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, url_base,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            getActivity().onBackPressed();
                        }catch (Exception e){}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", user_email, pass).getBytes(),
                                Base64.DEFAULT))
                );
                return params;
            }
        };
        queue.add(postRequest);

    }

    public void saveData(){
        String ids_aceptados = "true";
        String ids_rechazados = "false";
        for (int i=0;i< modificados.size();i++){

            if (modificados.get(i).checked){
                ids_aceptados+="&ids[]="+modificados.get(i).id;
            }else{
                ids_rechazados +="&ids[]="+modificados.get(i).id;
            }
        }

        request(ids_aceptados);
        request(ids_rechazados);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.guardar_menu){
            saveData();
        }
    }

    private static class Platillo {
        private String name = "" ;
        private String id = "";
        private boolean checked = false ;

        public Platillo(String name,String id,boolean checked ) {
            this.name = name ;
            this.id = id;
            this.checked = checked;
        }

        public Platillo( String name, boolean es_categoria ) {
            this.name = name ;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public boolean isChecked() {
            return checked;
        }
        public void setChecked(boolean checked) {
            this.checked = checked;
        }
        public String toString() {
            return name ;
        }
        public void toggleChecked() {
            checked = !checked ;
        }
    }

    private static class PlanetViewHolder {
        private CheckBox checkBox ;
        private TextView textView ;
        public PlanetViewHolder( TextView textView, CheckBox checkBox ) {
            this.checkBox = checkBox ;
            this.textView = textView ;
        }
        public CheckBox getCheckBox() {
            return checkBox;
        }
        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
        public TextView getTextView() {
            return textView;
        }
        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    private static class PlanetArrayAdapter extends ArrayAdapter<Platillo> implements View.OnClickListener {
        private LayoutInflater inflater;
        public PlanetArrayAdapter( Context context, List<Platillo> planetList ) {
            super( context, R.layout.item_producto, R.id.nombre_platillo, planetList );
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context) ;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Platillo platillo = (Platillo) this.getItem(position);
            TextView nombre_tv ;
            CheckBox checkBox;
            if ( convertView == null ) {
                    convertView = inflater.inflate(R.layout.item_producto, null);
                    nombre_tv = (TextView) convertView.findViewById( R.id.nombre_platillo );
                    checkBox = (CheckBox) convertView.findViewById( R.id.platillo_disponible );
                    nombre_tv.setText(platillo.getName());
                    convertView.setTag( new PlanetViewHolder(nombre_tv,checkBox) );
                    checkBox.setOnClickListener(this);
            }

            else {
                PlanetViewHolder viewHolder = (PlanetViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox() ;
                nombre_tv = viewHolder.getTextView() ;

            }
            checkBox.setTag(platillo);
            checkBox.setChecked(platillo.isChecked());
            return convertView;
        }

        @Override
        public void onClick(View v) {
            CheckBox cb = (CheckBox) v ;
            Platillo planet = (Platillo) cb.getTag();
            modificados.add(planet);
            planet.setChecked(cb.isChecked());
        }
    }

    private static class Modificados extends ArrayList<Platillo> {
        public Modificados(){
            super();
        }
       @Override
        public boolean add(Platillo platillo) {

           if (!contiene(platillo)) {
               //Agrega el Platillo
               super.add(platillo);
           }
            return true;
        }
        public boolean contiene(Platillo platillo){
            for (int i=0;i<this.size();i++){
                if (this.get(i).id == platillo.id){
                    return true;
                }
            }
            return false;
        }

    }

    public void suportToolBar(){
        Toolbar toolbar= (Toolbar)getActivity().findViewById(R.id.tool_bar_custom);
        TextView titleBat = (TextView) toolbar.findViewById(R.id.toolbar_title);
        titleBat.setGravity(Gravity.LEFT);
        titleBat.setText("Mi menu");
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void donwloadMenu(){
        mainFragment.sendAlertView();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplication().getApplicationContext());
        String url = "https://cocina-click.herokuapp.com/api/menus.json";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            Log.e("MenuFragment", "JSON: "+response.toString());
                            mainFragment.dismis();
                            getDishes(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mainFragment.dismis();
                        Log.e("MenuFragment", "Murio volley " + error.toString());
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s",user_email, pass).getBytes(),
                                Base64.DEFAULT))
                );
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void getDishes(String json){
        try {
                JSONArray menu = new JSONArray(json);
                for(int i =0;i<menu.length();i++){
                    JSONObject menus =  menu.getJSONObject(i);
                    JSONArray secciones = menus.getJSONArray("sections");
                    for (int j =0;j<secciones.length();j++){
                     JSONObject seccion = secciones.getJSONObject(j);
                        JSONArray dishes = seccion.getJSONArray("dishes");
                        for (int k=0;k<dishes.length();k++){
                            JSONObject dish = dishes.getJSONObject(k);
                            dish.getString("name");
                            platillos_items.add(new Platillo(dish.getString("name"), dish.getInt("id") + "", true));
                        }
                    }
                }
                listAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.e("MenuFragment","Get Dishes Error :  "+e.toString());
        }

    }
}
