package com.univ.it.db;

import com.univ.it.dbtype.Attribute;
import com.univ.it.dbtype.DBTypeId;

import java.util.ArrayList;

public class Row {
    private DBTypeId id;
    private ArrayList<Attribute> values;

    public Row() {
        values = new ArrayList<>();
    }

    public Row(DBTypeId id, ArrayList<Attribute> values) {
        this.id = id;
        this.values = values;
    }

    public void pushBack(Attribute a) {
        values.add(a);
    }

    public void set(int ind, Attribute newValue) {
        values.set(ind, newValue);
    }

    public Attribute getAt(int ind) {
        return values.get(ind);
    }

    public void replaceAt(int ind, Attribute newValue) {
        values.set(ind, newValue);
    }

    public String get(int ind) {
        return values.get(ind).toString();
    }

    public int size() { return values.size(); }

    public int getId() { return id.getId(); }

    public void setId(int id) { this.id = new DBTypeId(id); }

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
