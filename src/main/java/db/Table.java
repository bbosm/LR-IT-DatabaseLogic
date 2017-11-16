package db;

import dbtype.Attribute;
import dbtype.AttributeEnum;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Table implements Serializable {
    private String name;
    private ArrayList<Row> rows;
    private ArrayList<Column> columns;

    public Table(String fileStr) {
        this.fromString(fileStr);
    }

    public Table(String folder, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(folder + filename))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while( (line = br.readLine()) != null) {
                stringBuilder.append(line);
            }

            this.fromString(stringBuilder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Table(String name, ArrayList<Column> columns) {
        this.name = name;
        this.rows = new ArrayList<>();
        this.columns = columns;
    }

    private void fromString(String fileStr) {
        String[] fileLines = fileStr.split("(\\r\\n|\\r|\\n)+");

        // first line
        this.name = fileLines[0];

        // second line
        int rowsSize = 0, columnsSize = 0;
        String sCurrentLine = fileLines[1];
        String[] firstLineParameters = sCurrentLine.split("\\s+");
        rowsSize = Integer.parseInt(firstLineParameters[0]);
        columnsSize = Integer.parseInt(firstLineParameters[1]);

        // column lines
        columns = new ArrayList<>(columnsSize);
        for (int columnId = 0; columnId < columnsSize; columnId++) {
            sCurrentLine = fileLines[2 + columnId];
            columns.add(ColumnFactory.createColumn(sCurrentLine));
        }

        // row lines
        rows = new ArrayList<>(rowsSize);
        for (int rowId = 0; rowId < rowsSize; rowId++) {
            sCurrentLine = fileLines[2 + columnsSize + rowId];
            String[] rowFields = sCurrentLine.split("\\t");
            ArrayList<Attribute> rowValues = new ArrayList<>(columnsSize);

            for (int columnId = 0; columnId < columnsSize; columnId++) {
                rowValues.add(constructField(columnId, rowFields[columnId]));
            }

            rows.add(new Row(rowValues));
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        // first line
        out.append(name + System.lineSeparator());

        // second line
        out.append(rows.size() + " " + columns.size() + System.lineSeparator());

        // column lines
        for (Column column : columns) {
            String columnLine = column.toString();
            out.append(columnLine + System.lineSeparator());
        }

        // row lines
        for (Row row : rows) {
            String rowLine = row.toString();
            out.append(rowLine + System.lineSeparator());
        }
        return out.toString();
    }

    public void saveToFile(String folder, String filename) {
        // TODO: check for not null paths (pathToFile or this.pathToFile)
        try(PrintWriter out = new PrintWriter(new FileWriter(folder + filename))) {
            out.print(this.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Table search(ArrayList<String> fieldsSearch) {
        Table result = new Table("Search in " + getName(), getColumns());
        for (int i = 0; i < getRows().size(); ++i) {
            boolean add = true;
            Row tmpRow = getRows().get(i);
            for (int j = 0; j < getColumns().size(); ++j) {
                String tmpString = fieldsSearch.get(j);
                if (!tmpString.equals("")) {
                    if (!tmpRow.get(j).toString().equals(tmpString)) {
                        add = false;
                        break;
                    }
                }
            }
            if (add) {
                result.addRow(tmpRow.getValues());
            }
        }
        return result;
    }

    public Attribute constructField(int columnNumber, String s) {
        Column currColumn = columns.get(columnNumber);

        if (currColumn.getAttributeShortTypeName().equals("Enum")) {
            return new AttributeEnum(s, currColumn);
        }

        try {
            return (Attribute)currColumn.getStringConstructor().newInstance(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setCell(int rowNumber, int columnNumber, String s) {
        rows.get(rowNumber).set(columnNumber, constructField(columnNumber, s));
    }

    public Attribute getCell(int rowNumber, int columnNumber) {
        return rows.get(rowNumber).get(columnNumber);
    }

    public void addRow(ArrayList<Attribute> values) {
        // TODO: check if values types are as column types
        Row row = new Row(values);
        rows.add(row);
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
}
