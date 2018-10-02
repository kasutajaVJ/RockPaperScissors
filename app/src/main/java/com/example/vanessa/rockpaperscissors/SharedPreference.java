package com.example.vanessa.rockpaperscissors;

public enum SharedPreference {
    TAG("userInfo"),
    TAG_NAME("userName");

    private String stringValue;

    SharedPreference(String toString) {
        stringValue = toString;
    }

    @Override
    public String toString() {
            return stringValue;
    }
}
