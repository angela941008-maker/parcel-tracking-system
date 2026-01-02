package models;
import java.time.LocalDateTime;

public class LoginToken {

    private String loginId;
    private int role; //1-系統管理員 2-客服人員 3-倉儲人員 4-駕駛員, 0-客戶
  

    public LoginToken() {
    }

    public LoginToken(String loginId, int role) {
        this.loginId = loginId;
 
        this.role = role;
    }

    public String getLoginId() {
        return loginId;
    }

    public int getRole() {
        return role;
    }    

}