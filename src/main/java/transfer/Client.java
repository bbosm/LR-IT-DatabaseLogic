package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

import java.net.ConnectException;
import java.util.ArrayList;

public abstract class Client {
    protected DataBase clientDataBase = null;

    public abstract void updateDB() throws ConnectException;
    public abstract void createTable(String tableName, ArrayList<Column> columns) throws ConnectException;
    public abstract void deleteTable(String tableName) throws ConnectException;
    public abstract Table search(String tableName, ArrayList<String> fieldsSearch) throws ConnectException;
    public abstract void addNewRow(String tableName, ArrayList<Attribute> attributes) throws ConnectException;
    public abstract void editCell(String tableName, int rowId, int columnId, String value) throws ConnectException;

    public DataBase getClientDataBase() {
        return clientDataBase;
    }
}
