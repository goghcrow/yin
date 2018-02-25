package org.yinwang.yin.test;

import org.jetbrains.annotations.NotNull;
import org.yinwang.yin.Scope;
import org.yinwang.yin.ast.Node;
import org.yinwang.yin.parser.Parser;
import org.yinwang.yin.value.PrimFun;
import org.yinwang.yin.value.Value;

import java.util.List;

public class Testing {

    public static Scope getInitScope() {
        Scope env = Scope.buildInitScope();
        env.putValue("println", new PrimFun("println", -1) {
            @Override
            public Value apply(List<Value> args, Node location) {
                // System.out.println(args.get(0));
                for (Value arg : args) {
                    System.out.println(arg);
                }
                return Value.VOID;
            }

            @Override
            public Value typecheck(List<Value> args, Node location) {
//                if (args.size() != 1) {
//                    $.syntaxError(location, "println accept only one arg");
//                }
                return null;
            }
        });
        return env;
    }


    public static Value eval(@NotNull String code) {
        Scope env = getInitScope();
        Node program = Parser.parse("Test Code", code);
        Value val = program.interp(env);

        System.out.println(code + "\n==> " + val + "\n\n");
        return val;
    }

    public static void main(String[] args) {
        // "begin with only one statement"
        eval("1");

        // "function with no arguments"
        eval("(fun () 1)");

        // "single positional arg"
        eval("(define f (fun (x) x))" +
                "(f 1)");

        // "default value without actual arg"
        eval("(define f (fun ([x Int :default 1]) x))" +
                "(f)");

        // "default argument in expression"
        eval("(define f (fun ([x Any :default ((fun () [1 2 3]))]) x))" +
                "(f)");

        // "function with multiple positional args"
        eval("(define f (fun (x y) [x y]))" +
                "(f 3 5)");

        // "mutural recursion (even 9 = false)"
        eval("(define even (fun (x) (if (= x 0) true (odd (- x 1)))))" +
                "(define odd (fun (x) (if (= x 0) false (even (- x 1)))))" +
                "(even 9)");
    }
}
