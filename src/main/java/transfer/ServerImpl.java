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


    public String dbRequest() {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);

        try {
            serverMaster.getDB().writeToPrintWriter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toString();
    }

    public String tableRequest(String tableName) {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);

        try {
            serverMaster.getTable(tableName).writeToPrintWriter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toString();
    }

    public void createTable(String str) {
        String[] inputLines = str.split("\\n");

        // first line
        String tableName = inputLines[0];

        // second line
        int rowsSize = 0, columnsSize = 0;
        String sCurrentLine = inputLines[1];
        String[] firstLineParameters = sCurrentLine.split("\\s+");
        rowsSize = Integer.parseInt(firstLineParameters[0]);
        columnsSize = Integer.parseInt(firstLineParameters[1]);

        // column lines
        ArrayList<Column> columns = new ArrayList<>(columnsSize);
        for (int columnId = 0; columnId < columnsSize; columnId++) {
            sCurrentLine = inputLines[columnId + 2];
            try {
                columns.add(ColumnFactory.createColumn(sCurrentLine));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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
