package controller.site;

import dal.AreaDAO;
import dal.EmployeeDAO;
import dal.SiteDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Employee;
import model.ParkingArea;
import model.ParkingSite;
import utils.UrlConstants;
import utils.ValidationUtils;

/**
 *
 * @author Admin
 */
//@WebServlet(name = "DetailSite", urlPatterns = {UrlConstants.URL_ADMIN + "/site/detail"})
public class DetailSite extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SiteDAO siteDAO = new SiteDAO();
        AreaDAO areaDAO = new AreaDAO();
        EmployeeDAO empDAO = new EmployeeDAO();

        try {
            // 2. Validate dữ liệu đầu vào
            String idStr = request.getParameter("id");
            int id = ValidationUtils.requireValidInt(idStr, "ID bãi xe");

            // 3. Truy vấn thực thể chính
            ParkingSite site = siteDAO.getById(id);

            // 4. Xử lý trường hợp không tìm thấy dữ liệu (ID đúng định dạng nhưng không có trong DB)
            if (site == null) {
                String ctx = (String) request.getSession().getAttribute("ctx");
                // Gắn thêm tham số báo lỗi để hiển thị ở trang List
                response.sendRedirect(ctx + "/site/list?error=not_found");
                return;
            }

            // 5. Truy vấn dữ liệu liên quan (Chỉ chạy khi chắc chắn site tồn tại)
            List<ParkingArea> areaList = areaDAO.getAreasBySite(id);
            List<Employee> empList = empDAO.getBySiteId(id);

            // 6. Gắn dữ liệu vào Request
            request.setAttribute("site", site);
            request.setAttribute("areaList", areaList);
            request.setAttribute("employeeList", empList);

            // 7. Forward tới View (Đã bổ sung đuôi .jsp)
            request.getRequestDispatcher("/WEB-INF/views/site/detail.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            // BẮT LỖI NGƯỜI DÙNG NHẬP SAI (VD: ?id=abc hoặc bỏ trống)
            String ctx = (String) request.getSession().getAttribute("ctx");
            response.sendRedirect(ctx + "/site/list?error=invalid_id");
        } catch (Exception e) {
            // BẮT LỖI HỆ THỐNG (Database chết, đứt mạng...)
            e.printStackTrace(); // In ra console để Dev sửa
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống khi tải chi tiết bãi xe.");
        }
    }
}
