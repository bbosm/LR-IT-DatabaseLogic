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
    protected final String dbFolderPath = "";
    protected final String dbFileName = "bd.db";

    protected DataBase serverDataBase = null;

    public DataBase getDB() throws FileNotFoundException {
        if (null == serverDataBase)
        {
            try {
                serverDataBase = new DataBase(dbFolderPath + dbFileName);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException
                    | IllegalAccessException e) {
                throw new FileNotFoundException();
            }
        }
        return serverDataBase;
    }

    public Table getTable(String tableName) throws FileNotFoundException {
        return serverDataBase.getTable(tableName);
    }

    protected void saveDb() throws FileNotFoundException {
        try {
            serverDataBase.saveToFile();
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    public void createTable(String tableName, ArrayList<Column> currColumns) throws FileNotFoundException {
        String tableFilePath = dbFolderPath + tableName + ".tb";
        Table newTable = new Table(tableFilePath, tableName, currColumns);
        serverDataBase.getTables().put(tableName, newTable);
        saveDb();
    }

    public void deleteTable(String tableName) throws FileNotFoundException {
        serverDataBase.getTables().remove(tableName);
        saveDb();
    }

    public Table search(String tableName, ArrayList<String> fieldsSearch) throws FileNotFoundException {
        return serverDataBase.getTable(tableName).search(fieldsSearch);
    }

    public void addNewRow(String tableName, ArrayList<Attribute> attributes) throws FileNotFoundException {
        serverDataBase.getTable(tableName).getRows().add(new Row(attributes));
        saveDb();
    }

    public void editCell(String tableName, int rowId, int columnId, String value) throws FileNotFoundException {
        try {
            serverDataBase.getTable(tableName).setCell(rowId, columnId, value);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new FileNotFoundException();
        }
        saveDb();
    }
}
