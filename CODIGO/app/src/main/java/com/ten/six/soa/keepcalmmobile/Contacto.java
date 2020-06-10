package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Contacto extends AppCompatActivity {
    private EditText nombre1;
    private EditText numero1;
    private EditText nombre2;
    private EditText numero2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_guardado);

        this.nombre1 = (EditText)findViewById(R.id.textNombre1);
        this.numero1 = (EditText)findViewById(R.id.textNumero1);
        this.nombre2= (EditText)findViewById(R.id.textNombre2);
        this.numero2 = (EditText)findViewById(R.id.textNumero2);

        SharedPreferences preferences = getSharedPreferences("contactoMensajetext", Context.MODE_PRIVATE);
        this.nombre1.setText(preferences.getString("nombre1",""));
        this.numero1.setText(preferences.getString("numero1",""));
        this.nombre2.setText(preferences.getString("nombre2",""));
        this.numero2.setText(preferences.getString("numero2",""));


    }

    public void guardar(View view) {
        SharedPreferences preferences = Contacto.this.getSharedPreferences("contactoMensajetext", Context.MODE_PRIVATE);
        SharedPreferences.Editor objEditor = preferences.edit();// indico que voy a editar el archivo SharedPreferences
        objEditor.putString("nombre1",this.nombre1.getText().toString());
        objEditor.putString("numero1",this.numero1.getText().toString());
        objEditor.putString("nombre2",this.nombre2.getText().toString());
        objEditor.putString("numero2",this.numero2.getText().toString());
        objEditor.commit();
        onBackPressed();
    }
}
