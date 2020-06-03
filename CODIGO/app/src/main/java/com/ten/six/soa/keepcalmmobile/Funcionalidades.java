package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Funcionalidades extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcionalidades);
    }

    //Metodo para llamar activar la alarma
    public void activarAlarma(View view){
        Intent alarma = new Intent(this, Alarama.class);
        startActivity(alarma);
    }

    //Metodo para llamar activar la alarma silenciosa
    public void activarAlarmaSilenciosa(View view){
        Intent alarmaSilenciosa = new Intent(this, AlarmaSilenciosa.class);
        startActivity(alarmaSilenciosa);
    }

    public void verHistorial(View view){
        Intent Historial = new Intent(this, Historial.class);
        startActivity(Historial);
    }
}
