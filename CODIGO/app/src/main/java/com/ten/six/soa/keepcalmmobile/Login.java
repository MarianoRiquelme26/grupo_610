package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private EditText password;
    private TextView nombre;
    private AlumnoDTO alu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.nombre = (TextView)findViewById(R.id.textView8);
        this.password = (EditText)findViewById(R.id.editText);


        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            alu = (AlumnoDTO) bundle.getSerializable("AlumnoDTO");

            //(AlumnoDTO) getIntent().getSerializableExtra("AlumnoDTO");
        }
        else//no tendria que llegar a esta etapa que quiere loguearse y no hay datos, tendria que frenarlo la pantalla anterior
            alu = new AlumnoDTO();

        this.nombre.setText("Hola "+alu.getName());


    }

    public void Funcionalidades(View view){
        alu.setPassword(this.password.getText().toString());
        //ENVIO EL REGISTRO AL SERVER
        ServiceTask servidorSOA = new ServiceTask(this, "http://so-unlam.net.ar/api/api/login",alu);
        servidorSOA.execute();

        //GUARDO EL ULTIMO TOKEN EL LOGIN PARA USARLO CUANDO NECESIE REGISTRAR EVENTOS
        SharedPreferences preferences = getSharedPreferences("datoUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor objEditor = preferences.edit();// indico que voy a editar el archivo SharedPreferences
        objEditor.putString("token",servidorSOA.getToken());
        objEditor.commit();


        Intent funcionalidad = new Intent(this, Funcionalidades.class);
        startActivity(funcionalidad);
    }
}
