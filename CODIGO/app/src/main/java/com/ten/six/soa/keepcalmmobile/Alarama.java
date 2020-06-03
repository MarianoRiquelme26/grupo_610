package com.ten.six.soa.keepcalmmobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;


import static com.ten.six.soa.keepcalmmobile.R.id.acelerometro;

public class Alarama extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private TextView acelerometro, rectanguloPararAlarma, ingresePasswordPararContraseña, password;
    private Button activarAlarma ,desActivarAlarma, confirmar;
    private ImageView cerrarIngresoPassword;
    private TextView contador;
    private boolean alarmaOn=false;
    private float x , y, z;
    private double tolerancia=0.5;
    private int tiempoContador=10;
    private MediaPlayer alertaMediaPlayer;
    private RegistrarEventDTO evento;
    private ServiceTask servidorSOA;
    private boolean eventoRegistrado;
    private SharedPreferences preferences;
    private Service alarmasilenciosa;


    DecimalFormat         dosdecimales = new DecimalFormat("###.###");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarama);
        preferences = getSharedPreferences("datoUsuario", Context.MODE_PRIVATE);
        alertaMediaPlayer = MediaPlayer.create(this, R.raw.sonidoalarma);

        activarAlarma = (Button) findViewById(R.id.button6);
        desActivarAlarma = (Button) findViewById(R.id.button4);

        acelerometro = (TextView) findViewById(R.id.acelerometro);
        contador = (TextView) findViewById(R.id.contador);
        rectanguloPararAlarma = (TextView) findViewById(R.id.rectanguloPararAlarma);
        ingresePasswordPararContraseña = (TextView) findViewById(R.id.ingresePasswordPararContraseña);
        password= (TextView) findViewById(R.id.password);
        cerrarIngresoPassword = findViewById(R.id.cerrarIngresoPassword);
        confirmar = (Button) findViewById(R.id.confirmar);

        servidorSOA = new ServiceTask(this, "http://so-unlam.net.ar/api/api/event");
        eventoRegistrado = false;


        // Accedemos al servicio de sensores
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //oculto todo si cancelo el ingreso de contraseña
        cerrarIngresoPassword.setOnClickListener( new OnClickListener(){

            @Override
            public void onClick(View v) {
                rectanguloPararAlarma.setVisibility(v.INVISIBLE);
                ingresePasswordPararContraseña.setVisibility(v.INVISIBLE);
                password.setVisibility(v.INVISIBLE);
                cerrarIngresoPassword.setVisibility(v.INVISIBLE);
                confirmar.setVisibility(v.INVISIBLE);

            }
        });
        confirmar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password.getText().toString().equals((preferences.getString("password","")))){
                    alarmaOn=false;
                    alertaMediaPlayer.stop();
                    contador.setText("ALARMA DESACTIVADA");
                    onBackPressed();
                }

            }
        });

        activarAlarma.setOnClickListener(new OnClickListener() {
                                                     @Override
                                                    public void onClick(View v)
                                                     {

                                                        cuentaRegresiva(10000);
                                                     }
                                             });
        desActivarAlarma.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {

                pararAlarma(v);
            }
        });

        //requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        }
    @Override
    public void onBackPressed (){
        if (alarmaOn) {

        } else {
            super.onBackPressed();
        }
    }

    private void cuentaRegresiva(int ms) {
        new CountDownTimer(ms, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                contador.setText("La alarma se activara en: "+millisUntilFinished/1000);

            }

            @Override
            public void onFinish() {
                desActivarAlarma.setVisibility(View.VISIBLE);
                activarAlarma.setVisibility(View.VISIBLE);

                contador.setText("ALARMA ACTIVADA");
                alarmaOn = true;
            }
        }.start();

    }

    //Metodo para volver a la pantalla de funcionalidades
    public void pararAlarma(View v){
        //HAY QUE PEDIR CONTRASEÑA ANTES
        activarAlarma.setVisibility(v.INVISIBLE);
        desActivarAlarma.setVisibility(v.INVISIBLE);
        rectanguloPararAlarma.setVisibility(v.VISIBLE);
        ingresePasswordPararContraseña.setVisibility(v.VISIBLE);
        password.setVisibility(v.VISIBLE);
        cerrarIngresoPassword.setVisibility(v.VISIBLE);
        confirmar.setVisibility(v.VISIBLE);

        /*Intent pararAlarma = new Intent(this, Funcionalidades.class);
        startActivity(pararAlarma);*/
    }

    protected void Ini_Sensores() {
         mSensorManager.registerListener( this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }
    private void Parar_Sensores() {

        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    // Metodo que escucha el cambio de los sensores
    @Override
    public void onSensorChanged(SensorEvent event) {
        String txt = "";
        synchronized (this) {
            Log.d("sensor", event.sensor.getName());

            switch (event.sensor.getType()) {

                case Sensor.TYPE_ACCELEROMETER:
                    if(!alarmaOn) {
                        x=event.values[0];
                        y=event.values[1];
                        z=event.values[2];

                    }
                    else {
                        if ( event.values[0]>x+tolerancia || event.values[0]<x-tolerancia
                                || event.values[1]>y+tolerancia || event.values[1]<y-tolerancia
                                    || event.values[2]>z+tolerancia || event.values[2]<z-tolerancia
                                ) {
                            acelerometro.setText("Alerta Alarma");
                            alertaMediaPlayer.start();
                            registrarEvento();

                        }
                    }
                    break;
            }
        }
    }

    @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {

        }

    @Override
    protected void onStop()
    {

        Parar_Sensores();

        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Parar_Sensores();

        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        Parar_Sensores();

        super.onPause();
    }

    @Override
    protected void onRestart()
    {
        Ini_Sensores();

        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Ini_Sensores();
    }
    public void registrarEvento(){

        if(!this.eventoRegistrado){
            this.eventoRegistrado = true;
            evento = new RegistrarEventDTO("SENSOR","ACTIVO","Alarma sonora activada - Ayuda!! me estan robando");
            servidorSOA.setReqOriginal(evento);
            servidorSOA.execute();
            //SharedPreferences preferences1 = getSharedPreferences("historial", Context.MODE_PRIVATE);
            SharedPreferences preferences2 = getSharedPreferences("historial", Context.MODE_PRIVATE);
            String notasAux = preferences2.getString("notas","");
            SharedPreferences.Editor objEditor = preferences2.edit();// indico que voy a editar el archivo SharedPreferences
            objEditor.putString("notas",notasAux+"-"+System.currentTimeMillis()+"Alarma-Sonora-MeRoban,");
            objEditor.commit();
//SystemClock.currentGnssTimeClock()
        }

    }

}