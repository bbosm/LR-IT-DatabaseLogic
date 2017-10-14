package db;

import java.lang.reflect.Constructor;

public class PlainColumn extends Column {

    private Constructor stringConstructor;

    public PlainColumn(String name, String className) throws ClassNotFoundException, NoSuchMethodException {
        super(name, className);
        this.stringConstructor = Class.forName(className).getConstructor(String.class);
    }

    public PlainColumn(String s) throws NoSuchMethodException, ClassNotFoundException {
        super(s);
        this.stringConstructor = Class.forName(attributeType).getConstructor(String.class);
    }

    @Override
    public String toString() {
        return "PlainColumn" + '\t' + super.toString();
    }

    public String getName() {
        return name;
    }

    public String getAttributeType() {
        return attributeType.substring(attributeType.lastIndexOf(".Attribute") + 10);
    }

    @Override
    public Constructor getStringConstructor() {
        return stringConstructor;
    }

}
