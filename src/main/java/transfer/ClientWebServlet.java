package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientWebServlet extends ClientMaster {
    private final String linkToServer =
            "http://mydb.us-east-2.elasticbeanstalk.com";
//            "http://localhost:8080/mydb";

    private int sendRequest(HttpURLConnection connection, String requestMethod, String requestString) {
        try {
            connection.setRequestMethod(requestMethod);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        if (requestMethod.equals("GET")) {
            connection.setDoOutput(false);
        } else {
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("charset", "utf-8");

            try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
                out.writeBytes(requestString);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseCode;
    }

    public void updateDB() {
        String requestURL = linkToServer + "/db";

        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendRequest(connection, "GET", "");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder stringBuilder = new StringBuilder();
            String sCurrentLine;
            while ((sCurrentLine = in.readLine()) != null) {
                stringBuilder.append(sCurrentLine + System.lineSeparator());
            }
            clientDataBase = new DataBase(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        connection.disconnect();

        for(Table table : clientDataBase.getTables().values()) {
            requestURL =  linkToServer + "/table?tableName=" + table.getName();
            try {
                URL url = new URL(requestURL);
                connection = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            sendRequest(connection, "GET", "");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder stringBuilder = new StringBuilder();
                String sCurrentLine;
                while ((sCurrentLine = in.readLine()) != null) {
                    stringBuilder.append(sCurrentLine + System.lineSeparator());
                }
                Table responseTable = new Table(stringBuilder.toString());
                clientDataBase.getTables().put(responseTable.getName(), responseTable);
            } catch (IOException e) {
                e.printStackTrace();
            }

            connection.disconnect();
        }
    }

    public void createTable(String tableName, ArrayList<Column> columns) {
        String requestURL = linkToServer + "/createTable?tableName=" + tableName;

        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendRequest(connection, "POST", createTableStr(tableName, columns));

        connection.disconnect();
    }

//    public void deleteTable(String tableName) {}

    public Table search(String tableName, ArrayList<String> fieldsSearch) {
        String requestURL = linkToServer + "/search?tableName=" + tableName;

        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendRequest(connection, "POST", searchStr(fieldsSearch));

        Table responseTable = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder stringBuilder = new StringBuilder();
            String sCurrentLine;
            while ((sCurrentLine = in.readLine()) != null) {
                stringBuilder.append(sCurrentLine + System.lineSeparator());
            }
            responseTable = new Table(stringBuilder.toString());
            clientDataBase.getTables().put(responseTable.getName(), responseTable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        connection.disconnect();

        return responseTable;
    }

    public void addNewRow(String tableName, ArrayList<Attribute> attributes) {
        String requestURL = linkToServer + "/tableAddRow?tableName=" + tableName;

        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendRequest(connection, "POST", addNewRowStr(attributes));

        connection.disconnect();
    }

    public void editCell(String tableName, int rowId, int columnId, String value) {
        String requestURL = linkToServer + "/tableEdit?tableName=" + tableName;

        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendRequest(connection, "POST", editCellStr(rowId, columnId, value));

        connection.disconnect();
    }

    public DataBase getClientDataBase() {
        return clientDataBase;
    }
}
