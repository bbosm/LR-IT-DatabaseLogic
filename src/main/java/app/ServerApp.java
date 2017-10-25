package app;

import transfer.*;

import javax.naming.InitialContext;
import java.util.Properties;

public class ServerApp {

    static final String CONTEXT_NAME = "java.naming.factory.initial";
    static final String IIOP_STRING  = "com.sun.jndi.cosnaming.CNCtxFactory";

    static final String URL_NAME = "java.naming.provider.url";
    static final String IIOP_URL_STRING  = "iiop://localhost:8080";

    static final String OBJECT_NAME = "SERVER";

    public static void main(String[] args) throws Exception {

        try {
            ServerImpl server = new ServerImpl();

            // Create the IIOP Initial Context
            Properties iiopProperties = new Properties();
            iiopProperties.put(CONTEXT_NAME, IIOP_STRING);
            iiopProperties.put(URL_NAME, IIOP_URL_STRING);
            InitialContext iiopContext = new InitialContext(iiopProperties);

            // Bind the object to the IIOP registry
            iiopContext.rebind(OBJECT_NAME, server);

            System.out.println("RMI(IIOP) Server ready...");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}