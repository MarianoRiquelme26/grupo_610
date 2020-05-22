package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText nombre;
    private EditText apellido;
    private EditText edad;
    private EditText email;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.nombre = (EditText)findViewById(R.id.editText);
        this.apellido = (EditText)findViewById(R.id.editText2);
        this.edad = (EditText)findViewById(R.id.editText3);
        this.email = (EditText)findViewById(R.id.editText4);
        this.password = (EditText)findViewById(R.id.editText5);

        SharedPreferences preferences = getSharedPreferences("datoUsuario", Context.MODE_PRIVATE);
        this.nombre.setText(preferences.getString("nombre",""));
        this.apellido.setText(preferences.getString("apellido",""));
        this.edad.setText(preferences.getString("edad",""));
        this.email.setText(preferences.getString("email",""));
        this.password.setText(preferences.getString("password",""));


    }

    //Metodo para pasar a la pantalla de funcionalidades
    public void Funcionalidades(View view){

        // GUARDO LA INFORMACION DEL USUARIO
        SharedPreferences preferences = getSharedPreferences("datoUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor objEditor = preferences.edit();// indico que voy a editar el archivo SharedPreferences
        objEditor.putString("nombre",nombre.getText().toString());
        objEditor.putString("apellido",apellido.getText().toString());
        objEditor.putString("edad",edad.getText().toString());
        objEditor.putString("email",email.getText().toString());
        objEditor.putString("password",password.getText().toString());
        objEditor.commit();

        Intent funcionalidad = new Intent(this, Funcionalidades.class);
        startActivity(funcionalidad);
    }
}
