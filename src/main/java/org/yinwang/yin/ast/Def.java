package org.yinwang.yin.ast;

import org.yinwang.yin.Binder;
import org.yinwang.yin.Constants;
import org.yinwang.yin.Scope;
import org.yinwang.yin.value.Value;

public class Def extends Node {
    public Node pattern;
    public Node value;


    public Def(Node pattern, Node value, String file, int start, int end, int line, int col) {
        super(file, start, end, line, col);
        this.pattern = pattern;
        this.value = value;
    }


    @Override
    public Value interp(Scope s) {
        Value valueValue = value.interp(s);
        // 在当前环境中查重 并加入当前环境
        Binder.checkDup(pattern);
        Binder.define(pattern, valueValue, s);
        // define 无返回值
        return Value.VOID;
    }


    @Override
    public Value typecheck(Scope s) {
        Value t = value.typecheck(s);
        Binder.checkDup(pattern);
        Binder.define(pattern, t, s);
        return Value.VOID;
    }


    @Override
    public String toString() {
        return "(" + Constants.DEF_KEYWORD + " " + pattern + " " + value + ")";
    }

}
