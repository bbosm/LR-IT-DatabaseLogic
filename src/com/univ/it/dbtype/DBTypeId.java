package com.univ.it.dbtype;

public class DBTypeId {
    private int id;

    public DBTypeId(int id) {
        this.id = id;
    }

    public DBTypeId(String s) {
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
