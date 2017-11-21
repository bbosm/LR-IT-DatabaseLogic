package db;

import java.io.Serializable;
import java.lang.reflect.Constructor;

public class Column implements Serializable {
    private String attributeShortTypeName;
    private String name;

    public Column(String s) {
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
