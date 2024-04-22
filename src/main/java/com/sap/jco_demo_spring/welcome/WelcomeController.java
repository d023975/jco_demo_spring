package com.sap.jco_demo_spring.welcome;

import com.sap.cloud.security.spring.config.XsuaaServiceConfigurations;
import com.sap.cloud.security.spring.token.SpringSecurityContext;
import com.sap.cloud.security.token.SecurityContext;
import com.sap.cloud.security.token.Token;
import com.sap.conn.jco.*;
import com.sap.jco_demo_spring.config.DestinationProperties;
import com.sap.jco_demo_spring.config.XsuaaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class WelcomeController {

    @Autowired
    private Environment environment;
    private static final String strDefine = "Hello, %s!";

    @Autowired
    XsuaaServiceConfigurations xsuaaServiceConfigurations;

    @Autowired
    XsuaaProperties xsuaaProperties;

    @Autowired
    DestinationProperties destinationProperties;


    @GetMapping("/welcome/client")
    public Welcome handleWelcome(@RequestParam(value = "name", defaultValue = "Enthusiast") String name) {


             name =   xsuaaProperties.getClientid();



        return new Welcome(String.format(strDefine, name),destinationProperties);
    }

    @GetMapping("/welcome/destination")
    @PreAuthorize("hasAuthority('run_rfc')")
    public Welcome handleDestination(@RequestParam(value = "destination", required=true) String destination, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        String result = "initial";
        try {
            JCoDestination  dest =  JCoDestinationManager.getDestination(destination);
            result= dest.getDestinationID();


            JCoRepository repo = dest.getRepository();
            JCoFunction stfcConnection = repo.getFunction("STFC_CONNECTION");
            JCoParameterList imports = stfcConnection.getImportParameterList();
            imports.setValue("REQUTEXT", "SAP BTP Connectivity runs with JCo");
            stfcConnection.execute(dest);
            JCoParameterList exports = stfcConnection.getExportParameterList();
            String echotext = exports.getString("ECHOTEXT");
            String resptext = exports.getString("RESPTEXT");


            result = result + "||||||" + echotext + "||||||" + resptext;



        } catch (JCoException e) {
            result = e.getMessage();
        }




        return new Welcome(result,destinationProperties);
    }





}
