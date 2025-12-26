public class Customer {
    private String customerId;
    private String name;
    private String phone;
    // ★ [新增] 依據需求補上 Email 與 地址
    private String email;
    private String address;
    
    private boolean isContract; 

    // ★ [修改] 建構子 (Constructor) 增加 email 和 address 參數
    public Customer(String customerId, String name, String phone, String email, String address, boolean isContract) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        // ★ [新增] 初始化新欄位
        this.email = email;
        this.address = address;
        this.isContract = isContract;
    }

    @Override
    public String toString() {
        // ★ [修改] toString 也印出新資訊
        return "客戶ID: " + customerId + " | 姓名: " + name + " | Email: " + email + " | 合約: " + (isContract ? "是" : "否");
    }

    // Getters
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone;}
    
    // ★ [新增] 新欄位的 Getters (存檔時會用到)
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    
    public boolean isContract() { return isContract; }
}