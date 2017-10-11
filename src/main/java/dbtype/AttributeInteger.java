package dbtype;

public class AttributeInteger extends Attribute {
    private int val;

    public AttributeInteger(int val) {
        super("");
        this.val = val;
    }

    public AttributeInteger(String s) {
        super(s);
        val = Integer.parseInt(s);
    }

    @Override
    public String toString() {
        return Integer.toString(val);
    }
}
