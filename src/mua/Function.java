package mua;

import java.util.HashMap;
import java.util.Map;

public class Function {
    /**
     * FuncBody, 函数内部的构造方式
     */
    public static class FuncBody {
        public String[] argslist; // 参数表
        public int argnum; // 参数个数
        public String[] runcode; // 执行代码

        FuncBody(String[] als, int anum, String[] rc) {
            argslist = als;
            argnum = anum;
            runcode = rc;
        }
    }

    /** 函数表，函数名到函数体的映射 */
    private Map<String, FuncBody> funMap;
    private Value localvalue;
    boolean infunc;

    public Function() {
        funMap = new HashMap<String, FuncBody>();
        localvalue = new Value();
        infunc = false;
    }

    public Function(boolean inFunc, Value locValue) {
        funMap = new HashMap<String, FuncBody>();
        this.localvalue = locValue;
        this.infunc = inFunc;
    }

    /**
     * 判断一个name是否为函数，并将其加入函数表
     * 
     * @param funcname String：需要判断的函数名
     * @return bool：是否为函数
     */
    public boolean isFunction(String funcname) {
        // 函数表中已有
        if (funMap.containsKey(funcname)) {
            return true;
        }
        // 函数表中没有，查找变量表 TODO: 局部函数定义
        // 局部变量查找
        String definition;
        if (infunc && localvalue.hasName(funcname)) {
            definition = localvalue.getValue(funcname);
        } else if (Processor.globalvalue.hasName(funcname)) {
            definition = Processor.globalvalue.getValue(funcname);
        } else {
            return false;
        }
        return makeFunc(funcname, definition);
    }

    private boolean makeFunc(String funcname, String definition) {
        if (!Value.isList(definition))
            return false;
        definition = definition.substring(1, definition.length() - 1).trim();
        // 尝试获得参数表与执行代码
        try {
            int arglsindex = getArglistIndex(definition);
            // 只有一个表
            if (arglsindex == definition.length() - 1)
                throw new Exception();
            String argstr = definition.substring(1, arglsindex).trim();
            String[] argsls = argstr.split(" ");
            String rest = definition.substring(arglsindex + 1).trim();
            // 执行部分不只一个list
            // if (rest.indexOf("]") != rest.length() - 1)
            // throw new Exception();
            String[] codels = rest.substring(1, rest.length() - 1).trim().split(" ");

            FuncBody newfun;
            if (argstr.isEmpty()) {
                newfun = new FuncBody(null, 0, codels);
            } else {
                newfun = new FuncBody(argsls, argsls.length, codels);
            }

            funMap.put(funcname, newfun);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOldFunction(String funcname) {
        return funMap.containsKey(funcname);
    }

    public void rewriteFunc(String funcname, String definition) {
        // 尝试重写函数
        try {
            if (!Value.isList(definition))
                throw new Exception();
            definition = definition.substring(1, definition.length() - 1).trim();
            int arglsindex = getArglistIndex(definition);
            // 只有一个表
            if (arglsindex == definition.length() - 1)
                throw new Exception();
            String argstr = definition.substring(1, arglsindex);
            String[] argsls = argstr.split(" ");
            String rest = definition.substring(arglsindex + 1).trim();
            // 执行部分不只一个list
            // if (rest.indexOf("]") != rest.length() - 1)
            // throw new Exception();
            String[] codels = rest.substring(1, rest.length() - 1).trim().split(" ");

            FuncBody newfun;
            if (argstr.isEmpty()) {
                newfun = new FuncBody(null, 0, codels);
            } else {
                newfun = new FuncBody(argsls, argsls.length, codels);
            }

            funMap.put(funcname, newfun);
        } catch (Exception e) {
            // 重写失败，说明新make了非函数形式的内容
            funMap.remove(funcname);
        }
    }

    private int getArglistIndex(String definition) {
        int listlevel = 0;
        int i = 0;
        for (i = 0; i < definition.length(); i++) {
            if (definition.charAt(i) == '[') {
                listlevel++;
            }
            if (definition.charAt(i) == ']') {
                listlevel--;
                if (listlevel == 0)
                    return i;
            }
        }
        return i;
    }

    public int getArgNum(String funcname) {
        return funMap.get(funcname).argnum;
    }

    public String Process(String funcname, String[] parmlist) {
        FuncBody funcbody = funMap.get(funcname);
        Value locvalue = new Value();
        for (int i = 0; i < funcbody.argnum; i++) {
            locvalue.makeName(funcbody.argslist[i], parmlist[parmlist.length - 1 - i]);
        }
        locvalue.makeName("@return", "");

        String res = "";
        Processor processor = new Processor(true, locvalue);
        for (String code : funcbody.runcode) {
            res = processor.Process(code);
            if (!locvalue.getValue("@return").isEmpty()) {
                res = locvalue.getValue("@return");
                break;
            }
        }

        return res;
    }
}
