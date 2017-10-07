package com.univ.it.test;

import com.univ.it.db.Column;
import com.univ.it.db.Table;
import com.univ.it.dbtype.*;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

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
    // TODO


    // Table
    @Test
    public void emptyTableFileOutIn() throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("Date", "com.univ.it.dbtype.AttributeDate"));
        columns.add(new Column("Date","com.univ.it.dbtype.AttributeInteger"));

        Table table = new Table(new DBTypeId(0), "testTable", columns);
        table.saveToFile("D:\\Temp");
    }
}
