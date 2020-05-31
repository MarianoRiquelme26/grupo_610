package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
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
    private float x;
    private float y;
    private float z;
    private int tiempoContador=10;


    DecimalFormat         dosdecimales = new DecimalFormat("###.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarama);
        Button ActivarAlarma = (Button) findViewById(R.id.button6);
        acelerometro = (TextView) findViewById(R.id.acelerometro);
        contador = (TextView) findViewById(R.id.contador);
        contador.setText(R.string.ContadorActivacion);
        // Accedemos al servicio de sensores
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ActivarAlarma.setOnClickListener(new OnClickListener() {
                                                     @Override
                                                    public void onClick(View v)
                                                    {
                                                        contador.setText(R.string.ContadorActivacion);
                                                        for (; tiempoContador > 0; tiempoContador--) {
                                                            SystemClock.sleep(1000);
                                                            contador.setText(R.string.ContadorActivacion+ tiempoContador);
                                                        }
                                                        contador.setText("Alarma activada");
                                                        SystemClock.sleep(1000);
                                                        contador.setText("");
                                                        // Empezamos dentro de 10ms y luego lanzamos la tarea cada 1000ms
                                                        alarmaOn = true;
                                                    }
                                             });
    }

    //Metodo para volver a la pantalla de funcionalidades
    public void pararAlarma(View view){
        Intent pararAlarma = new Intent(this, Funcionalidades.class);
        startActivity(pararAlarma);
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
                    if(alarmaOn) {
                        txt += "Acelerometro:\n";
                        txt += "x: " + dosdecimales.format(event.values[0]) + " m/seg2 \n";
                        txt += "y: " + dosdecimales.format(event.values[1]) + " m/seg2 \n";
                        txt += "z: " + dosdecimales.format(event.values[2]) + " m/seg2 \n";
                        acelerometro.setText(R.string.AlarmaActivada);
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

}