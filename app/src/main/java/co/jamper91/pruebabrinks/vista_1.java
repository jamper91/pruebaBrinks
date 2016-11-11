package co.jamper91.pruebabrinks;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.androidanimations.library.Techniques;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import co.jamper91.pruebabrinks.Util.Administrador;
import co.jamper91.pruebabrinks.Util.Animacion;
import co.jamper91.pruebabrinks.Util.AppController;
import co.jamper91.pruebabrinks.diseno.ListAdapter_ImagenTexto;
import co.jamper91.pruebabrinks.diseno.Popup;
import co.jamper91.pruebabrinks.objetos.Aplicacion;
import co.jamper91.pruebabrinks.objetos.Category;

public class vista_1 extends FragmentActivity {

    private Administrador admin;
    private static LinkedHashMap<String, Animacion> elementos;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_1);
        init_gui();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init_gui();
    }

    Popup p = null;

    private void init_gui()
    {
        elementos = new LinkedHashMap<>();
        admin = Administrador.getInstance(this, elementos);
        elementos.put("header_img1", new Animacion((ImageView)findViewById(R.id.header_img1), Techniques.SlideInDown));
        elementos.put("header_txt1", new Animacion((TextView)findViewById(R.id.header_txt1), Techniques.SlideInDown));
        elementos.put("header_img2", new Animacion((ImageView)findViewById(R.id.header_img2), Techniques.SlideInDown));
        elementos.put("lst1", new Animacion((AbsListView)findViewById(R.id.lst1), Techniques.ZoomIn));
        elementos.put("edt1", new Animacion((EditText)findViewById(R.id.edt1), Techniques.ZoomIn));
        elementos.put("spn1", new Animacion((Spinner)findViewById(R.id.spn1), Techniques.ZoomIn));
        admin = Administrador.getInstance(this, elementos);
        admin.init_header(this, getString(R.string.categorias));
        admin.animar_in(0);
        get_data();
    }

    private void get_data()
    {
        if(admin.isOnline())
        {
            get_last_data();
        }else{
            get_categories("name");
        }

        EditText edt1 = (EditText)elementos.get("edt1").getElemento();
        edt1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                vista_1.this.adapterImagenTexto.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });
        Spinner spn1 =(Spinner)elementos.get("spn1").getElemento();
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1)
                {
                    get_categories("fecha");
                }else{
                    get_categories("name");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void show_popup()
    {
        p= new Popup(vista_1.this, admin);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(p, "loading");
        transaction.commitAllowingStateLoss();
    }
    private void get_last_data()
    {
        if(admin.isOnline())
        {
            show_popup();

            // Nueva petición JSONObject
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://itunes.apple.com/us/rss/toppaidapplications/limit=20/json",
                    "",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            parseJson(response);
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            get_categories("name");
                                            try {
                                                p.dismiss();
                                            }catch (Exception e)
                                            {

                                            }
                                        }
                                    },
                                    5000);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            );
            AppController.getInstance().getRequestQueue().add(jsArrayRequest);
        }

    }
    ListAdapter_ImagenTexto adapterImagenTexto;
    private void get_categories(String order_by)
    {
        final LinkedList<Hashtable<String, String>> categories = admin.obtener_datos_por_tabla("categories", order_by);
        if(categories!=null){
            adapterImagenTexto = new ListAdapter_ImagenTexto(vista_1.this, categories, admin);
            AbsListView lst1 = (AbsListView) elementos.get("lst1").getElemento();
//            lst1.setTransitionEffect(new CardsEffect());
            lst1.setAdapter(adapterImagenTexto);
            lst1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Obtengo la categoria
                    for (Hashtable<String, String> dato:categories) {
                        if(dato.get("id").equals(id+""))
                        {
                            Intent i = new Intent(vista_1.this, vista_2.class);
                            i.putExtra("category_id", dato.get("id"));
                            startActivity(i);
                        }
                    }

                }
            });
        }
    }
    public List<Aplicacion> parseJson(JSONObject jsonObject) {
        List<Aplicacion> retornar = new ArrayList();

        try {
            // Obtener el array del objeto
            JSONObject feed = jsonObject.getJSONObject("feed");
            JSONArray entries = feed.getJSONArray("entry");

            for (int i = 0; i < entries.length(); i++) {

                try {
                    JSONObject objeto = entries.getJSONObject(i);
                    //Extraer campos
                    String artista = objeto.getJSONObject("im:artist").getString("label");
                    String fecha = objeto.getJSONObject("im:releaseDate").getString("label");
                    String fecha2 = objeto.getJSONObject("im:releaseDate").getJSONObject("attributes").getString("label");
                    artista = artista.replace("'", "");
                    String price = objeto.getJSONObject("im:price").getJSONObject("attributes").getString("amount");
                    String summary = objeto.getJSONObject("summary").getString("label");
                    summary = summary.replace("'", "");
                    String category_id = objeto.getJSONObject("category").getJSONObject("attributes").getString("im:id");
                    String category_name = objeto.getJSONObject("category").getJSONObject("attributes").getString("label");
                    category_name = category_name.replace("'", "");
                    String tittle = objeto.getJSONObject("title").getString("label");
                    tittle = tittle.replace("'", "");
                    //Listado de imagenes
                    JSONArray im_images = objeto.getJSONArray("im:image");
                    String imagen_url="";
                    for(int j = 0; j<im_images.length(); j++){
                        JSONObject image = im_images.getJSONObject(j);
                        imagen_url = image.getString("label");
                    }
                    String link = objeto.getJSONObject("link").getJSONObject("attributes").getString("href");
                    link = link.replace("'", "");
                    String rights = objeto.getJSONObject("rights").getString("label");
                    rights = rights.replace("'", "");
                    String im_contentType = objeto.getJSONObject("im:contentType").getJSONObject("attributes").getString("label");
                    im_contentType = im_contentType.replace("'", "");
                    String name = objeto.getJSONObject("im:name").getString("label");
                    name = name.replace("'", "");
                    String id = objeto.getJSONObject("id").getJSONObject("attributes").getString("im:id");
                    //Generacion de objetos
                    Category category = new Category(category_id, category_name);
                    Aplicacion aplicacion = new Aplicacion(artista, price, summary, category_id, tittle, imagen_url, link, rights, im_contentType, name, id, fecha, fecha2);
                    //Inserto en la base de datos
                    admin.insertar_category(category);
                    admin.insertar_app(aplicacion);
                    retornar.add(aplicacion);

                } catch (JSONException e) {
                    retornar = null;
                }
            }

        } catch (JSONException e) {
            retornar = null;
        }
        return retornar;
    }

}
