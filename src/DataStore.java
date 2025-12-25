import java.util.ArrayList;
import java.util.List;

public class DataStore {
    // 靜態倉庫，全程式共用
    public static List<Package> packages = new ArrayList<>();
    public static List<Customer> customers = new ArrayList<>();

    // 初始化假資料 (這是為了讓隊友能測試功能)
    public static void initData() {
        // 1. 建立假客戶
        customers.add(new Customer("C001", "王小明", "0912345678", true));
        customers.add(new Customer("C002", "李大華", "0987654321", false));

        // 2. 建立假包裹
        Package p1 = new Package("TRK-1001", "C001", "張三", "台北市信義區", 2.5, "隔日達");
        packages.add(p1);

        System.out.println(">> 系統提示：假資料載入完成 (1位客戶, 1個包裹)");
    }
    
    // loadFromFile 和 saveToFile 先留空，等 Day 3 再做
    public static void loadFromFile() {}
    public static void saveToFile() {}
}