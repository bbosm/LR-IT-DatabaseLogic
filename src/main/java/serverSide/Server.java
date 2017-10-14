package serverSide;

import db.Column;
import db.DataBase;
import db.Table;

import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    // TODO: remove this
    private static final String tempDB = "/home/romashka/mydb/1.db";

    private static DataBase currentDB;

    static public void createDb(String name) {
        String fileName;

        if (null == name) {
            fileName = tempDB;
        }
        else {
            fileName = name + ".db";
        }

        currentDB = new DataBase(fileName, new HashMap<>());
    }

    // TODO : public static ArrayList<String> getListOfDatabases();

    static public void openDb(String fileName) throws
            ClassNotFoundException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException {
        currentDB = new DataBase(fileName);
    }

    static public void saveDb() throws IOException {
        currentDB.saveToFile();
    }

    //TODO: return table with name -> database class
    static public Table getTable(String name) {
        return currentDB.getTables().get(name);
    }

    static public void createTable(String tableName, ArrayList<Column> currColumns) {
        String tableFilePath = currentDB.getPathForTables() + File.separator + tableName + ".db";
        Table newTable = new Table(tableFilePath, tableName, currColumns);
        currentDB.getTables().put(tableName, newTable);
    }

    public static HashMap<String, Table> getTables() {
        return currentDB.getTables();
    }

    public static Table search(String tableName, ArrayList<String> fieldsSearch) {
        return getTable(tableName).search(fieldsSearch);
    }

}
