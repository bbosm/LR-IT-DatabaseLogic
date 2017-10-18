package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.util.ArrayList;

public class ClientLocal extends Client {
    private Server server;

    public ClientLocal() {
        super();
        server = new Server();
    }

    public void updateDB() throws ConnectException {
        try {
            clientDataBase = server.dbRequest();
        }
        catch (FileNotFoundException e) {
            throw new ConnectException();
        }
    }

    public void createTable(String tableName, ArrayList<Column> currColumns) throws ConnectException {
        try {
            server.createTable(tableName, currColumns);
        } catch (FileNotFoundException e) {
            throw new ConnectException();
        }
    }

    public void deleteTable(String tableName) throws ConnectException {
        try {
            server.deleteTable(tableName);
        } catch (FileNotFoundException e) {
            throw new ConnectException();
        }
    }

    public Table search(String tableName, ArrayList<String> fieldsSearch) throws ConnectException {
        try {
            return server.search(tableName, fieldsSearch);
        } catch (FileNotFoundException e) {
            throw new ConnectException();
        }
    }

    public void addNewRow(String tableName, ArrayList<Attribute> attributes) throws ConnectException {
        try {
            server.addNewRow(tableName, attributes);
        } catch (FileNotFoundException e) {
            throw new ConnectException();
        }
    }

    public void editCell(String tableName, int rowId, int columnId, String value) throws ConnectException {
        try {
            server.editCell(tableName, rowId, columnId, value);
        } catch (FileNotFoundException e) {
            throw new ConnectException();
        }
    }

    public DataBase getClientDataBase() {
        return clientDataBase;
    }
}
