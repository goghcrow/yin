package org.yinwang.yin.ast;


import org.yinwang.yin.Scope;
import org.yinwang.yin.value.Value;


/**
 * parser 分隔符 () [] {} .
 */
public class Delimiter extends Node {
    public String shape;


    public Delimiter(String shape, String file, int start, int end, int line, int col) {
        super(file, start, end, line, col);
        this.shape = shape;
    }


    @Override
    public Value interp(Scope s) {
        return null;
    }


    @Override
    public Value typecheck(Scope s) {
        return null;
    }


    @Override
    public String toString() {
        return shape;
    }
}

