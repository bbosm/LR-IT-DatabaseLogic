package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientRestServlet extends Client {
    private final String linkToServer = "http://localhost:8080/mydb/rest";

    public ClientRestServlet() {
        super();
    }

    private int sendRequest(HttpURLConnection connection, String requestMethod, String requestString) throws ConnectException {
        try {
            connection.setRequestMethod(requestMethod);
        } catch (ProtocolException e) {
            throw new ConnectException();
        }

        System.out.println(requestMethod + " " + connection.getURL().toString());
        System.out.println(requestString);

        if (requestMethod.equals("GET")) {
            connection.setDoOutput(false);
            connection.setDoInput(true);
        } else {
            connection.setDoOutput(true);
//            connection.setDoInput(true);

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(requestString.getBytes().length));
            connection.setUseCaches(false);

            DataOutputStream out = null;
            try {
                out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(requestString);
                out.flush();
                out.close();
            } catch (IOException e) {
                throw new ConnectException();
            }
        }

        connection.setInstanceFollowRedirects(false);

        int responseCode = 500;
        try {
            connection.connect();
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new ConnectException();
        }
        return responseCode;
    }

    public void updateDB() throws ConnectException {
        String requestURL = linkToServer + "/db";

        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            throw new ConnectException();
        }

        sendRequest(connection, "GET", "");

        ArrayList<String> tableNames = new ArrayList<>();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine = in.readLine();
            int numberOfTables = Integer.parseInt(inputLine);

            for (int i = 0; i < numberOfTables; i++) {
                inputLine = in.readLine();

                int indexBegin = inputLine.lastIndexOf("/");
                if (indexBegin == -1) {
                    indexBegin = inputLine.lastIndexOf("\\");
                }
                if (indexBegin == -1) {
                    indexBegin = 0;
                }
                int indexEnd = inputLine.length() - ".tb".length();

                String tableName = inputLine.substring(indexBegin, indexEnd);
                tableNames.add(tableName);
            }
        } catch (Exception e) {
            throw new ConnectException();
        }

        connection.disconnect();

        try {
            clientDataBase = new DataBase(null, new HashMap<>());
        } catch (Exception e) {
            throw new ConnectException();
        }

        for(String tableName : tableNames) {
            requestURL =  linkToServer + "/db/" + tableName;
            try {
                url = new URL(requestURL);
                connection = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                throw new ConnectException();
            }
            sendRequest(connection, "GET", "");

            try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                Table table = new Table(in);
                clientDataBase.getTables().put(tableName, table);
            } catch (Exception e) {
                throw new ConnectException();
            }

            connection.disconnect();
        }
    }

    public void createTable(String tableName, ArrayList<Column> currColumns) throws ConnectException {
        String requestURL = linkToServer + "/db";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tableName + "\n");
        for (Column column : currColumns) {
            stringBuilder.append(column.toString() + "\n");
        }

        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            throw new ConnectException();
        }

        sendRequest(connection, "POST", stringBuilder.toString());

        connection.disconnect();
    }

    public void deleteTable(String tableName) throws ConnectException {}

    public Table search(String tableName, ArrayList<String> fieldsSearch) throws ConnectException {
        String requestURL = linkToServer + "/db/" + tableName + "/search";

        StringBuilder stringBuilder = new StringBuilder();
        for (String fieldSearch : fieldsSearch) {
            stringBuilder.append(fieldSearch + "\n");
        }

        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            throw new ConnectException();
        }

        sendRequest(connection, "GET", stringBuilder.toString());

        Table table = null;
        try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            table = new Table(in);
            clientDataBase.getTables().put(table.getName(), table);
        } catch (Exception e) {
            throw new ConnectException();
        }

        connection.disconnect();

        return table;
    }

    public void addNewRow(String tableName, ArrayList<Attribute> attributes) throws ConnectException {
        String requestURL = linkToServer + "/db/" + tableName;
        StringBuilder stringBuilder = new StringBuilder();
        for (Attribute attribute : attributes) {
            stringBuilder.append(attribute.toFile() + "\n");
        }

        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            throw new ConnectException();
        }

        sendRequest(connection, "POST", stringBuilder.toString());

        connection.disconnect();
    }

    public void editCell(String tableName, int rowId, int columnId, String value) throws ConnectException {
        String requestURL = linkToServer + "/db/" + tableName;

        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            throw new ConnectException();
        }

        sendRequest(connection, "PUT", rowId + "\n" + columnId + "\n" + value);

        connection.disconnect();
    }

    public DataBase getClientDataBase() {
        return clientDataBase;
    }
}
