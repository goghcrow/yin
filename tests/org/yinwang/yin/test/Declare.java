package org.yinwang.yin.test;

public class Declare extends Testing {
    public static void main(String[] args) {
        eval("(declare (: x Int))");

        eval("-- factorial\n" +
                "(define fact\n" +
                "  (fun (x)\n" +
                "    (declare (: x Int))                  -- declare x to be an integer\n" +
                "    (if (= x 0) 1 (* x (fact (- x 1))))))\n" +
                "\n" +
                "(fact 5)\n");
    }
}
