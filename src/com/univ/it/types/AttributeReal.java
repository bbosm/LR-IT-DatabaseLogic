package com.univ.it.types;

public class AttributeReal extends Attribute {
    private double val;

    public AttributeReal(double val) {
        super("");
        this.val = val;
    }

    public AttributeReal(String s) {
        super(s);
        val = Double.parseDouble(s);
    }

    @Override
    public String toString() {
        return Double.toString(val);
    }
}
