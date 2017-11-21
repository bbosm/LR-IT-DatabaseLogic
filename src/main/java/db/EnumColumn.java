package db;

import java.util.ArrayList;

public class EnumColumn extends Column {
    private ArrayList<String> values;

    public EnumColumn(String s) {
        super(s);
        String[] columnsEnumValues = s.split("\\t")[2].split("\\s+");
        this.values = new ArrayList<>(columnsEnumValues.length);
        for (int i = 0; i < columnsEnumValues.length; i++) {
            values.add(columnsEnumValues[i]);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\t" + String.join(" ", values);
    }

    public String getValue(int index) {
        return values.get(index);
    }

    public int getIndex(String value) {
        for (int i = 0; i < values.size(); i++) {
            if (value.equals(values.get(i)))
                return i;
        }
        return -1;
    }
}
