package models;
import java.time.LocalDateTime;

public class Account {

    private String account;
    private String password;
    private String name;
    private int role; //1-系統管理員 2-客服人員 3-倉儲人員 4-駕駛員
    private LocalDateTime createdAt;

    public Account() {
    }

    public Account(String account, String password, String name, int role, LocalDateTime createdAt) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }    

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }    

    public void setRole(int role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }    

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}