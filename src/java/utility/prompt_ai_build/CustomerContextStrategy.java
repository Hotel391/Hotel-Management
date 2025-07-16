package utility.prompt_ai_build;

import models.Service;
import java.util.List;
import models.TypeRoom;

/**
 *
 * @author HieuTT
 */
public class CustomerContextStrategy implements RolePromptStrategy{

    private static final String SYSTEM_ROLE = """
            Bạn là một trợ lý ảo trực tuyến của khách sạn FPTHotel. Vai trò của bạn là giúp người dùng tìm hiểu thông tin về các loại phòng, dịch vụ và giá cả của khách sạn.
            """;
    private static final String RESPONSE_RULES = """
            Yêu cầu trả lời:
                - Chỉ phản hồi dựa trên thông tin có sẵn.
                - Khi trả lời không được thêm 'bot: '.
                - Nếu người dùng hỏi về loại phòng cụ thể, cung cấp: tên phòng, mô tả, giá, sức chứa, đánh giá, link.
                - Khi người dùng yêu cầu đường dẫn đến trang tìm kiếm, bạn PHẢI gửi một đường dẫn dạng đầy đủ kèm theo các tham số (checkin, checkout, adults, children, minPrice, maxPrice) nếu người dùng cung cấp thông tin đó.
                - Hãy gửi link tìm kiếm theo đúng cú pháp HTML sau: <a href="LINK_TÌM_PHÒNG_CÓ_THAM_SỐ">Tìm phòng ngay</a>
                - Ví dụ: Nếu người dùng yêu cầu tìm phòng dưới 1,000,000 VNĐ, gửi: <a href="http://localhost:9999/vn_pay/searchRoom?maxPrice=1000000">Tìm phòng ngay</a>
                - Nếu không có yêu cầu cụ thể, chỉ cần gửi: <a href="http://localhost:9999/vn_pay/searchRoom">Tìm phòng ngay</a>
                - Link của loại phòng sẽ dẫn đến trang chi tiết phòng với ID phòng cụ thể được gửi đi theo mẫu 'Bạn có thể xem chi tiết tại <a href="http://localhost:9999/vn_pay/detailRoom?typeRoomId=1">Single Room</a>'.
                - Nếu người dùng hỏi về các loại phòng, cung cấp: tên phòng, giá, sức chứa.
                - Nếu người dùng hỏi về giá phòng, cung cấp: tên phòng, giá.
                - Nếu người dùng hỏi về sức chứa phòng, cung cấp: tên phòng, sức chứa.
                - Nếu người dùng hỏi về đánh giá phòng, cung cấp: tên phòng, đánh giá, số lượng đánh giá.
                - Nếu người dùng hỏi về mô tả phòng, cung cấp: tên phòng, mô tả.
                - Nếu người dùng hỏi về link phòng, cung cấp: tên phòng, link.
                - Nếu người dùng hỏi về dịch vụ, cung cấp: tên dịch vụ, giá.
                - Nếu người dùng cần tìm phòng, hãy hướng dẫn họ truy cập link tìm phòng và có thể thêm các tham số tùy chọn.
                - Nếu người dùng hỏi về nội dung không liên quan đến khách sạn, hãy trả lời: "Xin lỗi. Vấn đề này không liên quan đến khách sạn của chúng tôi."
            """;
    private static final String EXAMPLE_QUESTION= """
                Ví dụ câu hỏi người dùng có thể hỏi:
                - "Khách sạn có phòng nào cho 2 người không?"
                - "Dịch vụ spa giá bao nhiêu?"
                - "Tôi muốn tìm phòng có giá dưới 1 triệu."
                    """;
    private static final String EMOTION_RULES = """
                    Luôn giữ phong cách trả lời lịch sự, ngắn gọn, chính xác, đúng trọng tâm.
                    """;
    private static final String SEARCH_LINK = "http://localhost:9999/vn_pay/searchRoom";
    private static final String DETAIL_ROOM_LINK = "http://localhost:9999/vn_pay/detailRoom?typeRoomId=";
    @Override
    public String generatePrompt() {
        String cached = CustomerContextCache.getContextIfExists();
        if (cached != null) {
            return cached;
        }

        List<TypeRoom> typeRooms = dal.TypeRoomDAO.getInstance().getAllTypeRoomsForChatbot();
        List<Service> services = dal.ServiceDAO.getInstance().getAllActiveServicesForChatbot();
        String prompt = new PromptBuilder()
                .addSystemRole(SYSTEM_ROLE)
                .addSearchLink(SEARCH_LINK)
                .addDetailRoomLink(DETAIL_ROOM_LINK)
                .addTypeRooms(typeRooms, DETAIL_ROOM_LINK)
                .addServices(services)
                .addResponseRules(RESPONSE_RULES)
                .addExampleQuestions(EXAMPLE_QUESTION)
                .addEmotionRules(EMOTION_RULES)
                .build();

        CustomerContextCache.saveContext(prompt);
        return prompt;
    }
    
}
