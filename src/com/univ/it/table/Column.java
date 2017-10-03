package com.univ.it.table;

import java.lang.reflect.Constructor;

public class Column {
    private String className;
    private Class attributeType;
    private Constructor stringConstructor;

    public Column(String className) throws ClassNotFoundException, NoSuchMethodException {
        this.className = className;
        this.attributeType = Class.forName(className);
        this.stringConstructor = attributeType.getConstructor(String.class);
    }

    @Override
    public String toString() {
        return className;
    }

    public String getClassName() { return className; }

    public Class getAttributeType() { return attributeType; }

    public Constructor getStringConstructor() { return stringConstructor; }
}
