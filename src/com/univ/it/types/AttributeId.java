package com.univ.it.types;

public class AttributeId extends Attribute {
    private int id;

    public AttributeId(int i) {
        super("");
        id = i;
    }

    public AttributeId(String s) {
        super(s);
        id = Integer.parseInt(s);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
