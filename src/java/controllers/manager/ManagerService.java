package controllers.manager;

import dal.ServiceDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.Normalizer;
import java.util.List;
import models.Service;

public class ManagerService extends HttpServlet {

    private String serviceIdd = "serviceId";
    private String serviceServlet = "service";
    private String submitt = "submit";
    private String serviceNamee = "serviceName";
    private String errorPrice = "Giá phải là chữ số lớn hơn hoặc bằng 0 và nhỏ hơn 5000 (5 nghìn) vnđ.";
    private int maxPrice = 5000;

    private ServiceDAO dao = dal.ServiceDAO.getInstance(); // dùng thay cho ServiceDAO.getInstance()

    public void setDao(ServiceDAO mockDao) {
        this.dao = mockDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String choose = request.getParameter("choose");
        if (choose == null) {
            choose = "ViewAllService";
        }
        String pageStr = request.getParameter("page");
        List<Service> list;

        String serviceNameSearch = request.getParameter("serviceNameSearch");
        if (serviceNameSearch == null || serviceNameSearch.trim().isEmpty() || "null".equalsIgnoreCase(serviceNameSearch)) {
            list = dao.getAllService();
            paginateServiceList(request, list, pageStr);
        } else {
            list = dal.ServiceDAO.getInstance().searchAllService(serviceNameSearch);
            paginateServiceList(request, list, pageStr);
        }

        if (choose.equals("search")) {
            request.getRequestDispatcher("/View/Manager/ViewService.jsp").forward(request, response);
        }

        if (choose.equals("deleteService")) {
            String serviceIdStr = request.getParameter(serviceIdd);
            int serviceId = Integer.parseInt(serviceIdStr);
            List<Integer> allServiceOfDetailService = dal.ServiceDAO.getInstance().getAllServiceIdsFromDetailService();
            for (Integer integer : allServiceOfDetailService) {
                if (integer == serviceId) {
                    paginateServiceList(request, list, pageStr);
                    request.setAttribute("canNotDelete", "không thể xóa dịch vụ vì dịch vụ đã được sử dụng");
                    request.getRequestDispatcher("/View/Manager/ViewService.jsp").forward(request, response);
                    return;
                }
            }

            dao.deleteService(serviceId);
            paginateServiceList(request, list, pageStr);
            response.sendRedirect(request.getContextPath() + "/manager/service?choose=search&serviceNameSearch="+serviceNameSearch+"&page=" + pageStr + "&action=delete&success=true");
        }
        //list all service
        if (choose.equals("ViewAllService")) {
            paginateServiceList(request, list, pageStr);
            request.getRequestDispatcher("/View/Manager/ViewService.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String choose = request.getParameter("choose");
        List<Service> list;
        String pageStr = request.getParameter("page");
        String serviceNameSearch = request.getParameter("serviceNameSearch");

        if (serviceNameSearch == null || serviceNameSearch.trim().isEmpty()) {
            list = dao.getAllService();
        } else {
            list = dao.searchAllService(serviceNameSearch);
        }

        if (pageStr != null) {
            request.setAttribute("page", pageStr);
        }

        if ("toggleStatus".equals(choose)) {
            int serviceId = Integer.parseInt(request.getParameter(serviceIdd));
            dal.ServiceDAO.getInstance().toggleServiceStatus(serviceId);
            response.sendRedirect(request.getContextPath() + "/manager/service?choose=search&serviceNameSearch=" + serviceNameSearch + "&page=" + pageStr + "&action=isActive&success=true");
        }

        //insert, update service
        if (choose.equals("insertService")) {
            insert(list, pageStr, serviceNameSearch, request, response);
        } else if (choose.equals("updateService")) {
            update(list, pageStr, serviceNameSearch, request, response);
        }
    }

    private void update(List<Service> list, String pageStr, String serviceNameSearch, HttpServletRequest request, HttpServletResponse response)
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
        if (oldName != null && isUsed && !normalize(serviceName).equals(normalize(oldName))) {
            request.setAttribute("canNotUpdate", "Không thể thay đổi tên dịch vụ vì dịch vụ đã được sử dụng.");
            paginateServiceList(request, list, pageStr);
            request.getRequestDispatcher("/View/Manager/ViewService.jsp").forward(request, response);
            return;
        }

        if (serviceName == null || !serviceName.matches("^[\\p{L}0-9]+( [\\p{L}0-9]+)*$")) {
            request.setAttribute("serviceNameUpdateError", "Tên dịch vụ không được để trống,"
                    + "Tên dịch vụ chỉ được chứa các chữ cái, chữ số và một khoảng trắng giữa các từ.");
            haveError = true;
        }

        try {
            price = Integer.parseInt(priceStr);
            if (price <= 0 || price > maxPrice) {
                request.setAttribute("priceUpdateError", errorPrice);
                haveError = true;
            }
        } catch (NumberFormatException e) {
            haveError = true;
            request.setAttribute("priceUpdateError", errorPrice);
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
            request.getRequestDispatcher("/View/Manager/ViewService.jsp").forward(request, response);
            return;
        }

        Service s = new Service(serviceId, serviceName, price);
        dao.updateService(s);
        response.sendRedirect(request.getContextPath() + "/manager/service?choose=search&serviceNameSearch=" + serviceNameSearch + "&page=" + pageStr + "&action=update&success=true");

    }

    protected void insert(List<Service> list, String pageStr, String serviceNameSearch, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String serviceName = request.getParameter(serviceNamee + "Add");
        String priceStr = request.getParameter("priceServiceAdd");
        int price = 0;
        boolean haveError = false;

        if (serviceName == null || !serviceName.matches("^[\\p{L}0-9]+( [\\p{L}0-9]+)*$")) {
            request.setAttribute("nameAddError", "Tên dịch vụ không được để trống,"
                    + " Tên dịch vụ chỉ được chứa các chữ cái, chữ số và một khoảng trắng giữa các từ.");
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
            if (price <= 0 || price > maxPrice) {
                request.setAttribute("priceAddError", errorPrice);
                haveError = true;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("priceAddError", errorPrice);
            haveError = true;
        }

        if (haveError) {
            paginateServiceList(request, list, pageStr);
            request.getRequestDispatcher("/View/Manager/ViewService.jsp").forward(request, response);
            return;
        }

        dao.insertService(serviceName, price);
        response.sendRedirect(request.getContextPath() + "/manager/service?choose=search&serviceNameSearch=" + serviceNameSearch + "&page=" + pageStr + "&action=add&success=true");

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
