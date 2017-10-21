package db;

import dbtype.Attribute;

import java.io.Serializable;
import java.util.ArrayList;

public class Row implements Serializable {
    private ArrayList<Attribute> values;

    public Row(ArrayList<Attribute> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Attribute attribute : values) {
            result.append(attribute.toFile());
            result.append("\t");
        }
        return result.toString();
    }

    public void set(int ind, Attribute newValue) {
        values.set(ind, newValue);
    }

    public Attribute get(int ind) {
        return values.get(ind);
    }

    public ArrayList<Attribute> getValues() {
        return values;
    }
}
