package com.univ.it.db;

import com.univ.it.dbtype.Attribute;
import com.univ.it.dbtype.AttributeEnum;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Table {
    private String pathToFile;
    private String name;
    private ArrayList<Row> rows;
    private ArrayList<Column> columns;

    public Table(String pathToFile, String name, ArrayList<Column> columns) {
        this.pathToFile = pathToFile;
        this.name = name;
        this.rows = new ArrayList<>();
        this.columns = columns;
    }

    public Table(String pathToFile) throws
            ClassNotFoundException,
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        this.pathToFile = pathToFile;

        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            // first line
            this.name = br.readLine();

            // second line
            int rowsSize = 0, columnsSize = 0;
            String sCurrentLine = br.readLine();
            String[] firstLineParameters = sCurrentLine.split("\\s+");
            rowsSize = Integer.parseInt(firstLineParameters[0]);
            columnsSize = Integer.parseInt(firstLineParameters[1]);

            // column lines
            columns = new ArrayList<>(columnsSize);
            for (int columnId = 0; columnId < columnsSize; columnId++) {
                sCurrentLine = br.readLine();
                if (sCurrentLine.split("\\t")[0].equals("ColumnEnum"))
                    columns.add(new ColumnEnum(sCurrentLine));
                else
                    columns.add(new Column(sCurrentLine));
            }

            // row lines
            rows = new ArrayList<>(rowsSize);
            for (int rowId = 0; rowId < rowsSize; rowId++) {
                sCurrentLine = br.readLine();
                String[] rowFields = sCurrentLine.split("\\t");
                ArrayList<Attribute> rowValues = new ArrayList<>(columnsSize);
                for (int columnId = 0; columnId < columnsSize; columnId++) {
                    Column column = columns.get(columnId);
                    if (column.getClassName().equals("com.univ.it.dbtype.AttributeEnum")) {
                        rowValues.add(new AttributeEnum(rowFields[columnId], (ColumnEnum) column));
                    } else {
                        rowValues.add((Attribute) column.getStringConstructor().newInstance(rowFields[columnId]));
                    }
                }
                rows.add(new Row(rowValues));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void constructField(int rowNumber, int columnNumber, String s) throws
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        rows.get(rowNumber).set(columnNumber,
                (Attribute) columns.get(columnNumber).getStringConstructor().newInstance(s));
    }

    public void addRow(ArrayList<Attribute> values) {
        // TODO: check if values types are as column types
        Row row = new Row(values);
        rows.add(row);
    }

    public void saveToFile(String pathToFile) throws IOException {
        // TODO: check for not null paths (pathToFile or this.pathToFile)
        this.pathToFile = pathToFile;

        FileWriter fw = new FileWriter(pathToFile);
        try(PrintWriter out = new PrintWriter(fw)) {
            // first line
            out.println(name);

            // second line
            out.println(rows.size() + " " + columns.size());

            // column lines
            for (Column column : columns) {
                String columnLine = column.toString();
                out.println(columnLine);
            }

            // row lines
            for (Row row : rows) {
                String rowLine = row.toString();
                out.println(rowLine);
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void saveToFile() throws IOException {
        saveToFile(this.pathToFile);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public String getPathToFile() {
        return pathToFile;
    }
}
