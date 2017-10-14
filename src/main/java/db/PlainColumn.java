package db;

import java.lang.reflect.Constructor;

public class PlainColumn extends Column {

    private Constructor stringConstructor;

    public PlainColumn(String name, String className) throws
            NoSuchMethodException,
            ClassNotFoundException {
        super(name, className);
        this.stringConstructor = Class.forName(className).getConstructor(String.class);
    }

    public PlainColumn(String s) throws
            NoSuchMethodException,
            ClassNotFoundException {
        super(s);
        this.stringConstructor = Class.forName(attributeTypeName).getConstructor(String.class);
    }

    @Override
    public String toString() {
        return "PlainColumn" + '\t' + super.toString();
    }

    @Override
    public Constructor getStringConstructor() {
        return stringConstructor;
    }

}
