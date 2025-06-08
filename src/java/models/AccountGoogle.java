/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Hai Long
 */
public class AccountGoogle {
    private String id;

	private String email;

	private boolean verified;

	private String name;

	private String givenName;

	private String familyNae;

	private String picture;

    public AccountGoogle() {
    }

    public AccountGoogle(String id, String email, boolean verified, String name, String givenName, String familyNae, String picture) {
        this.id = id;
        this.email = email;
        this.verified = verified;
        this.name = name;
        this.givenName = givenName;
        this.familyNae = familyNae;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyNae() {
        return familyNae;
    }

    public void setFamilyNae(String familyNae) {
        this.familyNae = familyNae;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "AccountGoogle{" + "id=" + id + ", email=" + email + ", verified=" + verified + ", name=" + name + ", givenName=" + givenName + ", familyNae=" + familyNae + ", picture=" + picture + '}';
    }

	
}
