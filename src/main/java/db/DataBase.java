package db;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBase implements Serializable {
    private HashMap<String, Table> tables;

    public DataBase(String fileStr) {
        this.fromString(fileStr);
    }

    public DataBase(String folder, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(folder + filename))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while( (line = br.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }

            this.fromString(stringBuilder.toString());

            for (Table table : getTables().values()) {
                Table fileTable = new Table(folder, getTableFilename(table));
                tables.put(fileTable.getName(), fileTable);
            }
        } catch (IOException e) {
            tables = new HashMap<>(0);
            this.saveToFile(folder, filename);
        }
    }

    private void fromString(String fileStr) {
        String[] fileLines = fileStr.split("(\\r\\n|\\r|\\n)+");

        // first line
        int tablesSize = Integer.parseInt(fileLines[0]);

        // other lines
        tables = new HashMap<>(tablesSize);
        for (int tableId = 0; tableId < tablesSize; tableId++) {
            Table table = new Table(fileLines[tableId + 1], (ArrayList<Column>) null);
            tables.put(table.getName(), table);
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        // first line
        out.append(tables.size());
        out.append(System.lineSeparator());

        // table lines
        for (Table table : tables.values()) {
            out.append(table.getName());
            out.append(System.lineSeparator());
        }
        return out.toString();
    }

    public void saveToFile(String folder, String filename) {
        try(PrintWriter out = new PrintWriter(new FileWriter(folder + filename))) {
            out.print(this.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Table table : tables.values()) {
            table.saveToFile(folder, table.getName() + ".tb");
        }
    }

    public static String getTableFilename(Table table) {
        return table.getName() + ".tb";
    }

    public Table getTable(String name) {
       return tables.get(name);
    }

    public HashMap<String, Table> getTables() {
        return tables;
    }
}
