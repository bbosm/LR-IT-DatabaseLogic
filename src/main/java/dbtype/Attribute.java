package dbtype;

public class Attribute {
    public Attribute(@SuppressWarnings("unused") String s) {
    }

    public String toFile() {
        return toString();
    }

    public static String getShortTypeName(String fullTypeName) {
        // 9 == length of ".Attribute"
        return fullTypeName.substring("dbtype.Attribute".length() + 1);
    }

    public static String getFullTypeName(String shortTypeName) {
        return "dbtype.Attribute" + shortTypeName;
    }
}
