package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import java.rmi.*;
import java.util.Properties;

public class ClientRMI {

    private static DataBase clientDataBase = null;
    private static Server server = null;

    static final String CONTEXT_NAME = "java.naming.factory.initial";
    static final String IIOP_STRING  = "com.sun.jndi.cosnaming.CNCtxFactory";

    static final String URL_NAME = "java.naming.provider.url";
    static final String IIOP_URL_STRING  = "iiop://localhost:8080";

    static final String OBJECT_NAME = "SERVER";

    public static void init() {
        try {
            Properties iiopProperties = new Properties();
            iiopProperties.put(CONTEXT_NAME, IIOP_STRING);
            iiopProperties.put(URL_NAME, IIOP_URL_STRING);
            InitialContext iiopContext = new InitialContext(iiopProperties);

            server = (Server)PortableRemoteObject.narrow(iiopContext.lookup(OBJECT_NAME), Server.class);

            if (null == server) {
                System.out.println("ololo");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateDB() {
        try {
            clientDataBase = server.dbRequest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String tableName, ArrayList<Column> currColumns) {
        try {
            server.createTable(tableName, currColumns);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTable(String tableName) {
        try {
            server.deleteTable(tableName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static Table search(String tableName, ArrayList<String> fieldsSearch) {
        try {
            return server.search(tableName, fieldsSearch);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addNewRow(String tableName, ArrayList<Attribute> attributes) {
        try {
            server.addNewRow(tableName, attributes);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void editCell(String tableName, int rowId, int columnId, String value) throws RemoteException, InvocationTargetException, InstantiationException, IllegalAccessException {
        server.editCell(tableName, rowId, columnId, value);
    }

    public static DataBase getClientDataBase() {
        return clientDataBase;
    }
}
