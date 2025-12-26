import java.util.ArrayList;
import java.util.List;
import java.io.*;

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
// --- 以下是檔案存取功能 (複製貼上這裡) ---

    // 1. 存檔功能：將記憶體中的資料寫入 CSV 檔案
    public static void saveToFile() {
        // A. 儲存客戶資料 (customers.csv)
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter("customers.csv"))) {
            for (Customer c : customers) {
                // 格式: ID,Name,Phone,IsContract
                String line = c.getCustomerId() + "," + c.getName() + "," + c.getPhone() + "," + c.isContract();
                writer.println(line);
            }
        } catch (java.io.IOException e) {
            System.out.println("客戶存檔失敗: " + e.getMessage());
        }

        // B. 儲存包裹資料 (packages.csv)
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter("packages.csv"))) {
            for (Package p : packages) {
                // 格式: TrackingNum,SenderId,ReceiverName,ReceiverAddr,Weight,ServiceType
                String line = p.getTrackingNumber() + "," + p.getSenderId() + "," +
                              p.getReceiverName() + "," + p.getReceiverAddress() + "," +
                              p.getWeight() + "," + p.getServiceType();
                writer.println(line);
            }
        } catch (java.io.IOException e) {
            System.out.println("包裹存檔失敗: " + e.getMessage());
        }
        
        // C. (進階) 儲存追蹤紀錄 (events.csv) - 這樣歷史紀錄才不會丟失
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter("events.csv"))) {
            for (Package p : packages) {
                for (TrackingEvent e : p.getEventHistory()) {
                    // 格式: TrackingNum,Time,Location,Status,Desc
                    String line = p.getTrackingNumber() + "," + e.getTimestamp() + "," +
                                  e.getLocation() + "," + e.getStatus() + "," + e.getDescription();
                    writer.println(line);
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("追蹤紀錄存檔失敗: " + e.getMessage());
        }

        System.out.println(">> [系統] 所有資料已成功儲存至檔案！");
    }

    // 2. 讀檔功能：程式啟動時，把檔案讀回來
    public static void loadFromFile() {
        // 先清空目前的記憶體，避免重複載入
        customers.clear();
        packages.clear();

        // A. 讀取客戶
        java.io.File cFile = new java.io.File("customers.csv");
        if (cFile.exists()) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(cFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        // 重新建立 Customer 物件
                        boolean isContract = Boolean.parseBoolean(parts[3]);
                        customers.add(new Customer(parts[0], parts[1], parts[2], isContract));
                    }
                }
            } catch (java.io.IOException e) {
                System.out.println("讀取客戶失敗: " + e.getMessage());
            }
        }

        // B. 讀取包裹
        java.io.File pFile = new java.io.File("packages.csv");
        if (pFile.exists()) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(pFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        double weight = Double.parseDouble(parts[4]);
                        Package pkg = new Package(parts[0], parts[1], parts[2], parts[3], weight, parts[5]);
                        
                        // ★重要修正：因為 new Package() 會自動產生一筆「剛建立」的初始紀錄，
                        // 但我們是讀取舊檔，所以要先把那筆自動產生的清掉，以免重複
                        pkg.getEventHistory().clear(); 
                        
                        packages.add(pkg);
                    }
                }
            } catch (java.io.IOException e) {
                System.out.println("讀取包裹失敗: " + e.getMessage());
            }
        }
        
        // C. 讀取追蹤紀錄 (把歷史紀錄掛回去包裹身上)
        java.io.File eFile = new java.io.File("events.csv");
        if (eFile.exists()) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(eFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String trackingNum = parts[0];
                        // 尋找這筆紀錄屬於哪個包裹
                        for (Package p : packages) {
                            if (p.getTrackingNumber().equals(trackingNum)) {
                                // 直接加回去 List 裡面
                                p.getEventHistory().add(new TrackingEvent(parts[1], parts[2], parts[3], parts[4]));
                                break;
                            }
                        }
                    }
                }
            } catch (java.io.IOException e) {
                System.out.println("讀取追蹤紀錄失敗: " + e.getMessage());
            }
        }

        System.out.println(">> [系統] 資料讀取完成 (客戶: " + customers.size() + ", 包裹: " + packages.size() + ")");
    }
}