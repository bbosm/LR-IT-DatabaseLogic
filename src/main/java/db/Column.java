package db;

import java.lang.reflect.Constructor;

public class Column {
    String name;
    String attributeType;

    public Column(String name, String attributeType) throws ClassNotFoundException, NoSuchMethodException {
        this.name = name;
        this.attributeType = attributeType;
    }

    public Column(String s) throws NoSuchMethodException, ClassNotFoundException {
        String[] columnsFields = s.split("\\t");
        this.name = columnsFields[1];
        this.attributeType = columnsFields[2];
    }

    @Override
    public String toString() {
        return name + '\t' + attributeType;
    }

    public String getName() {
        return name;
    }

    public String getAttributeType() {
        return attributeType.substring(attributeType.lastIndexOf(".Attribute") + 10);
    }

    public Constructor getStringConstructor() {
        return null;
    }
}
