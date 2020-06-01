package com.ten.six.soa.keepcalmmobile;

import com.google.gson.Gson;

import java.io.Serializable;

public class AlumnoDTO implements IRequest, Serializable {
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

    public AlumnoDTO(String name, String lastname, String dni, String email, String password) {
        this.env = "TEST";
        this.name = name;
        this.lastname = lastname;
        this.dni = Integer.valueOf(dni);
        this.email = email;
        this.password = password;
        this.commission =3900;
        this.group = 610;
    }
    public AlumnoDTO() {
        this.env = "TEST";
        this.commission =3900;
        this.group = 610;
        this.name="";
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
    public boolean isEmpty(){
        return this.name.equals("");
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public int getDni() {
        return dni;
    }
    public String getDniToString() {
        return dni+"";
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }
    public void setDnifromString(String dni) {
        this.dni = Integer.valueOf(dni);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
