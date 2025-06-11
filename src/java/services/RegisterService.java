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
public class RegisterService implements IRegisterService {

    private final CustomerDAO customerDAO = CustomerDAO.getInstance();
    private final CustomerAccountDAO customerAccountDAO = CustomerAccountDAO.getInstance();
    private final EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
    private final EmailVerificationTokenDAO tokenDAO=EmailVerificationTokenDAO.getInstance();

    @Override
    public void registerToken(EmailVerificationToken token) {
        tokenDAO.insertToken(token);
    }
    
    @Override
    public int insertCustomer(Customer customer){
        return customerDAO.insertCustomer(customer);
    }
    
    @Override
    public void inssertCustomerAccount(CustomerAccount account){
        customerAccountDAO.insertCustomerAccount(account);
    }
    
    @Override
    public EmailVerificationToken getTokenByEmail(String email){
        return tokenDAO.getTokenByEmail(email);
    }
    
    @Override
    public EmailVerificationToken getTokenByToken(String token){
        return tokenDAO.getTokenByToken(token);
    }
    
    @Override
    public void updateToken(EmailVerificationToken token){
        tokenDAO.updateToken(token);
    }
    
    @Override
    public void deleteConfirmedToken(int tokenId){
        tokenDAO.deleteToken(tokenId);
    }
    
    @Override
    public void deleteTokenByEmail(String email){
        tokenDAO.deleteTokenByEmail(email);
    }

    @Override
    public boolean isEmailExists(String email) {
        return customerDAO.getAllEmail().contains(email)
                || employeeDAO.getAllString("Email").contains(email);
    }
    @Override
    public boolean isUsernameExists(String username){
        return customerAccountDAO.isUsernameExisted(username) ||
                employeeDAO.isUsernameExisted(username);
    }
    @Override
    public boolean isUsernameExistInToken(String username){
        return tokenDAO.checkExistedUsername(username);
    }
}
