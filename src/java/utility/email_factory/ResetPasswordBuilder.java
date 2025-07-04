package utility.email_factory;

import java.util.Map;

/**
 *
 * @author HieuTT
 */
public class ResetPasswordBuilder implements EmailContentBuilder{

    @Override
    public String build(Map<String, Object> data) {
        String username = (String) data.get("username");
        String link = (String) data.get("confirmLink");
        return """
            <html>
              <body style="font-family: Arial, Helvetica, sans-serif; background-color: #f4f4f4;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                  <h2 style="color: #2c3e50;">Yêu cầu đặt lại mật khẩu</h2>
                  <p>Chào <b>${username}</b>,</p>
                  <p>Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản của mình. Vui lòng nhấn vào nút bên dưới để tiếp tục:</p>
                  <p style="text-align: center; margin: 30px 0;">
                    <a href="${confirmLink}" style="background-color: #e67e22; color: #ffffff; padding:12px 20px; border-radius: 5px; text-decoration: none; font-weight: bold;">
                      Đặt lại mật khẩu
                    </a>
                  </p>
                  <p>Nếu bạn không yêu cầu thao tác này, vui lòng bỏ qua email này.</p>
                  <hr style="margin: 30px 0;">
                  <p style="font-size: 12px; color: #888888;">Đây là email tự động. Vui lòng không trả lời.</p>
                </div>
              </body>
            </html>
            """.replace("${username}", username).replace("confirmLink", link);
    }
    
}
