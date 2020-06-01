package com.ten.six.soa.keepcalmmobile;

import com.google.gson.Gson;

public class RegistroLoginEventoRspDTO {
    private String state;
    private String env;
    private AlumnoDTO user;
    private String token;
    private String msg;

    public RegistroLoginEventoRspDTO(String state, String env, AlumnoDTO user, String token, String msg) {
        this.state = state;
        this.env = env;
        this.user = user;
        this.token = token;
        this.msg = msg;
    }
    // VER SI ES NECESARIO ESTA FUNCIONALIDAD
    public String toJson() {
        return new Gson().toJson(this);
    }

    public static RegistroLoginEventoRspDTO toObjeto(String payload){
        Gson gson = new Gson();
        RegistroLoginEventoRspDTO body = gson.fromJson(payload, RegistroLoginEventoRspDTO.class);
        return body;
    }
    public String getToken(){
        return this.token;
    }
}
