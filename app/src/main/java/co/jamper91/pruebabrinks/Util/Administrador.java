package co.jamper91.pruebabrinks.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.jamper91.pruebabrinks.R;
import co.jamper91.pruebabrinks.objetos.Aplicacion;
import co.jamper91.pruebabrinks.objetos.Category;


/**
 * Created by jamper91 on 27/01/2015.
 */

public class Administrador{

    private static Administrador instancia = null;
    private static Context context = null;
    private static FragmentActivity actividad = null;

    private static DataBase db;

    private static LinkedHashMap<String, Animacion> elementos;
    public static Typeface[] fuentes = null;




    private Administrador(Context c, FragmentActivity actividad) {

        context = c;
        init_db();
        this.actividad = actividad;
        fuentes = new Typeface[2];
        fuentes[0] = Typeface.createFromAsset(c.getAssets(),
                "fonts/Abril_Regultar.otf");
        fuentes[1] = Typeface.createFromAsset(c.getAssets(),
                "fonts/Poppins_Regular.ttf");

    }

    private void init_db()
    {
        db = new DataBase(this.context);
    }
    private Administrador(Context c) {
        context = c;
        init_db();
        actividad = null;
        fuentes = new Typeface[2];
        fuentes[0] = Typeface.createFromAsset(c.getAssets(),
                "fonts/Abril_Regultar.otf");
        fuentes[1] = Typeface.createFromAsset(c.getAssets(),
                "fonts/Poppins_Regular.ttf");

    }

    public static Administrador getInstance(Context c, FragmentActivity a) {
        if (instancia == null) {

            instancia = new Administrador(c, actividad);
        }
        actividad = a;
        elementos=new LinkedHashMap<String, Animacion>();
        return instancia;
    }
    public static Administrador getInstance_sinelementos(Context c, FragmentActivity a) {
        if (instancia == null) {

            instancia = new Administrador(c, actividad);
        }
        actividad = a;
        return instancia;
    }
    public static Administrador getInstance(Context c, LinkedHashMap<String, Animacion> elementos2) {
        if (instancia == null) {

            instancia = new Administrador(c);
        }
        elementos=elementos2;
        return instancia;
    }
    public static Administrador getInstance_sinelementos(Context c) {
        if (instancia == null) {

            instancia = new Administrador(c);
        }
        return instancia;
    }

    //region Preferences

    public SharedPreferences obtener_preferencias()
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void escribir_preferencia(String key, String value)
    {
        SharedPreferences pre= obtener_preferencias();
        SharedPreferences.Editor editor = pre.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String obtener_preferencia(String key)
    {
        SharedPreferences pre= obtener_preferencias();
        return pre.getString(key, "");
    }

    //endregion

    //region Funciones utiles

    public void toast(String mensaje)
    {
        Toast.makeText(this.context,mensaje, Toast.LENGTH_SHORT).show();
    }

    public void toast_error(int posicion)
    {
        String mensaje[] = this.context.getResources().getStringArray(R.array.errores);
        if(posicion>=0 && posicion<mensaje.length){
            Toast.makeText(this.context.getApplicationContext(),mensaje[posicion], Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this.context.getApplicationContext(),"-1", Toast.LENGTH_SHORT).show();
        }

    }
    public void toast_mensaje(int posicion)
    {
        String mensaje[] = this.context.getResources().getStringArray(R.array.mensajes);
        if(posicion>=0 && posicion<mensaje.length)
            Toast.makeText(this.context.getApplicationContext(),mensaje[posicion], Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(this.context.getApplicationContext(),"-1", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean r = netInfo != null && netInfo.isConnectedOrConnecting();
        if(!r)
        {
            toast_error(0);
        }

        return r;

    }


    public void animar_in(int pos) {
        Animacion a = (new ArrayList<Animacion>(this.elementos.values())).get(pos);
        a.animar();
        pos++;
        if (pos < elementos.values().size()) {
            final int posi = pos;
            Animacion b = (new ArrayList<Animacion>(this.elementos.values())).get(posi);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    animar_in(posi);
                }
            }, b.getDuration());
        }
    }

    public void animar_out(int pos) {
        Animacion a = (new ArrayList<Animacion>(this.elementos.values())).get(pos);
        a.animar_out();
        pos++;
        if (pos < elementos.values().size()) {
            final int posi = pos;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    animar_out(posi);
                }
            }, 1 * 100);
        }


    }


    public boolean validar_email(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public int getResourceId(String name)
    {
        int id = -1;
        try {
            id = context.getResources().getIdentifier(name, "drawable", context.getApplicationContext().getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = this.context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent( this.context,  this.context.getClass());
        this.context.startActivity(refresh);
        Activity activity = (Activity) context;
        activity.finish();
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public boolean copyToClipboard(String text) {
        try {
            int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                        .getSystemService(context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                        .getSystemService(context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData
                        .newPlainText("blushed", text);
                clipboard.setPrimaryClip(clip);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void share(String subject, String text)
    {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(sharingIntent);
    }

    public void animar(Techniques efecto, View elemento, int duration)
    {
        if(duration==0)
            duration=300;
        YoYo.with(efecto)
                .duration(duration)
                .playOn(elemento);
    }

    public Drawable getDrawable(int id)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id, context.getTheme());
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public ProgressDialog mostrar_dialog(String mensaje)
    {
        ProgressDialog progDailog = new ProgressDialog(context);
        progDailog.setMessage(mensaje);
        progDailog.setIndeterminate(true);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        return progDailog;
    }
    //endregion

    public static void atras(final FragmentActivity a)
    {
        //Logica del boton atras
        if(a.getLocalClassName().equals("vista_2"))
        {

        }else if(a.getLocalClassName().equals("vista_0"))
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            a.startActivity(intent);
        }
    }

    public void init_header(Activity a, String titulo)
    {
        ImageView img1 = (ImageView) a.findViewById(R.id.header_img1);
        TextView txt1 = (TextView) a.findViewById(R.id.header_txt1);
        ImageView img2 = (ImageView) a.findViewById(R.id.header_img2);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividad.onBackPressed();
            }
        });
        txt1.setTypeface(fuentes[0]);
        txt1.setText(titulo);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if(!isOnline())
        {
            img2.setImageResource(R.drawable.offline);
        }else{
            img2.setImageResource(R.drawable.online);
        }
    }

    //region Base de datos

    public static void cerrarConexionBD() {
        db.close();
    }

    /**
     * Se encarga de ingresar un registro dentro de una tabla
     *
     * @param table_name Nombre de la tabla donde se va a insertar
     * @param campos     Lista con las columnas a agregar, la lista debe contener
     *                   un array de string donde el primer elemento es el nombre de la columna
     *                   y el segundo es el valor a ingresar, ejemplo: [name][Julian]
     * @return
     */
    public static void insertar(final String table_name, final LinkedList<String[]> campos) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                db.insertar_registro(table_name, campos);
                return null;
            }

        }.execute();
    }

    /**
     * Esta funcion se encarga de actualizar un elemento dentro de una tabla en especifico
     *
     * @param table_name Nombre de la tabla a actualizar
     * @param id         Id del elemento a actualizar
     * @param campos     Lista con las columnas a agregar, la lista debe contener
     *                   un array de string donde el primer elemento es el nombre de la columna
     *                   y el segundo es el valor a ingresar, ejemplo: [name][Julian]
     * @return
     */
    public static void actualizar(final String table_name, final String id, final LinkedList<String[]> campos) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                db.update_registro(table_name, id, campos);
                return null;
            }

        }.execute();
    }

    /**
     * Esta funcion se encarga de eliminar el registro de una tabla especifica
     *
     * @param table_name Nombre de la tabla a eliminar
     * @param id         Id del elemento a eliminar
     * @return
     */
    public static void eliminar(final String table_name, final String id) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                db.delete_registro(table_name, id);
                return null;
            }

        }.execute();
    }


    /**
     * Esta funcion se encarga de consultar el elemento de una tabla, basado en el id
     *
     * @param table_name
     * @param id
     * @return
     */
    public Hashtable<String, String> obtener_datos_por_id(String table_name, String id) {
        Hashtable<String, String> retornar = db.obtener_datos_por_id(table_name, id);
        return retornar;
    }

    /**
     * Esta accion se encarga de consultar todos los elementos de una tabla
     *
     * @param table_name Nombre de la tabla a consultar
     * @return
     */
    public LinkedList<Hashtable<String, String>> obtener_datos_por_tabla(String table_name) {
        LinkedList<Hashtable<String, String>> retornar = db.obtener_datos_por_tabla(table_name);
        return retornar;
    }

    public LinkedList<Hashtable<String, String>> obtener_datos_por_tabla(String table_name, String order_by) {
        LinkedList<Hashtable<String, String>> retornar = db.obtener_datos_por_tabla(table_name, order_by);
        return retornar;
    }
    /**
     * Se encarga de obtener todos los datos de una tabla dada, filtrandolos por una columna
     * @param table_name Nombre de la tabla a consultar
     * @param column Nombre de la columna con la cual se realizara el filtro
     * @param value Valor con el que se compararan los datos
     * @return
     */
    public LinkedList<Hashtable<String, String>> obtener_datos_por_columna(String table_name, String column, String value) {
        LinkedList<Hashtable<String, String>> retornar = db.obtener_datos_por_columna(table_name, column, value);
        return retornar;
    }
    /**
     * Se encarga de obtener todos los datos de una tabla dada, filtrandolos por una columna y ordenandolos por una columna dada
     * @param table_name Nombre de la tabla a consultar
     * @param column Nombre de la columna con la cual se realizara el filtro
     * @param value Valor con el que se compararan los datos
     * @param order Columna por el cual se ordenaran los datos
     * @return
     */
    public LinkedList<Hashtable<String, String>> obtener_datos_por_columna(String table_name, String column, String value, String order) {
        LinkedList<Hashtable<String, String>> retornar = db.obtener_datos_por_columna(table_name, column, value, order);
        return retornar;
    }

    public LinkedList<Hashtable<String, String>> obtener_datos_por_sql(String sql) {
        LinkedList<Hashtable<String, String>> retornar = db.obtener_datos(sql);
        return retornar;
    }

    public boolean ejecutar_sql(final String sql) {
        ejecutar_Sql es = new ejecutar_Sql();
        String[] parametros = new String[]{sql};
        es.execute(parametros);
        return es.getResultado();

    }
    public boolean ejecutar_sql2(final String sql) {
        return db.ejecutar_sql(null, sql);
    }
    class ejecutar_Sql extends AsyncTask<String, Void, Boolean> {
        private boolean resultado = false;

        @Override
        protected Boolean doInBackground(String... params) {
            return db.ejecutar_sql(null, params[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            resultado = aBoolean;
        }

        public boolean getResultado() {
            return resultado;
        }
    }

    public static void insertar_app(Aplicacion aplicacion){
        String table_name = "apps";
        LinkedList<String[]> campos = aplicacion.get_fields();
        insertar(table_name, campos);

    }
    //endregion
    public static void insertar_category(Category category){
        String table_name = "categories";
        LinkedList<String[]> campos = category.get_fields();
        insertar(table_name, campos);

    }



}
