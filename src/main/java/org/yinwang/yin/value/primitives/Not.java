package org.yinwang.yin.value.primitives;


import org.yinwang.yin.$;
import org.yinwang.yin.ast.Node;
import org.yinwang.yin.value.*;

import java.util.List;

public class Not extends PrimFun {

    public Not() {
        super("not", 1);
    }


    @Override
    public Value apply(List<Value> args, Node location) {

        Value v1 = args.get(0);
        if (v1 instanceof BoolValue) {
            return new BoolValue(!((BoolValue) v1).value);
        }
        $.syntaxError(location, "incorrect argument type for not: " + v1);
        return null;
    }

    @Override
    public Value typecheck(List<Value> args, Node location) {
        Value v1 = args.get(0);
        if (v1 instanceof BoolType) {
            return Type.BOOL;
        }
        $.syntaxError(location, "incorrect argument type for not: " + v1);
        return null;
    }
}
