package com.mbondarenko.univ.test;

import com.mbondarenko.univ.AttributeAbstractFactory;
import com.mbondarenko.univ.AttributeFactory;
import com.mbondarenko.univ.CharInterval;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AttributeTypesTest {
    private AttributeAbstractFactory factory = new AttributeFactory();
    @Test
    public void testCharInterval() {
        try {
            Object charInterval = factory.createCharIntervalFromString("");
            assertEquals(0, 1);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Not valid string");
        }

        try {
            Object charInterval = factory.createCharIntervalFromString("[][][");
            assertEquals(0, 1);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Not valid string");
        }

        try {
            Object charInterval = factory.createCharIntervalFromString("[c:a]");
            assertEquals(0, 1);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Not valid string");
        }

        try {
            CharInterval charInterval = (CharInterval)factory.createCharIntervalFromString("[a:c]");
            assertEquals(true, charInterval.includes('a'));
            assertEquals(true, charInterval.includes('b'));
            assertEquals(true, charInterval.includes('c'));
            assertEquals(false, charInterval.includes('d'));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
