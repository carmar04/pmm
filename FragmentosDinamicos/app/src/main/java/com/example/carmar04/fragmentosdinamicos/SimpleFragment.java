package com.example.carmar04.fragmentosdinamicos;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleFragment extends Fragment {
    int mNum;
    public static SimpleFragment newInstance(int number) {
        SimpleFragment f = new SimpleFragment();
        // Mantenemos el nÃºmero para usarlo en cualquier momento.
        Bundle args = new Bundle();
        args.putInt("num", number);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // obtenemos el nÃºmero que se habia pasado como argumento en
        // la creaciÃ³n de la instancia
        mNum = getArguments().getInt("num");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View tv = null;
        View v  = null;
        // dependiendo de si es par o impar mostramos distintos layouts
        if (mNum % 2 == 0){
            v = inflater.inflate(R.layout.fram_simple, container, false);
             tv = v.findViewById(R.id.text);
        }
        else{
            v = inflater.inflate(R.layout.frame_simple2 , container, false);
             tv = v.findViewById(R.id.text2);

        }

        ((TextView)tv).setText("Fragmento nÃºmero #" + mNum);
        return v;
    }
}
