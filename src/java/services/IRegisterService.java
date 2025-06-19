package services;

import models.AccountUser;
import models.Customer;
import models.CustomerAccount;
import models.EmailVerificationToken;

/**
 *
 * @author HieuTT
 */
public interface IRegisterService {
    void registerToken(EmailVerificationToken token);

    int insertCustomer(Customer customer);

    void insertCustomerAccount(CustomerAccount account);

    EmailVerificationToken getTokenByEmail(String email);

    EmailVerificationToken getTokenByToken(String token);

    void updateToken(EmailVerificationToken token);

    void deleteConfirmedToken(int tokenId);

    void deleteTokenByEmail(String email);

    boolean isEmailExists(String email);

    boolean isUsernameExists(String username);

    boolean isUsernameExistInToken(String username);
    
    AccountUser getAccountByEmail(String email);

    void resetPassword(String email, String newPassword);

    int getCustomerIdByEmail(String email);

    boolean isEmailExistsInCustomer(String email);

    void updateCustomerInfo(int customerId, String fullName, boolean gender);
}
