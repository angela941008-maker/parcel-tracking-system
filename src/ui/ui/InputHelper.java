import java.util.Scanner;

public class InputHelper {
    private final Scanner sc = new Scanner(System.in);

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) {
                    System.out.println("請輸入範圍 " + min + " ~ " + max);
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("格式錯誤，請輸入整數。");
            }
        }
    }

    public double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                double v = Double.parseDouble(s);
                if (v < min || v > max) {
                    System.out.println("請輸入範圍 " + min + " ~ " + max);
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("格式錯誤，請輸入數字。");
            }
        }
    }

    public String readString(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (!allowEmpty && s.trim().isEmpty()) {
                System.out.println("不可空白，請重新輸入。");
                continue;
            }
            return s.trim();
        }
    }
}
