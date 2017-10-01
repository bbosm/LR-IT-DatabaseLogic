package com.mbondarenko.univ;

public class CharInterval {
    private char leftBorder, rightBorder;

    public CharInterval(char leftBorder, char rightBorder) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }

    public CharInterval(String s) throws IllegalArgumentException {
        if (!checkValidity(s)) {
            throw new IllegalArgumentException("Not valid string");
        }
        leftBorder = s.charAt(1);
        rightBorder = s.charAt(3);
    }

    boolean checkValidity(String s) {
        if (s.length() != 5) return false;
        if (s.charAt(0) != '[' || s.charAt(4) != ']' || s.charAt(2) != ':')
            return false;
        char lB = s.charAt(1), rB = s.charAt(3);
        if (lB > rB) return false;
        return true;
    }

    @Override
    public String toString() {
        return "[" + leftBorder + ":" + rightBorder + "]";
    }

    public boolean includes(char c) {
        return leftBorder <= c && c <= rightBorder;
    }
}
