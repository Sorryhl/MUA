package mua;

import java.util.Stack;

public class Processor {
    public static Value globalvalue = new Value();

    private Value localvalue;

    private Stack<String> nameStack;
    private Stack<String> opStack;
    private Stack<Integer> opParmNumStack;
    private int listlevel;
    private String liststr;
    private int infixlevel;
    private String infixstr;

    //
    boolean infunc;
    private Operation opproc;
    // 在Processor中实例化一个Function对象，作为全局的函数定义
    private Function func;

    // 在Processor中

    Processor() {
        nameStack = new Stack<String>();
        opStack = new Stack<String>();
        opParmNumStack = new Stack<Integer>();
        listlevel = 0;
        liststr = new String();
        infixlevel = 0;
        infixstr = new String();

        infunc = false;
        localvalue = null;
        func = new Function();
        opproc = new Operation(func);
    }

    Processor(boolean infunc, Value localvalue) {
        this.infunc = infunc;
        this.localvalue = localvalue;

        nameStack = new Stack<String>();
        opStack = new Stack<String>();
        opParmNumStack = new Stack<Integer>();
        listlevel = 0;
        liststr = new String();
        infixlevel = 0;
        infixstr = new String();

        func = new Function(infunc, localvalue);
        opproc = new Operation(infunc, localvalue, func);
    }

    public String Process(final String in) {
        try {
            // 正在读取list，注意处理多层list
            if (listlevel > 0) {
                readList(in);
                return "";
            }

            // 正在读取中缀表达式，注意处理多重()
            if (infixlevel > 0) {
                return readInfix(in);
            }

            if (in.charAt(0) == '\"') {
                // 以 “ 开头，word的字面量
                return addValueProcess(in.substring(1));
            } else if (Value.isNumber(in) || Value.isBool(in)) {
                // 数字或布尔
                return addValueProcess(in);
            } else if (in.charAt(0) == ':') {
                // 此处执行thing函数代码
                String[] parm = { in.substring(1) };
                return addValueProcess(opproc.Process("thing", parm));
            } else if (in.charAt(0) == '[') {
                // 以 [ 开头，说明开始读表
                return readList(in);
            } else if (in.charAt(0) == '(') {
                // 以 ( 开头，需要处理中缀表达式
                // 先将整个括号读取作为一个字符串，再对该字符串进行处理
                return readInfix(in);
            } else {
                // 字母开头, 视为operator
                if (!(Operation.opMap.containsKey(in) || func.isFunction(in))) {
                    throw new Exception("ERROR: No operations or functions called \"" + in + "\"");
                }

                // 不需要参数的operation, 如read
                if (Operation.opMap.containsKey(in) && Operation.opMap.get(in) == 0) {
                    return addValueProcess(opproc.Process(in, null));
                } else if (func.isFunction(in) && func.getArgNum(in) == 0) {
                    // TODO: 函数处理方式
                    return addValueProcess(func.Process(in, null));
                } else if (Operation.opMap.containsKey(in)) {
                    opParmNumStack.push(Operation.opMap.get(in));
                    opStack.push(in);
                } else {
                    opParmNumStack.push(func.getArgNum(in));
                    opStack.push(in);
                }
            }
            return "";
        } catch (final Exception e) {
            // System.out.println(e.getMessage());
            return "";
        }

    }

    private final int countInString(String s, String c) {
        return s.length() - s.replace(c, "").length();
    }

    private String readInfix(String in) {
        infixlevel += countInString(in, "(");
        infixlevel -= countInString(in, ")");
        if (infixstr.isEmpty()) {
            infixstr += in;
        } else {
            infixstr += " " + in;
        }
        if (infixlevel == 0) {
            // 读取中缀表达式完毕，对其进行处理，并将整个的结果返回压栈
            String res = new String();
            res = Infix.infixProcess(infixstr, infunc, localvalue);
            infixstr = "";
            return addValueProcess(res);
        }
        return "";
    }

    /**
     * 递归处理添加value的操作 需要递归的原因在于value入栈后可能触发操作 返回值仍需要继续入栈
     */
    private String addValueProcess(String value) {
        // 错误检测
        if (opParmNumStack.empty()) {
            // System.out.println("ERROR: input oprand without operator!");
            // System.exit(-1);

            // 改变执行逻辑，只有操作数则直接返回操作数，而非报错
            return value;
        }
        nameStack.push(value);
        opParmNumStack.push(opParmNumStack.pop() - 1);
        if (opParmNumStack.peek() <= 0) {
            // 执行op功能
            opParmNumStack.pop();
            // 将参数传递功能移动至此
            int opnum;
            String res;
            if (Operation.opMap.containsKey(opStack.peek())) {
                opnum = Operation.opMap.get(opStack.peek());

                String[] parmlist = new String[opnum];
                for (int i = 0; i < opnum; i++) {
                    parmlist[i] = nameStack.pop();
                }

                // TODO: 执行函数功能
                res = opproc.Process(opStack.pop(), parmlist);
            } else {
                opnum = func.getArgNum(opStack.peek());

                String[] parmlist = new String[opnum];
                for (int i = 0; i < opnum; i++) {
                    parmlist[i] = nameStack.pop();
                }

                // TODO: 执行函数功能
                res = func.Process(opStack.pop(), parmlist);
            }

            // 只要返回值不为null，都进行入栈处理，空栈有返回机制
            if (res != null) {
                return addValueProcess(res);
            } else {
                return res;
            }
        }
        return "";
    }

    private String readList(String ls) {
        // 通过函数优化计算层数的方式
        listlevel += countInString(ls, "[");
        listlevel -= countInString(ls, "]");
        if (liststr.isEmpty())
            liststr += ls;
        else
            liststr += " " + ls;

        if (listlevel == 0) {
            String tmp = new String(liststr);
            liststr = "";
            return addValueProcess(tmp);
        }
        return "";
    }

}
