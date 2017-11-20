package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientRestServlet extends ClientMaster {
    private final String linkToServer = "http://localhost:8080/mydb/rest";

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

//    public void deleteTable(String tableName) throws ConnectException {}

    public Table search(String tableName, ArrayList<String> fieldsSearch) {
        StringBuilder requestURL = new StringBuilder();
        requestURL.append(linkToServer + "/db/" + tableName + "/search");

        try {
            requestURL.append("?p=" + URLEncoder.encode(fieldsSearch.get(0), "UTF-8"));
            for (int i = 1; i < fieldsSearch.size(); i++) {
                requestURL.append("&p=" + URLEncoder.encode(fieldsSearch.get(i), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestURL.toString());
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendRequest(connection, "GET", "");

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
        String requestURL = linkToServer + "/db/" + tableName;
        StringBuilder stringBuilder = new StringBuilder();
        for (Attribute attribute : attributes) {
            stringBuilder.append(attribute.toFile());
            stringBuilder.append(System.lineSeparator());
        }

        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendRequest(connection, "POST", stringBuilder.toString());

        connection.disconnect();
    }

    public void editCell(String tableName, int rowId, int columnId, String value) {
        String requestURL = linkToServer + "/db/" + tableName;

        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendRequest(connection, "PUT", rowId + "\n" + columnId + "\n" + value);

        connection.disconnect();
    }

    public DataBase getClientDataBase() {
        return clientDataBase;
    }
}
