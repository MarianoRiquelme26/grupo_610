package com.ten.six.soa.registraralumnofun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

public class ConexionInternet extends AppCompatActivity {

    private TextView mesaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_internet);
        mesaje = (TextView)findViewById(R.id.textView);

        //valido si existe conexion a internet
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            mesaje.setText("Bienvenido a keepcalmmobile");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ConexionInternet.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}
