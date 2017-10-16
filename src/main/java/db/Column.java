package db;

import java.lang.reflect.Constructor;

public class Column {
    private String attributeShortTypeName;
    private String name;

    public Column(String s) throws NoSuchMethodException, ClassNotFoundException {
        String[] columnsFields = s.split("\\t");
        this.attributeShortTypeName = columnsFields[0];
        this.name = columnsFields[1];
    }

    @Override
    public String toString() {
        return attributeShortTypeName + "\t" + name;
    }

    public String getName() {
        return name;
    }

    public String getAttributeShortTypeName() {
        return attributeShortTypeName;
    }

    public Constructor getStringConstructor() {
        System.out.println("ERROR wrong column type, string constructor null");
        return null;
    }
}
