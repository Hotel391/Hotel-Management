package Models;

public class CustomerAccount {

    private String username;
    private String password;
    private Customer customer;

    public CustomerAccount() {
    }

    public CustomerAccount(String username, String password, Customer customer) {
        this.username = username;
        this.password = password;
        this.customer = customer;
    }
    
    
}
