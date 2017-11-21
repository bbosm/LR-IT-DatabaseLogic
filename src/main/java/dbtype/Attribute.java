package dbtype;

import java.io.Serializable;

public class Attribute implements Serializable {
    private static final String packageTemplate = "dbtype.Attribute";

    public Attribute(@SuppressWarnings("unused") String s) {
    }

    public String toFile() {
        return toString();
    }

    public static String getShortTypeName(String fullTypeName) {
        // 9 == length of ".Attribute"
        return fullTypeName.substring(packageTemplate.length() + 1);
    }

    public static String getFullTypeName(String shortTypeName) {
        return packageTemplate + shortTypeName;
    }
}
