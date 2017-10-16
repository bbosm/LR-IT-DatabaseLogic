package transfer;

import db.Column;
import db.DataBase;
import db.Table;
import dbtype.Attribute;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ClientWebServlet {
    private static final String linkToServer = "http://localhost:8080/mydb";
    private static DataBase clientDataBase = null;

    private static void sendRequest(HttpURLConnection connection, String requestString) throws IOException {
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", Integer.toString(requestString.getBytes().length));
        connection.setUseCaches(false);

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(requestString);
        out.flush();
        out.close();

        int tmp = connection.getResponseCode();
    }

//    private static String readResponse(HttpURLConnection connection) {
//        StringBuffer response = new StringBuffer();
//        try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//            String inputLine;
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return response.toString();
//    }

    public static void updateDB() throws IOException {
        String requestURL = linkToServer + "/DBServlet";

        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

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
            e.printStackTrace();
        }

        connection.disconnect();

        try {
            clientDataBase = new DataBase(new BufferedReader(new StringReader("0")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(String tableName : tableNames) {
            requestURL =  linkToServer + "/GetTableServlet?tableName=" + tableName;
            url = new URL(requestURL);
            connection = (HttpURLConnection)url.openConnection();
            sendRequest(connection, "");

            try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                Table table = new Table(in);
                clientDataBase.getTables().put(tableName, table);
            } catch (Exception e) {
                e.printStackTrace();
            }

            connection.disconnect();
        }
    }

    public static void createTable(String tableName, ArrayList<Column> currColumns) throws
            IOException {
        String requestURL = linkToServer + "/CreateTableServlet?tableName=" + tableName;
        System.out.println(requestURL);

        StringBuilder stringBuilder = new StringBuilder();
        for (Column column : currColumns) {
            stringBuilder.append(column.toString() + "\n");
        }

        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        sendRequest(connection, stringBuilder.toString());

        connection.disconnect();
    }

//    public static void deleteTable(String tableName) {}

    public static Table search(String tableName, ArrayList<String> fieldsSearch) throws IOException {
        String requestURL = linkToServer + "/SearchServlet?tableName=" + tableName;

        StringBuilder stringBuilder = new StringBuilder();
        for (String fieldSearch : fieldsSearch) {
            stringBuilder.append(fieldSearch + "\n");
        }

        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        sendRequest(connection, stringBuilder.toString());

        Table table = null;
        try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            table = new Table(in);
            clientDataBase.getTables().put(table.getName(), table);
        } catch (Exception e) {
            e.printStackTrace();
        }

        connection.disconnect();

        return table;
    }

    public static void addNewRow(String tableName, ArrayList<Attribute> attributes) throws IOException {
        String requestURL = linkToServer + "/AddNewRowServlet?tableName=" + tableName;
        StringBuilder stringBuilder = new StringBuilder();
        for (Attribute attribute : attributes) {
            stringBuilder.append(attribute.toFile() + "\n");
        }

        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        sendRequest(connection, stringBuilder.toString());

        connection.disconnect();
    }

    public static void editCell(String tableName, int rowId, int columnId, String value) throws
            IllegalAccessException,
            InstantiationException,
            InvocationTargetException, IOException {
        String requestURL = linkToServer + "/EditCellServlet?tableName=" + tableName
                + "&rowId=" + rowId + "&columnId" + columnId;

        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        sendRequest(connection, value);

        connection.disconnect();
    }

    public static DataBase getClientDataBase() {
        return clientDataBase;
    }
}
