package mua;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Infix {
    // TODO: 考虑 print (1 + print ((5 + 2) * mul 3 4))
    // 想法：将Parser变成需要实例化，在run，if，主函数，中缀处分别实例化

    public static String infixProcess(String infixstr) {
        // 去掉最外层表示中缀表达式整体的符号
        infixstr = infixstr.substring(1, infixstr.length() - 1);

        Processor processor = new Processor();
        // 分割表达式, 使得各种操作符都不与数字相连
        infixstr = spiltOp(infixstr);
        // 首先整体进行扫描，执行前缀表达式，将执行结果取代原前缀表达式
        String[] infixArr = infixstr.trim().split("\\s+");
        Queue<String> queue = new LinkedList<String>();
        String res = "initial";
        for (int i = 0; i < infixArr.length; i++) {
            if (res.isEmpty() || !Infix.isInfixElement(infixArr[i])) {
                res = processor.Process(infixArr[i]);
            } else {
                res = infixArr[i];
            }

            if (!res.isEmpty()) {
                queue.offer(res);
            }
        }

        return calculate(queue);
    }

    private static String spiltOp(String s) {
        String res = new String();
        res = s.replace("+", " + ");
        res = res.replace("-", " - ");
        res = res.replace("*", " * ");
        res = res.replace("/", " / ");
        res = res.replace("(", " ( ");
        res = res.replace(")", " ) ");
        return res;
    }

    private static String calculate(Queue<String> infix) {
        Queue<String> postfix = new LinkedList<String>();
        Stack<String> opstack = new Stack<String>();
        while (!infix.isEmpty()) {
            // 非op，数字直接入队
            if (!isInfixOp(infix.peek())) {
                postfix.offer(infix.poll());
                // op，根据当前栈情况进行考虑
            } else {
                // op栈空，直接入栈
                if (opstack.isEmpty()) {
                    opstack.push(infix.poll());
                }
                // 当前op为), 所有op出栈直到( 或栈空
                else if (infix.peek().equals(")")) {
                    while (!opstack.isEmpty() && !opstack.peek().equals("(")) {
                        postfix.offer(opstack.pop());
                    }
                    // 丢弃剩余的 ( 和 )
                    if (!opstack.isEmpty() && opstack.peek().equals("(")) {
                        opstack.pop();
                    }
                    infix.poll();
                }
                // 当前op为(, 直接入栈
                else if (infix.peek().equals("(")) {
                    opstack.push(infix.poll());
                }
                // 操作符入栈条件: 当前栈顶为(, 或优先级高
                else if (opstack.peek().equals("(") || getPriority(infix.peek()) > getPriority(opstack.peek())) {
                    opstack.push(infix.poll());
                }
                // 优先级相等或低, 需要出栈
                else {
                    while (!(opstack.isEmpty() || opstack.peek().equals("(")
                            || getPriority(infix.peek()) > getPriority(opstack.peek()))) {
                        postfix.offer(opstack.pop());
                    }
                    opstack.push(infix.poll());
                }
            }
        }

        // 队列已空, 将op全部出栈
        while (!opstack.isEmpty()) {
            postfix.offer(opstack.pop());
        }
        // 得到postfix, 后缀表达式
        // System.out.println(postfix);

        // 返回后缀表达式的计算结果
        return postcalc(postfix);
    }

    private static String postcalc(Queue<String> postfix) {
        Stack<String> oprandStack = new Stack<String>();
        String res = new String();
        while (!postfix.isEmpty()) {
            if (!isInfixOp(postfix.peek())) {
                oprandStack.push(postfix.poll());
            } else {
                String op = postfix.poll();
                String oprand2 = oprandStack.pop();
                String oprand1 = oprandStack.pop();
                switch (op) {
                    case "+":
                        res = String.valueOf(Double.valueOf(oprand1) + Double.valueOf(oprand2));
                        oprandStack.push(res);
                        break;
                    case "-":
                        res = String.valueOf(Double.valueOf(oprand1) - Double.valueOf(oprand2));
                        oprandStack.push(res);
                        break;
                    case "*":
                        res = String.valueOf(Double.valueOf(oprand1) * Double.valueOf(oprand2));
                        oprandStack.push(res);
                        break;
                    case "/":
                        res = String.valueOf(Double.valueOf(oprand1) / Double.valueOf(oprand2));
                        oprandStack.push(res);
                        break;
                    case "%":
                        int resint = Double.valueOf(oprand1).intValue() % Double.valueOf(oprand2).intValue();
                        res = String.valueOf(resint);
                        oprandStack.push(res);
                        break;

                    default:
                        break;
                }
            }
        }
        return oprandStack.pop();
    }

    private static int getPriority(String op) {
        // 设置运算符的优先级
        if (op.startsWith("+") || op.startsWith("-"))
            return 1;
        else if (op.startsWith("*") || op.startsWith("/") || op.startsWith("%"))
            return 2;
        else if (op.startsWith("(") || op.startsWith(")"))
            return 3;

        return 0;
    }

    private static boolean isInfixElement(String s) {
        if (Value.isNumber(s))
            return true;
        if (isInfixOp(s))
            return true;
        return false;
    }

    private static boolean isInfixOp(String s) {
        return s.startsWith("+") || s.startsWith("-") || s.startsWith("*") || s.startsWith("/") || s.startsWith("%")
                || s.startsWith("(") || s.startsWith(")");
    }
}
