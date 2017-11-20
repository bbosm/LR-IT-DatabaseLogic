package db;

public class ColumnFactory {

    public static Column createColumn(String s) {
        String columnAttributeType = s.split("\\t")[0];

        if (columnAttributeType.equals("Enum")) {
            return new EnumColumn(s);
        }

        return new PlainColumn(s);
    }

    public static String makeEnumColumnString(String columnName, String columnValues) {
        return "Enum" + "\t" + columnName + "\t" + columnValues;
    }

    public static String makePlainColumnString(String shortTypeName, String columnName) {
        return shortTypeName + "\t" + columnName;
    }
}
