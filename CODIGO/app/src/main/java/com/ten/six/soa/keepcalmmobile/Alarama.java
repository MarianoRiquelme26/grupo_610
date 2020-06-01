package com.ten.six.soa.keepcalmmobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
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
    private TextView acelerometro;
    private TextView contador;
    private boolean alarmaOn=false;
    private float x , y, z;
    private double tolerancia=0.5;
    private int tiempoContador=10;
    MediaPlayer alertaMediaPlayer;
    RegistrarEventDTO evento;
    ServiceTask servidorSOA;
    boolean eventoRegistrado;


    DecimalFormat         dosdecimales = new DecimalFormat("###.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarama);
         alertaMediaPlayer = MediaPlayer.create(this, R.raw.sonidoalarma);

        Button activarAlarma = (Button) findViewById(R.id.button6);
        Button desActivarAlarma = (Button) findViewById(R.id.button4);
        acelerometro = (TextView) findViewById(R.id.acelerometro);
        contador = (TextView) findViewById(R.id.contador);

        servidorSOA = new ServiceTask(this, "http://so-unlam.net.ar/api/api/event");
        eventoRegistrado = false;


        // Accedemos al servicio de sensores
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
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
                contador.setText("ALARMA ACTIVADA");
                alarmaOn = true;
            }
        }.start();

    }

    //Metodo para volver a la pantalla de funcionalidades
    public void pararAlarma(View view){
        //HAY QUE PEDIR CONTRASEÑA ANTES
        alarmaOn=false;
        alertaMediaPlayer.stop();
        contador.setText("ALARMA DESACTIVADA");
        super.onBackPressed();
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
        }

    }

}