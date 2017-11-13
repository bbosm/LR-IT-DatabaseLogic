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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateDB() throws RemoteException, InvocationTargetException, InstantiationException, FileNotFoundException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        ArrayList<String> tableNames = new ArrayList<>();
        String response = server.dbRequest();
        String[] responseLines = response.split("\\n");

        String inputLine = responseLines[0];
        int numberOfTables = Integer.parseInt(inputLine);

        for (int i = 0; i < numberOfTables; i++) {
            inputLine = responseLines[i + 1];

            int indexBegin = inputLine.lastIndexOf("/");
            if (indexBegin == -1) {
                indexBegin = inputLine.lastIndexOf("\\");
            }
            if (indexBegin == -1) {
                indexBegin = 0;
            }
            int indexEnd = inputLine.length() - ".tb".length();

            String tableName = inputLine.substring(indexBegin, indexEnd);
            tableNames.add(tableName);
        }

        clientDataBase = new DataBase(null, new HashMap<>());

        for(String tableName : tableNames) {
            String tableStr = server.tableRequest(tableName);

            // convert String into InputStream
            InputStream is = new ByteArrayInputStream(tableStr.getBytes());

            // read it with BufferedReader
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                Table table = new Table(br);
                clientDataBase.getTables().put(tableName, table);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createTable(String tableName, ArrayList<Column> currColumns) {
        Table table = new Table(null, tableName, currColumns);
        try {
            server.createTable(table.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static Table search(String tableName, ArrayList<String> fieldsSearch) {
        String fieldsSearchStr = String.join("\t", fieldsSearch);

        String response = null;
        try {
            response = server.search(tableName, fieldsSearchStr);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // convert String into InputStream
        InputStream is = new ByteArrayInputStream(response.getBytes());

        // read it with BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Table table = null;
        try {
            table = new Table(br);
            clientDataBase.getTables().put(tableName, table);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    public static void addNewRow(String tableName, ArrayList<Attribute> attributes) {
        StringBuilder sb = new StringBuilder();
        for (Attribute attribute : attributes) {
            sb.append(attribute.toFile() + "\t");
        }

        String attributesStr = sb.toString();

        try {
            server.addNewRow(tableName, attributesStr);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static DataBase getClientDataBase() {
        return clientDataBase;
    }
}
