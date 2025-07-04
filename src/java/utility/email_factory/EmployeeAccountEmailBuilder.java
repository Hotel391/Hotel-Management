package utility.email_factory;

import java.util.Map;

/**
 *
 * @author HieuTT
 */
public class EmployeeAccountEmailBuilder implements EmailContentBuilder{

    @Override
    public String build(Map<String, Object> data) {
        String fullname= (String) data.get("fullname");
        String username = (String) data.get("username");
        String password = (String) data.get("password");
        String loginLink= (String) data.get("loginLink");
        return """
        <html>
            <body>
                <p>Xin chào <strong>%s</strong>,</p>
                <p>Quản lý đã tạo tài khoản cho bạn tại hệ thống khách sạn FPTHotel.</p>
                <p><strong>Tên đăng nhập:</strong> %s</p>
                <p><strong>Mật khẩu tạm thời:</strong> %s</p>
                <p>Vui lòng đăng nhập tại: <a href="%s">đây</a></p>
                <p>Để bảo mật, bạn nên đổi mật khẩu sau khi đăng nhập lần đầu.</p>
                <p>Trân trọng,<br/>Bộ phận Quản lý FPTHotel</p>
            </body>
        </html>
                """.formatted(fullname, username, password, loginLink);
    }
    
}
