package co.jamper91.pruebabrinks.diseno;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.Hashtable;
import java.util.LinkedList;

import co.jamper91.pruebabrinks.R;
import co.jamper91.pruebabrinks.Util.Administrador;
import co.jamper91.pruebabrinks.Util.AppController;


/**
 * Created by Desarrollador on 21/05/2016.
 */
public class ListAdapter_ImagenTexto extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private LinkedList<Hashtable<String, String>> datos;
    private LinkedList<Hashtable<String, String>> todos;
    private int layout_fila=0;
    private Administrador admin;
    private boolean is_online;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ListAdapter_ImagenTexto(Activity activity, LinkedList<Hashtable<String, String>> aplicaciones, Administrador admin1) {
        this.activity = activity;
        this.datos = aplicaciones;
        this.todos= aplicaciones;
        this.layout_fila= R.layout.row1;
        admin=admin1;
        is_online=admin.isOnline();
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int location) {
        return datos.get(location);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(datos.get(position).get("id"));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        View item = convertView;
        if(viewHolder == null ||  !(item.getTag() instanceof ViewHolder))
        {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(layout_fila, null);
            // Creates a ViewHolder and store references to the two children views we want to bind data to.
            viewHolder = new ViewHolder();
            NetworkImageView img1 = (NetworkImageView) item.findViewById(R.id.img1);
            TextView txt1 = (TextView) item.findViewById(R.id.txt1);
            txt1.setTypeface(admin.fuentes[1]);
            viewHolder.setTxt1(txt1);
            viewHolder.setImg1(img1);
            //Save holder
            item.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) item.getTag();
        }
        viewHolder.getTxt1().setText(datos.get(position).get("name"));

        //Obtengo la foto
        try {
            int r = admin.getResourceId("categorie_"+datos.get(position).get("id"));
            viewHolder.getImg1().setDefaultImageResId(r);
        }catch (Exception e){
            viewHolder.getImg1().setImageDrawable(admin.getDrawable(R.drawable.fondo_verde_1));
        }







        return item;
    }
    class ViewHolder{
        TextView txt1;
        NetworkImageView img1;

        public ViewHolder() {
        }

        public ViewHolder(TextView txt1, NetworkImageView img1) {
            this.txt1 = txt1;
            this.img1 = img1;
        }

        public TextView getTxt1() {
            return txt1;
        }

        public void setTxt1(TextView txt1) {
            this.txt1 = txt1;
        }

        public NetworkImageView getImg1() {
            return img1;
        }

        public void setImg1(NetworkImageView img1) {
            this.img1 = img1;
        }
    }

    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if(results.count ==0)
                {
                    notifyDataSetChanged();
                }else{
                    datos = (LinkedList<Hashtable<String, String>>) results.values;
                    notifyDataSetChanged();
                }

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();


                constraint = constraint.toString().toLowerCase();
                if(constraint==null && constraint.length() == 0)
                {
                    results.values = todos;
                    results.count = todos.size();
                }else{
                    LinkedList<Hashtable<String, String>> FilteredArrayNames = new LinkedList<Hashtable<String, String>>();
                    for (int i = 0; i < todos.size(); i++) {
                        Hashtable<String, String> dataNames = todos.get(i);
                        if (dataNames.get("name").toLowerCase().contains(constraint))  {
                            FilteredArrayNames.add(dataNames);
                        }
                    }
                    results.count = FilteredArrayNames.size();
                    results.values = FilteredArrayNames;
                }
                // perform your search here using the searchConstraint String.






                return results;
            }
        };

        return filter;
    }
}

