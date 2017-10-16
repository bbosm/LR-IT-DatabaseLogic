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

public class Server {
    // ends with File.separator or empty ("") String
    private static final String dbFolderPath = "";
    private static final String dbFileName = "bd.db";

    private static DataBase serverDataBase = null;

    public static DataBase dbRequest() throws
            ClassNotFoundException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException,
            FileNotFoundException {
        if (null == serverDataBase)
        {
            serverDataBase = new DataBase(dbFolderPath + dbFileName);
        }
        return serverDataBase;
    }

    public static Table tableRequest(String tableName) {
        return serverDataBase.getTable(tableName);
    }

    private static void saveDb() {
        try {
            serverDataBase.saveToFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String tableName, ArrayList<Column> currColumns) {
        String tableFilePath = dbFolderPath + tableName + ".tb";
        Table newTable = new Table(tableFilePath, tableName, currColumns);
        serverDataBase.getTables().put(tableName, newTable);
        saveDb();
    }

    public static void deleteTable(String tableName) {
        serverDataBase.getTables().remove(tableName);
        saveDb();
    }

    public static Table search(String tableName, ArrayList<String> fieldsSearch) {
        return serverDataBase.getTable(tableName).search(fieldsSearch);
    }

    public static void addNewRow(String tableName, ArrayList<Attribute> attributes) {
        serverDataBase.getTable(tableName).getRows().add(new Row(attributes));
        saveDb();
    }

    public static void editCell(String tableName, int rowId, int columnId, String value) throws
            IllegalAccessException,
            InstantiationException,
            InvocationTargetException {
        serverDataBase.getTable(tableName).setCell(rowId, columnId, value);
        saveDb();
    }
}
