package utility;

/**
 *
 * @author TranTrungHieu
 */
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

    private final int LIMIT_MINUS = 5;
    private String from = "fpthotel@gmail.com";
    private String password = "jcfu lbfu zxvz mpkc";

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(LIMIT_MINUS);
    }

    public boolean isExpireTime(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }

    public Email() {
    }

    public void sendEmail(String to) {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject("5560");
            message.setContent(htmlConfirmEmail, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            //
        }
    }
    private String htmlConfirmEmail = """
<html>
    <body style="font-family: Arial, Helvetica, sans-serif; background-color: #f4f4f4;">
        <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
            <h2 style="color: #2c3e50;">Xác minh địa chỉ email của bạn</h2>
            <p>Chào <b>${username}</b>,</p>
            <p>Cảm ơn bạn đã đăng ký tài khoản. Vui lòng xác nhận email của bạn bằng cách nhấn nút bên dưới:</p>
            <p style="text-align: center; margin: 30px 0;">
                <a href="${confirmLink}" style="background-color: #3498db; color: #ffffff; padding:12px 20px; border-radius: 5px; text-decoration: none; font-weight: bold;">
                    Xác nhận email
                </a>
            </p>    
            <p>Nếu bạn không tạo tài khoản này, vui lòng bỏ qua email này.</p> 
            <hr style="margin: 30px 0;">
            <p style="font-size: 12px; color: #888888;">Đây là email tự động. Vui lòng không trả lời</p>   
        </div>
    </body>
</html>
""";

}
