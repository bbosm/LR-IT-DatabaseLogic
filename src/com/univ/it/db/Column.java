package com.univ.it.db;

import java.lang.reflect.Constructor;

public class Column {
    private String name;
    private String className;
    private String typeName;
    private Class attributeType;
    private Constructor stringConstructor;

    public Column(String name, String className) throws ClassNotFoundException, NoSuchMethodException {
        this.name = name;
        this.className = className;
        this.typeName = className.substring(className.lastIndexOf('.') + 1);
        this.attributeType = Class.forName(className);
        this.stringConstructor = attributeType.getConstructor(String.class);
    }

    @Override
    public String toString() {
        return name + '\t' + className;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() { return typeName; }

    public Class getAttributeType() { return attributeType; }

    public Constructor getStringConstructor() { return stringConstructor; }
}
