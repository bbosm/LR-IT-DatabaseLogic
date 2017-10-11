package db;

import java.util.ArrayList;

public class ColumnEnum extends Column {
    private ArrayList<String> values;

    public ColumnEnum(String name, String className, ArrayList<String> values) throws
            NoSuchMethodException,
            ClassNotFoundException {
        super(name, className);
        this.values = values;
    }

    public ColumnEnum(String s) throws
            NoSuchMethodException,
            ClassNotFoundException {
        super(s);
        this.values = new ArrayList<>();
        String columnsEnumValue = s.split("\\t")[3];
        String[] columnEnumValues = columnsEnumValue.split("\\s+");
        int enumSize = Integer.parseInt(columnEnumValues[0]);
        for (int i = 0; i < enumSize; i++) {
            values.add(columnEnumValues[i + 1]);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(values.size());
        for(String val : values)
            sb.append(" " + val);
        return "ColumnEnum" + '\t' + this.name + '\t' + this.className + '\t' + sb.toString();
    }

    public ArrayList<String> getValues() {
        return values;
    }
}
