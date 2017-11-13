package transfer;

import db.*;
import dbtype.Attribute;

import javax.rmi.PortableRemoteObject;
import java.io.FileNotFoundException;
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


    public String dbRequest() throws NoSuchMethodException, InstantiationException, FileNotFoundException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return serverMaster.getDB().toString();
    }

    public String tableRequest(String tableName) {
        try {
            return serverMaster.getTable(tableName).toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void createTable(String str) {
        String[] inputLines = str.split("\\r?\\n");

        String tableName = inputLines[0];
        ArrayList<Column> columns = new ArrayList<>();
        for (int i = 1; i < inputLines.length; i++) {
            try {
                columns.add(ColumnFactory.createColumn(inputLines[i]));
            } catch (NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            serverMaster.createTable(tableName, columns);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String search(String tableName, String fieldsSearchStr) {
        ArrayList<String> fieldsSearch = new ArrayList<>(Arrays.asList(fieldsSearchStr.split("\\t")));

        Table searchResult = null;
        try {
            searchResult = serverMaster.search(tableName, fieldsSearch);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);

        searchResult.writeToPrintWriter(writer);
        return out.toString();
    }

    public void addNewRow(String tableName, String attributesStr) {
        String[] inputLines = attributesStr.split("\\r?\\n");

        Table table = null;
        try {
            table = serverMaster.getTable(tableName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Attribute> attributes = new ArrayList<>(inputLines.length);
        for (int i = 0; i < inputLines.length; i++) {
            try {
                attributes.add(table.constructField(i, inputLines[i]));
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        try {
            serverMaster.addNewRow(tableName, attributes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
