package db;

import java.lang.reflect.Constructor;

public class Column {
    String name;
    String attributeTypeName;

    public Column(String name, String attributeTypeName) throws ClassNotFoundException, NoSuchMethodException {
        this.name = name;
        this.attributeTypeName = attributeTypeName;
    }

    public Column(String s) throws NoSuchMethodException, ClassNotFoundException {
        String[] columnsFields = s.split("\\t");
        this.name = columnsFields[1];
        this.attributeTypeName = columnsFields[2];
    }

    @Override
    public String toString() {
        return name + '\t' + attributeTypeName;
    }

    public String getName() {
        return name;
    }

    public String getAttributeTypeName() {
        return attributeTypeName.substring(attributeTypeName.lastIndexOf(".Attribute") + 10);
    }

    public Constructor getStringConstructor() {
        return null;
    }
}
