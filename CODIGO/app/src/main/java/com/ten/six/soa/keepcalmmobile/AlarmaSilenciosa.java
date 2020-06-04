package com.ten.six.soa.keepcalmmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmaSilenciosa extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private int contador = 0;
    private boolean alarmaSilenciosaActivada = false;
    private boolean iniciarconteodevuleta = false   ;
    private Button desActivarAlarma;
    private TextView alarmaActivada;
    private boolean eventoRegistrado;
    private ServiceTask servidorSOA;
    private RegistrarEventDTO evento;
    private Vibrator vibrador;
    private String nombreContacto1, nombreContacto2, numero1Contacto, numero2Contacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma_silenciosa);
        alarmaActivada = (TextView) findViewById(R.id.textActivada);
        vibrador = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        desActivarAlarma = (Button) findViewById(R.id.button5);
        servidorSOA = new ServiceTask(this, "http://so-unlam.net.ar/api/api/event");
        eventoRegistrado = false;
        desActivarAlarma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        try {
            cuentaRegresiva();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SharedPreferences preferences = getSharedPreferences("contactoMensajetext", Context.MODE_PRIVATE);
        this.nombreContacto1 =preferences.getString("nombre1","");
        this.numero1Contacto =preferences.getString("numero1","");
        this.nombreContacto2 =preferences.getString("nombre2","");
        this.numero2Contacto =preferences.getString("numero2","");
        checkSMSStatePermission();

    }
    private void checkSMSStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para enviar SMS.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para enviar SMS!");
        }
    }
    protected void Ini_Sensores() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);

    }

    private void Parar_Sensores() {

        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }

    private void cuentaRegresiva() throws InterruptedException {

        final Timer conteo = new Timer("Conteo");
        conteo.schedule(new TimerTask() {
            @Override
            public void run() {
                if(contador>=3){
                    //alarmaActivada.setVisibility(View.VISIBLE);
                    vibrador.vibrate(1000);
                    alarmaSilenciosaActivada=true;
                    Log.e("Gaston","activo alarma");
                    registrarEvento(contador);
                    conteo.cancel();
                    conteo.purge();

                }else
                    Log.e("Gaston","tic");

            }
        },0,3000);
    }
    private void enviarMensaje(){
        try{
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numero1Contacto,null, "Ayuda me estan robando!",null, null);
            sms.sendTextMessage(numero2Contacto,null, "Ayuda me estan robando!",null, null);
        }catch (Exception e){
            Log.e("Error", "No se pudo enviar mensaje");
            e.printStackTrace();
        }
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

        Ini_Sensores();

    }

    public void registrarEvento(int contador) {

        if (!this.eventoRegistrado) {
            Date fecha = new Date();
            enviarMensaje();
            this.eventoRegistrado = true;
            evento = new RegistrarEventDTO("SENSOR", "ACTIVO", "Alarma silenciosa activada -"+fecha.toString()+  "- contador: "+contador);
            servidorSOA.setReqOriginal(evento);
            servidorSOA.execute();
            SharedPreferences preferences2 = getSharedPreferences("historial", Context.MODE_PRIVATE);
            String notasAux = preferences2.getString("notas", "");
            SharedPreferences.Editor objEditor = preferences2.edit();// indico que voy a editar el archivo SharedPreferences
            objEditor.putString("notas", notasAux + "-" +fecha.toString() + "-Alarma-Silenciosa-MeSecuestran,");
            objEditor.commit();
        }
    }
}

