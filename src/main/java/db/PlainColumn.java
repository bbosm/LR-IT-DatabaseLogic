package db;

import dbtype.Attribute;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

public class PlainColumn extends Column {

    private transient Constructor stringConstructor;

    private void writeObject(ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException, NoSuchMethodException {
        stream.defaultReadObject();
        start();
    }

    public PlainColumn(String s) throws
            NoSuchMethodException,
            ClassNotFoundException {
        super(s);
        start();
    }

    private void start() throws ClassNotFoundException, NoSuchMethodException {
        this.stringConstructor = Class.forName(
                Attribute.getFullTypeName(getAttributeShortTypeName())).getConstructor(String.class);
    }

    @Override
    public Constructor getStringConstructor() {
        return stringConstructor;
    }

}
