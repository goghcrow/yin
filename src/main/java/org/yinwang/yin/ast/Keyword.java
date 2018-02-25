package org.yinwang.yin.ast;


import org.yinwang.yin.$;
import org.yinwang.yin.Scope;
import org.yinwang.yin.value.Value;


/**
 * :\\w.* 匹配正则
 */
public class Keyword extends Node {
    public String id;


    public Keyword(String id, String file, int start, int end, int line, int col) {
        super(file, start, end, line, col);
        this.id = id;
    }


    public Name asName() {
        return new Name(id, file, start, end, line, col);
    }


    @Override
    public Value interp(Scope s) {
        $.syntaxError(this, "keyword used as value");
        return null;
    }


    @Override
    public Value typecheck(Scope s) {
        $.syntaxError(this, "keyword used as value");
        return null;
    }


    @Override
    public String toString() {
        return ":" + id;
    }
}
