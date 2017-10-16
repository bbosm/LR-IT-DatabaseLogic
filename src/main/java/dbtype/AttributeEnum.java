package dbtype;

import db.Column;
import db.EnumColumn;

public class AttributeEnum extends Attribute {
    private int index;
    private EnumColumn enumColumn;

    @Deprecated
    public AttributeEnum(String s) {
        super(s);
        System.out.println("Wrong usage AttributeEnum, " + s);
    }

    public AttributeEnum(String s, Column column) {
        super(s);
        try {
            this.index = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            this.index = ((EnumColumn)column).getIndex(s);
        }
        this.enumColumn = (EnumColumn)column;
    }

    @Override
    public String toString() {
        return enumColumn.getValue(index);
    }

    @Override
    public String toFile() {
        return "" + index;
    }
}
