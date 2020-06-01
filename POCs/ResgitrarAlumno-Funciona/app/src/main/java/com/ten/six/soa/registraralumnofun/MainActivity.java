package com.ten.six.soa.registraralumnofun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnRegistrar;
    Button btnLogin;
    Button btnEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistrar = (Button) findViewById(R.id.btn1);
        btnEvento = (Button) findViewById(R.id.btnEvent);


        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            btnRegistrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registrarAlumnoSOA();
                }
            });
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginAlumnoSOA();
                }
            });
            btnEvento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registrareEventoSOA();
                }
            });


        } else {
            finish();
        }

    }

    public void loginAlumnoSOA(){
        ServiceTask servidorSOA = new ServiceTask(this,"http://so-unlam.net.ar/api/api/login");
        servidorSOA.execute();
    }

    public void registrarAlumnoSOA(){
        ServiceTask servidorSOA = new ServiceTask(this,"http://so-unlam.net.ar/api/api/register");
        servidorSOA.execute();
    }
    public void registrareEventoSOA(){
        ServiceTask servidorSOA = new ServiceTask(this,"http://so-unlam.net.ar/api/api/event");
        servidorSOA.execute();
    }

}
