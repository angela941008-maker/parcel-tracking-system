package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import models.Package;
import models.TrackingEvent;
import models.Customer;
import java.time.LocalDateTime;


public class DataStore {
    public static List<Package> packages = new ArrayList<>();
    public static List<Customer> customers = new ArrayList<>();
    public static List<Account> accounts = new ArrayList<>();
    public static final HashMap<Integer,int[]> roleOpers;
    public static final HashMap<Integer,String> OperDesc;
    
    
    //roleId 0- 客戶, 1-系統管理員 2-客服人員 3-倉儲人員 4-駕駛員
    
    //operid 1. 建立客戶資料
    //operid 2. 建立包裹
    //operid 3. 查詢包裹/歷史紀錄
    //operid 4. 更新包裹狀態
    //operid 5. 計算運費
    //operid 9. 登出
    //operid 0. 離開
    static {
        roleOpers = new HashMap<>();
        roleOpers.put(0, new int[]{3,9});
        roleOpers.put(1, new int[]{1, 2,3,4,5,9,0});
        roleOpers.put(2, new int[]{1, 2,3,5,9});
        roleOpers.put(3, new int[]{3,4,9});
        roleOpers.put(4, new int[]{3,4,9});
    }
    
    static {
    	OperDesc = new HashMap<>();
    	OperDesc.put(1, "建立客戶資料");
    	OperDesc.put(2, "建立包裹");
    	OperDesc.put(3, "查詢包裹/歷史紀錄");
        OperDesc.put(4, "更新包裹狀態");
        OperDesc.put(5, "計算運費");
        OperDesc.put(9, "登出");
        OperDesc.put(0, "離開");
        
    }
    
    
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private static void chkFolderExistAndCreate(){
    	String folderPath = "data";
    	File folder = new File(folderPath);

        // 檢查資料夾是否存在，如果不存在且不是目錄
        if (!folder.exists() && !folder.isDirectory()) {
            // 創建資料夾（mkdirs可以創建多層路徑）
            boolean success = folder.mkdirs();
            if (success) {
                System.out.println("資料夾已創建: " + folderPath);
            } else {
                System.out.println("資料夾創建失敗: " + folderPath);
            }
        } else {
            System.out.println("資料夾已存在: " + folderPath);
        }
    }
    
    //檢查客戶ID(寄件人ID)是否存在
    public static boolean chkCustomerExist(String _custId){
    	boolean isFind=false;
    	for(Customer cust:customers)
    	{
    		if (cust.getCustomerId().equalsIgnoreCase(_custId))
    		{
    			isFind= true;
    			break;
    		}
    	
    		
    	}
    	return isFind;
    }
    
    public static void initData() {
    	
        // [修改] 1. 建立假客戶 (補上 email 和 address)
        customers.add(new Customer("C001", "王小明", "0912345678", "wang@email.com", "台北市大安區", "A"));
        customers.add(new Customer("C002", "李大華", "0987654321", "lee@email.com", "新北市板橋區", "B"));

        // [修改] 2. 建立假包裹 (補上 dimensions, value, content)
        Package p1 = new Package("TRK-1001", "C001", "張三", "台北市信義區", 2.5, "隔日達",
                                 "30x20x10", 1000.0, "書籍");
        p1.setFee(9999);
        packages.add(p1);
        
     
        accounts.add(new Account(
                "admin",
                "123456",
                "系統管理員",
                1,
                LocalDateTime.now()
        ));
     
        accounts.add(new Account(
                "operA",
                "123456",
                "客服人員",
                2,
                LocalDateTime.now()
        ));
        accounts.add(new Account(
                "operB",
                "123456",
                "倉儲人員",
                3,
                LocalDateTime.now()
        ));
        accounts.add(new Account(
                "operC",
                "123456",
                "駕駛員",
                4,
                LocalDateTime.now()
        ));
        saveToFile();
        System.out.println(">> 系統提示：假資料載入完成");
    }

    
    // 1. 存檔功能
    public static void saveToFile() {
        // A. 儲存客戶
        try (PrintWriter writer = new PrintWriter(new FileWriter("data/customers.csv"))) {
            for (Customer c : customers) {
                // [修改] 格式: ID,Name,Phone,Email,Address,IsContract
                String line = c.getCustomerId() + "," + c.getName() + "," + c.getPhone() + "," + 
                              c.getEmail() + "," + c.getAddress() + "," + c.getContractType();
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("客戶存檔失敗: " + e.getMessage());
        }

        // B. 儲存包裹
        try (PrintWriter writer = new PrintWriter(new FileWriter("data/packages.csv"))) {
            for (Package p : packages) {
                // [修改] 格式: TrackingNum,SenderId,ReceiverName,ReceiverAddr,Weight,ServiceType,Dims,Value,Desc
                String line = p.getTrackingNumber() + "," + p.getSenderId() + "," +
                              p.getReceiverName() + "," + p.getReceiverAddress() + "," +
                              p.getWeight() + "," + p.getServiceType() + "," +
                              p.getDimensions() + "," + p.getDeclaredValue() + "," + p.getContentDescription()+ "," + p.getFee();
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("包裹存檔失敗: " + e.getMessage());
        }
        
        // C. 儲存追蹤紀錄 (這部分不用改，原本的就好)
        try (PrintWriter writer = new PrintWriter(new FileWriter("data/events.csv"))) {
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

        // D. 帳號管理系統寫入CSV
        try (PrintWriter writer = new PrintWriter(new FileWriter("data/account.csv"))) {
	        for (Account acc : accounts) {
	        	String line = String.join(",",
	                    acc.getAccount(),
	                    acc.getPassword(),
	                    acc.getName(),
	                    String.valueOf(acc.getRole()),
	                    acc.getCreatedAt().format(FORMATTER));
	        	
	        	writer.println(line);
	        }
        }catch (IOException e) {
            System.out.println("帳號管理資料存檔失敗: " + e.getMessage());
        }
        
        
        
       
        
        
        System.out.println(">> [系統已儲存]！");
    }

    // 2. 讀檔功能
    public static void loadFromFile() {
    	chkFolderExistAndCreate();
        customers.clear();
        packages.clear();

        // A. 讀取客戶
        File cFile = new File("data/customers.csv");
        if (cFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    // [修改] 因為欄位變多，現在長度要是 6
                    if (parts.length >= 6) {
                        // parts[3]=Email, parts[4]=Address, parts[5]=IsContract
                        boolean isContract = Boolean.parseBoolean(parts[5]);
                        customers.add(new Customer(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                    }
                }
            } catch (IOException e) {
                System.out.println("讀取客戶失敗: " + e.getMessage());
            }
        }

        // B. 讀取包裹
        File pFile = new File("data/packages.csv");
        if (pFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    // [修改] 因為欄位變多，現在長度要是 10
                    if (parts.length >= 10) {
                        double weight = Double.parseDouble(parts[4]);
                        //parts[6]=Dims, parts[7]=Value, parts[8]=desc
                        double value = Double.parseDouble(parts[7]);
                        
                        Package pkg = new Package(parts[0], parts[1], parts[2], parts[3], weight, parts[5],
                                                  parts[6], value, parts[8]);
                        pkg.setFee(Double.parseDouble(parts[9]));
                        
                        pkg.getEventHistory().clear(); 
                        packages.add(pkg);
                    }
                }
            } catch (IOException e) {
                System.out.println("讀取包裹失敗: " + e.getMessage());
            }
        }
        
        // C. 讀取追蹤紀錄 (這部分也不用改)
        File eFile = new File("data/events.csv");
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

     // D. 讀取帳號資料 (這部分也不用改)
        File accountFile = new File("data/account.csv");
        if (accountFile.exists()) {
        	try (BufferedReader reader = new BufferedReader(new FileReader(accountFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
           
                    if (parts.length ==5) {
                    	Account item=new Account(parts[0], parts[1], parts[2],Integer.parseInt(parts[3]),LocalDateTime.parse(parts[4], FORMATTER));
                        accounts.add(item);
                    }
                }
            } catch (IOException e) {
                System.out.println("讀取帳號資料: " + e.getMessage());
            }
        }
        
        System.out.println(">> [系統] 資料讀取完成");
    }
}