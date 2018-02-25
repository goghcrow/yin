package org.yinwang.yin.value;


public class FloatValue extends Value {
    public double value;


    public FloatValue(double value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return Double.toString(value);
    }

}
