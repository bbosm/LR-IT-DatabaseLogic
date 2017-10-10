package com.univ.it.test;

import com.univ.it.dbtype.*;
import org.junit.Test;

import java.text.ParseException;
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
}
