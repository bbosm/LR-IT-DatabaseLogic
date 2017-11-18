package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import java.rmi.*;
import java.util.HashMap;
import java.util.Properties;

public class ClientRMI {

    private static DataBase clientDataBase = null;
    private static Server server = null;

    static final String SERVICE_NAME = "NameService";
    static final String ORB_PORT = "8080";

    static final String OBJECT_NAME = "SERVER";

    public static void init() {
        try {
            String[] args = {};
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", ORB_PORT);

            ORB orb = ORB.init(args, props);
            org.omg.CORBA.Object object = orb.resolve_initial_references(SERVICE_NAME);
            NamingContext context = NamingContextHelper.narrow(object);
            System.out.println ("The ORB has been created and initialized ...");

            NameComponent component = new NameComponent(OBJECT_NAME, "");
            NameComponent[] path = {component};
            server = ServerHelper.narrow(context.resolve(path));
            System.out.println ("The Object Reference has been resolved in Naming ...");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateDB() throws RemoteException {
        String response = server.dbRequest();
        clientDataBase = new DataBase(response);
        for (Table table : clientDataBase.getTables().values()) {
            Table newTable = new Table(server.tableRequest(table.getName()));
            clientDataBase.getTables().put(table.getName(), newTable);
        }
    }

    public static void createTable(String tableName, ArrayList<Column> currColumns) throws RemoteException {
        Table table = new Table(tableName, currColumns);
        server.createTable(table.toString());
    }

    public static Table search(String tableName, ArrayList<String> fieldsSearch) {
        String fieldsSearchStr = String.join("\t", fieldsSearch);

        String response = null;
        try {
            response = server.search(tableName, fieldsSearchStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Table table = new Table(response);
        clientDataBase.getTables().put(tableName, table);
        return table;
    }

    public static void addNewRow(String tableName, ArrayList<Attribute> attributes) throws RemoteException {
        StringBuilder sb = new StringBuilder();
        for (Attribute attribute : attributes) {
            sb.append(attribute.toFile() + "\t");
        }

        server.addNewRow(tableName, sb.toString());
    }

    public static DataBase getClientDataBase() {
        return clientDataBase;
    }
}
