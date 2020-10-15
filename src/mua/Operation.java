package mua;

import java.util.HashMap;
import java.util.Map;

public class Operation {
    public static Map<String, Integer> opMap = initMap();

    private static Map<String, Integer> initMap() {
        Map<String, Integer> res = new HashMap<String, Integer>();
        res.put("make", 2);
        res.put("thing", 1);
        res.put("print", 1);
        res.put("read", 0);
        res.put("add", 2);
        res.put("sub", 2);
        res.put("mul", 2);
        res.put("div", 2);
        res.put("mod", 2);
        res.put("erase", 1);
        res.put("isname", 1);
        res.put("eq", 2);
        res.put("gt", 2);
        res.put("lt", 2);
        return res;
    }

    public static String Process(final String op) {
        switch (op) {
            case "make":
                return op_Make();
            case "thing":
                return op_Thing();
            case "print":
                return op_Print();
            case "read":
                return op_Read();
            case "add":
                return op_Add();
            case "sub":
                return op_Sub();
            case "mul":
                return op_Mul();
            case "div":
                return op_Div();
            case "mod":
                return op_Mod();
            case "erase":
                return op_Erase();
            case "isname":
                return op_Isname();
            case "eq":
                return op_Eq();
            case "gt":
                return op_Gt();
            case "lt":
                return op_Lt();

            default:
                break;
        }
        return new String();
    }

    private static String[] getParmList(String op) {
        int parmNum = opMap.get(op);
        String[] res = new String[parmNum];

        for (int i = 0; i < parmNum; i++) {
            res[i] = Parser.nameStack.pop();
        }
        return res;
    }

    private static String op_Make() {
        String[] parmList;
        parmList = getParmList("make");

        if (!(('a' <= parmList[1].charAt(0) && parmList[1].charAt(0) <= 'z')
                || ('A' <= parmList[1].charAt(0) && parmList[1].charAt(0) <= 'Z'))) {
            System.out.println("ERROR: In make, \"" + parmList[1] + "\" is not a name.");
            System.exit(-1);
        }

        if (!Value.makeName(parmList[1], parmList[0])) {
            System.out.println("Make failed! The name \"" + parmList[1] + "\" has existed.");
        }

        return parmList[0];
    }

    private static String op_Thing() {
        String[] parmList;
        parmList = getParmList("thing");

        if (!Value.hasName(parmList[0])) {
            System.out.println("ERROR: In thing, \"" + parmList[0] + "\" is not a name.");
            System.exit(-1);
        }

        return Value.getValue(parmList[0]);
    }

    private static String op_Print() {
        String[] parmList;
        parmList = getParmList("thing");

        System.out.println(parmList[0]);

        return parmList[0];
    }

    private static String op_Read() {
        String res;

        while (true) {
            res = Main.input.next();
            if (Value.isWord(res)) {
                res = res.substring(1);
                break;
            } else if (Value.isNumber(res) || Value.isBool(res)) {
                break;
            } else {
                System.out.println("Your input in read is not a word or number! Please retry.");
            }
        }

        // 这里close会导致Main函数中的Scanner发生异常

        return res;
    }

    private static String op_Add() {
        String[] parmList;
        parmList = getParmList("add");

        if (!Value.isNumber(parmList[0]) || !Value.isNumber(parmList[1])) {
            System.out.println("ERROR: In add, a vulue is not a number!");
            System.exit(-1);
        }

        try {
            double res, m, n;
            m = Double.valueOf(parmList[0]);
            n = Double.valueOf(parmList[1]);
            res = m + n;
            return String.valueOf(res);
        } catch (Exception e) {
            System.out.println("ERROR: In add, a vulue is not a number!");
            System.exit(-1);
        }
        return new String();
    }

    private static String op_Sub() {
        String[] parmList;
        parmList = getParmList("sub");

        if (!Value.isNumber(parmList[0]) || !Value.isNumber(parmList[1])) {
            System.out.println("ERROR: In sub, a vulue is not a number!");
            System.exit(-1);
        }

        try {
            double res, m, n;
            m = Double.valueOf(parmList[0]);
            n = Double.valueOf(parmList[1]);
            res = n - m;
            return String.valueOf(res);
        } catch (Exception e) {
            System.out.println("ERROR: In sub, a vulue is not a number!");
            System.exit(-1);
        }
        return new String();
    }

    private static String op_Mul() {
        String[] parmList;
        parmList = getParmList("mul");

        if (!Value.isNumber(parmList[0]) || !Value.isNumber(parmList[1])) {
            System.out.println("ERROR: In mul, a vulue is not a number!");
            System.exit(-1);
        }

        try {
            double res, m, n;
            m = Double.valueOf(parmList[0]);
            n = Double.valueOf(parmList[1]);
            res = m * n;
            return String.valueOf(res);
        } catch (Exception e) {
            System.out.println("ERROR: In mul, a vulue is not a number!");
            System.exit(-1);
        }
        return new String();
    }

    private static String op_Div() {
        String[] parmList;
        parmList = getParmList("div");

        if (!Value.isNumber(parmList[0]) || !Value.isNumber(parmList[1])) {
            System.out.println("ERROR: In div, a vulue is not a number!");
            System.exit(-1);
        }

        try {
            double res, m, n;
            m = Double.valueOf(parmList[0]);
            n = Double.valueOf(parmList[1]);
            res = n / m;
            return String.valueOf(res);
        } catch (Exception e) {
            System.out.println("ERROR: In div, a vulue is not a number!");
            System.exit(-1);
        }
        return new String();
    }

    private static String op_Mod() {
        String[] parmList;
        parmList = getParmList("mod");

        if (!Value.isNumber(parmList[0]) || !Value.isNumber(parmList[1])) {
            System.out.println("ERROR: In mod, a vulue is not a number!");
            System.exit(-1);
        }

        try {
            int res, m, n;
            double dm, dn;
            dm = Double.valueOf(parmList[0]);
            dn = Double.valueOf(parmList[1]);
            m = (int) dm;
            n = (int) dn;
            res = n % m;
            return String.valueOf(res);
        } catch (ArithmeticException e) {
            System.out.println("ERROR: In mod, there is a arithmetic expection.");
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("ERROR: In mod, a vulue is not a integer number!");
            System.exit(-1);
        }
        return new String();
    }

    private static String op_Erase() {
        String[] parmList;
        parmList = getParmList("erase");

        String res = "null";
        if (Value.hasName(parmList[0])) {
            res = Value.getValue(parmList[0]);
            Value.eraseName(parmList[0]);
        }

        return res;
    }

    private static String op_Isname() {
        String[] parmList;
        parmList = getParmList("isname");

        if (Value.hasName(parmList[0]))
            return "true";
        else
            return "false";
    }

    private static String op_Eq() {
        String[] parmList;
        parmList = getParmList("eq");

        if (Value.isNumber(parmList[0]) && Value.isNumber(parmList[1])) {
            double left, right;
            left = Double.valueOf(parmList[1]);
            right = Double.valueOf(parmList[0]);

            return String.valueOf(left == right);
        } else {
            return String.valueOf(parmList[1].equals(parmList[0]));
        }
    }

    private static String op_Gt() {
        String[] parmList;
        parmList = getParmList("gt");

        if (Value.isNumber(parmList[0]) && Value.isNumber(parmList[1])) {
            double left, right;
            left = Double.valueOf(parmList[1]);
            right = Double.valueOf(parmList[0]);

            return String.valueOf(left > right);
        } else {
            return String.valueOf(parmList[1].compareTo(parmList[0]) > 0);
        }
    }

    private static String op_Lt() {
        String[] parmList;
        parmList = getParmList("lt");

        if (Value.isNumber(parmList[0]) && Value.isNumber(parmList[1])) {
            double left, right;
            left = Double.valueOf(parmList[1]);
            right = Double.valueOf(parmList[0]);

            return String.valueOf(left < right);
        } else {
            return String.valueOf(parmList[1].compareTo(parmList[0]) < 0);
        }
    }
}
