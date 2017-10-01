package com.mbondarenko.univ;

public interface AttributeAbstractFactory {
    Object createCharIntervalFromString(String s) throws Exception;
    Object createIntegerFromString(String s) throws Exception;
    Object createCharFromString(String s) throws Exception;
    Object createRealFromString(String s) throws Exception;
}
