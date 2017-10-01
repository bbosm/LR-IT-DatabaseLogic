package com.univ.it.types;

public class AttributeChar extends Attribute {
    private char val;
    public AttributeChar(String s) {
        super(s);
        val = s.charAt(0);
    }

    @Override
    public String toString() {
        return Character.toString(val);
    }
}
