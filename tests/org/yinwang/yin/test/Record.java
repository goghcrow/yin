package org.yinwang.yin.test;

public class Record extends Testing {
    public static void main(String[] args) {
        // record ?? 多继承结构体 ?

        // record 字面量, 匿名 record
        eval("(define a {:x 1 :y 2}) a");

        eval("(record [])");
    }
}
