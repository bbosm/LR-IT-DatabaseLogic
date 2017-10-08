package com.univ.it.db;

import com.univ.it.dbtype.Attribute;
import com.univ.it.dbtype.DBTypeId;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.lang.reflect.Constructor;

public class Table {
    private DBTypeId id;
    private String name;
    private ArrayList<Row> rows;
    private ArrayList<Column> columns;

    public Table(DBTypeId id, String name, ArrayList<Column> columns) {
        this.id = id;
        this.name = name;
        this.rows = new ArrayList<>();
        this.columns = columns;
    }

    public Table(String name) {
        this.name = name;
        rows = new ArrayList<>();
        columns = new ArrayList<>();
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
                ArrayList<Attribute> rowValues = new ArrayList<>(columnsSize);
                for (int columnId = 0; columnId < columnsSize; columnId++) {
                    Column column = columns.get(columnId);
                    rowValues.set(columnId,
                            (Attribute) column.getAttributeConstructor().newInstance(rowFields[columnId]));
                }
                rows.set(rowId, new Row(new DBTypeId(rowId), rowValues));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int columnNumber() {
        return columns.size();
    }

    public Column getColumn(int ind) {
        return columns.get(ind);
    }

    public int size() {
        return rows.size();
    }

    public void addRow(ArrayList<Attribute> values) {
        int id = rows.size();
        Row row = new Row(new DBTypeId(id), values);
        rows.add(row);
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

    public static Table readFromFile(String file) throws Exception {
        Table result = new Table(Paths.get(file).getFileName().toString());
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String sCurrentLine;
        boolean firstLine = true;
        int columnNumber = 0;

        while ((sCurrentLine = br.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                String[] columnNames = sCurrentLine.split("\t");
                columnNumber = columnNames.length;
                for (String columnName : columnNames) {
                    result.columns.add(new Column(columnName));
                }
            } else {
                String[] row = sCurrentLine.split("\t");
                if (columnNumber != row.length) {
                    throw new Exception("Invalid file");
                }
                Row newRow = new Row();
                int i = 0;
                for (String stringAttribute : row) {
                    Constructor attributeConstructor = result.columns.get(i).getAttributeConstructor();
                    Attribute attribute = (Attribute) attributeConstructor.newInstance(stringAttribute);
                    newRow.pushBack(attribute);
                    ++i;
                }
                result.rows.add(newRow);
            }
        }
        return result;
    }

    public String getName() { return name; }

    public Row getRow(int id) { return rows.get(id); }
}
