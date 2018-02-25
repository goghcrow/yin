package org.yinwang.yin.ast;


import org.yinwang.yin.$;
import org.yinwang.yin.Scope;
import org.yinwang.yin.value.Value;

/**
 * symbol
 * name.id 作为 scope 的key
 * name.Value要lookup scope获取
 */
public class Name extends Node {
    public String id;


    public Name(String id, String file, int start, int end, int line, int col) {
        super(file, start, end, line, col);
        this.id = id;
    }


    @Override
    public Value interp(Scope s) {
        return s.lookup(id);
    }


    @Override
    public Value typecheck(Scope s) {
        Value v = s.lookup(id);
        if (v != null) {
            return v;
        } else {
            $.syntaxError(this, "unbound variable: " + id);
            return Value.VOID;
        }
    }


    @Override
    public String toString() {
        return id;
    }
}
