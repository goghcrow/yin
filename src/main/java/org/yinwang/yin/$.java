package org.yinwang.yin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yinwang.yin.ast.Node;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

// for 1.8 : _ -> $
@SuppressWarnings("WeakerAccess")
public class $ {

    @NotNull
    public static String readFile(@NotNull String path) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return Charset.forName("UTF-8").decode(ByteBuffer.wrap(encoded)).toString();
        } catch (IOException e) {
            $.abort("failed to read file: " + path + ", caused by " + e.getMessage());
            return "";
        }
    }


    public static void msg(String m) {
        System.out.println(m);
    }


    public static void generalError(String m) {
        throw new GeneralError(m);
    }


    public static void syntaxError(Node loc, String msg) {
        throw new SyntaxError(loc, msg);
    }


    public static void abort(String m) {
        System.err.println(m);
        System.err.flush();
        Thread.dumpStack();
        System.exit(1);
    }


    @NotNull
    public static String joinWithSep(@NotNull Collection<?> ls, @NotNull String sep) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Object s : ls) {
            if (i > 0) {
                sb.append(sep);
            }
            sb.append(s.toString());
            i++;
        }
        return sb.toString();
    }


    public static String unifyPath(@NotNull String filename) {
        return unifyPath(new File(filename));
    }


    public static String unifyPath(@NotNull File file) {
        try {
            return file.getCanonicalPath();
        } catch (Exception e) {
            generalError("Failed to get canonical path");
            return "";
        }
    }

}
