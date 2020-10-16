package mua;

public class Parser {

    public static Processor processor = new Processor();

    public static int Process(String in) {
        if (in.equals("q") || in.equals("quit"))
            return -1;
        else {
            processor.Process(in);
        }
        return 0;
    }

}