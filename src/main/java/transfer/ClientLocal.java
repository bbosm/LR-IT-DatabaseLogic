package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

import java.util.ArrayList;

public class ClientLocal extends ClientMaster {
    protected DataBase clientDataBase = null;

    private ServerMaster server;

    public ClientLocal() {
        server = new ServerMaster();
    }

    public void updateDB() {
        clientDataBase = new DataBase(server.getDB());

        for(Table table : clientDataBase.getTables().values()) {
            Table responseTable = new Table(server.getTable(table.getName()));
            clientDataBase.getTables().put(responseTable.getName(), responseTable);
        }
    }

    public void createTable(String tableName, ArrayList<Column> columns) {
        server.createTable(createTableStr(tableName, columns));
        updateDB();
    }

//    public void deleteTable(String tableName) throws ConnectException {
//        server.deleteTable(tableName);
//        updateDB();
//    }

    public Table search(String tableName, ArrayList<String> fieldsSearch) {
        return new Table(server.search(tableName, searchStr(fieldsSearch)));
    }

    public void addNewRow(String tableName, ArrayList<Attribute> attributes) {
        server.addNewRow(tableName, addNewRowStr(attributes));
    }

    public void editCell(String tableName, int rowId, int columnId, String value) {
        server.editCell(tableName, editCellStr(rowId, columnId, value));
    }

    public DataBase getClientDataBase() {
        return clientDataBase;
    }
}
