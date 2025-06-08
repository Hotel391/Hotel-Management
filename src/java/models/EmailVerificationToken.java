package models;

import java.sql.Timestamp;

public class EmailVerificationToken {
    private int tokenId;
    private String fullname;
    private String email;
    private String username;
    private String password;
    private boolean gender;
    private String token;
    private Timestamp expiryDate;
    private boolean isUsed;

    public EmailVerificationToken() {
    }

    public EmailVerificationToken(int tokenId, String fullname, String email, String username, String password, boolean gender, String token, Timestamp expiryDate, boolean isUsed) {
        this.tokenId = tokenId;
        this.fullname = fullname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.token = token;
        this.expiryDate = expiryDate;
        this.isUsed = isUsed;
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    
}
