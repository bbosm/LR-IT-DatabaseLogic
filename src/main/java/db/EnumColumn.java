package db;

import java.util.ArrayList;

public class EnumColumn extends Column {
    private ArrayList<String> values;

    public EnumColumn(String name, String className, ArrayList<String> values) throws
            NoSuchMethodException,
            ClassNotFoundException {
        super(name, className);
        this.values = values;
    }

    public EnumColumn(String s) throws
            NoSuchMethodException,
            ClassNotFoundException {
        super(s);
        this.values = new ArrayList<>();
        String columnsEnumValue = s.split("\\t")[3];
        String[] EnumColumnValues = columnsEnumValue.split("\\s+");
        int enumSize = Integer.parseInt(EnumColumnValues[0]);
        for (int i = 0; i < enumSize; i++) {
            values.add(EnumColumnValues[i + 1]);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(values.size());
        for(String val : values)
            sb.append(" " + val);
        return "EnumColumn" + '\t' + super.toString() + '\t' + sb.toString();
    }

    public ArrayList<String> getValues() {
        return values;
    }
}
