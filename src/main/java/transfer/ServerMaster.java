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

public class ServerMaster {
    // ends with File.separator or empty ("") String
    protected final String dbFolderPath = "";
    protected final String dbFileName = "bd.db";

    protected DataBase serverDataBase = null;

    public DataBase getDB() {
        if (null == serverDataBase)
        {
            serverDataBase = new DataBase(dbFolderPath, dbFileName);
        }
        return serverDataBase;
    }

    public Table getTable(String tableName) {
        return serverDataBase.getTable(tableName);
    }

    public void createTable(String fileStr) {
        Table table = new Table(fileStr);
        serverDataBase.getTables().put(table.getName(), table);
        serverDataBase.saveToFile(dbFolderPath, table.getName() + ".tb");
    }

//    public void deleteTable(String tableName) throws FileNotFoundException {
//        serverDataBase.getTables().remove(tableName);
//        saveDb();
//    }

    public Table search(String tableName, ArrayList<String> fieldsSearch) {
        return serverDataBase.getTable(tableName).search(fieldsSearch);
    }

    public void addNewRow(String tableName, ArrayList<String> attributesStrings) {
        Table table = serverDataBase.getTable(tableName);
        ArrayList<Attribute> attributes = new ArrayList<>(table.getColumns().size());
        for (int i = 0; i < table.getColumns().size(); i++) {
            attributes.add(table.constructField(i, attributesStrings.get(i)));
        }

        table.getRows().add(new Row(attributes));
        table.saveToFile(dbFolderPath, table.getName() + ".tb");
    }

//    public void editCell(String tableName, int rowId, int columnId, String value) {
//        serverDataBase.getTable(tableName).setCell(rowId, columnId, value);
//        serverDataBase.getTable(tableName).saveToFile();
//    }
}