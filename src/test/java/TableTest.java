import db.*;
import dbtype.Attribute;
import dbtype.AttributeDate;
import dbtype.AttributeEnum;
import dbtype.AttributeInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TableTest {
    String pathToTable = "D:\\Temp\\bd.db";
    private Table table;
    private final int columnsSize = 3;

    @Before
    public void initTable() throws
            NoSuchMethodException,
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            InvocationTargetException,
            IOException,
            ParseException {

        ArrayList<Column> columns = new ArrayList<>();
        columns.add(
                ColumnFactory.createColumn(
                        ColumnFactory.makePlainColumnString("Date", "DateColumnName")));
        columns.add(
                ColumnFactory.createColumn(
                        ColumnFactory.makePlainColumnString("Integer", "IntColumnName")));
        columns.add(
                ColumnFactory.createColumn(
                        ColumnFactory.makeEnumColumnString("EnumColumnName", "Red Yellow")));

        table = new Table(pathToTable, "testTable", columns);

        ArrayList<Attribute> row = new ArrayList<>(columnsSize);
        row.add(new AttributeDate("2017.10.9"));
        row.add(new AttributeInteger(4));
        row.add(new AttributeEnum("0", columns.get(2)));
        table.addRow(row);

        row = new ArrayList<>();
        row.add(new AttributeDate("2017.10.13"));
        row.add(new AttributeInteger(2));
        row.add(new AttributeEnum("1", columns.get(2)));
        table.addRow(row);

        row = new ArrayList<>();
        row.add(new AttributeDate("2017.10.16"));
        row.add(new AttributeInteger("4"));
        row.add(new AttributeEnum("0", columns.get(2)));
        table.addRow(row);

        row = new ArrayList<>();
        row.add(new AttributeDate("2017.10.13"));
        row.add(new AttributeInteger(6));
        row.add(new AttributeEnum("0", columns.get(2)));
        table.addRow(row);
    }

    @After
    public void deleteTable() {
        table = null;
    }

    @Test
    public void tableSearchAll() {
        ArrayList<String> search = new ArrayList<>(columnsSize);
        for (int i = 0; i < columnsSize; i++)
            search.add("");
        Table res = table.search(search);
        assertEquals(res.getRows().size(), table.getRows().size());
    }

    @Test
    public void tableSearchEmpty() {
        int searchColumn = 1;
        String searchString = "-1";

        ArrayList<String> search = new ArrayList<>(columnsSize);
        for (int i = 0; i < columnsSize; i++)
            search.add("");
        search.set(searchColumn, searchString);
        Table res = table.search(search);
        assertEquals(0, res.getRows().size());
    }

    @Test
    public void tableSearchDate() {
        int searchColumn = 0;
        String searchString = "2017.10.13";

        ArrayList<String> search = new ArrayList<>(columnsSize);
        for (int i = 0; i < columnsSize; i++)
            search.add("");
        search.set(searchColumn, searchString);
        Table res = table.search(search);

        assertEquals(2, res.getRows().size());
        for (Row row : res.getRows()) {
            assertEquals(searchString, row.get(searchColumn).toString());
        }
    }

    @Test
    public void tableSearchInt() {
        int searchColumn = 1;
        String searchString = "4";

        ArrayList<String> search = new ArrayList<>(columnsSize);
        for (int i = 0; i < columnsSize; i++)
            search.add("");
        search.set(searchColumn, searchString);
        Table res = table.search(search);

        assertEquals(2, res.getRows().size());
        for (Row row : res.getRows()) {
            assertEquals(searchString, row.get(searchColumn).toString());
        }
    }

    @Test
    public void tableSearchEnum() {
        int searchColumn = 2;
        String searchString = "Red";

        ArrayList<String> search = new ArrayList<>(columnsSize);
        for (int i = 0; i < columnsSize; i++)
            search.add("");
        search.set(searchColumn, searchString);
        Table res = table.search(search);

        assertEquals(3, res.getRows().size());
        for (Row row : res.getRows()) {
            assertEquals(searchString, row.get(searchColumn).toString());
        }
    }

    @Test
    public void tableSearchDateEnum() {
        String dateSearchString = "2017.10.13";
        String enumSearchString = "Red";

        ArrayList<String> search = new ArrayList<>(columnsSize);
        search.add(dateSearchString);
        search.add("");
        search.add(enumSearchString);
        Table res = table.search(search);

        assertEquals(1, res.getRows().size());
        Row resRow = res.getRows().get(0);
        assertEquals(dateSearchString, resRow.get(0).toString());
        assertEquals("6", resRow.get(1).toString());
        assertEquals(enumSearchString, resRow.get(2).toString());
    }



    @Test
    public void fileIO() throws
            IOException,
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {
        table.saveToFile();

        Table table2 = new Table(pathToTable);

        assertEquals("testTable", table2.getName());
        assertEquals(3, table2.getColumns().size());
        assertEquals(4, table2.getRows().size());

        assertEquals("Date", table2.getColumns().get(0).getAttributeShortTypeName());
        assertEquals("DateColumnName", table2.getColumns().get(0).getName());

        assertEquals("Integer", table2.getColumns().get(1).getAttributeShortTypeName());
        assertEquals("IntColumnName", table2.getColumns().get(1).getName());

        EnumColumn enumColumn = (EnumColumn) table2.getColumns().get(2);
        assertEquals("Enum", enumColumn.getAttributeShortTypeName());
        assertEquals("EnumColumnName", enumColumn.getName());
        assertEquals("Red", enumColumn.getValue(0));
        assertEquals("Yellow", enumColumn.getValue(1));

        assertEquals("2017.10.09", table2.getRows().get(0).getValues().get(0).toString());
        assertEquals("4",          table2.getRows().get(0).getValues().get(1).toString());
        assertEquals("Red",        table2.getRows().get(0).getValues().get(2).toString());
        assertEquals("0",          table2.getRows().get(0).getValues().get(2).toFile());

        assertEquals("2017.10.13", table2.getRows().get(1).getValues().get(0).toString());
        assertEquals("2",          table2.getRows().get(1).getValues().get(1).toString());
        assertEquals("Yellow",     table2.getRows().get(1).getValues().get(2).toString());
        assertEquals("1",          table2.getRows().get(1).getValues().get(2).toFile());

        assertEquals("2017.10.16", table2.getRows().get(2).getValues().get(0).toString());
        assertEquals("4",          table2.getRows().get(2).getValues().get(1).toString());
        assertEquals("Red",        table2.getRows().get(2).getValues().get(2).toString());
        assertEquals("0",          table2.getRows().get(2).getValues().get(2).toFile());

        assertEquals("2017.10.13", table2.getRows().get(3).getValues().get(0).toString());
        assertEquals("6",          table2.getRows().get(3).getValues().get(1).toString());
        assertEquals("Red",        table2.getRows().get(3).getValues().get(2).toString());
        assertEquals("0",          table2.getRows().get(3).getValues().get(2).toFile());

        table2.saveToFile();
    }
}
