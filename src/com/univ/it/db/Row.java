package com.univ.it.db;

import com.univ.it.dbtype.Attribute;

import java.util.ArrayList;

public class Row {
    private ArrayList<Attribute> values;

    public Row(ArrayList<Attribute> values) {
        this.values = values;
    }

    public void set(int ind, Attribute newValue) {
        values.set(ind, newValue);
    }

    public Attribute get(int ind) {
        return values.get(ind);
    }

    public int size() { return values.size(); }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Attribute attribute : values) {
            result.append(attribute.toFile());
            result.append("\t");
        }
        return result.toString();
    }

    public ArrayList<Attribute> getValues() {
        return values;
    }
}
