package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Historial extends AppCompatActivity {

    private ArrayList<String> nombres;
    private ArrayList<String> nombres2;
    private ListView lv1;
    private TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        tv1 = (TextView)findViewById(R.id.tv1);
        lv1 = (ListView)findViewById(R.id.lv1);

        nombres2 = new ArrayList<>();
        String notasAux;
        SharedPreferences preferences2 = getSharedPreferences("historial", Context.MODE_PRIVATE);
        notasAux = preferences2.getString("notas","");

        nombres2 = new ArrayList(Arrays.asList(notasAux.split(",")));
        Log.e("MARIAN","recuper: "+generateBitacora(nombres2));



        ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,nombres2);
        lv1.setAdapter(ada);
    }

    public String  generateBitacora(ArrayList<String> list){
        String trama = "";
        for(String linea : list){
            trama = trama+","+linea;
        }
        Log.e("MARIAN2","trama generada "+trama.substring(1));
        return trama.substring(1);
    }
}
