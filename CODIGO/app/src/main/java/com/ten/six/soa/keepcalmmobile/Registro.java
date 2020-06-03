package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Registro extends AppCompatActivity {

    private EditText nombre;
    private EditText apellido;
    private EditText dni;
    private EditText email;
    private EditText password;
    private AlumnoDTO alu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        this.nombre = (EditText)findViewById(R.id.editText8);
        this.apellido = (EditText)findViewById(R.id.editText7);
        this.dni = (EditText)findViewById(R.id.editText6);
        this.email = (EditText)findViewById(R.id.editText9);
        this.password = (EditText)findViewById(R.id.editText10);

        //  RECUPERO LOS DATOS DE LA PANTALLA ANTERIOR
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            alu = (AlumnoDTO) bundle.getSerializable("AlumnoDTO");
                //(AlumnoDTO) getIntent().getSerializableExtra("AlumnoDTO");
        }
        else
            alu = new AlumnoDTO();

        if(!alu.isEmpty()){
            Log.e("MARIAN", "se pasaron datos");
            this.nombre.setText(alu.getName());
            this.apellido.setText(alu.getLastname());
            this.dni.setText(alu.getDniToString());
            this.email.setText(alu.getEmail());
            this.password.setText(alu.getPassword());
        }
        else
            Log.e("MARIAN", "No se pasaron datos");

    }

    public void Funcionalidades(View view){

        /* GUARDO/ACTUALIZO LA INFORMACION DEL USUARIO
        SharedPreferences preferences = getSharedPreferences("datoUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor objEditor = preferences.edit();// indico que voy a editar el archivo SharedPreferences
        objEditor.putString("nombre",nombre.getText().toString());
        objEditor.putString("apellido",apellido.getText().toString());
        objEditor.putString("dni",dni.getText().toString());
        objEditor.putString("email",email.getText().toString());
        objEditor.putString("password",password.getText().toString());
        objEditor.commit();

        //CARGO LOS DATOS DEL ALUMNO PARA ENVIAR A REGISTRAR*/
        alu.setName(nombre.getText().toString());
        alu.setLastname(apellido.getText().toString());
        alu.setDnifromString(dni.getText().toString());
        alu.setEmail(email.getText().toString());
        alu.setPassword(password.getText().toString());

        //ENVIO EL REGISTRO AL SERVER
        ServiceTask servidorSOA = new ServiceTask(this, "http://so-unlam.net.ar/api/api/register",alu);
        servidorSOA.execute();
/*
        Intent funcionalidad = new Intent(this, Funcionalidades.class);
        startActivity(funcionalidad);*/
    }
}
/*
SE PODRIA MEJORAR LA LOGICA PARA EL PASAJE DE LOS DATOS GUARDADOS ENTRE MAIN Y REGISTRO, PODRIA
CARGAR SIEMPRE EL ALUMNO, VACIO Y NO HAY INFO, O CON DATOS EN CASO NECSARIO Y SIMPERE PASAR ALUMNO
A REGISTRO
 */