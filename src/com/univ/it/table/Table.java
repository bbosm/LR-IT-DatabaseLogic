package com.univ.it.table;

import com.univ.it.types.Attribute;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Table {
    private String name;
    private ArrayList<Row> rows;
    private ArrayList<Column> columns;

    public Table(String name, ArrayList<Column> columns) {
        this.name = name;
        this.rows = new ArrayList<>();
        this.columns = columns;
    }

    public Table(String pathToFile, String name) throws
            ClassNotFoundException,
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile + File.separator + name + ".db"))) {
            this.name = name;

            // first line
            int rowsSize = 0, columnsSize = 0;
            String sCurrentLine = br.readLine();
            String[] firstLineParameters = sCurrentLine.split("\\s+");
            rowsSize = Integer.parseInt(firstLineParameters[0]);
            columnsSize = Integer.parseInt(firstLineParameters[1]);

            // second line
            columns = new ArrayList<>(columnsSize);
            for (int columnId = 0; columnId < columnsSize; columnId++) {
                sCurrentLine = br.readLine();
                String[] columnsFields = sCurrentLine.split("\\t");
                columns.set(columnId, new Column(columnsFields[0], columnsFields[1]));
            }

            // other lines
            rows = new ArrayList<>(rowsSize);
            for (int rowId = 0; rowId < rowsSize; rowId++) {
                sCurrentLine = br.readLine();
                String[] rowFields = sCurrentLine.split("\\t");
                rows.set(rowId, new Row(rowId, columnsSize));
                for (int columnId = 0; columnId < columnsSize; columnId++) {
                    Column column = columns.get(columnId);
                    getRow(rowId).set(columnId,
                            (Attribute) column.getStringConstructor().newInstance(rowFields[columnId]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Row addNewRow() {
        int id = rows.size();
        Row row = new Row(id, columns.size());
        rows.add(row);
        return row;
    }

    public void saveToFile(String pathToFile) throws IOException {
        FileWriter fw = new FileWriter(pathToFile + File.separator + name + ".db");
        try(PrintWriter out = new PrintWriter(fw)) {
            out.println(rows.size() + " " + columns.size());

            for (Column column : columns) {
                String columnString = column.toString();
                out.println(columnString);
            }

            for (Row row : rows) {
                String rowString = row.toString();
                out.println(rowString);
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public String getName() { return name; }

    public Row getRow(int id) { return rows.get(id); }
}
