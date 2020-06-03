package com.ten.six.soa.keepcalmmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServiceTask extends AsyncTask<Void,Void,String> {
    private Context httpContext;
    ProgressDialog progressDialog;
    public String resultApi="";
    public String linkrequestAPI = "";
    private  IRequest reqOriginal;
    private boolean esOk;

    public String token;// = "$2y$10$mMBNTCENdsjIQS73Tsy/Nuu9SV2uMJxKNk2aKSnHft/UYfwpSEG8a";
    public String tokenb = "$2y$10$mMBNTCENdsjIQS73Tsy/Nuu9SV2uMJxKNk2aKSnHft/UYfwpSEG8a";

    public ServiceTask(Context httpContext, String linkAPI, IRequest req){
        this.httpContext = httpContext;
        this.linkrequestAPI = linkAPI;
        reqOriginal = req;
        esOk = false;

    }

    public ServiceTask(Context httpContext, String linkAPI){
        this.httpContext = httpContext;
        this.linkrequestAPI = linkAPI;
    }
    public void setReqOriginal(IRequest req){
        reqOriginal = req;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        if(!this.linkrequestAPI.contains("event"))
            progressDialog = ProgressDialog.show(httpContext,"Procesando Solicitud","aguarde unos instantes");
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = null;
        String msj = "";
        String apiREST = this.linkrequestAPI;
        URL url = null;
        RegistroLoginEventoRspDTO responseBody;
        IRequest request;
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*
        LEVANTO EL TOKEN POR LAS DUDAS
         */
        SharedPreferences preferences = this.httpContext.getSharedPreferences("datoUsuario", Context.MODE_PRIVATE);
        this.token = preferences.getString("token","");
        Log.e("MARIAN", "levanto el token: "+  preferences.getString("token",""));
        /////////////////////////////////////////////////////////////////////////////////////////////
        try{
            url = new URL(apiREST);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            /*
            if(!apiREST.contains("event"))
                request = (AlumnoDTO) reqOriginal;
            else
                request = (RegistrarEventDTO) reqOriginal;
                //request = new RegistrarEventDTO("TEST","estos son test del team 610","Activo","Flaaaaaaaaaaaaaandeeeeeeeeers ... presta atencion");

            */

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            if(apiREST.contains("event")){
                Log.e("MARIAN", "evento token: "+ token);
                if(!(this.token == null))
                    urlConnection.setRequestProperty("token", token);
                else
                    urlConnection.setRequestProperty("token", tokenb);

            }


            Log.e("MARIAN", "request: "+ reqOriginal.toJson());
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(reqOriginal.toJson());
            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            msj = urlConnection.getResponseMessage();
            if(HttpURLConnection.HTTP_CREATED == responseCode || HttpURLConnection.HTTP_OK == responseCode){
                esOk = true;
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String linea = "";

                while((linea=in.readLine())!=null){
                    sb.append(linea);
                    break;
                }
                in.close();
                result = sb.toString();

                responseBody = RegistroLoginEventoRspDTO.toObjeto(result);
                Log.e("MARIAN", "body: "+ responseBody.toJson());
                this.token = responseBody.getToken();

            }
            else{
                result = new String("ErrorCode " + responseCode);

            }

        } catch (MalformedURLException e) {
            Log.e("MARIAN"," MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("MARIAN"," IOException");
            e.printStackTrace();
        }


        return result;
    }

    @Override
    protected void onPostExecute(String s){
        Log.e("MARIAN"," onPostExecute: "+s);
        super.onPostExecute(s);
        if(!this.linkrequestAPI.contains("event"))
            progressDialog.dismiss();
        resultApi = s;


        if(esOk){

            if(this.linkrequestAPI.contains("login")){
                Log.e("MARIAN", "guardando el token: "+ token);
                SharedPreferences preferences = httpContext.getSharedPreferences("datoUsuario", Context.MODE_PRIVATE);
                SharedPreferences.Editor objEditor = preferences.edit();// indico que voy a editar el archivo SharedPreferences
                objEditor.putString("token",this.token);
                objEditor.commit();
                Log.e("MARIAN", "se guardo token: "+ token);
            }

            if(this.linkrequestAPI.contains("register")){
                AlumnoDTO alu = (AlumnoDTO) reqOriginal;
                SharedPreferences preferences = httpContext.getSharedPreferences("datoUsuario", Context.MODE_PRIVATE);
                SharedPreferences.Editor objEditor = preferences.edit();// indico que voy a editar el archivo SharedPreferences
                objEditor.putString("nombre",alu.getName());
                objEditor.putString("apellido",alu.getLastname());
                objEditor.putString("dni",alu.getDniToString());
                objEditor.putString("email",alu.getEmail());
                objEditor.putString("password",alu.getPassword());
                objEditor.commit();
            }
            if(!this.linkrequestAPI.contains("event")){
                Intent funcionalidad = new Intent(httpContext, Funcionalidades.class);
                httpContext.startActivity(funcionalidad);
            }

        }
        else
            Toast.makeText(httpContext,"valores incorrectos, vuelva a intentarlo ",Toast.LENGTH_LONG).show();

    }

    public String getToken(){
        return this.token;
    }

}
