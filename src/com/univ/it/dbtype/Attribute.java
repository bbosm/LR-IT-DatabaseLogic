package com.univ.it.dbtype;

import com.univ.it.db.ColumnEnum;

public class Attribute {
    public Attribute(String s) {}

    public void setColumnEnum(ColumnEnum ce) {}

    public boolean matches(String regex) {
        return toString().matches(regex);
    }
}
