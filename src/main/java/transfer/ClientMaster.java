package transfer;

import db.Column;
import db.DataBase;
import db.Row;
import db.Table;
import dbtype.Attribute;

import java.net.ConnectException;
import java.util.ArrayList;

public abstract class ClientMaster {
    protected DataBase clientDataBase = null;

    public abstract void updateDB();
    public abstract void createTable(String tableName, ArrayList<Column> columns);
//    public abstract void deleteTable(String tableName) throws ConnectException;
    public abstract Table search(String tableName, ArrayList<String> fieldsSearch);
    public abstract void addNewRow(String tableName, ArrayList<Attribute> attributes);
    public abstract void editCell(String tableName, int rowId, int columnId, String value);

    public DataBase getClientDataBase() {
        return clientDataBase;
    }

    protected String createTableStr(String tableName, ArrayList<Column> columns) {
        Table table = new Table(tableName, columns);
        return table.toString();
    }

    protected String searchStr(ArrayList<String> fieldsSearch) {
        StringBuilder result = new StringBuilder();
        for (String attribute : fieldsSearch) {
            result.append(attribute);
            result.append("\t");
        }
        return result.toString();
    }

    public String addNewRowStr(ArrayList<Attribute> attributes) {
        Row row = new Row(attributes);
        return row.toString();
    }

    protected String editCellStr(int rowId, int columnId, String value) {
        return rowId + System.lineSeparator()
                + columnId + System.lineSeparator()
                + value;
    }
}
