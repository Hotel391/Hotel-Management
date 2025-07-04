package utility;

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

public class Email {

    private static final int LIMIT_MINUS = 5;
    private String from = "fpthotel@gmail.com";
    private String password = "jcfu lbfu zxvz mpkc";

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

    public Email() {
        linkHtml.put("register", htmlConfirmEmail);
        linkHtml.put("reset", htmlResetPassword);
    }

    public void sendEmail(String to, String username, String linkConfirm, String type) {
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
        String linkRaw = linkHtml.get(type);
        String htmlContent = linkRaw.replace("${username}", username)
                .replace("${confirmLink}", linkConfirm);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject("Confirm your email");
            message.setContent(htmlContent, "text/html; charset=utf-8");

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
    private String htmlResetPassword = """
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
            """;

    public void sendReceipt(String to, List<String> typeRoom, List<Integer> quantityTypeRoom,
            List<Double> priceTypeRoom, List<String> services, List<Integer> serviceQuantity,
            List<Integer> servicePrice, int totalRoomPrice, int totalServicePrice, int fineMoney, String paymentMethod,
            String customerName) {
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
        String receipt=buildReceipt(typeRoom, quantityTypeRoom, priceTypeRoom, services, serviceQuantity, servicePrice, totalRoomPrice, totalServicePrice, fineMoney, paymentMethod, customerName);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject("Receipt of FPTHotel");
            message.setContent(receipt, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            //
        }
    }

    private String buildReceipt(List<String> typeRoom, List<Integer> quantityTypeRoom,
            List<Double> priceTypeRoom, List<String> services, List<Integer> serviceQuantity,
            List<Integer> servicePrice, int totalRoomPrice, int totalServicePrice, int fineMoney, String paymentMethod,
            String customerName) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("""
                                <!DOCTYPE html>
                <html lang="vi">
                    <head>
                        <meta charset="UTF-8">
                        <title>Hóa Đơn Thanh Toán Khách Sạn</title>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                margin: 0;
                                padding: 0;
                                background-color: #f0f0f0;
                            }
                            .invoice {
                                max-width: 800px;
                                margin: 20px auto;
                                padding: 20px;
                                border: 1px solid #ddd;
                                background-color: #fff;
                                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                            }
                            h2 {
                                text-align: center;
                                font-size: 24px;
                                margin-bottom: 10px;
                            }
                            h3 {
                                font-size: 18px;
                                margin-top: 20px;
                                margin-bottom: 10px;
                            }
                            table {
                                width: 100%;
                                border-collapse: collapse;
                                margin: 20px 0;
                            }
                            th, td {
                                border: 1px solid #000;
                                padding: 10px;
                            }
                            thead th {
                                background-color: #555;
                                color: white;
                            }
                            tbody tr:nth-child(even) {
                                background-color: #f9f9f9;
                            }
                            table th:nth-child(1), table td:nth-child(1) {
                                text-align: center;
                            }
                            table th:nth-child(2), table td:nth-child(2) {
                                text-align: left;
                            }
                            table th:nth-child(3), table td:nth-child(3),
                            table th:nth-child(4), table td:nth-child(4),
                            table th:nth-child(5), table td:nth-child(5) {
                                text-align: right;
                            }
                            .summary {
                                width: 50%;
                                margin-left: auto;
                            }
                            .summary td {
                                text-align: right;
                                padding: 5px 10px;
                            }
                            .total td {
                                font-weight: bold;
                                background-color: #e0e0e0;
                            }
                            p {
                                margin: 10px 0;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="invoice">
                            <h2>KHÁCH SẠN FPTHotel<br>HÓA ĐƠN THANH TOÁN</h2>
                            <p><strong>Khách hàng:</strong> """).append(customerName).append("""
                                </p>
                            <p><strong>Ngày lập hóa đơn:</strong> """).append(new SimpleDateFormat("dd/MM/yyyy").format(new Date())).append("""
                                </p>
                            <h3>Danh sách phòng đã đặt</h3>
                            <table>
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Loại phòng</th>
                                        <th>Số lượng</th>
                                        <th>Thành tiền (VNĐ)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                """);
        for (int i = 0; i < typeRoom.size(); i++) {
            receipt.append("<tr>");
            receipt.append("<td>").append(i + 1).append("</td>");
            receipt.append("<td>").append(typeRoom.get(i)).append("</td>");
            receipt.append("<td>").append(quantityTypeRoom.get(i)).append("</td>");
            receipt.append("<td>").append(quantityTypeRoom.get(i) * priceTypeRoom.get(i)).append("</td>");
            receipt.append("</tr>");
        }
        receipt.append("</tbody></table>");
        receipt.append("""
                    <h3>Dịch vụ sử dụng</h3>
                <table>
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Tên dịch vụ</th>
                            <th>Số lượng</th>
                            <th>Thành tiền (VNĐ)</th>
                        </tr>
                    </thead>
                    <tbody>
                    """);
        for (int i = 0; i < services.size(); i++) {
            receipt.append("<tr>");
            receipt.append("<td>").append(i + 1).append("</td>");
            receipt.append("<td>").append(services.get(i)).append("</td>");
            receipt.append("<td>").append(serviceQuantity.get(i)).append("</td>");
            receipt.append("<td>").append(serviceQuantity.get(i) * servicePrice.get(i)).append("</td>");
            receipt.append("</tr>");
        }
        receipt.append("""
                    </tbody>
                </table>
                <h3>Tổng kết thanh toán</h3>
                <table class="summary">
                    """);
        receipt.append("""
                <table class="summary">
                <tr>
                    <td>Tổng tiền phòng:</td>
                    <td>""").append(String.format("%,d", totalRoomPrice)).append("""
                                VNĐ</td>
                """);
        receipt.append("""
                <tr>
                        <td>Tổng tiền dịch vụ:</td>
                        <td>""").append(String.format("%,d", totalServicePrice)).append("""
                                        VNĐ</td>
                                    </tr>
                """);
        if (fineMoney > 0) {
            receipt.append("""
                    <tr>
                        <td>Tiền phạt:</td>
                        <td>""").append(String.format("%,d", fineMoney)).append("""
                                    VNĐ</td>
                    </tr>
                    """);
        }
        receipt.append("""
                <tr class="total">
                    <td>Tổng cộng:</td>
                    <td>""").append(String.format("%,d", totalRoomPrice + totalServicePrice + fineMoney)).append("""
                                        VNĐ</td>
                        </tr>
                    </table>
                <p><strong>Phương thức thanh toán:</strong> """).append(paymentMethod).append("</p>\n");
        receipt.append("""
                <p style="text-align: center; margin-top: 20px;"><em>Cảm ơn Quý khách đã sử dụng dịch vụ của chúng tôi!</em></p>
        </div>
    </body>
</html>
                """);
        return receipt.toString();
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
