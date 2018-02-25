package org.yinwang.yin.test;

public class Attr extends Testing {
    public static void main(String[] args) {
        eval("(define r {:x 1 :y 2})\n" +
                "(r :x 0 :y 0)");


        eval("(define aa [1 2 3])\n" +
                "(set! aa#0 10)\n" +
                "(define bb {:x 1 :y 2})\n" +
                "(set! bb.x 10)\n" +
                "[aa bb]");


// fixme
//        eval("(record A [id Int :k1 v1 :k2 v2])\n");
//
//        eval("(record A :x 1 :y 2)\n" +
//                "(record B :u 12 :v 2)\n" +
//                "(define b (B :v 20))\n" +
//                "(define a (A :x b :y 4))\n" +
//                        "a" //+
////                "a.x.u\n" +
////                "\n" +
////                "(record C (A B) :t 42)\n" +
////                "(define c (C))\n" +
////                "c\n"
//);
    }
}
