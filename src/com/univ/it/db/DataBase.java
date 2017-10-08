package com.univ.it.db;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.Paths;

public class DataBase {
    private String name;
    private HashMap<String, Table> tables;

    public  DataBase(String name) {
        this.name = name;
        this.tables = new HashMap<>();
    }

    public boolean addTable(Table table) {
        if (tables.containsKey(table)) {
            return false;
        } else {
            tables.put(table.getName(), table);
            return true;
        }
    }

   /* public DataBase(String pathToFile, String name) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.name = name;
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile + File.separator + name + ".db"))) {
            this.name = name;

            // first line
            int tablesSize = 0;
            String sCurrentLine = br.readLine();
            tablesSize = Integer.parseInt(sCurrentLine);

            // second line
            tables = new ArrayList<>(tablesSize);
            sCurrentLine = br.readLine();
            String[] tablesClassNames = sCurrentLine.split("\\t");
            for (int tableId = 0; tableId < tablesSize; tableId++) {
                tables.set(tableId, new Table(pathToFile, tablesClassNames[tableId]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public HashMap<String, Table> getTables() {
        return tables;
    }

    public void saveToFile(String pathToFile) throws IOException {
        FileWriter fw = new FileWriter(pathToFile + File.separator + name + ".db");
        try(PrintWriter out = new PrintWriter(fw)){
            out.println(tables.size());

            StringBuilder tablesLine = new StringBuilder();
            for (Table table : tables.values()) {
                tablesLine.append(table.getName());
                tablesLine.append("\t");
            }
            out.println(tablesLine.toString());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static DataBase readFromFile(String dbFile) throws Exception {
        String dbName = Paths.get(dbFile).getFileName().toString();
        String dpPath = Paths.get(dbFile).getParent().toString();
        FileReader fr = new FileReader(dbFile);
        BufferedReader br = new BufferedReader(fr);

        String sCurrentLine;

        DataBase db = new DataBase(dbName);

        while ((sCurrentLine = br.readLine()) != null) {
            db.addTable(Table.readFromFile(dpPath + File.separator + sCurrentLine));
        }

        return db;
    }
}
