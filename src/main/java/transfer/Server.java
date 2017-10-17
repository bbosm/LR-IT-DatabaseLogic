package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    DataBase dbRequest() throws RemoteException, NoSuchMethodException, InstantiationException, FileNotFoundException, IllegalAccessException, InvocationTargetException, ClassNotFoundException;
    Table tableRequest(String tableName) throws RemoteException;
    void createTable(String tableName, ArrayList<Column> currColumns) throws RemoteException;
    void deleteTable(String tableName) throws RemoteException;
    Table search(String tableName, ArrayList<String> fieldsSearch) throws RemoteException;
    void addNewRow(String tableName, ArrayList<Attribute> attributes) throws RemoteException;
    void editCell(String tableName, int rowId, int columnId, String value) throws RemoteException, IllegalAccessException, InstantiationException, InvocationTargetException;
}
