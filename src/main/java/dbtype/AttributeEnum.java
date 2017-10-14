package dbtype;

import db.Column;
import db.EnumColumn;

public class AttributeEnum extends Attribute {
    private int index;
    private EnumColumn enumColumn;

    public AttributeEnum(String s) {
        super(s);
        System.out.println("Wrong usage AttributeEnum, " + s);
    }

    public AttributeEnum(String s, Column column) {
        super(s);
        this.index = Integer.parseInt(s);
        this.enumColumn = (EnumColumn)column;
    }

    @Override
    public String toString() {
        return enumColumn.getValues().get(index);
    }

    @Override
    public String toFile() {
        return "" + index;
    }
}
