/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Hai Long
 */
public class Constants {

    private Constants() {
        // Prevent instantiation through reflection
        throw new AssertionError("Cannot instantiate Constants class");
    }

    public static final String GOOGLE_CLIENT_ID = "200776812058-qrg1li14uugvdeb351am8g4savbpjnvo.apps.googleusercontent.com";

    public static final String GOOGLE_CLIENT_SECRET = "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";

    public static final String GOOGLE_REDIRECT_URI = "http://localhost:9999/fptHotel/login";

    public static final String GOOGLE_LINK_GET_TOKEN = "https://accounts.google.com/o/oauth2/token";

    public static final String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

    public static final String GOOGLE_GRANT_TYPE = "authorization_code";
}
