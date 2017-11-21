package transfer;

import db.DataBase;
import db.Row;
import db.Table;

import java.util.ArrayList;
import java.util.Arrays;

public class ServerMaster {
    protected final String dbFolderPath = "D:\\Temp\\"; // ends with File.separator or empty ("") String
    protected final String dbFileName = "bd.db";

    protected DataBase serverDataBase = null;

    public ServerMaster() {
        serverDataBase = new DataBase(dbFolderPath, dbFileName);
    }

    public DataBase getDB() {
        return serverDataBase;
    }

    public Table getTable(String tableName) {
        return serverDataBase.getTable(tableName);
    }

    public void createTable(String fileStr) {
        Table table = new Table(fileStr);
        serverDataBase.getTables().put(table.getName(), table);
        serverDataBase.saveToFile(dbFolderPath, dbFileName);
        table.saveToFile(dbFolderPath, DataBase.getTableFilename(table));
    }

//    public void deleteTable(String tableName) throws FileNotFoundException {
//        serverDataBase.getTables().remove(tableName);
//        serverDataBase.saveToFile(dbFolderPath, dbFileName);
//    }

    public Table search(String tableName, String attributesStr) {
        ArrayList<String> fieldsSearch = new ArrayList<>(Arrays.asList(attributesStr.split("\\t")));
        return serverDataBase.getTable(tableName).search(fieldsSearch);
    }

    public void addNewRow(String tableName, String attributesStr) {
        Table table = serverDataBase.getTable(tableName);
        Row row = new Row(table, attributesStr);
        table.getRows().add(row);
        table.saveToFile(dbFolderPath, DataBase.getTableFilename(table));
    }

    public void editCell(String tableName, String editCellStr) {
        String[] editCellStrs = editCellStr.split("(\\r\\n|\\r|\\n)+");
        Table table = serverDataBase.getTable(tableName);
        int rowId = Integer.parseInt(editCellStrs[0]);
        int columnId = Integer.parseInt(editCellStrs[1]);
        table.setCell(rowId, columnId, editCellStrs[2]);
        table.saveToFile(dbFolderPath, DataBase.getTableFilename(table));
    }
}