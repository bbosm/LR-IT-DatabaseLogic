package com.univ.it.test;

import com.univ.it.db.Column;
import com.univ.it.db.ColumnEnum;
import com.univ.it.db.Table;
import com.univ.it.dbtype.*;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class AttributeTypesTest {

    // AttributeChar
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void charEmptyString() {
        Attribute a = new AttributeChar("");
    }

    // AttributeInteger
    @Test(expected = NumberFormatException.class)
    public void realEmptyString() {
        Attribute a = new AttributeReal("");
    }

    // AttributeInteger
    @Test(expected = NumberFormatException.class)
    public void intEmptyString() {
        Attribute a = new AttributeInteger("");
    }

    // AttributeDate
    @Test(expected = ParseException.class)
    public void dateEmptyString() throws ParseException {
        Attribute a = new AttributeDate("");
    }

    // AttributeDateInvl
    @Test(expected = ParseException.class)
    public void dateInvlEmptyString() throws ParseException {
        Attribute a = new AttributeDateInvl("");
    }

    @Test
    public void dateInvlIsInside() throws ParseException {
        AttributeDateInvl a = new AttributeDateInvl("2017.02.1 2017.10.10");
        Date dateIn = AttributeDate.dateToStr.parse("2017.4.1");
        Date dateLess = AttributeDate.dateToStr.parse("2017.1.01");
        Date dateGreater = AttributeDate.dateToStr.parse("2018.1.01");

        assertEquals(a.isInside(dateIn), true);
        assertEquals(a.isInside(dateLess), false);
        assertEquals(a.isInside(dateGreater), false);
    }

    @Test
    public void dateInvlBorders() throws ParseException {
        AttributeDateInvl a = new AttributeDateInvl("2017.2.1 2017.10.10");
        Date dateLeft = AttributeDate.dateToStr.parse("2017.2.1");
        Date dateRight = AttributeDate.dateToStr.parse("2017.10.10");

        assertEquals(a.isInside(dateLeft), true);
        assertEquals(a.isInside(dateRight), true);
    }



    // Table
    @Test
    public void tableFileOutIn() throws
            NoSuchMethodException,
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            InvocationTargetException,
            IOException,
            ParseException {
        String pathToTable = "D:\\Temp\\bd.db";

        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("Date", "com.univ.it.dbtype.AttributeDate"));
        columns.add(new Column("Int","com.univ.it.dbtype.AttributeInteger"));

        Table table = new Table(pathToTable, "testTable", columns);

        ArrayList<Attribute> row = new ArrayList<>();
        row.add(new AttributeDate("2017.10.9"));
        row.add(new AttributeInteger(3));
        table.addRow(row);
        row = new ArrayList<>();
        row.add(new AttributeDate("2017.10.13"));
        row.add(new AttributeInteger(6));
        table.addRow(row);

        table.saveToFile();

        Table table2 = new Table(pathToTable);
        table2.saveToFile();
    }

    @Test
    public void enumTest() throws
            NoSuchMethodException,
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            InvocationTargetException,
            IOException,
            ParseException {
        String pathToTable = "D:\\Temp\\bd2.db";

        ArrayList<String> colors = new ArrayList<>();
        colors.add("Red");
        colors.add("Yellow");

        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("Date", "com.univ.it.dbtype.AttributeDate"));
        ColumnEnum ce = new ColumnEnum("Color","com.univ.it.dbtype.AttributeEnum", colors);
        columns.add(ce);

        Table table = new Table(pathToTable, "testTable", columns);

        ArrayList<Attribute> row = new ArrayList<>();
        row.add(new AttributeDate("2017.10.9"));
        row.add(new AttributeEnum("0", ce));
        table.addRow(row);
        row = new ArrayList<>();
        row.add(new AttributeDate("2017.10.13"));
        row.add(new AttributeEnum("1", ce));
        table.addRow(row);

        table.saveToFile();

        Table table2 = new Table(pathToTable);
        table2.saveToFile();
    }
}
