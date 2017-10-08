package com.univ.it.types;

public class AttributeInteger extends Attribute {
    private int val;
    public AttributeInteger(String s) {
        super(s);
        val = Integer.parseInt(s);
    }

    @Override
    public String toString() {
        return Integer.toString(val);
    }
}
