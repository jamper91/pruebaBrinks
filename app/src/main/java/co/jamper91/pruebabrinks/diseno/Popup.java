package co.jamper91.pruebabrinks.diseno;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.DoubleBounce;

import co.jamper91.pruebabrinks.R;
import co.jamper91.pruebabrinks.Util.Administrador;


/**
 * Created by jamper91 on 03/03/2015.
 */
public class Popup extends DialogFragment {

    private Context c;
    private boolean cancelable=false;
    private  Administrador admin;
    @SuppressLint("ValidFragment")
    public Popup()
    {
    }
    @SuppressLint("ValidFragment")
    public Popup(Context c, Administrador admin)
    {
        this.c=c;
        this.admin = admin;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(cancelable);
        //dialog.getWindow().getAttributes().windowAnimations= R.style.MyCustomTheme;
        return dialog;
    }

    /**
     * En esta funcion editamos el mensaje que se mostrara en el popup, y tambien
     * cambiamos el layout con el cual se mostrara el popup
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Indicamos cual sera el layout que se mostrara en el mensaje
        View view = inflater.inflate(R.layout.dialog_1, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(cancelable);

        TextView txt1 = (TextView) view.findViewById(R.id.txt1);
        txt1.setTypeface(admin.fuentes[1]);

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.spin_kit);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);


        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        // <--------- You may overload onPause
        this.dismiss();
    }
}
