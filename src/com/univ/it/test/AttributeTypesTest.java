package com.univ.it.test;

import com.univ.it.table.Column;
import com.univ.it.table.Table;
import com.univ.it.types.*;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;

import static jdk.nashorn.internal.runtime.PrototypeObject.getConstructor;

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
        columns.add(new Column("com.univ.it.types.AttributeDate"));
        columns.add(new Column("com.univ.it.types.AttributeInteger"));

        Table table = new Table("testTable", columns);
        table.saveToFile("D:\\Temp");
    }
}
