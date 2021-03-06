package org.yinwang.yin.ast;

import org.yinwang.yin.Scope;
import org.yinwang.yin.value.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * 词法分析阶段数据结构
 */
public class Tuple extends Node {
    public List<Node> elements = new ArrayList<>();
    public Node open;
    public Node close;


    public Tuple(List<Node> elements, Node open, Node close, String file, int start, int end, int line, int col) {
        super(file, start, end, line, col);
        this.elements = elements;
        this.open = open;
        this.close = close;
    }


    public Node getHead() {
        if (elements.isEmpty()) {
            return null;
        } else {
            return elements.get(0);
        }
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
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).toString());
            if (i != elements.size() - 1) {
                sb.append(" ");
            }
        }

        return (open == null ? "" : open) + sb.toString() + (close == null ? "" : close);
    }
}
