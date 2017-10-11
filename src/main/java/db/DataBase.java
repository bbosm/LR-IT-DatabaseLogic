package db;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class DataBase {
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
            IllegalAccessException {
        this.pathToFile = pathToFile;

        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            // first line
            int tablesSize = Integer.parseInt(br.readLine());

            // other lines
            tables = new HashMap<>(tablesSize);
            for (int tableId = 0; tableId < tablesSize; tableId++) {
                String tableFilePath = br.readLine();
                Table table = new Table(tableFilePath);
                tables.put(table.getName(), table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String pathToFile) throws IOException {
        this.pathToFile = pathToFile;

        FileWriter fw = new FileWriter(pathToFile);
        try(PrintWriter out = new PrintWriter(fw)) {
            // firat line
            out.println(tables.size());

            // table lines
            for (Table table : tables.values()) {
                out.println(table.getPathToFile());
                table.saveToFile();
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void saveToFile() throws IOException {
        saveToFile(this.pathToFile);
    }

    public String getPathForTables() {
        return pathToFile.substring(0, pathToFile.lastIndexOf(File.separatorChar));
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public HashMap<String, Table> getTables() {
        return tables;
    }
}
