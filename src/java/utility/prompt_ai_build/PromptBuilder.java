package utility.prompt_ai_build;

import models.Service;
import java.util.List;

import models.TypeRoom;

/**
 *
 * @author HieuTT
 */
public class PromptBuilder {
    private final StringBuilder sb = new StringBuilder();

    public PromptBuilder addSystemRole(String context) {
        sb.append(context).append("\n");
        return this;
    }

    public PromptBuilder addSearchLink(String searchLink) {
        sb.append(searchLink).append("\n");
        sb.append("Link tìm phòng theo yêu cầu: ").append(searchLink).append("\n");
        sb.append("Bạn có thể gợi ý người dùng thêm các param như: checkin, checkout, adults, children, minPrice, maxPrice\n");
        sb.append("Các param này sử dụng bằng cách thêm vào cuối link tìm kiếm, ví dụ: \n");
        sb.append(searchLink).append("?checkin=2023-10-01&checkout=2023-10-05&adults=2&children=1\n");
        sb.append("Nếu không có param nào, bạn có thể để trống link tìm kiếm.\n");
        return this;
    }

    public PromptBuilder addDetailRoomLink(String detailRoomLink) {
        sb.append("Link chi tiết phòng để xem chi tiết về loại phòng cụ thể: ").append(detailRoomLink).append("\n");
        return this;
    }

    public PromptBuilder addTypeRooms(List<TypeRoom> typeRooms, String detailRoomLink) {
        sb.append("Danh sách loại phòng: \n");
        for (TypeRoom typeRoom : typeRooms) {
            sb.append(" - ").append(typeRoom.getTypeName())
                    .append(": Giá: ").append(String.format("%,d", typeRoom.getPrice())).append(" VNĐ")
                    .append(", Mô tả: ").append(typeRoom.getDescription())
                    .append(", Số lượng phòng: ").append(typeRoom.getTotalRooms());
            if (typeRoom.getAverageRating() > 0) {
                sb.append(", Đánh giá trung bình: ").append(String.format("%.1f", typeRoom.getAverageRating()))
                        .append(" (").append(typeRoom.getNumberOfReviews()).append(" đánh giá)");
            }
            sb.append(String.format(" | Sức chứa: %d người lớn, %d trẻ em", typeRoom.getAdults(),
                    typeRoom.getChildren()));
            sb.append(" | Id phòng: ").append(typeRoom.getTypeId()).append("\n");
            sb.append(" | Link: ").append(detailRoomLink).append(typeRoom.getTypeId());
        }
        return this;
    }

    public PromptBuilder addServices(List<Service> services) {
        sb.append("Danh sách dịch vụ:\n");
        for (Service s : services) {
            String priceText = s.getPrice() == 0.0 ? "Miễn phí" : String.format("%,d VNĐ", s.getPrice());
            sb.append("- ").append(s.getServiceName()).append(": ").append(priceText).append("\n");
        }
        return this;
    }

    public PromptBuilder addResponseRules(String rules) {
        sb.append("Yêu cầu trả lời: ").append(rules).append("\n");
        return this;
    }

    public PromptBuilder addExampleQuestions(String exampleQuestions) {
        sb.append("Ví dụ câu hỏi người dùng có thể hỏi: ").append(exampleQuestions).append("\n");
        return this;
    }

    public PromptBuilder addEmotionRules(String emotionRules) {
        sb.append(emotionRules).append("\n");
        return this;
    }

    public String build() {
        return sb.toString();
    }
}
