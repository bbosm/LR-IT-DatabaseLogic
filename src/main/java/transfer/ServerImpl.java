package transfer;

import db.Column;
import db.DataBase;
import db.Row;
import db.Table;
import dbtype.Attribute;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl
        extends UnicastRemoteObject implements Server {

    public ServerImpl(String name) throws RemoteException {
        try {
            Naming.rebind(name, this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ends with File.separator or empty ("") String
    private final String dbFolderPath = "";
    private final String dbFileName = "bd.db";

    private DataBase serverDataBase = null;

    public DataBase dbRequest() throws NoSuchMethodException, InstantiationException, FileNotFoundException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        if (null == serverDataBase)
        {
            serverDataBase = new DataBase(dbFolderPath + dbFileName);
        }
        return serverDataBase;
    }

    public Table tableRequest(String tableName) {
        return serverDataBase.getTable(tableName);
    }

    private void saveDb() {
        try {
            serverDataBase.saveToFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String tableName, ArrayList<Column> currColumns) {
        String tableFilePath = dbFolderPath + tableName + ".tb";
        Table newTable = new Table(tableFilePath, tableName, currColumns);
        serverDataBase.getTables().put(tableName, newTable);
        saveDb();
    }

    public void deleteTable(String tableName) {
        serverDataBase.getTables().remove(tableName);
        saveDb();
    }

    public Table search(String tableName, ArrayList<String> fieldsSearch) {
        return serverDataBase.getTable(tableName).search(fieldsSearch);
    }

    public void addNewRow(String tableName, ArrayList<Attribute> attributes) {
        serverDataBase.getTable(tableName).getRows().add(new Row(attributes));
        saveDb();
    }

    public void editCell(String tableName, int rowId, int columnId, String value) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        serverDataBase.getTable(tableName).setCell(rowId, columnId, value);
        saveDb();
    }
}