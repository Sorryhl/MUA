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
        res.put("and", 2);
        res.put("or", 2);
        res.put("not", 1);
        res.put("isnumber", 1);
        res.put("isword", 1);
        res.put("islist", 1);
        res.put("isbool", 1);
        res.put("isempty", 1);
        res.put("islist", 1);
        res.put("isbool", 1);
        res.put("isempty", 1);
        res.put("run", 1);
        res.put("if", 3);

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
            case "and":
                return op_And();
            case "or":
                return op_Or();
            case "not":
                return op_Not();
            case "isnumber":
                return op_Isnumber();
            case "isword":
                return op_Isword();
            case "islist":
                return op_Islist();
            case "isbool":
                return op_Isbool();
            case "isempty":
                return op_Isempty();
            case "run":
                return op_Run();
            case "if":
                return op_If();

            default:
                break;
        }
        return new String();
    }

    private static String RunProcess(String runls) {
        // 执行空表，直接返回空表
        if (Value.value_is_Empty(runls))
            return "[]";
        // 获得list内容
        String[] runcode = runls.substring(1, runls.length() - 1).split(" ");

        // 单元素表，只有一个word，没有op，返回该word
        if (runcode.length == 1) {
            return runcode[0];
        }

        for (int i = 0; i < runcode.length; i++) {
            Parser.Process(runcode[i]);
        }
        return null;
    }

    private static String op_If() {
        String[] parmList;
        parmList = getParmList("if");
        // [0] -> <list2>; [1] -> <list1>; [2] -> <bool>

        // 格式检查
        // 类比scheme，非false均为true，bool不作类型检查
        // if (!Value.isBool(parmList[2])){
        // System.out.println("");
        // }
        if (!(Value.isList(parmList[1]) && Value.isList(parmList[0]))) {
            System.out.println("ERROR: operator \"if\" needs lists as oprands!");
            System.exit(-1);
        }

        String runlist;

        if (!parmList[2].equals("false")) {
            // <bool>为真，执行<list1>
            runlist = parmList[1];
        } else {
            // <bool>为假，执行<list2>
            runlist = parmList[0];
        }

        // if 的runlist处理与run共用，同为伪返回机制
        return RunProcess(runlist);
    }

    private static String op_Run() {
        /*
         * run：伪返回；由于压栈机制的保证，在run中执行的运算结果将自动进栈与其余op进行运算；
         * 而run操作本身不需要返回值进栈，故直接返回null，并在上一层解释中忽略null值的进栈
         * 这样，在实现层，run没有真正返回值，但在运行时能够表现为是run的返回值参与了运算
         * 
         * 更新：对空表和单元素表的处理，分别返回空表和单元素，此时run有实现上的返回值； 而表内存在op时，仍是伪返回，实现上的返回值为null
         */
        String[] parmList;
        parmList = getParmList("run");

        if (!Value.isList(parmList[0])) {
            System.out.println("ERROR: \"run\" needs a list as oprand!");
            System.exit(-1);
        }

        return RunProcess(parmList[0]);
    }

    private static String op_Islist() {
        String[] parmList;
        parmList = getParmList("islist");
        return String.valueOf(Value.isList(parmList[0]));
    }

    private static String op_Isempty() {
        String[] parmList;
        parmList = getParmList("isempty");

        return String.valueOf(Value.value_is_Empty(parmList[0]));
    }

    private static String op_Isbool() {
        String[] parmList;
        parmList = getParmList("isbool");
        return String.valueOf(Value.isBool(parmList[0]));
    }

    private static String op_Isword() {
        String[] parmList;
        parmList = getParmList("isword");

        // 个人李姐，非表都为字
        return String.valueOf(Value.isWord(parmList[0]));
    }

    private static String op_Isnumber() {
        String[] parmList;
        parmList = getParmList("isnumber");

        return String.valueOf(Value.isNumber(parmList[0]));
    }

    private static String op_Not() {
        String[] parmList;
        parmList = getParmList("not");

        // 类scheme，非false都视为true
        if (parmList[0].equals("false"))
            return "true";

        return "false";
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

        // make条件：前为name，且不与基本operation重名
        if (!Value.isName(parmList[1])) {
            System.out.println("ERROR: In make, \"" + parmList[1] + "\" is not a name.");
            System.exit(-1);
        } else if (Operation.opMap.containsKey(parmList[1])) {
            System.out.println("ERROR: In make, \"" + parmList[1] + "\" is a keyword!");
            System.exit(-1);
        }

        if (!Value.makeName(parmList[1], parmList[0])) {
            System.out.println("Make failed!");
        }

        return parmList[0];
    }

    private static String op_Thing() {
        String[] parmList;
        parmList = getParmList("thing");

        if (!Value.hasName(parmList[0])) {
            System.out.println("ERROR: In thing, name \"" + parmList[0] + "\" has not bound to value.");
            System.exit(-1);
        }

        return Value.getValue(parmList[0]);
    }

    private static String op_Print() {
        String[] parmList;
        parmList = getParmList("print");

        if (parmList[0].isEmpty())
            System.out.println("null");
        else
            System.out.println(parmList[0]);

        return parmList[0];
    }

    private static String op_Read() {
        String res;

        // 使用main中的input输入
        while (true) {
            res = Main.input.next();
            if (res.charAt(0) == '\"') {
                res = res.substring(1);
                break;
            } else if (Value.isNumber(res) || Value.isBool(res)) {
                break;
            } else {
                System.out.println("Your input in read is not a word or number! Please retry.");
            }
        }

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

        String res = "";
        if (Value.hasName(parmList[0])) {
            res = Value.getValue(parmList[0]);
            Value.eraseName(parmList[0]);
        }

        return res;
    }

    private static String op_Isname() {
        String[] parmList;
        parmList = getParmList("isname");

        if (Value.isName(parmList[0]))
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

    private static String op_And() {
        String[] parmList;
        parmList = getParmList("and");

        // 类似scheme，非false全部取true
        if (Value.isBool(parmList[0]) && Value.isBool(parmList[0])) {
            boolean oprand1, oprand2;
            oprand1 = !parmList[0].equals("false");
            oprand2 = !parmList[1].equals("false");

            return String.valueOf(oprand1 & oprand2);
        } else {
            return "true";
        }
    }

    private static String op_Or() {
        String[] parmList;
        parmList = getParmList("or");

        // same as and
        if (Value.isBool(parmList[0]) && Value.isBool(parmList[0])) {
            boolean oprand1, oprand2;
            oprand1 = !parmList[0].equals("false");
            oprand2 = !parmList[1].equals("false");

            return String.valueOf(oprand1 | oprand2);
        } else {
            return "true";
        }
    }
}
