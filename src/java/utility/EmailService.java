package utility;

/**
 *
 * @author TranTrungHieu
 */
import utility.email_factory.EmailTemplateFactory;
import utility.email_factory.EmailTemplateFactory.EmailType;
import utility.email_factory.EmailContentBuilder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
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

public class EmailService {

    private static final int LIMIT_MINUS = 5;
    private String from = "fpthotel@gmail.com";
    private String password = "jcfu lbfu zxvz mpkc";
    private final Session session;

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public Timestamp expireDateTime() {
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(LIMIT_MINUS);
        return Timestamp.valueOf(expireTime);
    }

    public static boolean isExpireTime(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }

    public EmailService() {
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
        this.session = Session.getInstance(props, auth);
    }

    public void sendEmail(String to, String subject, EmailType type, Map<String, Object> data) {
        try {
            EmailContentBuilder builder = EmailTemplateFactory.getBuilder(type);
            String htmlContent = builder.build(data);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            //
        }
    }

}
