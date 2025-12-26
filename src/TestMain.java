public class TestMain {
    public static void main(String[] args) {
        System.out.println("========== 1. 初始化資料 ==========");
        DataStore.initData(); // 載入假資料
        
        System.out.println("\n========== 2. 測試存檔 ==========");
        DataStore.saveToFile(); // 應該會產生 customers.csv, packages.csv, events.csv
        
        System.out.println("\n========== 3. 清除記憶體並測試讀檔 ==========");
        // 為了證明是真的從檔案讀的，我們先把記憶體清空
        DataStore.packages.clear();
        DataStore.customers.clear();
        System.out.println("記憶體已清空，目前包裹數: " + DataStore.packages.size());
        
        DataStore.loadFromFile(); // 讀取檔案
        
        System.out.println("\n========== 4. 驗證讀取結果 ==========");
        System.out.println("讀取後包裹數: " + DataStore.packages.size());
        for (Package p : DataStore.packages) {
            System.out.println(p); // 印出包裹資訊
            // 印出該包裹的歷史紀錄
            for (TrackingEvent e : p.getEventHistory()) {
                System.out.println("  -> " + e);
            }
        }
    }
}