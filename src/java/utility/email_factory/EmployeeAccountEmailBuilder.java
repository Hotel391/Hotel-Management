package utility.email_factory;

import java.util.Map;

/**
 *
 * @author HieuTT
 */
public class EmployeeAccountEmailBuilder implements EmailContentBuilder {

    @Override
    public String build(Map<String, Object> data) {
        String fullname = (String) data.get("fullname");
        String username = (String) data.get("username");
        String password = (String) data.get("password");
        String loginLink = (String) data.get("loginLink");
        return """
                        <!DOCTYPE html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Xác nhận tài khoản FPTHotel</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                            color: #333;
                        }
                        .container {
                            max-width: 600px;
                            margin: 40px auto;
                            background-color: #ffffff;
                            border-radius: 8px;
                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                            padding: 20px;
                        }
                        .header {
                            text-align: center;
                            padding: 20px 0;
                            border-bottom: 1px solid #eee;
                        }
                        .header h1 {
                            color: #1a73e8;
                            margin: 0;
                            font-size: 24px;
                        }
                        .content {
                            padding: 20px;
                            line-height: 1.6;
                        }
                        .content p {
                            margin: 10px 0;
                        }
                        .content strong {
                            color: #1a73e8;
                        }
                        .button {
                            display: inline-block;
                            padding: 12px 24px;
                            background-color: #1a73e8;
                            color: #ffffff;
                            text-decoration: none;
                            border-radius: 5px;
                            margin: 10px 0;
                            text-align: center;
                        }
                        .button:hover {
                            background-color: #1557b0;
                        }
                        .footer {
                            text-align: center;
                            padding: 20px;
                            border-top: 1px solid #eee;
                            color: #777;
                            font-size: 14px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Chào mừng đến với FPTHotel</h1>
                        </div>
                        <div class="content">
                            <p>Xin chào <strong>%s</strong>,</p>
                            <p>Chúc mừng bạn! Tài khoản của bạn tại hệ thống khách sạn <strong>FPTHotel</strong> đã được tạo thành công.</p>
                            <p><strong>Tên đăng nhập:</strong> %s</p>
                            <p><strong>Mật khẩu tạm thời:</strong> %s</p>
                            <p>Vui lòng đăng nhập để bắt đầu sử dụng hệ thống:</p>
                            <a href="%s" class="button">Đăng nhập ngay</a>
                            <p>Để đảm bảo an toàn, bạn nên đổi mật khẩu ngay sau khi đăng nhập lần đầu tiên.</p>
                        </div>
                        <div class="footer">
                            <p>Trân trọng,<br/>Bộ phận Quản lý FPTHotel</p>
                            <p>Nếu bạn cần hỗ trợ, vui lòng liên hệ: <a href="mailto:fpthotel@gmail.com">fpthotel@gmail.com</a></p>
                        </div>
                    </div>
                </body>
                </html>
                                """
                .formatted(fullname, username, password, loginLink);
    }

}
