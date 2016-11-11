package co.jamper91.pruebabrinks.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Created by jamper91 on 27/01/2015.
 */

public class DataBase extends SQLiteOpenHelper {

    private Context context;
    private static String DB_NAME = "prueba.sqlite";

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;

// TODO Auto-generated constructor stub
    }

    public DataBase(Context context)
    {
        super( context , DB_NAME , null , 5);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        generarSql(db);
    }

    protected void generarSql(SQLiteDatabase db)
    {
        //Creo las columnas de categoria
        LinkedList<String> columnas=new LinkedList<String>();
        columnas.add("name Varchar(255)");

        this.ejecutar_sql(db,crear_tabla("categories",columnas));

        //Tabla de aplicaciones
        columnas.clear();
        columnas.add("artist Varchar(255)");
        columnas.add("price Varchar(255)");
        columnas.add("summary Varchar(255)");
        columnas.add("category_id Int NOT NULL");
        columnas.add("title Varchar(255)");
        columnas.add("imagenen_url TEXT");
        columnas.add("link Varchar(255)");
        columnas.add("rights Varchar(255)");
        columnas.add("contentType Varchar(255)");
        columnas.add("name Varchar(255)");
        columnas.add("fecha Varchar(255)");
        columnas.add("fecha2 Varchar(255)");
        this.ejecutar_sql(db,crear_tabla("apps",columnas));


        //Tabla de opciones
        columnas.clear();
        columnas.add("name Varchar(250) NOT NULL");
        columnas.add("valor Varchar(250) NOT NULL");
        this.ejecutar_sql(db,crear_tabla("opciones",columnas));


    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Borro la base de datos
        Log.i("onUpgrade", "Actualizando la base de datos de "+oldVersion+ "  a "+newVersion);
    context.deleteDatabase(DB_NAME);

//        try {
//            copyDataBase();
//        } catch (IOException e) {
//            throw new Error("Error copying database");
//        }
    }

    private String crear_tabla(String table_name, LinkedList<String> columnas)
    {
        //Agrego el campo id
        columnas.addFirst("id INTEGER PRIMARY KEY AUTOINCREMENT");
        String sql="Create table %table_name (%columas);";
        sql=sql.replace("%table_name",table_name);
        String col=columnas.toString();
        col= col.substring(1,col.length()-1);
        sql=sql.replace("%columas",col);
        return sql;

    }

    /**
     * Se encarga de ingresar un registro dentro de una tabla
     * @param table_name Nombre de la tabla donde se va a insertar
     * @param columnas Lista con las columnas a agregar, la lista debe contener
     *                 un array de string donde el primer elemento es el nombre de la columna
     *                 y el segundo es el valor a ingresar, ejemplo: [name][Julian]
     * @return
     */
    protected String insertar_registro(String table_name, LinkedList<String[]> columnas)
    {
        String sql="";
        sql="INSERT INTO %table (%campos) values(%valores)";
        String campos="", valores="";
        for (int i = 0; i < columnas.size(); i++) {
            String columna[]=columnas.get(i);
            campos+=columna[0]+",";
            valores+=columna[1]+",";
        }
        campos=campos.substring(0,campos.length() - 1);
        valores=valores.substring(0, valores.length() - 1);

        sql=sql.replace("%table",table_name);
        sql=sql.replace("%campos",campos);
        sql=sql.replace("%valores", valores);

        this.ejecutar_sql(null,sql);
        Log.d("insertar", sql);
        return sql;
    }

    /**
     * Esta funcion se encarga de eliminar el registro de una tabla especifica
     * @param table_name Nombre de la tabla a eliminar
     * @param id Id del elemento a eliminar
     * @return
     */
    protected String delete_registro(String table_name, String id)
    {
        String sql="delete from %table where id=%id";
        sql=sql.replace("%table",table_name);
        sql=sql.replace("%id",id);
        this.ejecutar_sql(null,sql);
        return sql;
    }


    /**
     * Esta funcion se encarga de actualizar un elemento dentro de una tabla en especifico
     * @param table_name Nombre de la tabla a actualizar
     * @param id Id del elemento a actualizar
     * @param columnas Lista con las columnas a agregar, la lista debe contener
     *                 un array de string donde el primer elemento es el nombre de la columna
     *                 y el segundo es el valor a ingresar, ejemplo: [name][Julian]
     * @return
     */
    protected String update_registro(String table_name, String id, LinkedList<String[]> columnas)
    {
        String sql="UPDATE %table SET %campos WHERE id=%id";
        String campos="";
        for (int i = 0; i < columnas.size(); i++) {
            String columna[]=columnas.get(i);
            campos+=columna[0]+"="+columna[1]+",";

        }
        campos=campos.substring(0,campos.length()-1);

        sql=sql.replace("%table",table_name);
        sql=sql.replace("%campos",campos);
        sql=sql.replace("%id",id);
        this.ejecutar_sql(null,sql);
        return sql;
    }

    public String unescapeHtml4(String text)
    {
        text = text.replace("\n","\n ");
        text = text.replace("\r","\r ");
        text = StringEscapeUtils.unescapeHtml4(text);
        text = text.replace("&ntilde;","ñ");

        return text;
    }

    /**
     * Esta funcion se encarga de ejecutar una sentencia sql que no sea un select
     * @param sql Codigo sql a ejecutar
     * @return
     */
    protected boolean ejecutar_sql(SQLiteDatabase db, String sql)
    {
        try{
            if(db==null)
                db = getWritableDatabase();
            //Convierto htmlentites a texto
//            Log.e("ejecutar_sql","antes: "+sql);
            sql = unescapeHtml4(sql);
            db.execSQL(sql);
//            Log.e("ejecutar_sql", "despues: " + sql);
            return true;
        }catch (Exception e)
        {
//            Log.e("ejecutar_sql",sql);
//            Log.e("ejecutar_sql", e.getMessage());
//            Log.e("ejecutar_sql", e.getLocalizedMessage());
            return false;
        }

    }

    /**
     * Esta funcion se encarga de ejecutar una accion select de sql y retornar los resultados
     * @param sql Sentencia sql a ejecutar
     * @return
     */
    private Cursor ejecutar_select(String sql)
    {
        try{
            SQLiteDatabase db = getWritableDatabase();
            Cursor c= db.rawQuery(sql,null);
//            db.close();
            return c;
        }catch (Exception e)
        {
            Log.e("Error ejecutando sql",sql);
            Log.e("Error ejecutando sql", e.getMessage());
            return null;
        }
    }

    /**
     * Esta funcion se encarga de consultar el elemento de una tabla, basado en el id
     * @param table_name
     * @param id
     * @return
     */
    protected Hashtable<String,String> obtener_datos_por_id(String table_name, String id)
    {
        Hashtable<String, String> datos = new Hashtable<String, String>();
        String sql="SELECT * FROM %table where id=%id";
        sql=sql.replace("%table",table_name);
        sql=sql.replace("%id", id);
        Cursor c=this.ejecutar_select(sql);
        if(c!=null)
        {
            while (c.moveToNext())
            {
                for(int i=0;i<c.getColumnCount();i++)
                {
                    if(c.getString(i)!=null)
                    {
                        String columna=c.getColumnName(i);
                        columna=columna.replace("\n","\n ");
                        columna=columna.replace("\r","\r ");
                        columna= StringEscapeUtils.unescapeHtml4(columna);
                        columna=columna.replace("&ntilde;","ñ");
                        String valor=c.getString(i);
                        valor = valor.replace("\\n", System.getProperty("line.separator"));
                        valor = valor.replace("\\r", "");
                        valor = StringEscapeUtils.unescapeHtml4(valor);
                        valor = valor.replace("&ntilde;","ñ");

                        datos.put(columna,valor);
                    }else
                        datos.put(c.getColumnName(i), "null");
                }
            }
            if(datos.size()>0)
                return  datos;
            else
                return null;
        }else{
            return null;
        }

    }

    /**
     * Esta accion se encarga de consultar todos los elementos de una tabla
     * @param table_name Nombre de la tabla a consultar
     * @return
     */
    protected LinkedList<Hashtable<String,String>> obtener_datos_por_tabla(String table_name)
    {
        LinkedList<Hashtable<String,String>> retornar= new LinkedList<Hashtable<String,String>>();
        String sql="SELECT * FROM %table";
        sql=sql.replace("%table",table_name);
        Cursor c=this.ejecutar_select(sql);
        if(c!=null)
        {
            while (c.moveToNext())
            {
                Hashtable<String, String> datos = new Hashtable<String, String>();
                for(int i=0;i<c.getColumnCount();i++)
                {
                    if(c.getString(i)!=null)
                    {
                        String columna=c.getColumnName(i);
                        columna=columna.replace("\n","\n ");
                        columna=columna.replace("\r","\r ");
                        columna= StringEscapeUtils.unescapeHtml4(columna);
                        columna=columna.replace("&ntilde;","ñ");
                        String valor=c.getString(i);
                        valor=valor.replace("\n","\n ");
                        valor=valor.replace("\r","\r ");
                        valor = StringEscapeUtils.unescapeHtml4(valor);
                        valor = valor.replace("&ntilde;","ñ");

                        datos.put(columna,valor);
                    }else
                        datos.put(c.getColumnName(i), "null");
                }
                retornar.add(datos);
            }
            if(retornar.size()>0)
                return  retornar;
            else
                return null;
        }else{
            return null;
        }

    }

    protected LinkedList<Hashtable<String,String>> obtener_datos_por_tabla(String table_name, String order_by)
    {
        LinkedList<Hashtable<String,String>> retornar= new LinkedList<Hashtable<String,String>>();
        String sql="SELECT * FROM %table order by %order";
        sql=sql.replace("%table",table_name);
        sql=sql.replace("%order",order_by);
        Cursor c=this.ejecutar_select(sql);
        if(c!=null)
        {
            while (c.moveToNext())
            {
                Hashtable<String, String> datos = new Hashtable<String, String>();
                for(int i=0;i<c.getColumnCount();i++)
                {
                    if(c.getString(i)!=null)
                    {
                        String columna=c.getColumnName(i);
                        columna=columna.replace("\n","\n ");
                        columna=columna.replace("\r","\r ");
                        columna= StringEscapeUtils.unescapeHtml4(columna);
                        columna=columna.replace("&ntilde;","ñ");
                        String valor=c.getString(i);
                        valor=valor.replace("\n","\n ");
                        valor=valor.replace("\r","\r ");
                        valor = StringEscapeUtils.unescapeHtml4(valor);
                        valor = valor.replace("&ntilde;","ñ");

                        datos.put(columna,valor);
                    }else
                        datos.put(c.getColumnName(i), "null");
                }
                retornar.add(datos);
            }
            if(retornar.size()>0)
                return  retornar;
            else
                return null;
        }else{
            return null;
        }

    }

    /**
     * Se encarga de obtener todos los datos de una tabla dada, filtrandolos por una columna
     * @param table_name Nombre de la tabla a consultar
     * @param column Nombre de la columna con la cual se realizara el filtro
     * @param value Valor con el que se compararan los datos
     * @return
     */
    protected LinkedList<Hashtable<String,String>> obtener_datos_por_columna(String table_name, String column, String value)
    {
        LinkedList<Hashtable<String,String>> retornar= new LinkedList<Hashtable<String,String>>();
        String sql="SELECT * FROM %table where %column=%valor";
        sql=sql.replace("%table",table_name);
        sql=sql.replace("%column",column);
        sql=sql.replace("%valor",value);
        Cursor c=this.ejecutar_select(sql);
        if(c!=null)
        {
            while (c.moveToNext())
            {
                Hashtable<String, String> datos = new Hashtable<String, String>();
                for(int i=0;i<c.getColumnCount();i++)
                {
                    if(c.getString(i)!=null)
                    {
                        String columna=c.getColumnName(i);
                        columna=columna.replace("\n","\n ");
                        columna=columna.replace("\r","\r ");
                        columna= StringEscapeUtils.unescapeHtml4(columna);
                        columna=columna.replace("&ntilde;","ñ");
                        String valor=c.getString(i);
                        valor=valor.replace("\n","\n ");
                        valor=valor.replace("\r","\r ");
                        valor = StringEscapeUtils.unescapeHtml4(valor);
                        valor = valor.replace("&ntilde;","ñ");

                        datos.put(columna,valor);
                    }else
                        datos.put(c.getColumnName(i), "null");
                }
                retornar.add(datos);
            }
            if(retornar.size()>0)
                return  retornar;
            else
                return null;
        }else{
            return null;
        }

    }
    /**
     * Se encarga de obtener todos los datos de una tabla dada, filtrandolos por una columna y ordenandolos por una columna dada
     * @param table_name Nombre de la tabla a consultar
     * @param column Nombre de la columna con la cual se realizara el filtro
     * @param value Valor con el que se compararan los datos
     * @param order Columna por el cual se ordenaran los datos
     * @return
     */
    protected LinkedList<Hashtable<String,String>> obtener_datos_por_columna(String table_name, String column, String value, String order)
    {
        LinkedList<Hashtable<String,String>> retornar= new LinkedList<Hashtable<String,String>>();
        String sql="SELECT * FROM %table where %column=%valor ORDER BY %order";
        sql=sql.replace("%table",table_name);
        sql=sql.replace("%column",column);
        sql=sql.replace("%valor",value);
        sql=sql.replace("%order",order);
        Cursor c=this.ejecutar_select(sql);
        if(c!=null)
        {
            while (c.moveToNext())
            {
                Hashtable<String, String> datos = new Hashtable<String, String>();
                for(int i=0;i<c.getColumnCount();i++)
                {
                    if(c.getString(i)!=null)
                    {
                        String columna=c.getColumnName(i);
                        columna=columna.replace("\n","\n ");
                        columna=columna.replace("\r","\r ");
                        columna= StringEscapeUtils.unescapeHtml4(columna);
                        columna=columna.replace("&ntilde;","ñ");
                        String valor=c.getString(i);
                        valor=valor.replace("\n","\n ");
                        valor=valor.replace("\r","\r ");
                        valor = StringEscapeUtils.unescapeHtml4(valor);
                        valor = valor.replace("&ntilde;","ñ");

                        datos.put(columna,valor);
                    }else
                        datos.put(c.getColumnName(i), "null");
                }
                retornar.add(datos);
            }
            if(retornar.size()>0)
                return  retornar;
            else
                return null;
        }else{
            return null;
        }

    }

    protected LinkedList<Hashtable<String,String>> obtener_datos(String sql)
    {
        LinkedList<Hashtable<String,String>> retornar= new LinkedList<Hashtable<String,String>>();

        Cursor c=this.ejecutar_select(sql);
        if(c!=null)
        {

            while (c.moveToNext())
            {

                Hashtable<String, String> datos = new Hashtable<String, String>();
                for(int i=0;i<c.getColumnCount();i++)
                {
                    if(c.getString(i)!=null)
                    {
                        String columna=c.getColumnName(i);
                        columna=columna.replace("\n","\n ");
                        columna=columna.replace("\r","\r ");
                        columna= StringEscapeUtils.unescapeHtml4(columna);
                        columna=columna.replace("&ntilde;","ñ");
                        String valor=c.getString(i);
                        valor=valor.replace("\n", "\n ");
                        valor=valor.replace("\r","\r ");
                        valor = StringEscapeUtils.unescapeHtml4(valor);
                        valor = valor.replace("&ntilde;", "ñ");
                        valor = valor.replaceAll("\\s+$", "");
                        datos.put( columna, valor);
                    }else
                        datos.put(StringEscapeUtils.unescapeHtml4(c.getColumnName(i)), "null");
                }
                retornar.add(datos);
            }
            if(retornar.size()>0)
                return  retornar;
            else
                return null;
        }else{
            return null;
        }

    }

}

