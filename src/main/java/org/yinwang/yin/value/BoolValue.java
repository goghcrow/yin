package org.yinwang.yin.value;


public class BoolValue extends Value {
    public boolean value;


    public BoolValue(boolean value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value ? "true" : "false";
    }

}
