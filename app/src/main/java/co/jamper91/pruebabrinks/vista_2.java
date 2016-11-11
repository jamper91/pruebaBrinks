package co.jamper91.pruebabrinks;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import co.jamper91.pruebabrinks.diseno.ListAdapter_ImagenTexto2;
import co.jamper91.pruebabrinks.Util.Administrador;
import co.jamper91.pruebabrinks.Util.Animacion;

public class vista_2 extends AppCompatActivity {

    private Administrador admin;
    private Hashtable<String, String> parametros;
    private static LinkedHashMap<String, Animacion> elementos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_2);
        
        get_paremeters(getIntent().getExtras());
        init_gui();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        admin.init_header(this, getString(R.string.aplicaciones));
    }

    private void get_paremeters(Bundle b)
    {
        parametros = new Hashtable<>();
        parametros.put("category_id", b.getString("category_id"));
    }
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
        admin.init_header(this, getString(R.string.aplicaciones));
        admin.animar_in(0);
        get_data();
    }

    private void get_data()
    {
        get_apps_by_categorie(parametros.get("category_id"), "name");
        EditText edt1 = (EditText)elementos.get("edt1").getElemento();
        edt1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                vista_2.this.adapterImagenTexto.getFilter().filter(cs);
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
                    get_apps_by_categorie(parametros.get("category_id"),"fecha");
                }else{
                    get_apps_by_categorie(parametros.get("category_id"),"name");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    ListAdapter_ImagenTexto2 adapterImagenTexto;
    private void get_apps_by_categorie(String category_id, String order_by)
    {
        final LinkedList<Hashtable<String, String>> apps = admin.obtener_datos_por_columna("apps", "category_id", category_id, order_by);
        if(apps!=null){
            adapterImagenTexto = new ListAdapter_ImagenTexto2(vista_2.this, apps, admin);
            AbsListView lst1 = (AbsListView) elementos.get("lst1").getElemento();
            lst1.setAdapter(adapterImagenTexto);
            lst1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (Hashtable<String, String> dato:apps) {
                        if(dato.get("id").equals(id+""))
                        {
                            Intent i = new Intent(vista_2.this, vista_3.class);
                            i.putExtra("app_id", dato.get("id"));
                            startActivity(i);
                        }
                    }

                }
            });
        }
    }


}
