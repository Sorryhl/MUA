import mua.Infix;

public class Test {
    public static void main(String[] args) {
        System.out.println(Infix.infixProcess("(1 + print ((5 + 2) * mul 3 4))"));
    }
}
