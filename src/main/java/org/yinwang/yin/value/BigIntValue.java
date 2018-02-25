package org.yinwang.yin.value;


import java.math.BigInteger;

public class BigIntValue extends Value {
    public BigInteger value;


    public BigIntValue(BigInteger value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value.toString();
    }

}
