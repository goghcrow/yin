package org.yinwang.yin.test;

public class Define extends Testing {
    public static void main(String[] args) {
        // 参见 Binder.bind
        // (define Name  Value)
        // (define RecordLiteral RecordType)
        // (define VectorLiteral VectorValue)

        eval("(define a 1)\n" +
                "a");


        eval("(define [x y [z]] [1 2 [3]])\n" +
                "[x y z]");


        eval("(define [u1 [v1 v2] u2] [2 [3 5] 7])\n" +
                "[u1 u2 v1 v2]");


        eval("(define arr [1 2 3])\n" +
                "(define [t1 t2 t3] arr)\n" +
                "[t1 t2 t3]");


        eval("(define\n" +
                "  {:x a :y b}\n" +
                "  {:x 1 :y 2})\n" +
                "[a b]");


        eval("(define\n" +
                "  {:x a :y b}\n" +
                "  {:y 1 :x 2})\n" +
                "[a b]");


        // 实现为递归匹配
        eval("(define\n" +
                "  {\n" +
                "    :a xx\n" +
                "    :b [uu vv]\n" +
                "  }\n" +
                "  {\n" +
                "   :b [8 9]\n" +
                "   :a 7\n" +
                "  }\n" +
                ")\n" +
                "[xx uu vv]");



        eval("(if (< 3 2) 10 (seq (define notthere 24) (+ 1 notthere)))");

        eval("(seq (define x 19) x)");


        // fixme
//        eval("(define (record A :a a1 :b b1) (record A :a 10 :b 20))" +
//                "[a1 b1]");

//        eval("(record A :a (Int 1) :b Float)\n" +
//                "(A)");

//        eval("(define a:Int 1)\n" +
//                "a");
    }
}
