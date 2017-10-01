package com.mbondarenko.univ;

public class AttributeFactory implements AttributeAbstractFactory {
    @Override
    public Object createCharIntervalFromString(String s) throws Exception {
        return new CharInterval(s);
    }

    @Override
    public Object createIntegerFromString(String s) throws Exception {
        return Integer.parseInt(s);
    }

    @Override
    public Object createCharFromString(String s) throws Exception {
        if (s.length() != 1) throw new IllegalArgumentException("Invalid char");
        return s.charAt(0);
    }

    @Override
    public Object createRealFromString(String s) throws Exception {
        return Double.parseDouble(s);
    }
}
