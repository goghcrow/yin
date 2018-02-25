package org.yinwang.yin.ast;

import org.yinwang.yin.Scope;
import org.yinwang.yin.value.Value;

import java.util.ArrayList;
import java.util.List;


/**
 * 1. seq 关键字 或者 程序本身
 * 2. 顺序执行statement，返回最后一项结果
 */
public class Block extends Node {
    public List<Node> statements = new ArrayList<>();


    public Block(List<Node> statements, String file, int start, int end, int line, int col) {
        super(file, start, end, line, col);
        this.statements = statements;
    }


    @Override
    public Value interp(Scope s) {
        if (statements.isEmpty()) {
            return Value.VOID;
        }

        // Block 通过parent Scope访问upValue, 新建scope， 保存解析过程中新加入属性
        s = new Scope(s);
        // 遍历解析，每一次解析都可能往scope添加属性
        // 下一次解析可以使用之前加入的属性
        // block 返回最后一项解析结果
        for (int i = 0; i < statements.size() - 1; i++) {
            statements.get(i).interp(s);
        }
        return statements.get(statements.size() - 1).interp(s);
    }


    @Override
    public Value typecheck(Scope s) {
        s = new Scope(s);
        for (int i = 0; i < statements.size() - 1; i++) {
            statements.get(i).typecheck(s);
        }
        return statements.get(statements.size() - 1).typecheck(s);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String sep = statements.size() > 5 ? "\n" : " ";
        sb.append("(seq" + sep);

        for (int i = 0; i < statements.size(); i++) {
            sb.append(statements.get(i).toString());
            if (i != statements.size() - 1) {
                sb.append(sep);
            }
        }

        sb.append(")");
        return sb.toString();
    }
}
