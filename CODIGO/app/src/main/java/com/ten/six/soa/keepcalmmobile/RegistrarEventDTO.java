package com.ten.six.soa.keepcalmmobile;

import com.google.gson.Gson;

public class RegistrarEventDTO implements IRequest{
    private String env;
    private String type_events;
    private String state;
    private String description;

    public RegistrarEventDTO(String type_events, String state, String description) {
        this.env = "DEV";
        this.type_events = type_events;
        this.state = state;
        this.description = description;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
