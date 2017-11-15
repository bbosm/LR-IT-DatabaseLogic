package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

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

    static final String CONTEXT_NAME = "java.naming.factory.initial";
    static final String IIOP_CONTEXT_NAME  = "com.sun.jndi.cosnaming.CNCtxFactory";

    static final String URL_NAME = "java.naming.provider.url";
    static final String IIOP_URL_NAME  = "iiop://localhost:8080";

    static final String OBJECT_NAME = "SERVER";

    public static void init() {
        try {
            Properties iiopProperties = new Properties();
            iiopProperties.put(CONTEXT_NAME, IIOP_CONTEXT_NAME);
            iiopProperties.put(URL_NAME, IIOP_URL_NAME);
            InitialContext iiopContext = new InitialContext(iiopProperties);

            server = (Server)PortableRemoteObject.narrow(iiopContext.lookup(OBJECT_NAME), Server.class);
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
        } catch (RemoteException e) {
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
