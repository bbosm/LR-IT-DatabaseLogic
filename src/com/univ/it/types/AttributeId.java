package com.univ.it.types;

public class AttributeId extends Attribute {
    private int id;

    public AttributeId(int id) {
        super("");
        this.id = id;
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
