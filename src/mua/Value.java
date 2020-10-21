package mua;

import java.util.HashMap;
import java.util.Map;

public class Value {

    private static Map<String, String> nameMap = new HashMap<String, String>();

    public static boolean makeName(String name, String value) {
        try {
            if (nameMap.containsKey(name))
                nameMap.replace(name, value);
            else
                nameMap.put(name, value);

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean eraseName(String name) {
        if (nameMap.containsKey(name)) {
            nameMap.remove(name);
            return true;
        }

        return false;
    }

    public static boolean hasName(String name) {
        return nameMap.containsKey(name);
    }

    public static String getValue(String name) {
        return nameMap.get(name);
    }

    // 只要不是list，value都属于word
    // 10.21 修正：非list、非number、非bool
    public static boolean isWord(String s) {
        return !(isList(s) || isBool(s) || isNumber(s));
    }

    // start with '['
    public static boolean isList(String s) {
        if (s.startsWith("["))
            return true;

        return false;
    }

    public static boolean isName(String s) {
        if (s.isEmpty())
            return false;

        // 以字母开头
        if (!(('a' <= s.charAt(0) && s.charAt(0) <= 'z') || ('A' <= s.charAt(0) && s.charAt(0) <= 'Z')))
            return false;

        // 只含字母、数字、下划线
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9') || c == '_')
                ;
            else
                return false;
        }

        return true;
    }

    public static boolean isNumber(String s) {
        if (s.isEmpty())
            return false;

        if (!(('0' <= s.charAt(0) && s.charAt(0) <= '9') || s.charAt(0) == '-'))
            return false;

        // 修改判断number的逻辑
        // 原逻辑会使1234dd这样的word变成数字

        int dotcount = 0;
        for (int i = 1; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '.')
                dotcount++;
            else if (!('0' <= ch && ch <= '9'))
                return false;

            if (dotcount > 1)
                return false;
        }

        return true;
    }

    public static boolean isBool(String s) {
        if (s.equals("true") || s.equals("false")) {
            return true;
        }
        return false;
    }

    public static boolean value_is_Empty(String s) {
        if (isList(s)) {
            return s.substring(1, s.length() - 1).isBlank();
        } else {
            return s.isEmpty();
        }
    }
}