package mua;

import java.util.Stack;

public class Parser {

    // a stack storing "names during interpretation
    public static Stack<String> nameStack = new Stack<String>();

    // a stack storing operations during interpretation
    static Stack<String> opStack = new Stack<String>();

    private static Stack<Integer> opParmNumStack = new Stack<Integer>();

    private static int listlevel = 0; // used to read a list from input
    private static String liststring = new String(); // used to store list string from input

    // add return value 万物皆有返回值，适应run操作
    public static int Process(final String in) {
        try {
            // 正在读取list，注意处理多层list
            if (listlevel > 0) {
                readList(in);
                return 0;
            }

            boolean isOp = false;

            if (in.charAt(0) == '\"') {
                nameStack.push(in.substring(1));
            } else if (Value.isNumber(in) || Value.isBool(in)) {
                // 数字或布尔
                nameStack.push(in);
            } else if (in.charAt(0) == ':') {
                // 此处执行thing函数代码
                nameStack.push(in.substring(1));
                nameStack.push(Operation.Process("thing"));
            } else if (in.charAt(0) == '[') {
                readList(in);
                return 0;
            } else {
                // 字母开头, 视为operator
                if (in.equals("q") || in.equals("quit"))
                    return -1;

                if (!Operation.opMap.containsKey(in)) {
                    throw new Exception("ERROR: No operations called \"" + in + "\"");
                }

                // 不需要参数的operation, 如read
                if (Operation.opMap.get(in) == 0) {
                    String res = Operation.Process(in);
                    // 返回值判断是否需要入栈
                    if (!opStack.isEmpty()) {
                        nameStack.push(res);
                        addValueProcess();
                    }
                    isOp = true;
                } else {
                    opParmNumStack.push(Operation.opMap.get(in));
                    opStack.push(in);
                    isOp = true;
                }
            }

            if (!isOp) {
                addValueProcess();
            }
            return 0;
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }

    }

    /**
     * 递归处理添加value的操作 需要递归的原因在于value入栈后可能触发操作 返回值仍需要继续入栈
     */
    private static void addValueProcess() {
        // 错误检测
        if (opParmNumStack.empty()) {
            System.out.println("ERROR: input oprand without operator!");
            System.exit(-1);
        }
        opParmNumStack.push(opParmNumStack.pop() - 1);
        if (opParmNumStack.peek() <= 0) {
            // 执行op功能
            opParmNumStack.pop();
            String res = Operation.Process(opStack.pop());

            if (res != null && !opStack.empty()) {
                nameStack.push(res);
                addValueProcess();
            }
        }
    }

    private static void readList(String ls) {
        if (ls.startsWith("[")) {
            listlevel += ls.lastIndexOf('[') - ls.indexOf('[') + 1;
        }
        if (ls.endsWith("]")) {
            listlevel -= ls.lastIndexOf(']') - ls.indexOf(']') + 1;
        }
        if (liststring.isEmpty())
            liststring += ls;
        else
            liststring += " " + ls;

        if (listlevel == 0) {
            nameStack.push(liststring);
            liststring = "";
            addValueProcess();
        }
    }
}