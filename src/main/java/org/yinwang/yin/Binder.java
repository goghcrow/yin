package org.yinwang.yin;


import org.yinwang.yin.ast.*;
import org.yinwang.yin.value.RecordType;
import org.yinwang.yin.value.Value;
import org.yinwang.yin.value.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 变量绑定值
 */
public class Binder {

    /**
     * (define Name  Value)
     * (define RecordLiteral RecordType)
     * (define VectorLiteral VectorValue)
     *
     * define 是递归进行的， 决定了define的pattern与value也可以是递归的结构, 比如
     * (define [a [b c] d] [1 [2 3] 4])
     */
    public static void define(Node pattern, Value value, Scope env) {
        if (pattern instanceof Name) {
            String id = ((Name) pattern).id;
            // 本地作用域不允许重复定义，但可以覆盖父级作用域`变量`
            Value v = env.lookupLocal(id);
            if (v != null) {
                $.syntaxError(pattern, "trying to redefine name: " + id);
            } else {
                env.putValue(id, value);
            }
        } else if (pattern instanceof RecordLiteral) {
            // recordType ??
            if (value instanceof RecordType) {
                Map<String, Node> elms1 = ((RecordLiteral) pattern).map;
                Scope elms2 = ((RecordType) value).properties;
                if (elms1.keySet().equals(elms2.keySet())) {
                    for (String k1 : elms1.keySet()) {
                        define(elms1.get(k1), elms2.lookupLocal(k1), env);
                    }
                } else {
                    $.syntaxError(pattern, "define with records of different attributes: " +
                            elms1.keySet() + " v.s. " + elms2.keySet());
                }
            } else {
                $.syntaxError(pattern, "define with incompatible types: record and " + value);
            }
        } else if (pattern instanceof VectorLiteral) {
            if (value instanceof Vector) {
                List<Node> elms1 = ((VectorLiteral) pattern).elements;
                List<Value> elms2 = ((Vector) value).values;
                if (elms1.size() == elms2.size()) {
                    for (int i = 0; i < elms1.size(); i++) {
                        define(elms1.get(i), elms2.get(i), env);
                    }
                } else {
                    $.syntaxError(pattern,
                            "define with vectors of different sizes: " + elms1.size() + " v.s. " + elms2.size());
                }
            } else {
                $.syntaxError(pattern, "define with incompatible types: vector and " + value);
            }
        } else {
            $.syntaxError(pattern, "unsupported pattern of define: " + pattern);
        }
    }


    public static void assign(Node pattern, Value value, Scope env) {
        if (pattern instanceof Name) {
            String id = ((Name) pattern).id;
            Scope d = env.findDefiningScope(id);

            if (d == null) {
                $.syntaxError(pattern, "assigned name was not defined: " + id);
            } else {
                d.putValue(id, value);
            }
        } else if (pattern instanceof Subscript) {
            ((Subscript) pattern).set(value, env);
        } else if (pattern instanceof Attr) {
            ((Attr) pattern).set(value, env);
        } else if (pattern instanceof RecordLiteral) {
            if (value instanceof RecordType) {
                Map<String, Node> elms1 = ((RecordLiteral) pattern).map;
                Scope elms2 = ((RecordType) value).properties;
                if (elms1.keySet().equals(elms2.keySet())) {
                    for (String k1 : elms1.keySet()) {
                        assign(elms1.get(k1), elms2.lookupLocal(k1), env);
                    }
                } else {
                    $.syntaxError(pattern, "assign with records of different attributes: " +
                            elms1.keySet() + " v.s. " + elms2.keySet());
                }
            } else {
                $.syntaxError(pattern, "assign with incompatible types: record and " + value);
            }
        } else if (pattern instanceof VectorLiteral) {
            if (value instanceof Vector) {
                List<Node> elms1 = ((VectorLiteral) pattern).elements;
                List<Value> elms2 = ((Vector) value).values;
                if (elms1.size() == elms2.size()) {
                    for (int i = 0; i < elms1.size(); i++) {
                        assign(elms1.get(i), elms2.get(i), env);
                    }
                } else {
                    $.syntaxError(pattern, "assign vectors of different sizes: " + elms1.size() + " v.s. " + elms2.size());
                }
            } else {
                $.syntaxError(pattern, "assign incompatible types: vector and " + value);
            }
        } else {
            $.syntaxError(pattern, "unsupported pattern of assign: " + pattern);
        }
    }


    public static void checkDup(Node pattern) {
        checkDup1(pattern, new HashSet<>());
    }


    public static void checkDup1(Node pattern, Set<String> seen) {

        if (pattern instanceof Name) {
            String id = ((Name) pattern).id;
            if (seen.contains(id)) {
                $.syntaxError(pattern, "duplicated name found in pattern: " + pattern);
            } else {
                seen.add(id);
            }
        } else if (pattern instanceof RecordLiteral) {
            for (Node v : ((RecordLiteral) pattern).map.values()) {
                checkDup1(v, seen);
            }
        } else if (pattern instanceof VectorLiteral) {
            for (Node v : ((VectorLiteral) pattern).elements) {
                checkDup1(v, seen);
            }
        }
    }

}
