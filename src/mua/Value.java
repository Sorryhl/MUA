package mua;

import java.util.HashMap;
import java.util.Map;

/**
 * Name
 */
public class Value {

    private static Map<String, String> nameMap = new HashMap<String, String>();

    public static boolean makeName(String name, String value) {
        if (nameMap.containsKey(name))
            return false;

        nameMap.put(name, value);
        return true;
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

    public static boolean isWord(String s) {
        if (s.charAt(0) == '\"')
            return true;

        return false;
    }

    public static boolean isNumber(String s) {
        if (('0' <= s.charAt(0) && s.charAt(0) <= '9') || s.charAt(0) == '-')
            return true;

        return false;
    }

    public static boolean isBool(String s) {
        if (s.equals("true") || s.equals("false")) {
            return true;
        }
        return false;
    }
}