package com.univ.it.dbtype;

import com.univ.it.db.ColumnEnum;

public class AttributeEnum extends Attribute {
    private int val;
    private ColumnEnum ce;

    public AttributeEnum(int val) {
        super("");
        this.val = val;
    }

    public AttributeEnum(String s) {
        super(s);
        val = Integer.parseInt(s);
    }

    @Override
    public void setColumnEnum(ColumnEnum ce) {
        this.ce = ce;
    }

    @Override
    public String toString() {
        return ce.getValues().get(val);
    }
}
