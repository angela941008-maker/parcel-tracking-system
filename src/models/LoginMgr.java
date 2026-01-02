package models;
import java.util.List;

import models.Account;
import models.DataStore;
import models.LoginToken;
import models.Package;
import models.Customer;
public class LoginMgr {

	
	
  
	//登入
    public LoginToken doLogin(String _account,String _passwd) {
    	LoginToken ret=null;
        List<Account> accs = DataStore.accounts;
       
        for (Account act : accs) {
            if (act.getAccount().equalsIgnoreCase(_account) && act.getPassword().equals(_passwd))
            {
            	ret=new LoginToken(act.getAccount(),act.getRole());
            	break;
            }
            	
        }
        
        if (ret==null)
        {
        	List<Customer> cts = DataStore.customers;
           
            for (Customer act : cts) {
                if (act.getCustomerId().equalsIgnoreCase(_account) && act.getPhone().equals(_passwd))
                {
                	ret=new LoginToken(act.getCustomerId(),0);
                	break;
                }
                	
            }
        }
        
        return ret;
       
    }
    
    //檢查有無該作業id的權限
    public boolean chkHavePrivilege(int theOperId, int[] myOperIds){
    	for (int item: myOperIds)
    	{
    		if (item==theOperId)
    			return true;
    	}
    	return false;
    }
    
    
}