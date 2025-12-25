public class Customer {
    private String customerId;
    private String name;
    private String phone;
    private boolean isContract; // true=月結客戶, false=非合約

    public Customer(String customerId, String name, String phone, boolean isContract) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.isContract = isContract;
    }

    @Override
    public String toString() {
        return "客戶ID: " + customerId + " | 姓名: " + name + " | 合約: " + (isContract ? "是" : "否");
    }

    // Getters
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public boolean isContract() { return isContract; }
}