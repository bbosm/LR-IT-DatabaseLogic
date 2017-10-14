package db;

public class ColumnFactory {

    public static Column createColumn(String s) throws NoSuchMethodException, ClassNotFoundException {
        String columnType = s.split("\\t")[0];

        if (columnType.equals("EnumColumn")) {
            return new EnumColumn(s);
        }

        return new PlainColumn(s);
    }

    public static String makeEnumColumnString(String s) {
        return "EnumColumn" + "\t" + s;
    }

    public static String makePlainColumnString(String s) {
        return "PlainColumn" + "\t" + s;
    }
}
