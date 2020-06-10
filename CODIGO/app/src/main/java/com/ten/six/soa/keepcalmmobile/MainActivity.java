package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText nombre;
    private EditText apellido;
    private EditText dni;
    private EditText email;
    private EditText password;
    private AlumnoDTO alu;
    private Button btonDialogo;


    ConnectivityManager connMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // BUSCO SI EXISTEN DATOS PREVIOS GUARDADOS
        SharedPreferences preferences = getSharedPreferences("datoUsuario", Context.MODE_PRIVATE);
        if(!preferences.getString("nombre","").equals("")){
            this.alu = new AlumnoDTO(preferences.getString("nombre",""),
                    preferences.getString("apellido",""),
                    preferences.getString("dni",""),
                    preferences.getString("email",""),
                    preferences.getString("password",""));

        }
        else{
            alu = new AlumnoDTO();
            Log.e("MARIAN", "No habia info");
        }

        connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if( !(networkInfo != null && networkInfo.isConnected()) ){
            finish(); // con esta linea, sale de la app
            System.exit(0);
        }
    }

    public void irRegistro(View view){
        if(hayConexion()){
            Intent registrarse = new Intent(this, Registro.class);
           //SI EXISTE INFORMACION GUARDAD SE ENVIA CARGA PARA ENVIAR A LA SIGUIENTE ACTIVITY
            Log.e("KeepCalmMobile", "valido si alu es vacio");
            if(!alu.isEmpty()){
                registrarse.putExtra("AlumnoDTO",alu);
            }
            else
                Log.e("KeepCalmMobile", "no se carga nada al intent");


            startActivity(registrarse);
        }
        else
            mesajeSinConexion();

    }

    public void irLogin(View view){
        if(hayConexion()){
            Intent Login;
            if(!alu.isEmpty()){
                Login = new Intent(this, Login.class);
                //SI EXISTE INFORMACION GUARDAD SE ENVIA CARGA PARA ENVIAR A LA SIGUIENTE ACTIVITY
                Log.e("KeepCalmMobile", "valido si alu es vacio");
                Login.putExtra("AlumnoDTO",alu);

            }
            else{
                alu.setDestino("http://so-unlam.net.ar/api/api/login");
                Login = new Intent(this, Registro.class);
                Login.putExtra("AlumnoDTO",alu);
            }
            startActivity(Login);

        }
        else
            mesajeSinConexion();

    }

    public boolean hayConexion(){
        connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void mesajeSinConexion(){
        Toast.makeText(this,"Sin conexion",Toast.LENGTH_SHORT).show();
    }

}
