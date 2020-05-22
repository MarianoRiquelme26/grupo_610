package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AlarmaSilenciosa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma_silenciosa);
    }

    //Metodo para volver a la pantalla de funcionalidades
    public void pararAlarma(View view){
        Intent pararAlarma = new Intent(this, Funcionalidades.class);
        startActivity(pararAlarma);
    }
}
