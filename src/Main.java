public class Main {
    public static void main(String[] args) {
        // 1. 程式一啟動，先讀檔
        System.out.println("正在載入資料庫...");
        DataStore.loadFromFile();

        // 如果讀不到檔案(第一次執行)，載入預設假資料
        if (DataStore.packages.isEmpty()) {
            DataStore.initData();
        }

        // 2. 啟動選單
        new Menu().start();

        // 3. 程式結束前，強制存檔
        System.out.println("正在儲存資料...");
        DataStore.saveToFile();

        System.out.println("系統已結束，謝謝使用。");
    }
}