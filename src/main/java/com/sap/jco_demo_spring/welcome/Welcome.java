package com.sap.jco_demo_spring.welcome;

public class Welcome {

    private String name;

    public Welcome(String name)
    {
        this.name = name;
    }

    public String getContent() {
        return name;
    }
}
