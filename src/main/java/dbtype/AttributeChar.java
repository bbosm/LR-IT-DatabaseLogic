package dbtype;

public class AttributeChar extends Attribute {
    private char val;

    public AttributeChar(char val) {
        super("");
        this.val = val;
    }

    public AttributeChar(String s) {
        super(s);
        val = s.charAt(0);
    }

    @Override
    public String toString() {
        return Character.toString(val);
    }
}
