package utility.email_factory;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HieuTT
 */
public class CheckInBuilder implements EmailContentBuilder{

    @Override
    public String build(Map<String, Object> data) {
        String customerName = (String) data.get("customerName");
        String email = (String) data.get("email");
        String phone = (String) data.get("phone");
        String startDate = (String) data.get("startDate");
        String endDate = (String) data.get("endDate");
        List<String> typeRoom = (List<String>) data.get("typeRoom");
        List<Integer> quantityTypeRoom = (List<Integer>) data.get("quantityTypeRoom");
        List<BigInteger> priceTypeRoom = (List<BigInteger>) data.get("priceTypeRoom");
        List<String> services = (List<String>) data.get("services");
        List<Integer> serviceQuantity = (List<Integer>) data.get("serviceQuantity");
        List<BigInteger> servicePrice = (List<BigInteger>) data.get("servicePrice");
        BigInteger total = (BigInteger) data.get("total");
        String paymentMethod = (String) data.get("paymentMethod");
        StringBuilder confirmCheckin = new StringBuilder();
        // Tính số đêm giữa startDate và endDate (định dạng yyyy-MM-dd)
        long nights;
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
            confirmCheckin.append("<td>").append(String.format("%,d", priceTypeRoom.get(i))).append("</td>");
            confirmCheckin.append("</tr>");
        }
        confirmCheckin.append("</tbody>");
        confirmCheckin.append("</table>");
        confirmCheckin.append("""
            <h4>Dịch vụ yêu cầu trước</h4>
        <table>
            <thead>
            <tr>
                <th>Tên dịch vụ</th>
                <th>Số lượng</th>
                <th>Thành tiền (VNĐ)</th>
            </tr>
            </thead>
            <tbody>
        """);
        for (int i = 0; i < services.size(); i++) {
            confirmCheckin.append("<tr>");
            confirmCheckin.append("<td>").append(services.get(i)).append("</td>");
            confirmCheckin.append("<td>").append(serviceQuantity.get(i)).append("</td>");
            confirmCheckin.append("<td>").append(String.format("%,d", servicePrice.get(i))).append("</td>");
            confirmCheckin.append("</tr>");
        }
        confirmCheckin.append("</tbody>");
        confirmCheckin.append("</table>");
        confirmCheckin.append("""
                <table style="width: 50%; float: right;">
            <tr>
                <td><strong>Tạm tính tổng cộng:</strong></td>
                <td><strong>""").append(String.format("%,d", total)).append(""" 
                    VNĐ</strong></td>
            </tr>
            <tr>
      <td><strong>Phương thức thanh toán:</strong></td>
      <td>""").append(paymentMethod).append("""
        </td>
    </tr>
        </table>

        <div style="clear: both;"></div>

        <p class="note">*Chi phí trên chỉ là tạm tính. Hóa đơn chính thức sẽ được xuất khi quý khách trả phòng và bao gồm các chi phí phát sinh (nếu có).</p>
        <p class="note">Chúng tôi rất hân hạnh được đón tiếp quý khách tại Khách Sạn FPTHotel!</p>

    </body>
</html>
                """);
        return confirmCheckin.toString();
    }
    
}
