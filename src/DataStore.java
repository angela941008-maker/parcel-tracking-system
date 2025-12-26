import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class DataStore {
    public static List<Package> packages = new ArrayList<>();
    public static List<Customer> customers = new ArrayList<>();

    public static void initData() {
        // ★ [修改] 1. 建立假客戶 (補上 email 和 address)
        customers.add(new Customer("C001", "王小明", "0912345678", "wang@email.com", "台北市大安區", true));
        customers.add(new Customer("C002", "李大華", "0987654321", "lee@email.com", "新北市板橋區", false));

        // ★ [修改] 2. 建立假包裹 (補上 dimensions, value, content)
        Package p1 = new Package("TRK-1001", "C001", "張三", "台北市信義區", 2.5, "隔日達",
                                 "30x20x10", 1000.0, "書籍");
        packages.add(p1);

        System.out.println(">> 系統提示：假資料載入完成");
    }

    // 1. 存檔功能
    public static void saveToFile() {
        // A. 儲存客戶
        try (PrintWriter writer = new PrintWriter(new FileWriter("customers.csv"))) {
            for (Customer c : customers) {
                // ★ [修改] 格式: ID,Name,Phone,Email,Address,IsContract
                String line = c.getCustomerId() + "," + c.getName() + "," + c.getPhone() + "," + 
                              c.getEmail() + "," + c.getAddress() + "," + c.isContract();
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("客戶存檔失敗: " + e.getMessage());
        }

        // B. 儲存包裹
        try (PrintWriter writer = new PrintWriter(new FileWriter("packages.csv"))) {
            for (Package p : packages) {
                // ★ [修改] 格式: TrackingNum,SenderId,ReceiverName,ReceiverAddr,Weight,ServiceType,Dims,Value,Desc
                String line = p.getTrackingNumber() + "," + p.getSenderId() + "," +
                              p.getReceiverName() + "," + p.getReceiverAddress() + "," +
                              p.getWeight() + "," + p.getServiceType() + "," +
                              p.getDimensions() + "," + p.getDeclaredValue() + "," + p.getContentDescription();
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("包裹存檔失敗: " + e.getMessage());
        }
        
        // C. 儲存追蹤紀錄 (這部分不用改，原本的就好)
        try (PrintWriter writer = new PrintWriter(new FileWriter("events.csv"))) {
            for (Package p : packages) {
                for (TrackingEvent e : p.getEventHistory()) {
                    String line = p.getTrackingNumber() + "," + e.getTimestamp() + "," +
                                  e.getLocation() + "," + e.getStatus() + "," + e.getDescription();
                    writer.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("追蹤紀錄存檔失敗: " + e.getMessage());
        }

        System.out.println(">> [系統] 所有資料已成功儲存至檔案！");
    }

    // 2. 讀檔功能
    public static void loadFromFile() {
        customers.clear();
        packages.clear();

        // A. 讀取客戶
        File cFile = new File("customers.csv");
        if (cFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    // ★ [修改] 因為欄位變多，現在長度要是 6
                    if (parts.length >= 6) {
                        // parts[3]=Email, parts[4]=Address, parts[5]=IsContract
                        boolean isContract = Boolean.parseBoolean(parts[5]);
                        customers.add(new Customer(parts[0], parts[1], parts[2], parts[3], parts[4], isContract));
                    }
                }
            } catch (IOException e) {
                System.out.println("讀取客戶失敗: " + e.getMessage());
            }
        }

        // B. 讀取包裹
        File pFile = new File("packages.csv");
        if (pFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    // ★ [修改] 因為欄位變多，現在長度要是 9
                    if (parts.length >= 9) {
                        double weight = Double.parseDouble(parts[4]);
                        // parts[6]=Dims, parts[7]=Value, parts[8]=Desc
                        double value = Double.parseDouble(parts[7]);
                        
                        Package pkg = new Package(parts[0], parts[1], parts[2], parts[3], weight, parts[5],
                                                  parts[6], value, parts[8]);
                        
                        pkg.getEventHistory().clear(); 
                        packages.add(pkg);
                    }
                }
            } catch (IOException e) {
                System.out.println("讀取包裹失敗: " + e.getMessage());
            }
        }
        
        // C. 讀取追蹤紀錄 (這部分也不用改)
        File eFile = new File("events.csv");
        if (eFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(eFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String trackingNum = parts[0];
                        for (Package p : packages) {
                            if (p.getTrackingNumber().equals(trackingNum)) {
                                p.getEventHistory().add(new TrackingEvent(parts[1], parts[2], parts[3], parts[4]));
                                break;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("讀取追蹤紀錄失敗: " + e.getMessage());
            }
        }

        System.out.println(">> [系統] 資料讀取完成");
    }
}