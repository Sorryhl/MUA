package mua;

import java.util.Stack;

public class Parser {

    // a stack storing "names during interpretation
    public static Stack<String> nameStack = new Stack<String>();

    // a stack storing operations during interpretation
    static Stack<String> opStack = new Stack<String>();

    private static Stack<Integer> opParmNumStack = new Stack<Integer>();

    public static int Process(final String in) {
        try {
            boolean isOp = false;

            if (in.charAt(0) == '\"') {
                nameStack.push(in.substring(1));
            } else if (('0' <= in.charAt(0) && in.charAt(0) <= '9') || in.charAt(0) == '-') {
                // 数字
                nameStack.push(in);
            } else if (in.charAt(0) == ':') {
                // 此处执行thing函数代码
                nameStack.push(in.substring(1));
                nameStack.push(Operation.Process("thing"));
            } else {
                // 字母开头, 视为operator
                if (in.equals("q") || in.equals("quit"))
                    System.exit(0);

                if (!Operation.opMap.containsKey(in)) {
                    throw new Exception("ERROR: No operations called \"" + in + "\"");
                }

                // 不需要参数的operation, 如read
                if (Operation.opMap.get(in) == 0) {
                    String res = Operation.Process(in);
                    // 返回值判断是否需要如栈
                    if (!opStack.isEmpty()) {
                        nameStack.push(res);
                        addNameProcess();
                    }
                    isOp = true;
                } else {
                    opParmNumStack.push(Operation.opMap.get(in));
                    opStack.push(in);
                    isOp = true;
                }
            }

            if (!isOp) {
                addNameProcess();
            }
            return 0;
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }

    }

    private static void addNameProcess() {
        opParmNumStack.push(opParmNumStack.pop() - 1);
        if (opParmNumStack.peek() <= 0) {
            // 执行op功能
            opParmNumStack.pop();
            String res = Operation.Process(opStack.pop());

            if (!opStack.empty()) {
                nameStack.push(res);
                addNameProcess();
            }
        }
    }
}