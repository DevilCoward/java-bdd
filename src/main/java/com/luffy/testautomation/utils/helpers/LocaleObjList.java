package com.luffy.testautomation.utils.helpers;

class LocaleObjList {

    private String key;
    private String value;

    public LocaleObjList(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LocaleObjList{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }
}
