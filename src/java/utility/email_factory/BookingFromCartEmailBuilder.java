package utility.email_factory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HieuTT
 */

public class BookingFromCartEmailBuilder implements EmailContentBuilder {

    @Override
    public String build(Map<String, Object> data) {
        String customerName= (String) data.get("customerName");
        String email = (String) data.get("email");
        String typeRoomName = (String) data.get("typeRoomName");
        String roomNumber = (String) data.get("roomNumber");
        String checkin = (String) data.get("checkin");
        String checkout = (String) data.get("checkout");
        int adults = (int) data.get("adults");
        int children = (int) data.get("children");
        String paymentDate = LocalDateTime.now().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String paymentMethod = (String) data.get("paymentMethod");
        List<String> serviceNames = (List<String>) data.get("serviceNames");
        List<Integer> serviceQuantities = (List<Integer>) data.get("serviceQuantities");
        List<Integer> servicePrices = (List<Integer>) data.get("servicePrices");
        int roomPrice = (int) data.get("roomPrice");
        int totalServicePrice = (int) data.get("totalServicePrice");
        int totalPrice = roomPrice + totalServicePrice;
        StringBuilder content = new StringBuilder();
        content.append("""
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xác Nhận Thanh Toán Đặt Phòng</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f0f0;
        }
        .container {
            max-width: 600px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            font-size: 24px;
            margin-bottom: 20px;
        }
        p {
            margin: 10px 0;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .summary {
            margin-top: 20px;
        }
        .summary p {
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>KHÁCH SẠN FPTHOTEL</h1>
                """);
        content.append("<p><strong>Kính gửi:</strong> ").append(customerName).append("</p>");
        content.append("<p><strong>Email:</strong> ").append(email).append("</p>");
        content.append("""
<h2>Xác Nhận Thanh Toán Đặt Phòng</h2>
<p>Chúng tôi xin xác nhận rằng quý khách đã thanh toán thành công cho đặt phòng tại khách sạn của chúng tôi. Dưới đây là chi tiết đặt phòng của quý khách:</p>
        
<h3>Chi Tiết Đặt Phòng</h3>
                """);
        content.append("<p><strong>Loại phòng:</strong> ").append(typeRoomName).append("</p>");
        content.append("<p><strong>Phòng số:</strong> ").append(roomNumber).append("</p>");
        content.append("<p><strong>Ngày nhận phòng:</strong> ").append(checkin).append("</p>");
        content.append("<p><strong>Ngày trả phòng:</strong> ").append(checkout).append("</p>");
        content.append("<p><strong>Người lớn:</strong> ").append(adults).append("</p>");
        if(children != 0) {
            content.append("<p><strong>Trẻ em:</strong> ").append(children).append("</p>");
        }
        content.append("<h3>Thông Tin Thanh Toán</h3>");
        content.append("<p><strong>Ngày thanh toán:</strong> ").append(paymentDate).append("</p>");
        content.append("<p><strong>Phương thức thanh toán:</strong> ").append(paymentMethod).append("</p>");
        content.append("<h3>Danh Sách Dịch Vụ</h3>");
        content.append("""
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
        for (int i = 0; i < serviceNames.size(); i++) {
            content.append("<tr>");
            content.append("<td>").append(i + 1).append("</td>");
            content.append("<td>").append(serviceNames.get(i)).append("</td>");
            content.append("<td>").append(serviceQuantities.get(i)).append("</td>");
            content.append("<td>").append(String.format("%,d", servicePrices.get(i))).append("</td>");
            content.append("</tr>");
        }
        content.append("""
            </tbody>
        </table>""");
        content.append("""
        <div class="summary">
            <p><strong>Tổng tiền phòng: </strong> """).append(String.format("%,d", roomPrice)).append(""" 
                VNĐ</p>
            <p><strong>Tổng tiền dịch vụ: </strong> """).append(String.format("%,d", totalServicePrice)).append(""" 
                VNĐ</p>
            <p><strong>Tổng cộng: </strong> """).append(String.format("%,d", totalPrice)).append(""" 
                VNĐ</p>
        </div>""");
        content.append("""
            <p style="text-align: center; margin-top: 20px;"><em>Cảm ơn Quý khách đã lựa chọn dịch vụ của chúng tôi. Chúc Quý khách có một kỳ nghỉ tuyệt vời!</em></p>
    </div>
</body>
</html>
                """);
        return content.toString();
    }

}
