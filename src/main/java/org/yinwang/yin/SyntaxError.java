package org.yinwang.yin;

import org.yinwang.yin.ast.Node;

@SuppressWarnings("WeakerAccess")
public class SyntaxError extends GeneralError {
    public SyntaxError(Node location, String msg) {
        super(location, msg);
    }

    public SyntaxError(String msg) {
        super(msg);
    }
}
