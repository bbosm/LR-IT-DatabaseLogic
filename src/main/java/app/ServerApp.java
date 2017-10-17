package app;

import transfer.*;

public class ServerApp {

    public static void main(String[] args) throws Exception {

        if (null == System.getSecurityManager()) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            String name = "SERVER";
            ServerImpl server = new ServerImpl(name);

            System.out.println("RMI Server ready...");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
