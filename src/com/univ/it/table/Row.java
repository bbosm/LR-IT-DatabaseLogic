package com.univ.it.table;

import com.univ.it.types.Attribute;
import com.univ.it.types.AttributeId;

import java.util.ArrayList;

public class Row {
    private ArrayList<Attribute> values;
    private AttributeId id;

    public Row(int id, int columns) {
        this.id = new AttributeId(id);
        values = new ArrayList<>(columns);
    }

    public void set(int ind, Attribute newValue) {
        values.set(ind, newValue);
    }

    public String get(int ind) {
        return values.get(ind).toString();
    }

    public int size() { return values.size(); }

    public int getId() { return id.getId(); }

    public void setId(int id) { this.id = new AttributeId(id); }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(id);
        result.append("\t");

        for (Attribute attribute : values) {
            result.append(attribute.toString());
            result.append("\t");
        }
        return result.toString();
    }
}
