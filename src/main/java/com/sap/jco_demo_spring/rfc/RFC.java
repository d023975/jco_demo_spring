package com.sap.jco_demo_spring.rfc;

import com.sap.jco_demo_spring.config.DestinationProperties;

public class RFC {

    private String name;
    private DestinationProperties destinationProperties;

    public RFC(String name, DestinationProperties destinationProperties)
    {
        this.name = name;
        this.destinationProperties = destinationProperties;
    }

    public String getContent() {
        return name;
    }
}
