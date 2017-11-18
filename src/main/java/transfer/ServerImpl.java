package transfer;

import db.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ServerImpl extends ServerPOA {

    ServerMaster serverMaster = null;

    public ServerImpl() {
       serverMaster = new ServerMaster();
    }

    public String dbRequest() {
        return serverMaster.getDB().toString();
    }

    public String tableRequest(String tableName) {
        return serverMaster.getTable(tableName).toString();
    }

    public void createTable(String fileStr) {
        serverMaster.createTable(fileStr);
    }

    public String search(String tableName, String fieldsSearchStr) {
        ArrayList<String> fieldsSearch = new ArrayList<>(Arrays.asList(fieldsSearchStr.split("\\t")));
        Table searchResult = serverMaster.search(tableName, fieldsSearch);
        return searchResult.toString();
    }

    public void addNewRow(String tableName, String attributesStr) {
        ArrayList<String> attributes = new ArrayList<>(Arrays.asList(attributesStr.split("\\t")));
        serverMaster.addNewRow(tableName, attributes);
    }
}
