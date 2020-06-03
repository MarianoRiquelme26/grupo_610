package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmaSilenciosa extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private int contador = 0;
    private boolean alarmaSilenciosaActivada = false;
    private boolean iniciarconteodevuleta = false   ;
    private Button desActivarAlarma;
    private TextView alarmaActivada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma_silenciosa);
        alarmaActivada = (TextView) findViewById(R.id.textActivada);
        desActivarAlarma = (Button) findViewById(R.id.button5);
        desActivarAlarma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


    }

    protected void Ini_Sensores() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);

    }

    private void Parar_Sensores() {

        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }

    private void cuentaRegresiva() throws InterruptedException {

        CountDownTimer conteo = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(contador>=3){
                    alarmaActivada.setVisibility(View.VISIBLE);
                    alarmaSilenciosaActivada=true;
                    Log.e("Gaston","activo alarma");
                }
                Log.e("Gaston","tictoc");

            }

            @Override
            public void onFinish() {
                contador = 0;
                Log.e("Gaston","reseteo contador");


            }

        };
        //conteo.start();
        while (!alarmaSilenciosaActivada){
            conteo.start();
            new CountDownTimer(3500, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    iniciarconteodevuleta=true;
                }
            }.start();
            while (!iniciarconteodevuleta){

            }
        }
        Log.e("Gaston","sali del while");

    }

    // Metodo que escucha el cambio de los sensores
    @Override
    public void onSensorChanged(SensorEvent event) {
        String txt = "";
        synchronized (this) {
            Log.d("sensor", event.sensor.getName());

            switch (event.sensor.getType()) {

                case Sensor.TYPE_PROXIMITY:
                    if (event.values[0] == 0) {
                        contador++;
                        Log.e("Gaston","proximity");
                    }
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
        try {
            cuentaRegresiva();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Ini_Sensores();

    }
}

