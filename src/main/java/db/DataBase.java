package db;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class DataBase implements Serializable {
    private String pathToFile;
    private HashMap<String, Table> tables;

    public DataBase(String pathToFile, HashMap<String, Table> tables) {
        this.pathToFile = pathToFile;
        this.tables = tables;
    }

    public DataBase(String pathToFile) throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            FileNotFoundException {
        this.pathToFile = pathToFile;

        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            this.readFromBufferedReader(br);
        } catch (IOException e) {
            tables = new HashMap<>(0);
            try {
                this.saveToFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public DataBase(BufferedReader br) throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            IOException {
        this.readFromBufferedReader(br);
    }

    private void readFromBufferedReader(BufferedReader br) throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            IOException {
        // first line
        int tablesSize = Integer.parseInt(br.readLine());

        // other lines
        tables = new HashMap<>(tablesSize);
        for (int tableId = 0; tableId < tablesSize; tableId++) {
            String tableFilePath = br.readLine();
            Table table = new Table(tableFilePath);
            tables.put(table.getName(), table);
        }
    }

    public void writeToPrintWriter(PrintWriter out) throws IOException {
        // firat line
        out.println(tables.size());

        // table lines
        for (Table table : tables.values()) {
            out.println(table.getPathToFile());
        }
    }

    public void saveToFile(String pathToFile) throws IOException {
        this.pathToFile = pathToFile;

        try(PrintWriter out = new PrintWriter(new FileWriter(pathToFile))) {
            this.writeToPrintWriter(out);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

        for (Table table : tables.values()) {
            table.saveToFile();
        }
    }

    public void saveToFile() throws IOException {
        saveToFile(this.pathToFile);
    }

    public Table getTable(String name) {
       return tables.get(name);
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public HashMap<String, Table> getTables() {
        return tables;
    }
}
