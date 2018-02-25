package org.yinwang.yin;


import org.jetbrains.annotations.NotNull;
import org.yinwang.yin.ast.Node;
import org.yinwang.yin.parser.Parser;
import org.yinwang.yin.value.Value;

public class Interpreter {

    public Value interp(@NotNull String file, @NotNull String text) {
        Node program = Parser.parse(file, text);
        return program.interp(Scope.buildInitScope());
    }


    public Value interp(@NotNull String file) {
        Node program = Parser.parse(file);
        return program.interp(Scope.buildInitScope());
    }


    public static void main(String[] args) {
        Interpreter i = new Interpreter();
        $.msg(i.interp(args[0]).toString());
    }

}
