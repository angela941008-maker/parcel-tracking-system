import java.util.Scanner;

public class InputHelper {
    private final Scanner sc;

    public InputHelper(Scanner sc) {
        this.sc = sc;
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("請輸入整數，重新輸入。");
            }
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("請輸入數字(可小數)，重新輸入。");
            }
        }
    }
}
