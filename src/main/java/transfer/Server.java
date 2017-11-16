package transfer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    String dbRequest() throws RemoteException;
    String tableRequest(String tableName) throws RemoteException;
    void createTable(String str) throws RemoteException;
    String search(String tableName, String fieldsSearchStr) throws RemoteException;
    void addNewRow(String tableName, String attributesStr) throws RemoteException;
}
