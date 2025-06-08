/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.Connection;
import models.AccountGoogle;
import models.Constants;
import org.apache.catalina.User;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

/**
 *
 * @author Hai Long
 */
public class AccountGoogleDAO {
    private static AccountGoogleDAO instance;
    private Connection con;

    public static AccountGoogleDAO getInstance() {
        if (instance == null) {
            instance = new AccountGoogleDAO();
        }
        return instance;
    }

    private AccountGoogleDAO() {
        con = new DBContext().connect;
    }
    
    public String getToken(String code) throws ClientProtocolException, IOException {
        
        String response = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", Constants.GOOGLE_CLIENT_ID)
                        .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI).add("code", code)
                        .add("grant_type", Constants.GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }

    public AccountGoogle getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = Constants.GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();

        AccountGoogle googlePojo = new Gson().fromJson(response, AccountGoogle.class);

        return googlePojo;
    }
}
