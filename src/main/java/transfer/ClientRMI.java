package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import java.rmi.*;

public class ClientRMI {

    private static DataBase clientDataBase = null;
    private static Server server = null;

    public static void init() {
        try {
            if (null == System.getSecurityManager()) {
                System.setSecurityManager(new SecurityManager());
            }
            server = (Server)Naming.lookup("rmi://localhost/SERVER");
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
