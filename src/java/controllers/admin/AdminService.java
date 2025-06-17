package controllers.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.Normalizer;
import java.util.List;
import models.Service;

public class AdminService extends HttpServlet {

    private String serviceIdd = "serviceId";
    private String serviceServlet = "service";
    private String submitt = "submit";
    private String serviceNamee = "serviceName";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String choose = request.getParameter("choose");
        if (choose == null) {
            choose = "ViewAllService";
        }
        String pageStr = request.getParameter("page");
        List<Service> list = dal.ServiceDAO.getInstance().getAllService();

        if (choose.equals("deleteService")) {
            String serviceIdStr = request.getParameter(serviceIdd);
            int serviceId = Integer.parseInt(serviceIdStr);
            List<Integer> allServiceOfDetailService = dal.ServiceDAO.getInstance().getAllServiceIdsFromDetailService();
            for (Integer integer : allServiceOfDetailService) {
                if (integer == serviceId) {
                    paginateServiceList(request, list, pageStr);
                    request.setAttribute("canNotDelete", "không thể xóa dịch vụ vì dịch vụ đã được sử dụng");
                    request.getRequestDispatcher("/View/Admin/ViewService.jsp").forward(request, response);
                    return;
                }
            }

            dal.ServiceDAO.getInstance().deleteService(serviceId);
            paginateServiceList(request, list, pageStr);
            response.sendRedirect(request.getContextPath() + "/admin/service?page=" + pageStr + "&action=delete&success=true");
        }
        //list all service
        if (choose.equals("ViewAllService")) {
            paginateServiceList(request, list, pageStr);
            request.getRequestDispatcher("/View/Admin/ViewService.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String choose = request.getParameter("choose");
        List<Service> list = dal.ServiceDAO.getInstance().getAllService();
        String pageStr = request.getParameter("page");
        if (pageStr != null) {
            request.setAttribute("page", pageStr);
        }

        if (choose.equals("search")) {
            paginateServiceList(request, list, pageStr);
            request.getRequestDispatcher("/View/Admin/ViewService.jsp").forward(request, response);
        }

        if ("toggleStatus".equals(choose)) {
            int serviceId = Integer.parseInt(request.getParameter(serviceIdd));
            dal.ServiceDAO.getInstance().toggleServiceStatus(serviceId);
            response.sendRedirect(request.getContextPath() + "/admin/service?page=" + pageStr + "&action=isActive&success=true");
        }

        //insert, update service
        if (choose.equals("insertService")) {
            insert(list, pageStr, request, response);
        } else if (choose.equals("updateService")) {
            update(list, pageStr, request, response);
        }
    }

    private void update(List<Service> list, String pageStr, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String serviceIdStr = request.getParameter(serviceIdd);
        String serviceName = request.getParameter(serviceNamee);
        String priceStr = request.getParameter("price");
        int serviceId = 0;
        int price = 0;
        boolean haveError = false;
        serviceId = Integer.parseInt(serviceIdStr);

        List<Integer> allServiceOfDetailService = dal.ServiceDAO.getInstance().getAllServiceIdsFromDetailService();
        boolean isUsed = allServiceOfDetailService.contains(serviceId);

        // Lấy tên cũ từ list
        String oldName = "";
        for (Service service : list) {
            if (service.getServiceId() == serviceId) {
                oldName = service.getServiceName();
                break;
            }
        }

        // Không cho đổi tên nếu đã dùng
        if (isUsed && !normalize(serviceName).equals(normalize(oldName))) {
            request.setAttribute("canNotUpdate", "Không thể thay đổi tên vì dịch vụ đã được sử dụng.");
            paginateServiceList(request, list, pageStr);
            request.getRequestDispatcher("/View/Admin/ViewService.jsp").forward(request, response);
            return;
        }

        if (serviceName == null || !serviceName.matches("^[\\p{L}0-9]+( [\\p{L}0-9]+)*$")) {
            request.setAttribute("serviceNameUpdateError", "Tên dịch vụ chỉ được chứa các chữ cái, chữ số và một khoảng trắng giữa các từ.");
            haveError = true;
        }

        try {
            price = Integer.parseInt(priceStr);
        } catch (Exception e) {
            haveError = true;
            request.setAttribute("priceUpdateError", "Giá phải là số lớn hơn 0");
        }

        for (Service service : list) {
            if (service.getServiceId() != serviceId
                    && normalize(service.getServiceName()).equals(normalize(serviceName))) {
                request.setAttribute("serviceNameUpdateError", "Tên dịch vụ này đã tồn tại.");
                haveError = true;
                break;
            }
        }

        if (haveError) {
            paginateServiceList(request, list, pageStr);
            request.getRequestDispatcher("/View/Admin/ViewService.jsp").forward(request, response);
            return;
        }

        Service s = new Service(serviceId, serviceName, price);
        dal.ServiceDAO.getInstance().updateService(s);
        response.sendRedirect(request.getContextPath() + "/admin/service?page=" + pageStr + "&action=update&success=true");

    }

    private void insert(List<Service> list, String pageStr, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String serviceName = request.getParameter(serviceNamee + "Add");
        String priceStr = request.getParameter("priceServiceAdd");
        int price = 0;
        boolean haveError = false;

        if (serviceName == null || !serviceName.matches("^[\\p{L}0-9]+( [\\p{L}0-9]+)*$")) {
            request.setAttribute("nameAddError", "Tên dịch vụ chỉ được chứa các chữ cái, chữ số và một khoảng trắng giữa các từ.");
            haveError = true;
        }
        for (Service service : list) {
            if (normalize(service.getServiceName()).equals(normalize(serviceName))) {
                request.setAttribute("nameAddError", "Tên dịch vụ này đã tồn tại.");
                haveError = true;
            }
        }

        try {
            price = Integer.parseInt(priceStr);
            if (price < 0) {
                request.setAttribute("priceAddError", "Giá phải là số lớn hơn 0.");
                haveError = true;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("priceAddError", "Giá phải là số lớn hơn 0.");
            haveError = true;
        }

        if (haveError) {
            paginateServiceList(request, list, pageStr);
            request.getRequestDispatcher("/View/Admin/ViewService.jsp").forward(request, response);
            return;
        }

        dal.ServiceDAO.getInstance().insertService(serviceName, price);
        response.sendRedirect(request.getContextPath() + "/admin/service?page=" + pageStr + "&action=add&success=true");

    }

    private void paginateServiceList(HttpServletRequest request, List<Service> fullList, String pageStr) {
        int page = 1;
        try {
            if (pageStr != null) {
                page = Integer.parseInt(pageStr);
            }
        } catch (NumberFormatException e) {
            // giữ page = 1 nếu sai định dạng
        }

        int recordsPerPage = 5;
        int totalRecords = fullList.size();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        // Giảm page nếu vượt quá số trang thực tế
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        int start = (page - 1) * recordsPerPage;
        int end = Math.min(start + recordsPerPage, totalRecords);
        List<Service> paginatedList = fullList.subList(start, end);

        request.setAttribute("listS", paginatedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
    }

    private String normalize(String input) {
        if (input == null) {
            return "";
        }
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // Bỏ dấu tiếng Việt
                .replaceAll("\\s+", " ") // Bỏ khoảng trắng thừa
                .trim()
                .toLowerCase();             // Về chữ thường
    }

}
