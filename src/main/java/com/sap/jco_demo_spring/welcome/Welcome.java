package com.sap.jco_demo_spring.welcome;

import com.sap.jco_demo_spring.config.DestinationProperties;

public class Welcome {

    private String name;
    private DestinationProperties destinationProperties;

    public Welcome(String name, DestinationProperties destinationProperties)
    {
        this.name = name;
        this.destinationProperties = destinationProperties;
    }

    public String getContent() {
        return name;
    }
}
