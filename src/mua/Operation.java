package mua;

import java.util.HashMap;
import java.util.Map;

public class Operation {

    /** 内置运算符表 */
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
        res.put("return", 1);
        res.put("export", 1);

        return res;
    }

    /** 全局变量 */
    // 在Operation类中储存全局变量，Function中储存局部变量
    // public static Value globalvalue = new Value();

    boolean infunc;
    public Value localvalue;
    public Function func;

    Operation() {
        localvalue = new Value();
        infunc = false;
        func = new Function();
    }

    Operation(Function f) {
        localvalue = new Value();
        infunc = false;
        func = f;
    }

    Operation(boolean infunc, Value locValue, Function f) {
        localvalue = locValue;
        this.infunc = infunc;
        func = f;
    }

    public String Process(final String op, String[] parmList) {
        switch (op) {
            case "make":
                return op_Make(parmList);
            case "thing":
                return op_Thing(parmList);
            case "print":
                return op_Print(parmList);
            case "read":
                return op_Read();
            case "add":
                return op_Add(parmList);
            case "sub":
                return op_Sub(parmList);
            case "mul":
                return op_Mul(parmList);
            case "div":
                return op_Div(parmList);
            case "mod":
                return op_Mod(parmList);
            case "erase":
                return op_Erase(parmList);
            case "isname":
                return op_Isname(parmList);
            case "eq":
                return op_Eq(parmList);
            case "gt":
                return op_Gt(parmList);
            case "lt":
                return op_Lt(parmList);
            case "and":
                return op_And(parmList);
            case "or":
                return op_Or(parmList);
            case "not":
                return op_Not(parmList);
            case "isnumber":
                return op_Isnumber(parmList);
            case "isword":
                return op_Isword(parmList);
            case "islist":
                return op_Islist(parmList);
            case "isbool":
                return op_Isbool(parmList);
            case "isempty":
                return op_Isempty(parmList);
            case "run":
                return op_Run(parmList);
            case "if":
                return op_If(parmList);
            case "return":
                return op_Return(parmList);
            case "export":
                return op_Export(parmList);

            default:
                break;
        }
        return new String();
    }

    private String op_Export(String[] parmList) {
        if (!infunc) {
            // System.out.println("ERROR: export out of function!");
        }
        if (!localvalue.hasName(parmList[0])) {
            // System.out.println("ERROR: export a non-exist variable!");
        }
        Processor.globalvalue.makeName(parmList[0], localvalue.getValue(parmList[0]));
        return localvalue.getValue(parmList[0]);
    }

    private String op_Return(String[] parmList) {
        if (!infunc) {
            // System.out.println("ERROR: return out of function!");
        }

        localvalue.makeName("@return", parmList[0]);
        return parmList[0];
    }

    private String RunProcess(String runls) {
        // 执行空表，直接返回空表
        if (Value.value_is_Empty(runls))
            return "[]";
        // 获得list内容
        String[] runcode = runls.substring(1, runls.length() - 1).split(" ");

        // 删除单表处理，功能重复
        Processor processor = new Processor(infunc, localvalue);
        String res = new String();
        for (int i = 0; i < runcode.length; i++) {
            res = processor.Process(runcode[i]);
        }
        return res;
    }

    private String op_If(String[] parmList) {
        // [0] -> <list2>; [1] -> <list1>; [2] -> <bool>

        // 格式检查
        // 类比scheme，非false均为true，bool不作类型检查
        // if (!Value.isBool(parmList[2])){
        // System.out.println("");
        // }
        if (!(Value.isList(parmList[1]) && Value.isList(parmList[0]))) {
            // System.out.println("ERROR: operator \"if\" needs lists as oprands!");
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

    private String op_Run(String[] parmList) {
        /*
         * 更新：重构Parser后，采用实例化一个新的processor来处理运行代码，在实现层面使得run操作有了返回值
         */

        if (!Value.isList(parmList[0])) {
            // System.out.println("ERROR: \"run\" needs a list as oprand!");
            System.exit(-1);
        }

        return RunProcess(parmList[0]);
    }

    private String op_Islist(String[] parmList) {
        return String.valueOf(Value.isList(parmList[0]));
    }

    private String op_Isempty(String[] parmList) {
        return String.valueOf(Value.value_is_Empty(parmList[0]));
    }

    private String op_Isbool(String[] parmList) {
        return String.valueOf(Value.isBool(parmList[0]));
    }

    private String op_Isword(String[] parmList) {
        // 个人李姐，非表都为字
        // 10.21 修正：word不包括number和bool
        return String.valueOf(Value.isWord(parmList[0]));
    }

    private String op_Isnumber(String[] parmList) {
        return String.valueOf(Value.isNumber(parmList[0]));
    }

    private String op_Not(String[] parmList) {
        // 类scheme，非false都视为true
        if (parmList[0].equals("false"))
            return "true";

        return "false";
    }

    // TODO: 局部变量的make
    private String op_Make(String[] parmList) {
        // make条件：前为name，且不与基本operation重名
        if (!Value.isName(parmList[1])) {
            // System.out.println("ERROR: In make, \"" + parmList[1] + "\" is not a name.");
            System.exit(-1);
        } else if (Operation.opMap.containsKey(parmList[1])) {
            // System.out.println("ERROR: In make, \"" + parmList[1] + "\" is a keyword!");
            System.exit(-1);
        }

        // make到全局变量
        if (!infunc) {
            if (!Processor.globalvalue.makeName(parmList[1], parmList[0])) {
                // System.out.println("Make failed!");
            }
            if (func.isOldFunction(parmList[1])) {
                func.rewriteFunc(parmList[1], parmList[0]);
            }
        } else {
            if (!localvalue.makeName(parmList[1], parmList[0])) {
                // System.out.println("Make failed!");
            }
            if (func.isOldFunction(parmList[1])) {
                func.rewriteFunc(parmList[1], parmList[0]);
            }
        }

        return parmList[0];
    }

    // TODO: 局部变量thing
    private String op_Thing(String[] parmList) {
        if (!infunc) {

            // 全局变量判断
            if (!Processor.globalvalue.hasName(parmList[0])) {
                // System.out.println("ERROR: In thing, name \"" + parmList[0] + "\" has not
                // bound to value.");
                // System.exit(-1);
            }
            return Processor.globalvalue.getValue(parmList[0]);
        } else {
            if (localvalue.hasName(parmList[0])) {
                return localvalue.getValue(parmList[0]);
            } else if (Processor.globalvalue.hasName(parmList[0])) {
                return Processor.globalvalue.getValue(parmList[0]);
            } else {
                // System.out.println("ERROR: In thing, name \"" + parmList[0] + "\" has not
                // bound to value.");
                // System.exit(-1);
            }
            return "";
        }

    }

    private String op_Print(String[] parmList) {
        if (parmList[0].isEmpty())
            System.out.println("null");
        else
            System.out.println(parmList[0]);

        return parmList[0];
    }

    private String op_Read() {
        String res;

        // 使用main中的input输入
        while (true) {
            res = Main.input.next();
            // 10.21 修改read功能，不需要再加“来识别word
            if (Value.isWord(res) || Value.isNumber(res) || Value.isBool(res)) {
                break;
            } else {
                // System.out.println("Your input in read is not a word or number! Please
                // retry.");
            }
        }

        return res;
    }

    private String op_Add(String[] parmList) {
        if (!Value.isNumber(parmList[0]) || !Value.isNumber(parmList[1])) {
            // System.out.println("ERROR: In add, a vulue is not a number!");
            System.exit(-1);
        }

        try {
            double res, m, n;
            m = Double.valueOf(parmList[0]);
            n = Double.valueOf(parmList[1]);
            res = m + n;
            return String.valueOf(res);
        } catch (Exception e) {
            // System.out.println("ERROR: In add, a vulue is not a number!");
            System.exit(-1);
        }
        return new String();
    }

    private String op_Sub(String[] parmList) {
        if (!Value.isNumber(parmList[0]) || !Value.isNumber(parmList[1])) {
            // System.out.println("ERROR: In sub, a vulue is not a number!");
            System.exit(-1);
        }

        try {
            double res, m, n;
            m = Double.valueOf(parmList[0]);
            n = Double.valueOf(parmList[1]);
            res = n - m;
            return String.valueOf(res);
        } catch (Exception e) {
            // System.out.println("ERROR: In sub, a vulue is not a number!");
            System.exit(-1);
        }
        return new String();
    }

    private String op_Mul(String[] parmList) {
        if (!Value.isNumber(parmList[0]) || !Value.isNumber(parmList[1])) {
            // System.out.println("ERROR: In mul, a vulue is not a number!");
            System.exit(-1);
        }

        try {
            double res, m, n;
            m = Double.valueOf(parmList[0]);
            n = Double.valueOf(parmList[1]);
            res = m * n;
            return String.valueOf(res);
        } catch (Exception e) {
            // System.out.println("ERROR: In mul, a vulue is not a number!");
            System.exit(-1);
        }
        return new String();
    }

    private String op_Div(String[] parmList) {
        if (!Value.isNumber(parmList[0]) || !Value.isNumber(parmList[1])) {
            // System.out.println("ERROR: In div, a vulue is not a number!");
            System.exit(-1);
        }

        try {
            double res, m, n;
            m = Double.valueOf(parmList[0]);
            n = Double.valueOf(parmList[1]);
            res = n / m;
            return String.valueOf(res);
        } catch (Exception e) {
            // System.out.println("ERROR: In div, a vulue is not a number!");
            System.exit(-1);
        }
        return new String();
    }

    private String op_Mod(String[] parmList) {
        if (!Value.isNumber(parmList[0]) || !Value.isNumber(parmList[1])) {
            // System.out.println("ERROR: In mod, a vulue is not a number!");
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
            // System.out.println("ERROR: In mod, there is a arithmetic expection.");
            System.exit(-1);
        } catch (Exception e) {
            // System.out.println("ERROR: In mod, a vulue is not a integer number!");
            System.exit(-1);
        }
        return new String();
    }

    // TODO: 局部变量清除
    private String op_Erase(String[] parmList) {
        String res = "";
        // 函数内
        if (infunc) {
            // 先清除局部变量
            if (localvalue.hasName(parmList[0])) {
                res = localvalue.getValue(parmList[0]);
                localvalue.eraseName(parmList[0]);
            } else if (Processor.globalvalue.hasName(parmList[0])) {
                // 没有则清除全局变量
                res = Processor.globalvalue.getValue(parmList[0]);
                Processor.globalvalue.eraseName(parmList[0]);
            }
        } else {
            // 清除全局
            if (Processor.globalvalue.hasName(parmList[0])) {
                res = Processor.globalvalue.getValue(parmList[0]);
                Processor.globalvalue.eraseName(parmList[0]);
            }
        }

        return res;
    }

    // TODO: 局部变量判断
    // 10.21 修改：isname, 按原为判断word，现修正为判断是否为Value表中的name
    private String op_Isname(String[] parmList) {
        boolean isname = false;
        if (infunc) {
            isname = isname | localvalue.hasName(parmList[0]);
        }
        isname = isname | Processor.globalvalue.hasName(parmList[0]);
        if (isname)
            return "true";
        else
            return "false";
    }

    private String op_Eq(String[] parmList) {
        if (Value.isNumber(parmList[0]) && Value.isNumber(parmList[1])) {
            double left, right;
            left = Double.valueOf(parmList[1]);
            right = Double.valueOf(parmList[0]);

            return String.valueOf(left == right);
        } else {
            return String.valueOf(parmList[1].equals(parmList[0]));
        }
    }

    private String op_Gt(String[] parmList) {
        if (Value.isNumber(parmList[0]) && Value.isNumber(parmList[1])) {
            double left, right;
            left = Double.valueOf(parmList[1]);
            right = Double.valueOf(parmList[0]);

            return String.valueOf(left > right);
        } else {
            return String.valueOf(parmList[1].compareTo(parmList[0]) > 0);
        }
    }

    private String op_Lt(String[] parmList) {
        if (Value.isNumber(parmList[0]) && Value.isNumber(parmList[1])) {
            double left, right;
            left = Double.valueOf(parmList[1]);
            right = Double.valueOf(parmList[0]);

            return String.valueOf(left < right);
        } else {
            return String.valueOf(parmList[1].compareTo(parmList[0]) < 0);
        }
    }

    private String op_And(String[] parmList) {
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

    private String op_Or(String[] parmList) {
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
