package com.ten.six.soa.registraralumnofun;

import com.google.gson.Gson;

public class AlumnoDTO implements IRequest{
    private String env;
    private String name;
    private String lastname;
    private int dni;
    private String email;
    private String password;
    private int commission;
    private int group;

    public AlumnoDTO(String env, String name, String lastname, String dni, String email, String password, String commission, String group) {
        this.env = env;
        this.name = name;
        this.lastname = lastname;
        this.dni = Integer.valueOf(dni);
        this.email = email;
        this.password = password;
        this.commission =Integer.valueOf(commission);
        this.group = Integer.valueOf(group);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
