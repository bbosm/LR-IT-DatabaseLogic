package com.univ.it.db;

import java.lang.reflect.Constructor;

import com.univ.it.dbtype.Attribute;

public class Column {
    private String name;
    private String className;
    private String typeName;
    private Class attributeType;
    private Constructor attributeConstructor;

    public Column(String name, String className) throws ClassNotFoundException, NoSuchMethodException {
        this.name = name;
        this.className = className;
        this.typeName = className.substring(className.lastIndexOf('.') + 1);
        this.attributeType = Class.forName(className);
        this.attributeConstructor = attributeType.getConstructor(String.class);
    }

    public Column(Class c) {
        attributeType = c;
        String s = attributeType.getCanonicalName();
        name = s.substring(s.lastIndexOf(".") + 1);
        try {
            attributeConstructor = attributeType.getConstructor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Column(String s) {
        try {
            name = s.substring(s.lastIndexOf(".") + 1);
            attributeType = Class.forName(s);
            attributeConstructor = findStringConstructor(attributeType);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Attribute createAttribute(String attrString) throws Exception {
        return (Attribute) attributeConstructor.newInstance(attrString);
    }

    private Constructor findStringConstructor(Class c) throws NoSuchMethodException {
        return c.getConstructor(String.class);
    }

    @Override
    public String toString() {
        return name + '\t' + className;
    }

    public String getTypeName() { return typeName; }

    public String getName() {
        return name;
    }

    public Class getAttributeType() { return attributeType; }

    public Constructor getStringConstructor() { return attributeConstructor; }
}
