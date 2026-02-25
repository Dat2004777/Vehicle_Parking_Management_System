/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.dashboard;

import dal.AreaDAO;
import dal.EmployeeDAO;
import dal.SessionDAO;
import dal.SiteDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Employee;
import model.ParkingArea;
import model.ParkingSite;
import model.VehicleType;
import model.dto.AreaDetailDTO;
import model.dto.SiteOverviewDTO;
import utils.UrlConstants;

/**
 *
 * @author Admin
 */
@WebServlet(name = "StaffSiteOverview", urlPatterns = {
    UrlConstants.URL_STAFF + "/site-overview", 
    UrlConstants.URL_ADMIN + "/parking-monitor"
})
public class StaffSiteOverview extends HttpServlet {

    // Khởi tạo các DAO cần thiết (ĐÃ XÓA VehicleTypeDAO)
    private final SiteDAO siteDAO = new SiteDAO();
    private final AreaDAO areaDAO = new AreaDAO();
    private final SessionDAO sessionDAO = new SessionDAO();
    private final EmployeeDAO empDAO = new EmployeeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Lấy ID Bãi xe từ nhân viên đang đăng nhập (Tạm gán cứng ID=2)
            Employee emp = empDAO.getById(2); 
            int currentSiteId = emp.getSiteId();

            // 1. Lắp ráp dữ liệu DTO
            SiteOverviewDTO overviewData = buildSiteOverview(currentSiteId);
            request.setAttribute("overview", overviewData);

            // 2. Phân biệt Role Admin
            if (request.getRequestURI().contains(UrlConstants.URL_ADMIN)) {
                request.setAttribute("isAdmin", true);
            }

            // 3. Đẩy sang JSP
            request.getRequestDispatcher("/WEB-INF/views/site/staff-site-overview.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải Sơ đồ bãi xe");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controller hiển thị Sơ đồ tổng quan Bãi đỗ xe";
    }
    
    // =========================================================
    // HÀM NỘI BỘ: MAP TỪ ENTITY SANG DTO
    // =========================================================
    private SiteOverviewDTO buildSiteOverview(int siteId) {
        // Lấy Entity Site
        ParkingSite site = siteDAO.getById(siteId);

        // 1. Lấy danh sách Entity Khu vực từ DAO (Đã có sẵn vehicleTypeName)
        List<ParkingArea> rawAreas = areaDAO.getAreasBySite(siteId);
        List<AreaDetailDTO> areaList = new ArrayList<>();

        // 2. Vòng lặp lắp ráp dữ liệu
        for (ParkingArea area : rawAreas) {
            
            // TỐI ƯU: Lấy trực tiếp tên loại xe từ Entity, không cần query thêm
            String vehicleTypeName = area.getVehicleTypeName();
            if (vehicleTypeName == null || vehicleTypeName.isEmpty()) {
                vehicleTypeName = "Chưa phân loại";
            }
            
            // Lấy số lượng xe đang đỗ tại khu này
            int occupiedSlots = sessionDAO.countActiveSessionsByArea(area.getAreaId());

            // Đóng gói vào DTO
            AreaDetailDTO dto = new AreaDetailDTO(
                    area.getAreaId(), 
                    area.getAreaName(), 
                    new VehicleType(area.getVehicleTypeId(), vehicleTypeName), 
                    area.getTotalSlots(), 
                    occupiedSlots
            );
            areaList.add(dto);
        }

        // 3. Trả về DTO tổng hợp
        return new SiteOverviewDTO(
                site.getSiteName(),
                site.getAddress(),
                site.getSiteStatus(),
                areaList
        );
    }
}