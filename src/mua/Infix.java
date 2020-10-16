package mua;

public class Infix {
    // TODO: 考虑 print (1 + print (2 * mul 12))
    // 想法：将Parser变成需要实例化，在run，if，主函数，中缀处分别实例化

    public static String infixProcess(String infixstr) {
        // 去掉最外层表示中缀表达式整体的符号
        infixstr = infixstr.substring(1, infixstr.length() - 1);

        Processor processor = new Processor();
        // 首先整体进行扫描，执行前缀表达式，将执行结果取代原前缀表达式
        String[] infixArr = infixstr.split(" ");
        boolean processing = false;
        for (int i = 0; i < infixArr.length; i++) {
            if (processing || !Infix.isInfixElement(infixArr[i])) {
                processing = true;
                processor.Process(infixArr[i]);
            }
        }

        return null;
    }

    public static boolean isInfixElement(String s) {
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
