package co.jamper91.pruebabrinks;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.androidanimations.library.Techniques;

import java.util.Hashtable;
import java.util.LinkedHashMap;

import co.jamper91.pruebabrinks.Util.Administrador;
import co.jamper91.pruebabrinks.Util.Animacion;
import co.jamper91.pruebabrinks.Util.AppController;

public class vista_3 extends AppCompatActivity {

    private Administrador admin;
    private Hashtable<String, String> parametros;
    private static LinkedHashMap<String, Animacion> elementos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_3);
        get_paremeters(getIntent().getExtras());
        init_gui();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        admin.init_header(this, "");
    }

    private void get_paremeters(Bundle b)
    {
        parametros = new Hashtable<>();
        parametros.put("app_id", b.getString("app_id"));
    }

    private void init_gui()
    {
        elementos = new LinkedHashMap<>();
        
        elementos.put("img1", new Animacion((NetworkImageView)findViewById(R.id.img1), Techniques.SlideInLeft));
        elementos.put("txt1", new Animacion((TextView)findViewById(R.id.txt1), Techniques.SlideInLeft, admin.fuentes[0]));
        elementos.put("txt2", new Animacion((TextView)findViewById(R.id.txt2), Techniques.SlideInLeft, admin.fuentes[1]));
        
        admin = Administrador.getInstance(this, elementos);
        admin.animar_in(0);
        admin.init_header(this, "");
        get_app(parametros.get("app_id"));
    }

    private void get_app(String id)
    {
        Hashtable<String, String> app = admin.obtener_datos_por_id("apps", id);
        if(app!=null)
        {
            elementos.get("txt1").setText(app.get("name"));
            elementos.get("txt2").setText(app.get("summary"));
            NetworkImageView img1 = (NetworkImageView)elementos.get("img1").getElemento();
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            //Descargo la foto

            img1.setImageUrl(app.get("imagenen_url"), imageLoader);
            img1.setErrorImageResId(R.drawable.fondo_verde_1);
            img1.setDefaultImageResId(R.drawable.fondo_verde_1);
        }
    }
}
