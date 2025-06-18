package controllers.manager;

import dal.RoomNServiceDAO;
import dal.TypeRoomDAO;
import models.TypeRoom;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import utility.Validation;

@WebServlet(name = "TypeRoomServlet", urlPatterns = {"/manager/types"})
public class TypeRoomServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String service = request.getParameter("service");

        if (service == null) {
            service = "view";
        }

        if ("view".equals(service)) {
            showTypeRoom(request, response);
        }
        
        if("addTypeRoom".equals(service)){
            System.out.println("123");
            String name = request.getParameter("typeName").trim();
            String price_raw = request.getParameter("price").trim();
            String description = request.getParameter("typeDesc").trim();
            
            System.out.println("desc: " + description);

            boolean check = false;

            if (Validation.validateField(request, "nameError", name, "TYPE_ROOM_NAME_BASIC",
                    "Type Name", "Chỉ bao gồm chữ cái")) {
                check = true;
                System.out.println("lỗi");
            }

            if (Validation.validateField(request, "priceError", price_raw, "ROOM_PRICE_INT",
                    "Price", "Chỉ bao gồm chữ số")) {
                check = true;
            }

            if (TypeRoomDAO.getInstance().getTypeRoomByName(name) != null) {
                check = true;
                request.setAttribute("name", name);
                request.setAttribute("price", price_raw);
                request.setAttribute("description", description);
                request.setAttribute("nameExistedError", "Tên loại phòng đã tồn tại");
            }

            if (!check) {

                int price = Integer.parseInt(price_raw);
                TypeRoom typeRoom = new TypeRoom();
                typeRoom.setDescription(description);
                typeRoom.setPrice(price);
                typeRoom.setTypeName(name);

                TypeRoomDAO.getInstance().insertTypeRoom(typeRoom);

                request.setAttribute("addSuccess", "Thêm loại phòng thành công");

            } else {
                request.setAttribute("name", name);
                request.setAttribute("price", price_raw);
                request.setAttribute("description", description);
                
            }
            request.setAttribute("showModalAdd", "show");
            showTypeRoom(request, response);
        }

        if ("updatePriceName".equals(service)) {

            int page = Integer.parseInt(request.getParameter("page"));

            String key = request.getParameter("key").trim();

            int typeRoomId = Integer.parseInt(request.getParameter("typeRoomId"));

            boolean check = false;

            int price = 0;

            String typeName = request.getParameter("typeName").trim();

            if (Validation.validateField(request, "nameError", typeName, "TYPE_ROOM_NAME_BASIC",
                    "Type Name", "Chỉ bao gồm chữ cái")) {
                check = true;
            }

            String priceRaw = request.getParameter("price").trim();

            if (Validation.validateField(request, "priceError", priceRaw, "ROOM_PRICE_INT",
                    "Price", "Chỉ bao gồm chữ số")) {
                check = true;
            } else {
                price = Integer.parseInt(priceRaw);
            }

            if (TypeRoomDAO.getInstance().getTypeRoomByNameAndPrice(typeName, price) != null) {
                check = true;
                System.out.println(check);
                request.setAttribute("typeName", typeName);
                request.setAttribute("price", priceRaw);
                request.setAttribute("noChangeError", "Không có gì thay đổi");
            }

            if (!check) {
                boolean success = TypeRoomDAO.getInstance().updateTypeRoom(typeRoomId, typeName, price);

                if (success) {
                    // Set attribute để hiển thị thông báo thành công
                    request.setAttribute("updateMessageNameAndPrice", "Cập nhật thông tin thành công!");
                    request.setAttribute("updateNameAndPrice", typeRoomId);
                    request.setAttribute("showModalEdit", typeRoomId);

                    request.setAttribute("key", key);

                }
            } else {

                request.setAttribute("showModalEdit", typeRoomId);

                request.setAttribute("updateNameAndPrice", typeRoomId);

                request.setAttribute("key", key);
            }
            showTypeRoom(request, response);
        }

        if ("updateDesc".equals(service)) {

            String description = request.getParameter("typeDesc").trim();

            int typeRoomId = Integer.parseInt(request.getParameter("typeRoomId"));

            if (description.equals(TypeRoomDAO.getInstance().getDescriptionByTypeId(typeRoomId))) {

                request.setAttribute("showModalDesc", typeRoomId);
                request.setAttribute("updateDesc", typeRoomId);
                request.setAttribute("descError", "Không có gì thay đổi");
            } else {

                boolean success = TypeRoomDAO.getInstance().updateDescription(typeRoomId, description);
                if (success) {
                    // Set attribute để hiển thị thông báo thành công
                    request.setAttribute("updateMessageDesc", "Cập nhật mô tả thành công!");
                    request.setAttribute("updateDesc", typeRoomId);
                    request.setAttribute("showModalDesc", typeRoomId);
                }
            }
            
            showTypeRoom(request, response);
        }

        if ("addServiceItem".equals(service)) {
            String serviceItem[] = request.getParameterValues("serviceItem");

            int typeId = Integer.parseInt(request.getParameter("typeId"));

            if (serviceItem != null) {

                boolean check = false;

                List<Integer> currentServices = RoomNServiceDAO.getInstance().getAllServiceIdByTypeId(typeId);

                List<Integer> updateServices = new ArrayList<>();

                for (String item : serviceItem) {
                    updateServices.add(Integer.parseInt(item));
                }

                Collections.sort(currentServices);
                Collections.sort(updateServices);

                if (currentServices.equals(updateServices)) {
                    check = true;
                    System.out.println(check);
                    request.setAttribute("noChangeServiceError", "Không có gì thay đổi");
                }

                if (!check) {
                    RoomNServiceDAO.getInstance().deleteRoomNServiceByTypeId(typeId);
                    for (String item : serviceItem) {
                        RoomNServiceDAO.getInstance().insertRoomNServiceByTypeId(typeId, Integer.parseInt(item));
                    }

                    request.setAttribute("updateMessageService", "Cập nhật dịch vụ thành công");
                }

                request.setAttribute("showModalService", typeId);
            } else {
                RoomNServiceDAO.getInstance().deleteRoomNServiceByTypeId(typeId);

                request.setAttribute("showModalService", typeId);
            }
            request.setAttribute("typeId", typeId);
            showTypeRoom(request, response);
        }
        
        if("deleleTypeRoom".equals(service)){
            
        }

    }

    private void showTypeRoom(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<TypeRoom> typeRoomList;

        //Search Type Room
        String key = request.getParameter("key");

        int typeRoomTotal;

        int endPage;

        if (key != null && !key.trim().isEmpty()) {
            typeRoomTotal = dal.TypeRoomDAO.getInstance().searchTypeRoom(key).size();

            endPage = typeRoomTotal / 5;

            if (typeRoomTotal % 5 != 0) {
                endPage++;
            }

            request.setAttribute("key", key);
        } else {
            

            typeRoomTotal = dal.TypeRoomDAO.getInstance().getTypeRoomQuantity();

            endPage = typeRoomTotal / 5;

            if (typeRoomTotal % 5 != 0) {
                endPage++;
            }
        }
        request.setAttribute("endPage", endPage);

        //Take data number each page
        int indexPage = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));

        typeRoomList = dal.TypeRoomDAO.getInstance().typeRoomPagination(indexPage, key);

        request.setAttribute("currentPage", indexPage);

        request.setAttribute("typeRoomList", typeRoomList);

        request.getRequestDispatcher("/View/manager/TypeRoom.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}