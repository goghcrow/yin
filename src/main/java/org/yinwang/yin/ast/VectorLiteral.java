package org.yinwang.yin.ast;

import org.yinwang.yin.Scope;
import org.yinwang.yin.value.Value;
import org.yinwang.yin.value.Vector;

import java.util.List;

public class VectorLiteral extends Node {

    public List<Node> elements;


    public VectorLiteral(List<Node> elements, String file, int start, int end, int line, int col) {
        super(file, start, end, line, col);
        this.elements = elements;
    }


    @Override
    public Value interp(Scope s) {
        return new Vector(interpList(elements, s));
    }


    @Override
    public Value typecheck(Scope s) {
        return new Vector(typecheckList(elements, s));
    }

    @Override
    public String toString() {
        if (elements.isEmpty()) {
            return "[ ]";
        }

        StringBuilder sb = new StringBuilder();

        sb.append('[');
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).toString());
            if (i != 0 && i != elements.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.append(']').toString();
    }
}
