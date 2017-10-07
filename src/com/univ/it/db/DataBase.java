package com.univ.it.db;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class DataBase {
    private String name;
    private ArrayList<Table> tables;

    public DataBase(String name, ArrayList<Table> tables) {
        this.name = name;
        this.tables = tables;
    }

    public DataBase(String pathToFile, String name) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
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
    }

    public void saveToFile(String pathToFile) throws IOException {
        FileWriter fw = new FileWriter(pathToFile + File.separator + name + ".db");
        try(PrintWriter out = new PrintWriter(fw)){
            out.println(tables.size());

            StringBuilder tablesLine = new StringBuilder();
            for (Table table : tables) {
                tablesLine.append(table.getName());
                tablesLine.append("\t");
            }
            out.println(tablesLine.toString());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
