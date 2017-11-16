package db;

import dbtype.Attribute;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

public class PlainColumn extends Column {

    private transient Constructor stringConstructor;

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException {
        try {
            stream.defaultReadObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        start();
    }

    public PlainColumn(String s) {
        super(s);
        start();
    }

    private void start() {
        try {
            this.stringConstructor = Class.forName(
                    Attribute.getFullTypeName(getAttributeShortTypeName())).getConstructor(String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Constructor getStringConstructor() {
        return stringConstructor;
    }

}
