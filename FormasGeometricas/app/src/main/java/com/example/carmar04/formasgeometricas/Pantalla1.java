package com.example.carmar04.formasgeometricas;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class Pantalla1 extends AppCompatActivity {

    Spinner spinner;

    int indice = 0;

    Figuras [] figuras = new Figuras []{
            new Figuras("Triangulo",0, 0, R.drawable.triangulo),
            new Figuras("Rectangulo",0, 0, R.drawable.rectangulo),
            new Figuras("Cuadrado",0, 0, R.drawable.cuadrado)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla1);


        final EditText altura = (EditText) findViewById(R.id.altura);
        final EditText base = (EditText) findViewById(R.id.base);
        final Button calcular = (Button) findViewById(R.id.calcular);

        spinner = (Spinner) findViewById(R.id.spinner);
        AdaptadorFiguras miAdaptador = new AdaptadorFiguras(this);
        spinner.setAdapter(miAdaptador);

        spinner.setSelection(-1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indice = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        calcular.setOnClickListener(new View.OnClickListener() {

            boolean alturaFlag = false;
            boolean baseFlag = false;

            public void onClick(View v) {
                if(altura.getText().toString().length() != 0){
                    alturaFlag = true;
                }
                if(base.getText().toString().length() != 0){
                    baseFlag = true;
                }
                if(alturaFlag && baseFlag){
                    figuras[indice].setBase(Integer.parseInt(base.getText().toString()));
                    figuras[indice].setAltura(Integer.parseInt(altura.getText().toString()));
                    Intent intent = new Intent(Pantalla1.this, Pantalla2.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Objeto", figuras[indice]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }
    class AdaptadorFiguras extends ArrayAdapter {
        Activity context;
        AdaptadorFiguras(Activity context){
            super(context, R.layout.spinner_formas, figuras);
            this.context = context;

        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public View getDropDownView(int position, View convertView, ViewGroup parent){
            View vista = getView(position, convertView, parent);
            return vista;
        }
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public View getView(int position, View convertiView, ViewGroup parent){
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.spinner_formas, null);

            TextView figura = (TextView) item.findViewById(R.id.formaSpinner);
            ImageView imagen = (ImageView) item.findViewById(R.id.imagenSpinner);

            figura.setText(figuras[position].getForma());
            imagen.setBackground(getDrawable(figuras[position].getImagen()));

            return item;
        }
    }
}
