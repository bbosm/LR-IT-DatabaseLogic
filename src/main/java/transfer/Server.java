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
    String dbRequest() throws RemoteException, NoSuchMethodException, InstantiationException, FileNotFoundException, IllegalAccessException, InvocationTargetException, ClassNotFoundException;
    String tableRequest(String tableName) throws RemoteException;
    void createTable(String str) throws RemoteException;
    String search(String tableName, String fieldsSearchStr) throws RemoteException;
    void addNewRow(String tableName, String attributesStr) throws RemoteException;
}
