package com.sap.jco_demo_spring.welcome;

import com.sap.cloud.security.spring.config.XsuaaServiceConfigurations;
import com.sap.cloud.security.spring.token.SpringSecurityContext;
import com.sap.cloud.security.token.SecurityContext;
import com.sap.cloud.security.token.Token;
import com.sap.conn.jco.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sap.conn.jco.AbapException;
import com.sap.cloud.security.token.Token;




@RestController
public class WelcomeController {

    @Autowired
    private Environment environment;
    private static final String strDefine = "Hello, %s!";

    @Autowired
    XsuaaServiceConfigurations xsuaaServiceConfigurations;


    @GetMapping("/welcome")
    public Welcome handleWelcome(@RequestParam(value = "name", defaultValue = "Enthusiast") String name) {


//
//        Token token = SpringSecurityContext.getAccessToken();
//
//        String clientId = SpringSecurityContext.getToken().getClientId();
//        try {
//
//            SecurityContext.setToken(token);
//            JCoDestination destination = JCoDestinationManager.getDestination("QKXCLNT910");
//            System.out.println(destination.getDestinationName());
//            JCoRepository repo = destination.getRepository();
//            JCoFunction stfcConnection = repo.getFunction("STFC_CONNECTION");
//            JCoParameterList imports = stfcConnection.getImportParameterList();
//            imports.setValue("REQUTEXT", "SAP BTP Connectivity runs with JCo");
//            stfcConnection.execute(destination);
//            JCoParameterList exports = stfcConnection.getExportParameterList();
//            String echotext = exports.getString("ECHOTEXT");
//            String resptext = exports.getString("RESPTEXT");
//
//        } catch (AbapException ae) {
//
//            ae.printStackTrace();
//
//            // just for completeness: As this function module does not have an exception
//
//            // in its signature, this exception cannot occur. But you should always
//
//            // take care of AbapExceptions
//
//        } catch (JCoException e) {
//
//            e.printStackTrace();
//        }

        return new Welcome(String.format(strDefine, name));
    }
}
