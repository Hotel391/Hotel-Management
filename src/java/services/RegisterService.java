package services;

import dal.CustomerAccountDAO;
import dal.CustomerDAO;
import dal.EmailVerificationTokenDAO;
import dal.EmployeeDAO;
import models.Customer;
import models.CustomerAccount;
import models.EmailVerificationToken;

/**
 *
 * @author TranTrungHieu
 */
public class RegisterService {

    private final CustomerDAO customerDAO = CustomerDAO.getInstance();
    private final CustomerAccountDAO customerAccountDAO = CustomerAccountDAO.getInstance();
    private final EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
    private final EmailVerificationTokenDAO tokenDAO=EmailVerificationTokenDAO.getInstance();

    public void registerToken(EmailVerificationToken token) {
        tokenDAO.insertToken(token);
    }
    
    public int insertCustomer(Customer customer){
        return customerDAO.insertCustomer(customer);
    }
    
    public void inssertCustomerAccount(CustomerAccount account){
        customerAccountDAO.insertCustomerAccount(account);
    }
    
    public EmailVerificationToken getTokenByEmail(String email){
        return tokenDAO.getTokenByEmail(email);
    }
    
    public EmailVerificationToken getTokenByToken(String token){
        return tokenDAO.getTokenByToken(token);
    }
    
    public void updateToken(EmailVerificationToken token){
        tokenDAO.updateToken(token);
    }
    
    public void deleteConfirmedToken(int tokenId){
        tokenDAO.deleteToken(tokenId);
    }
    
    public void deleteTokenByEmail(String email){
        tokenDAO.deleteTokenByEmail(email);
    }

    public boolean isEmailExists(String email) {
        return customerDAO.getAllEmail().contains(email)
                || employeeDAO.getAllString("Email").contains(email);
    }
    public boolean isUsernameExists(String username){
        return customerAccountDAO.isUsernameExisted(username) ||
                employeeDAO.isUsernameExisted(username);
    }
    public boolean isUsernameExistInToken(String username){
        return tokenDAO.checkExistedUsername(username);
    }
    public boolean isUsernameExistInToken(String username){
        return tokenDAO.checkExistedUsername(username);
    }
}
