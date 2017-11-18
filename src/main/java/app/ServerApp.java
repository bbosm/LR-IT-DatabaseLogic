package app;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import transfer.*;

import javax.naming.InitialContext;
import java.util.Properties;

public class ServerApp {

    static final String OBJECT_NAME = "SERVER";
    static final String ORB_PORT = "8080";

    static final String SERVICE_NAME = "NameService";

    public static void main(String[] args) throws Exception {

        try {
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", ORB_PORT);
            ORB orb = ORB.init(args, props);

            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            ServerImpl serverImpl = new ServerImpl();

            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(serverImpl);
            Server href = ServerHelper.narrow(ref);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references(SERVICE_NAME);
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            NameComponent path[] = ncRef.to_name(OBJECT_NAME);
            ncRef.rebind(path, href);

            System.out.println("CORBAServer ready and waiting ...");

            orb.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}