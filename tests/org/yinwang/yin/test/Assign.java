package org.yinwang.yin.test;


public class Assign extends Testing {

    public static void main(String[] args) {
        // !set
        // 蚕食 Binder.assign

        eval("(define a 1)\n" +
                "(set! a 2)\n" +
                "a");


        eval("(define a [1 2])\n" +
                "(set! a#0 0)\n" +
                "a");


        eval("(define [x y] [1 2])\n" +
                "(set! [x y] [y x])\n" +
                "[x y]");


        // 修改 RecordType value
        eval("(define a {:x 1 :y 2})\n" +
                "(set! a.x 0)\n" +
                "a");


        eval("(define [x y] [1 2])\n" +
                "(set! {:a x :b y} {:a 7 :b 8})\n" +
                "[x y]");


        eval("(define [a b c d] [1 2 3 4])\n" +
                "(set! [a [b c] d] [4 [3 2] 1])\n" +
                "[a b c d]");


        eval("(define [a b] [1 2])\n" +
                "(set! {:x a :y b} {:x 10 :y 20})\n" +
                "[a b]");


        // 递归匹配
        eval("(define [a b c d] [1 2 3 4])\n" +
                "(set! {:x a :y [b c {:z d}]} {:x 10 :y [20 30 {:z 40}]})\n" +
                "[a b c d]");


        eval("(define ok (fun (x) (set! x 10) x))\n" +
                "(define x 8)\n" +
                "[(ok x) x]");
    }


}
