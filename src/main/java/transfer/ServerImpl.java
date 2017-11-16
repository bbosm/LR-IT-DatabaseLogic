package transfer;

import db.*;
import dbtype.Attribute;

import javax.rmi.PortableRemoteObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.rmi.*;
import java.util.Arrays;

public class ServerImpl
        extends PortableRemoteObject implements Server {

    ServerMaster serverMaster = null;

    public ServerImpl() throws RemoteException {
       super();
       serverMaster = new ServerMaster();
    }


    public String dbRequest() throws RemoteException {
        return serverMaster.getDB().toString();
    }

    public String tableRequest(String tableName) throws RemoteException {
        return serverMaster.getTable(tableName).toString();
    }

    public void createTable(String fileStr) throws RemoteException {
        serverMaster.createTable(fileStr);
    }

    public String search(String tableName, String fieldsSearchStr) throws RemoteException {
        ArrayList<String> fieldsSearch = new ArrayList<>(Arrays.asList(fieldsSearchStr.split("\\t")));
        Table searchResult = serverMaster.search(tableName, fieldsSearch);
        return searchResult.toString();
    }

    public void addNewRow(String tableName, String attributesStr) throws RemoteException {
        ArrayList<String> attributes = new ArrayList<>(Arrays.asList(attributesStr.split("\\t")));
        serverMaster.addNewRow(tableName, attributes);
    }
}
