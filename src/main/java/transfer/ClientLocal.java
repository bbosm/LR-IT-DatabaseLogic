package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ClientLocal {
    private static DataBase clientDataBase = null;

    public static void updateDB() {
        try {
            clientDataBase = Server.dbRequest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String tableName, ArrayList<Column> currColumns) {
        Server.createTable(tableName, currColumns);
    }

    public static void deleteTable(String tableName) {
        Server.deleteTable(tableName);
    }

    public static Table search(String tableName, ArrayList<String> fieldsSearch) {
        return Server.search(tableName, fieldsSearch);
    }

    public static void addNewRow(String tableName, ArrayList<Attribute> attributes) {
        Server.addNewRow(tableName, attributes);
    }

    public static void editCell(String tableName, int rowId, int columnId, String value) throws
            IllegalAccessException,
            InstantiationException,
            InvocationTargetException {
        Server.editCell(tableName, rowId, columnId, value);
    }

    public static DataBase getClientDataBase() {
        return clientDataBase;
    }
}
