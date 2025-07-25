package controllers.manager;

import dal.RoomImageDAO;
import dal.RoomNServiceDAO;
import dal.ServiceDAO;
import dal.TypeRoomDAO;
import models.TypeRoom;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import utility.Validation;
import java.util.HashMap;
import java.util.Map;
import models.RoomNService;

@WebServlet(name = "TypeRoomServlet", urlPatterns = {"/manager/types"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10)
public class TypeRoomServlet extends HttpServlet {
    private int MIN_PRICE = 10000;
    private int MAX_PRICE = 20000;
    private int MIN_ADULT = 1;
    private int MAX_ADULT = 10;
    private int MIN_CHILDREN = 1;
    private int MAX_CHILDREN = 10;

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

        if ("addTypeRoom".equals(service)) {

            int maxAdult = 0, maxChildren = 0;

            String name = request.getParameter("typeName").trim();
            String price_raw = request.getParameter("price").trim();
            String description = request.getParameter("typeDesc").trim();
            String maxAdultRaw = request.getParameter("maxAdult").trim();
            String maxChildrenRaw = request.getParameter("maxChildren").trim();

            boolean check = false;

            if (Validation.validateField(request, "nameError", name, "TYPE_ROOM_NAME_BASIC",
                    "Type Name", "Chỉ bao gồm chữ cái và tối đa 100 ký tự")) {
                check = true;
            }

            int price = 0;
            try {
                price = Integer.parseInt(price_raw);
                if (!(MIN_PRICE <= price && price <= MAX_PRICE)) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                check = true;
                request.setAttribute("priceError", "Vui lòng nhập giá trong khoảng từ 10000 VND đến 20000 VND");
            }

            if (TypeRoomDAO.getInstance().getTypeRoomByName(name) != null) {
                check = true;
                request.setAttribute("name", name);
                request.setAttribute("price", price_raw);
                request.setAttribute("description", description);
                request.setAttribute("nameExistedError", "Tên loại phòng đã tồn tại");
            }

            try {
                maxAdult = Integer.parseInt(maxAdultRaw);
                if (maxAdult < MIN_ADULT || maxAdult > MAX_ADULT) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                check = true;
                request.setAttribute("maxAdultError", "Giới hạn người trong khoảng 1-10");
            }

            try {
                maxChildren = Integer.parseInt(maxChildrenRaw);
                if (maxChildren < MIN_CHILDREN || maxChildren > MAX_CHILDREN) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                check = true;
                request.setAttribute("maxChildrenError", "Giới hạn trẻ em trong khoảng 1-10");
            }
            if (Validation.validateField(request, "descError", description, "DESCRIPTION",
                    "Mô tả", "Vui lòng nhập mô tả với tối đa 255 ký tự")) {
                check = true;
            }

            Collection<Part> fileParts = request.getParts();
            HashMap<Part, String> imageParts = new HashMap<>();
            for (Part part : fileParts) {
                if (part.getName().equals("image") && part.getSize() > 0) {
                    String fileName = extractFileName(part);

                    if (fileName != null && part.getContentType() != null && part.getContentType().startsWith("image/")) {
                        imageParts.put(part, fileName);
                    }
                }
            }

            if (imageParts.isEmpty()) {
                check = true;
                request.setAttribute("imageError", "Vui lòng chọn ít nhất 1 ảnh");
            }

            if (!check) {
                TypeRoom typeRoom = new TypeRoom();
                typeRoom.setDescription(description);
                typeRoom.setPrice(price);
                typeRoom.setTypeName(name);
                typeRoom.setMaxAdult(maxAdult);
                typeRoom.setMaxChildren(maxChildren);

                int typeId = TypeRoomDAO.getInstance().insertTypeRoom(typeRoom);

                for (Map.Entry<Part, String> imagePart : imageParts.entrySet()) {
                    uploadImage(request, imagePart.getKey(), imagePart.getValue(), typeId);

                    RoomImageDAO.getInstance().insertImage(typeId, imagePart.getValue());
                }

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
            
            String priceRaw = request.getParameter("price").trim();

            int price = 0;

            try {
                price = Integer.parseInt(priceRaw);
                if (!(MIN_PRICE <= price && price <= MAX_PRICE)) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                check = true;
                request.setAttribute("priceError", "Vui lòng nhập giá trong khoảng từ 10000 VND đến 20000 VND");
            }

            if (!check) {
                boolean success = TypeRoomDAO.getInstance().editTypeRoom(typeRoomId, price);

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

            if (Validation.validateField(request, "descError", description, "DESCRIPTION",
                    "Mô tả", "Vui lòng nhập mô tả với tối đa 255 ký tự")) {
                request.setAttribute("showModalDesc", typeRoomId);
                request.setAttribute("updateDesc", typeRoomId);
                showTypeRoom(request, response);
                return;
            }

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
            int typeId = Integer.parseInt(request.getParameter("typeId"));

            String serviceItem[] = request.getParameterValues("serviceItem");

            String serviceQuantity[] = request.getParameterValues("serviceQuantity");

            Map<Integer, Integer> serviceQuantityMap = new HashMap<>();

            if (serviceItem != null && serviceItem.length > 0) {

                for (int i = 0; i < serviceItem.length; i++) {

                    String serviceId = serviceItem[i];
                    String quantity = request.getParameter("serviceQuantity_" + serviceId);

                    int quantityNum = 0;
                    try {
                        quantityNum = Integer.parseInt(quantity);
                        if (quantityNum < 1 || quantityNum > 10) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("typeId", typeId);
                        request.setAttribute("quantityError", "Vui lòng nhập số lượng trong khoảng từ 1 đến 10");
                        request.setAttribute("showModalService", typeId);
                        showTypeRoom(request, response);
                        return;
                    }

                    if (serviceId != null && !serviceId.isEmpty()) {
                        int serviceIdParse = 0;
                        try {
                            serviceIdParse = Integer.parseInt(serviceId);
                            int quantityValue = Integer.parseInt(quantity);
                            serviceQuantityMap.put(serviceIdParse, quantityValue);
                        } catch (NumberFormatException e) {
                            serviceQuantityMap.put(serviceIdParse, 1);
                        }
                    }
                }

            }

            List<Integer> currentServices = RoomNServiceDAO.getInstance().getAllServiceIdByTypeId(typeId);

            if (!serviceQuantityMap.isEmpty()) {

                TypeRoom typeRoom = TypeRoomDAO.getInstance().getTypeRoomById(typeId);

                int typePrice = typeRoom.getPrice();

                List<RoomNService> roomNService = RoomNServiceDAO.getInstance().getRoomNServicesByTypeId(typeRoom);

                if (!roomNService.isEmpty()) {
                    for (RoomNService rns : roomNService) {
//                            typePrice -= (rns.getService().getPrice() * rns.getQuantity());
                        RoomNServiceDAO.getInstance().deleteRoomNServiceByTypeId(typeId, rns.getService().getServiceId());

                    }
                }

//                        RoomNServiceDAO.getInstance().deleteRoomNServiceByTypeId(typeId);
                for (Map.Entry<Integer, Integer> entry : serviceQuantityMap.entrySet()) {

                    RoomNServiceDAO.getInstance().insertRoomNServiceByTypeId(typeId, entry.getKey(), entry.getValue());
//                        typePrice += ServiceDAO.getInstance().getServiceByServiceId(entry.getKey()).getPrice() * entry.getValue();

                }

                //TypeRoomDAO.getInstance().updateTypeRoom(typeRoom.getTypeId(), typeRoom.getTypeName(), typePrice);
                request.setAttribute("updateMessageService", "Cập nhật dịch vụ thành công");

                request.setAttribute("showModalService", typeId);
            } else {
                TypeRoom typeRoom = TypeRoomDAO.getInstance().getTypeRoomById(typeId);

                int typePrice = typeRoom.getPrice();

                List<RoomNService> roomNService = RoomNServiceDAO.getInstance().getRoomNServicesByTypeId(typeRoom);

                if (!roomNService.isEmpty()) {
                    for (RoomNService rns : roomNService) {
//                        typePrice -= (rns.getService().getPrice() * rns.getQuantity());
                        RoomNServiceDAO.getInstance().deleteRoomNServiceByTypeId(typeId, rns.getService().getServiceId());

                    }
                }

                request.setAttribute("showModalService", typeId);
            }
            request.setAttribute("typeId", typeId);
            showTypeRoom(request, response);
        }

        if ("deleteImg".equals(service)) {
            int imageId = Integer.parseInt(request.getParameter("imageId"));
            String imageName = request.getParameter("imageName");
            int typeId = Integer.parseInt(request.getParameter("typeId"));

            deleteImage(request, imageName, typeId);

            RoomImageDAO.getInstance().deleteImage(imageId);

            request.setAttribute("showModalImage", typeId);

            request.setAttribute("deleteImg", "Xóa ảnh thành công");

            showTypeRoom(request, response);
        }

        if ("addImg".equals(service)) {

            int typeId = Integer.parseInt(request.getParameter("typeId"));

            Collection<Part> fileParts = request.getParts();
            System.out.println("fileParts: " + fileParts);
            HashMap<Part, String> imageParts = new HashMap<>();
            for (Part part : fileParts) {
                System.out.println(part.toString());
                if (part.getName().equals("image") && part.getSize() > 0) {
                    String fileName = extractFileName(part);
                    if (fileName != null && part.getContentType() != null && part.getContentType().startsWith("image/")) {
                        imageParts.put(part, fileName);
                    }
                }
            }

            if (imageParts.isEmpty()) {
                request.setAttribute("showModalImage", typeId);
                request.setAttribute("imageError", "Vui lòng chọn ít nhất 1 ảnh");
                showTypeRoom(request, response);
                return;
            }

            for (Map.Entry<Part, String> imagePart : imageParts.entrySet()) {
                uploadImage(request, imagePart.getKey(), imagePart.getValue(), typeId);

                RoomImageDAO.getInstance().insertImage(typeId, imagePart.getValue());
            }

            request.setAttribute("showModalImage", typeId);

            request.setAttribute("addImg", "Thêm ảnh thành công");

            showTypeRoom(request, response);

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

        int indexPage = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));

        typeRoomList = dal.TypeRoomDAO.getInstance().typeRoomPagination(indexPage, key);

        request.setAttribute("currentPage", indexPage);

        request.setAttribute("typeRoomList", typeRoomList);

        request.getRequestDispatcher("/View/Manager/TypeRoom.jsp").forward(request, response);
    }

    private void uploadImage(HttpServletRequest request, Part filePart, String fileName, int typeId) throws IOException {

        TypeRoom typeRoom = TypeRoomDAO.getInstance().getTypeRoomById(typeId);

        String appPath = request.getServletContext().getRealPath("/");
        File rootDir = new File(appPath).getParentFile().getParentFile(); // thoát khỏi /build/web

        String buildPath = appPath + "Image" + File.separator + typeRoom.getTypeName().replace(" ", "");
        String savePath = rootDir.getAbsolutePath() + File.separator + "web" + File.separator + "Image" + File.separator + typeRoom.getTypeName().replace(" ", "");

        File fileBuildDir = new File(buildPath);
        if (!fileBuildDir.exists()) {
            fileBuildDir.mkdirs();
        }

        String fullBuildPath = buildPath + File.separator + fileName;

        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }

        String fullPath = savePath + File.separator + fileName;

        filePart.write(fullPath);
        filePart.write(fullBuildPath);
    }

    private void deleteImage(HttpServletRequest request, String fileName, int typeId) throws IOException {

        TypeRoom typeRoom = TypeRoomDAO.getInstance().getTypeRoomById(typeId);

        String appPath = request.getServletContext().getRealPath("/");
        File rootDir = new File(appPath).getParentFile().getParentFile(); // thoát khỏi /build/web

        String buildPath = appPath + "Image" + File.separator + typeRoom.getTypeName().replace(" ", "");
        String savePath = rootDir.getAbsolutePath() + File.separator + "web" + File.separator + "Image" + File.separator + typeRoom.getTypeName().replace(" ", "");

        String fullBuildPath = buildPath + File.separator + fileName;

        File imageBuildFile = new File(fullBuildPath);
        if (imageBuildFile.exists()) {
            imageBuildFile.delete();
        }

        String fullPath = savePath + File.separator + fileName;
        File imageWebFile = new File(fullPath);
        if (imageWebFile.exists()) {
            imageWebFile.delete();
        }
    }

    private String extractFileName(Part part) {
//        System.out.println("header: " + part.getHeader());
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("disposition: " + contentDisp);
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return new File(token.split("=")[1].replace("\"", "")).getName();
            }
        }
        return null;
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
