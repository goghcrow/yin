package org.yinwang.yin.test;

public class Subscript extends Testing {
    public static void main(String[] args) {
        eval("(define a [1 2])\n" +
                "(set! a#0 10)\n" +
                "(set! a#1 20)\n" +
                "a");

        eval("(define a [1 2])\n" +
                "(define idx 1)\n" +
                "(set! a#idx 20)\n" +
                "a");

        eval("(define a [1 2])\n" +
                "(set! a#(- 1 1) 10)\n" +
                "(set! a#(+ 0 1) 20)\n" +
                "a");

        eval("(define a [[1 2] [3 4]])\n" +
                "a#1#0");

        // fixme
//        eval("(define v1 [1 2 3 4 5 6])\n" +
//                "(define v2 [1 2 3 4 5 6])\n" +
//                "v1#(v2#1)\n");
    }
}
