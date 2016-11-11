package co.jamper91.pruebabrinks;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;

import java.util.LinkedHashMap;

import co.jamper91.pruebabrinks.Util.Administrador;
import co.jamper91.pruebabrinks.Util.Animacion;


public class vista_0 extends AppCompatActivity {

    private Administrador admin;
    private static LinkedHashMap<String, Animacion> elementos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_0);

        init_gui();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init_gui();
    }

    private void init_gui()
    {
        elementos = new LinkedHashMap<String, Animacion>();
        admin = Administrador.getInstance(this, elementos);
        elementos.put("txt1", new Animacion((TextView)findViewById(R.id.txt1), Techniques.SlideInDown, admin.fuentes[0]));
        elementos.put("txt2", new Animacion((TextView) findViewById(R.id.txt2), Techniques.SlideInDown, admin.fuentes[1]));
        elementos.put("img1", new Animacion((ImageView)findViewById(R.id.img1), Techniques.SlideInDown, admin.fuentes[1]));
        admin = Administrador.getInstance(this, elementos);
        admin.animar_in(0);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent i = new Intent(vista_0.this, vista_1.class);
                        startActivity(i);
                    }
                },
                2000);

    }
}
