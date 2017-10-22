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

public class ClientWebServlet extends Client {
    private final String linkToServer =
            "http://mydb.us-east-2.elasticbeanstalk.com";
//            "http://localhost:8080/mydb";

    public ClientWebServlet() {
        super();
    }

    private int sendRequest(HttpURLConnection connection, String requestString) throws ConnectException {
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new ConnectException();
        }
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

        int responseCode = 500;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new ConnectException();
        }
        return responseCode;
    }

//    private String readResponse(HttpURLConnection connection) {
//        StringBuffer response = new StringBuffer();
//        try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//            String inputLine;
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//        } catch (IOException e) {
//            throw new ConnectException();
//        }
//
//        return response.toString();
//    }

    public void updateDB() throws ConnectException {
        String requestURL = linkToServer + "/DBServlet";

        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            throw new ConnectException();
        }

        sendRequest(connection, "");

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
            clientDataBase = new DataBase(new BufferedReader(new StringReader("0")));
        } catch (Exception e) {
            throw new ConnectException();
        }

        for(String tableName : tableNames) {
            requestURL =  linkToServer + "/GetTableServlet?tableName=" + tableName;
            try {
                url = new URL(requestURL);
                connection = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                throw new ConnectException();
            }
            sendRequest(connection, "");

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
        String requestURL = linkToServer + "/CreateTableServlet?tableName=" + tableName;
        System.out.println(requestURL);

        StringBuilder stringBuilder = new StringBuilder();
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

        sendRequest(connection, stringBuilder.toString());

        connection.disconnect();
    }

    public void deleteTable(String tableName) throws ConnectException {}

    public Table search(String tableName, ArrayList<String> fieldsSearch) throws ConnectException {
        String requestURL = linkToServer + "/SearchServlet?tableName=" + tableName;

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

        sendRequest(connection, stringBuilder.toString());

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
        String requestURL = linkToServer + "/AddNewRowServlet?tableName=" + tableName;
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

        sendRequest(connection, stringBuilder.toString());

        connection.disconnect();
    }

    public void editCell(String tableName, int rowId, int columnId, String value) throws ConnectException {
        String requestURL = linkToServer + "/EditCellServlet?tableName=" + tableName
                + "&rowId=" + rowId + "&columnId" + columnId;

        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            throw new ConnectException();
        }

        sendRequest(connection, value);

        connection.disconnect();
    }

    public DataBase getClientDataBase() {
        return clientDataBase;
    }
}
