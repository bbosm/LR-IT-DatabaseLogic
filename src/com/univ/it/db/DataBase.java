package com.univ.it.db;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class DataBase {
    private String pathToFile;
    private String name;
    private ArrayList<Table> tables;

    public DataBase(String pathToFile, String name, ArrayList<Table> tables) {
        this.pathToFile = pathToFile;
        this.name = name;
        this.tables = tables;
    }

    public DataBase(String pathToFile) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.pathToFile = pathToFile;

        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            // first line
            this.name = br.readLine();

            // second line
            int tablesSize = Integer.parseInt(br.readLine());

            // other lines
            tables = new ArrayList<>(tablesSize);
            for (int tableId = 0; tableId < tablesSize; tableId++) {
                String tableFilePath = br.readLine();
                tables.add(new Table(tableFilePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String pathToFile) throws IOException {
        this.pathToFile = pathToFile;

        FileWriter fw = new FileWriter(pathToFile);
        try(PrintWriter out = new PrintWriter(fw)) {
            // first line
            out.println(name);

            // second line
            out.println(tables.size());

            // table lines
            for (Table table : tables) {
                out.println(table.getPathToFile());
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

    public ArrayList<Table> getTables() {
        return tables;
    }
}
