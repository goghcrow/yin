package org.yinwang.yin.parser;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yinwang.yin.$;
import org.yinwang.yin.Constants;
import org.yinwang.yin.ast.*;

import java.util.*;


/**
 * first phase parser
 * parse into S-expression like format
 *
 *
 * (), [], {}, --, string, int, float, keyword， 其余全是 name
 */
@SuppressWarnings("WeakerAccess")
public class PreParser {

    public String file;
    public String text;

    /**
     * current offset indicators
     */
    public int offset;
    public int line;
    public int col;

    /**
     * all delimiters
     */
    public final Set<String> delimiters = new HashSet<>();
    /**
     * map open delimiters to their matched closing ones
     */
    public final Map<String, String> delimiterMap = new HashMap<>();


    public PreParser(@NotNull String file) {
        this($.unifyPath(file), $.readFile(file));
    }


    public PreParser(@NotNull String file, @NotNull String text) {
        this.file = file;
        this.text = text;
        this.offset = 0;
        this.line = 0;
        this.col = 0;

        addDelimiterPair(Constants.TUPLE_BEGIN, Constants.TUPLE_END);
        addDelimiterPair(Constants.RECORD_BEGIN, Constants.RECORD_END);
        addDelimiterPair(Constants.ARRAY_BEGIN, Constants.ARRAY_END);

        addDelimiter(Constants.ATTRIBUTE_ACCESS);
        addDelimiter(Constants.SUBSCRIPT_ACCESS);
    }


    public void forward() {
        if (text.charAt(offset) == '\n') {
            line++;
            col = 0;
            offset++;
        } else {
            col++;
            offset++;
        }
    }


    public void addDelimiterPair(String open, String close) {
        delimiters.add(open);
        delimiters.add(close);
        delimiterMap.put(open, close);
    }


    public void addDelimiter(String delim) {
        delimiters.add(delim);
    }


    public boolean isDelimiter(char c) {
        return delimiters.contains(Character.toString(c));
    }


    public boolean isOpen(Node c) {
        //noinspection SimplifiableIfStatement
        if (c instanceof Delimiter) {
            return delimiterMap.keySet().contains(((Delimiter) c).shape);
        } else {
            return false;
        }
    }


    public boolean isClose(Node c) {
        //noinspection SimplifiableIfStatement
        if (c instanceof Delimiter) {
            return delimiterMap.values().contains(((Delimiter) c).shape);
        } else {
            return false;
        }
    }


    public boolean matchString(String open, String close) {
        String matched = delimiterMap.get(open);
        //noinspection SimplifiableIfStatement,RedundantIfStatement
        if (matched != null && matched.equals(close)) {
            return true;
        } else {
            return false;
        }
    }


    public boolean matchDelim(Node open, Node close) {
        return (open instanceof Delimiter &&
                close instanceof Delimiter &&
                matchString(((Delimiter) open).shape, ((Delimiter) close).shape));
    }


    /**
     * lexer
     *
     * @return a token or null if file ends
     */
    @Nullable
    private Node nextToken() {

        boolean seenComment = true;
        while (seenComment) {
            seenComment = false;

            // skip spaces
            while (offset < text.length() &&
                    Character.isWhitespace(text.charAt(offset)))
            {
                forward();
            }

            // comments
            int cmtlen = Constants.LINE_COMMENT.length();
            if (offset + cmtlen <= text.length() &&
                    text.substring(offset, offset + cmtlen).equals(Constants.LINE_COMMENT))
            {
                // skip line
                while (offset < text.length() && text.charAt(offset) != '\n') {
                    forward();
                }
                if (offset < text.length()) {
                    forward(); // skip "\n"
                }

                // 处理连续多行注释
                seenComment = true;
            }
        }

        // end of file
        if (offset >= text.length()) {
            return null;
        }

        char cur = text.charAt(offset);

        // delimiters
        if (isDelimiter(cur)) {
            Node ret = new Delimiter(Character.toString(cur), file, offset, offset + 1, line, col);
            forward();
            return ret;
        }

        // string
        if (text.charAt(offset) == '"' && (offset == 0 || text.charAt(offset - 1) != '\\')) {
            int start = offset;
            int startLine = line;
            int startCol = col;
            forward();   // skip "

            while (offset < text.length() &&
                    !(text.charAt(offset) == '"' && text.charAt(offset - 1) != '\\'))
            {
                if (text.charAt(offset) == '\n') {
                    $.generalError(file + ":" + startLine + ":" + startCol + ": runaway string");
                    return null;
                }
                forward();
            }

            if (offset >= text.length()) {
                $.generalError(file + ":" + startLine + ":" + startCol + ": runaway string");
                return null;
            }

            forward(); // skip "
            int end = offset;

            String content = text.substring(start + 1, end - 1);
            return new Str(content, file, start, end, startLine, startCol);
        }


        // find consecutive token
        int start = offset;
        int startLine = line;
        int startCol = col;

        if (Character.isDigit(text.charAt(start)) ||
                ((text.charAt(start) == '+' || text.charAt(start) == '-')
                        && Character.isDigit(text.charAt(start + 1))))
        {
            while (offset < text.length() &&
                    !Character.isWhitespace(cur) &&
                    !(isDelimiter(cur) && cur != '.') // skip 小数点
                    )
            {
                forward();
                if (offset < text.length()) {
                    cur = text.charAt(offset);
                }
            }

            String content = text.substring(start, offset);

            IntNum intNum = IntNum.parse(content, file, start, offset, startLine, startCol);
            if (intNum != null) {
                return intNum;
            } else {
                FloatNum floatNum = FloatNum.parse(content, file, start, offset, startLine, startCol);
                if (floatNum != null) {
                    return floatNum;
                } else {
                    $.generalError(file + ":" + startLine + ":" + startCol + " : incorrect number format: " + content);
                    return null;
                }
            }
        } else {
            while (offset < text.length() &&
                    !Character.isWhitespace(cur) &&
                    !isDelimiter(cur))
            {
                forward();
                if (offset < text.length()) {
                    cur = text.charAt(offset);
                }
            }

            String content = text.substring(start, offset);
            if (content.matches(":\\w.*")) {
                return new Keyword(content.substring(1), file, start, offset, startLine, startCol);
            } else {
                return new Name(content, file, start, offset, startLine, startCol);
            }
        }
    }


    /**
     * parser
     *
     * @return a Node or null if file ends
     */
    public Node nextNode(int depth) {
        Node begin = nextToken();

        // end of file
        if (begin == null) {
            return null;
        }

        if (depth == 0 && isClose(begin)) {
            $.syntaxError(begin, "unmatched closing delimeter: " + begin);
            return null;
        } else if (isOpen(begin)) {   // try to get matched (...)
            List<Node> elements = new ArrayList<>();
            Node iter = nextNode(depth + 1);

            while (!matchDelim(begin, iter)) {
                if (iter == null) {
                    $.syntaxError(begin, "unclosed delimiter: " + begin);
                    return null;
                } else if (isClose(iter)) {
                    $.syntaxError(iter, "unmatched closing delimiter: " + iter);
                    return null;
                } else {
                    elements.add(iter);
                    iter = nextNode(depth + 1);
                }
            }
            return new Tuple(elements, begin, iter, begin.file, begin.start, iter.end, begin.line, begin.col);
        } else {
            return begin;
        }
    }


    /**
     * wrapper for the actual parser
     */
    public Node nextSexp() {
        return nextNode(0);
    }


    /**
     * parse file into a Node
     */
    public Node parse() {
        List<Node> elements = new ArrayList<>();

        // 程序本身是seq，构成一个Block
        // synthetic block keyword
        elements.add(genName(Constants.SEQ_KEYWORD));

        Node s = nextSexp();
        while (s != null) {
            elements.add(s);
            s = nextSexp();
        }
        return new Tuple(elements, genName(Constants.TUPLE_BEGIN), genName(Constants.TUPLE_END),
                file, 0, text.length(), 0, 0);
    }


    public Name genName(String id) {
        return new Name(id, file, 0, 0, 0, 0);
    }


    public static void main(String[] args) {
        String file = args[0];
        PreParser p = new PreParser(file);
        $.msg("tree: " + p.parse());
    }
}
