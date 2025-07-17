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
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 10)
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

        if ("addTypeRoom".equals(service)) {

            int maxAdult = 0, maxChildren = 0;

            String name = request.getParameter("typeName").trim();
            String price_raw = request.getParameter("price").trim();
            String description = request.getParameter("typeDesc").trim();
            String maxAdultRaw = request.getParameter("maxAdult").trim();
            String maxChildrenRaw = request.getParameter("maxChildren").trim();

            System.out.println("desc: " + description);

            boolean check = false;

            if (Validation.validateField(request, "nameError", name, "TYPE_ROOM_NAME_BASIC",
                    "Type Name", "Chỉ bao gồm chữ cái và tối đa 100 ký tự")) {
                check = true;
            }

//            if (Validation.validateField(request, "priceError", price_raw, "ROOM_PRICE_INT",
//                    "Price", "Chỉ bao gồm chữ số và lớn hơn 0")) {
//                check = true;
//            }
            int price = 0;
            try {
                price = Integer.parseInt(price_raw);
                if (!(100_000 <= price && price <= 10_000_000)) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                check = true;
                request.setAttribute("priceError", "Vui lòng nhập giá trong khoảng từ 100,000 VND đến 10,000,000 VND");
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
                if (maxAdult < 1 || maxAdult > 10) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                check = true;
                request.setAttribute("maxAdultError", "Giới hạn người trong khoảng 1-10");
            }

            try {
                maxChildren = Integer.parseInt(maxChildrenRaw);
                if (maxChildren < 1 || maxChildren > 10) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                check = true;
                request.setAttribute("maxChildrenError", "Giới hạn trẻ em trong khoảng 1-10");
            }
            if (Validation.validateField(request, "descError", description, "DESCRIPTION",
                    "Mô tả", "Chỉ bao gồm chữ cái và tối đa 255 ký tự")) {
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

//            for (Part imagePart : imageParts.keySet()) {
//                if (imagePart.getSize() > 5 * 1024 * 1024) { // 5MB
//                    check = true;
//                    request.setAttribute("imageError", "Kích thước file không được vượt quá 5MB");
//                    break;
//                }
//
//                String contentType = imagePart.getContentType();
//                if (contentType == null || !contentType.startsWith("image/")) {
//                    check = true;
//                    request.setAttribute("imageError", "Chỉ chấp nhận file ảnh (jpg, png, gif, etc.)");
//                    break;
//                }
//            }
            System.out.println(imageParts);

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

            String typeName = request.getParameter("typeName").trim();
            String priceRaw = request.getParameter("price").trim();
            String maxAdultRaw = request.getParameter("maxAdult").trim();

            String maxChildrenRaw = request.getParameter("maxChildren").trim();

            System.out.println("Adult: " + maxAdultRaw);

            System.out.println("Children: " + maxChildrenRaw);

            int price = 0;

            int maxAdult = 0;

            int maxChildren = 0;

            if (Validation.validateField(request, "nameError", typeName, "TYPE_ROOM_NAME_BASIC",
                    "Type Name", "Chỉ bao gồm chữ cái và tối đa 100 ký tự")) {
                check = true;
            }

            if (TypeRoomDAO.getInstance().getTypeRoomByNameAndId(typeName, typeRoomId) != null) {

                request.setAttribute("name", typeName);
                request.setAttribute("price", priceRaw);
//                request.setAttribute("description", description);
                request.setAttribute("updateNameAndPrice", typeRoomId);
                request.setAttribute("nameUpdateExistedError", "Tên loại phòng đã tồn tại");
                request.setAttribute("showModalEdit", typeRoomId);

                request.setAttribute("key", key);

                showTypeRoom(request, response);
                return;
            }

            try {
                maxAdult = Integer.parseInt(maxAdultRaw);
                if (maxAdult < 1 || maxAdult > 10) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                check = true;
                request.setAttribute("maxAdultError", "Giới hạn người trong khoảng 1-10");
            }

            try {
                maxChildren = Integer.parseInt(maxChildrenRaw);
                if (maxChildren < 1 || maxChildren > 10) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                check = true;
                request.setAttribute("maxChildrenError", "Giới hạn trẻ em trong khoảng 1-10");
            }

            try {
                price = Integer.parseInt(priceRaw);
                if (!(100_000 <= price && price <= 10_000_000)) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                check = true;
                request.setAttribute("priceError", "Vui lòng nhập giá trong khoảng từ 100,000 VND đến 10,000,000 VND");
            }

            if (TypeRoomDAO.getInstance().getTypeRoomByNameAndPriceAndQuantity(typeRoomId, typeName, price, maxAdult, maxChildren) != null) {
                check = true;
                System.out.println(check);
                request.setAttribute("typeName", typeName);
                request.setAttribute("price", priceRaw);
                request.setAttribute("updateNameAndPrice", typeRoomId);
                request.setAttribute("noChangeError", "Không có gì thay đổi");

                request.setAttribute("showModalEdit", typeRoomId);

                request.setAttribute("key", key);

                showTypeRoom(request, response);
                return;
            }

            if (!check) {
                boolean success = TypeRoomDAO.getInstance().editTypeRoom(typeRoomId, typeName, price, maxAdult, maxChildren);

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

//            for (String string : serviceItem) {
//                System.out.println("serviceId: " + string);
//            }
            String serviceQuantity[] = request.getParameterValues("serviceQuantity");

//            for (String string : serviceQuantity) {
//                System.out.println("quantity: " + string);
//            }
            Map<Integer, Integer> serviceQuantityMap = new HashMap<>();

            if (serviceItem != null && serviceItem.length > 0) {

                for (int i = 0; i < serviceItem.length; i++) {

                    String serviceId = serviceItem[i];
                    String quantity = request.getParameter("serviceQuantity_" + serviceId);

                    System.out.println(serviceId + "-" + quantity);
                    
                    int quantityNum = 0;
                    try {
                        quantityNum = Integer.parseInt(quantity);
                        if(quantityNum < 1 && quantityNum > 10){
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

            System.out.println(serviceQuantityMap);

            List<Integer> currentServices = RoomNServiceDAO.getInstance().getAllServiceIdByTypeId(typeId);

            if (!serviceQuantityMap.isEmpty()) {

                boolean check = false;

                List<Integer> updateServices = new ArrayList<>();

                for (String item : serviceItem) {
                    updateServices.add(Integer.parseInt(item));
                }

                Collections.sort(currentServices);
                Collections.sort(updateServices);

//                if (currentServices.equals(updateServices)) {
//                    check = true;
//                    System.out.println(check);
//                    request.setAttribute("noChangeServiceError", "Không có gì thay đổi");
//                }
                if (!check) {
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
                }

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

        if ("deleleTypeRoom".equals(service)) {

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
            HashMap<Part, String> imageParts = new HashMap<>();
            for (Part part : fileParts) {
                if (part.getName().equals("image") && part.getSize() > 0) {
                    String fileName = extractFileName(part);
                    if (fileName != null && part.getContentType() != null && part.getContentType().startsWith("image/")) {
                        imageParts.put(part, fileName);
                    }
                }
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

        //Take data number each page
        int indexPage = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));

        typeRoomList = dal.TypeRoomDAO.getInstance().typeRoomPagination(indexPage, key);

        for (TypeRoom typeRoom : typeRoomList) {
            System.out.println(typeRoom.getTypeName() + ": " + typeRoom.getRoomImages());
        }

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

        System.out.println("appPath: " + appPath);
        System.out.println("rootDir: " + rootDir);
        System.out.println("buildPath: " + buildPath);
        System.out.println("fulBuildPath: " + fullBuildPath);
        System.out.println("savePath: " + savePath);
        System.out.println("fullPath: " + fullPath);

        filePart.write(fullPath);   // Lưu trong thư mục gốc
        filePart.write(fullBuildPath);  // Lưu trong build
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

        System.out.println("appPath: " + appPath);
        System.out.println("rootDir: " + rootDir);
        System.out.println("buildPath: " + buildPath);
        System.out.println("fulBuildPath: " + fullBuildPath);
        System.out.println("savePath: " + savePath);
        System.out.println("fullPath: " + fullPath);
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        System.out.println(contentDisp);
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
