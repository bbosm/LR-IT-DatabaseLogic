package com.univ.it.table;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Table {
    private String name;
    private ArrayList<Row> rows;

    public Table(String name) {
        this.name = name;
        rows = new ArrayList<>();
    }

    public void addNewRow() {
        rows.add(new Row());
    }

    public void addNewRow(Row newRow) {
        rows.add(newRow);
    }

    public void saveToFile(String pathToFile) {
        try(PrintWriter out = new PrintWriter(pathToFile + File.separator + name)){
            for (Row row : rows) {
                String rowString = row.toString();
                out.println(rowString);
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
