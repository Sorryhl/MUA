package mua;

import java.util.Scanner;

/**
 * Main
 */
public class Main {
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        int res;
        while (true) {
            res = Parser.Process(input.next());
            if (res == -1)
                break;
        }

        input.close();
    }
}