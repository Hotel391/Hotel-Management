package services;

import dal.CustomerAccountDAO;
import dal.CustomerDAO;
import dal.EmployeeDAO;
import models.Customer;
import models.CustomerAccount;

/**
 *
 * @author TranTrungHieu
 */
public class RegisterService {

    private final CustomerDAO customerDAO = CustomerDAO.getInstance();
    private final CustomerAccountDAO customerAccountDAO = CustomerAccountDAO.getInstance();
    private final EmployeeDAO employeeDAO = EmployeeDAO.getInstance();

    public int registerCustomer(Customer customer) {
        return customerDAO.insertCustomer(customer);
    }

    public void registerAccount(CustomerAccount account) {
        customerAccountDAO.insertCustomerAccount(account);
    }

    public boolean isEmailExists(String email) {
        return customerDAO.getAllEmail().contains(email)
                || employeeDAO.getAllString("Email").contains(email);
    }
    public boolean isUsernameExists(String username){
        return customerAccountDAO.getAllUsername().contains(username) ||
                employeeDAO.getAllString("Username").contains(username);
    }
}
