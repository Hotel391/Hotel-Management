package models;

/**
 *
 * @author HieuTT
 */
public class EmployeeAccount implements AccountUser{
    
    private String username;
    private String password;

    public EmployeeAccount() {
    }

    public EmployeeAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
