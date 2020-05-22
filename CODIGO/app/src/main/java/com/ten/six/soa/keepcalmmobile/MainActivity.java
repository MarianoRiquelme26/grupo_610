package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Metodo para pasar a la pantalla de funcionalidades
    public void Funcionalidades(View view){
        Intent funcionalidad = new Intent(this, Funcionalidades.class);
        startActivity(funcionalidad);
    }
}
