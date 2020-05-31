package com.ten.six.soa.registraralumnofun;

import com.google.gson.Gson;

public class RegistroLoginDTO {
    private String state;
    private String env;
    private AlumnoDTO user;
    private String token;
    private String msg;

    public RegistroLoginDTO(String state, String env, AlumnoDTO user, String token, String msg) {
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

    public static RegistroLoginDTO toObjeto(String payload){
        Gson gson = new Gson();
        RegistroLoginDTO body = gson.fromJson(payload,RegistroLoginDTO.class);
        return body;
    }
}
