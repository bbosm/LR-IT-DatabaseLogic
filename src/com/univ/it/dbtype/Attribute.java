package com.univ.it.dbtype;

public class Attribute {
    public Attribute(String s) {}

    public boolean matches(String regex) {
        return toString().matches(regex);
    }
}
