package com.ten.six.soa.keepcalmmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
    public String token = "$2y$10$mMBNTCENdsjIQS73Tsy/Nuu9SV2uMJxKNk2aKSnHft/UYfwpSEG8a";

    public ServiceTask(Context httpContext, String linkAPI){
        this.httpContext = httpContext;
        this.linkrequestAPI = linkAPI;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext,"Procesando Solicitud","aguarde unos instantes");
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = null;
        String msj = "";
        String apiREST = this.linkrequestAPI;
        URL url = null;
        RegistroLoginEventoRspDTO responseBody;
        IRequest req;

        try{
            url = new URL(apiREST);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if(!apiREST.contains("event"))
                req = new AlumnoDTO("TEST","Mariano","Riquelme","36287422","marianoprueba@soa.com","12345678","3900","610");
            else
                req = new RegistrarEventDTO("TEST","estos son test del team 610","Activo","Flaaaaaaaaaaaaaandeeeeeeeeers ... presta atencion");

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            if(apiREST.contains("event"))
                urlConnection.setRequestProperty("token", token);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(req.toJson());
            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            msj = urlConnection.getResponseMessage();
            if(HttpURLConnection.HTTP_CREATED == responseCode || HttpURLConnection.HTTP_OK == responseCode){
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
        progressDialog.dismiss();
        resultApi = s;
        Toast.makeText(httpContext,resultApi,Toast.LENGTH_SHORT);
    }

}
