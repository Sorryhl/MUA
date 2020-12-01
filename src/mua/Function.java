package mua;

import java.util.HashMap;
import java.util.Map;

public class Function {
    /**
     * FuncBody, 函数内部的构造方式
     */
    public static class FuncBody {
        String argslist; // 参数表
        int argnum; // 参数个数
        String runcode; // 执行代码
    }

    /** 函数表，函数名到函数体的映射 */
    private Map<String, FuncBody> funMap = new HashMap<String, FuncBody>();

    public boolean isFunction(String funcname) {
        // 函数表中已有
        if (funMap.containsKey(funcname)) {
            return true;
        }
        // 函数表中没有，查找变量表
        if (true)
            ;
        return false;
    }
}
