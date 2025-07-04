package utility;

import utility.email_factory.EmailTemplateFactory;
import utility.email_factory.EmailContentBuilder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
/**
 *
 * @author TranTrungHieu
 */
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    private Map<String, String> linkHtml = new HashMap<>();

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

    public void sendEmail(String to, String subject, String type, Map<String, Object> data) {
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

    private String buildConfirmCheckin(String customerName, String email, String phone, 
            String startDate, String endDate, List<String> typeRoom, List<Integer> quantityTypeRoom,
            List<Double> priceTypeRoom, List<String> services, List<Integer> serviceQuantity,
            List<Integer> servicePrice, int total, String paymentMethod){
        StringBuilder confirmCheckin = new StringBuilder();
        // Tính số đêm giữa startDate và endDate (định dạng yyyy-MM-dd)
        long nights = 1;
        try {
            java.time.LocalDate start = java.time.LocalDate.parse(startDate);
            java.time.LocalDate end = java.time.LocalDate.parse(endDate);
            nights = java.time.temporal.ChronoUnit.DAYS.between(start, end);
            if (nights < 1) nights = 1;
        } catch (Exception e) {
            nights = 1;
        }

        confirmCheckin.append("""
            <!DOCTYPE html>
    <html lang="vi">
        <head>
        <meta charset="UTF-8">
        <title>Xác Nhận Đặt Phòng - Khách Sạn FPTHotel</title>
        <style>
            body {
            font-family: Arial, sans-serif;
            max-width: 700px;
            margin: 40px auto;
            background-color: #fff;
            color: #333;
            padding: 20px;
            border: 1px solid #ddd;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            h2, h3 {
            text-align: center;
            margin-bottom: 20px;
            }
            table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
            }
            th, td {
            border: 1px solid #ccc;
            padding: 8px 12px;
            text-align: center;
            }
            th {
            background-color: #f0f0f0;
            }
            p {
            margin: 6px 0;
            }
            .note {
            font-style: italic;
            color: #555;
            text-align: center;
            }
        </style>
        </head>
        <body>

        <h2>KHÁCH SẠN FPTHotel</h2>
        <h3>XÁC NHẬN ĐẶT PHÒNG</h3>

        <p><strong>Khách hàng:</strong> """).append(customerName).append("""
        </p>
        <p><strong>Email:</strong> """).append(email).append("""
        </p>
        <p><strong>Số điện thoại:</strong> """).append(phone).append("""
        </p>
        <p><strong>Ngày nhận phòng:</strong> """).append(startDate).append("""
        </p>
        <p><strong>Ngày trả phòng:</strong> """).append(endDate).append("""
        </p>
        <p><strong>Số đêm:</strong> """).append(nights).append("""
        </p>

        <h4>Thông tin đặt phòng</h4>
        <table>
            <thead>
            <tr>
                <th>Loại phòng</th>
                <th>Số lượng</th>
                <th>Thành tiền (VNĐ)</th>
            </tr>
            </thead>
            <tbody>
        """);
        for (int i = 0; i < typeRoom.size(); i++) {
            confirmCheckin.append("<tr>");
            confirmCheckin.append("<td>").append(typeRoom.get(i)).append("</td>");
            confirmCheckin.append("<td>").append(quantityTypeRoom.get(i)).append("</td>");
            confirmCheckin.append("<td>").append(quantityTypeRoom.get(i) * priceTypeRoom.get(i)).append("</td>");
            confirmCheckin.append("</tr>");
        }
        confirmCheckin.append("</tbody>");
        confirmCheckin.append("</table>");
        return confirmCheckin.toString();
    }
}
