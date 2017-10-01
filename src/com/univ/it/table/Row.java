package com.univ.it.table;

import com.univ.it.types.Attribute;

import java.util.ArrayList;

public class Row {
    private ArrayList<Attribute> values;

    public Row() {
        values = new ArrayList<>(

        );
    }

    public Row(int n) {
        values = new ArrayList<>(n);
    }

    public void replaceAt(int ind, Attribute newValue) {
        values.set(ind, newValue);
    }

    public String getAt(int ind) {
        return values.get(ind).toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Attribute attribute : values) {
            result.append(attribute.toString());
            result.append("\t");
        }
        return result.toString();
    }
}
