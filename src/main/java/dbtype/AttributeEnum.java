package dbtype;

import db.ColumnEnum;

public class AttributeEnum extends Attribute {
    private int index;
    private ColumnEnum ce;

    public AttributeEnum(String s) {
        super(s);
        System.out.println("Wrong usage AttributeEnum, " + s);
    }

    public AttributeEnum(String s, ColumnEnum ce) {
        super(s);
        this.index = Integer.parseInt(s);
        this.ce = ce;
    }

    @Override
    public String toString() {
        return ce.getValues().get(index);
    }

    @Override
    public String toFile() {
        return "" + index;
    }
}
