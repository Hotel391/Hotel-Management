package utility.email_factory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HieuTT
 */
public class ReceiptBuilder implements EmailContentBuilder {

    @Override
    public String build(Map<String, Object> data) {
        List<String> typeRoom = (List<String>) data.get("typeRoom");
        List<Integer> quantityTypeRoom = (List<Integer>) data.get("quantityTypeRoom");
        List<Integer> priceTypeRoom = (List<Integer>) data.get("priceTypeRoom");
        List<String> services = (List<String>) data.get("services");
        List<Integer> serviceQuantity = (List<Integer>) data.get("serviceQuantity");
        List<Integer> servicePrice = (List<Integer>) data.get("servicePrice");
        int totalRoomPrice = (int) data.get("totalRoomPrice");
        int totalServicePrice = (int) data.get("totalServicePrice");
        int fineMoney = (int) data.get("fineMoney");
        String paymentMethod = (String) data.get("paymentMethod");
        String customerName = (String) data.get("customerName");
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
            receipt.append("<td>").append(String.format("%,d", priceTypeRoom.get(i))).append("</td>");
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
            receipt.append("<td>").append(String.format("%,d", servicePrice.get(i))).append("</td>");
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

}
