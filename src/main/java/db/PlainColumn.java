package db;

import dbtype.Attribute;

import java.lang.reflect.Constructor;

public class PlainColumn extends Column {

    private Constructor stringConstructor;

    public PlainColumn(String s) throws
            NoSuchMethodException,
            ClassNotFoundException {
        super(s);
        this.stringConstructor = Class.forName(
                Attribute.getFullTypeName(getAttributeShortTypeName())).getConstructor(String.class);
    }

    @Override
    public Constructor getStringConstructor() {
        return stringConstructor;
    }

}
