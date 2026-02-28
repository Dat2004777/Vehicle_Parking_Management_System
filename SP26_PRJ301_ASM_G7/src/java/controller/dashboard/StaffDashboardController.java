package controller.dashboard;

import dal.AreaDAO;
import dal.EmployeeDAO;
import dal.SessionDAO;
import model.dto.RecentActivityDTO;
import model.dto.DashboardStatsDTO;
import model.ParkingSession;
import dal.SiteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter; // IMPORT BỘ FORMAT MỚI
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Account;
import model.Employee;
import model.ParkingArea;
import model.ParkingSite;
import model.VehicleType;
import model.dto.AreaDetailDTO;
import model.dto.SiteDetailDTO;

import utils.UrlConstants;

@WebServlet(name = "StaffDashboardController", urlPatterns = {UrlConstants.URL_STAFF + "/dashboard", UrlConstants.URL_STAFF})
public class StaffDashboardController extends HttpServlet {

    private SiteDAO siteDAO = new SiteDAO();
    private SessionDAO sessionDAO = new SessionDAO();
    private EmployeeDAO empDAO = new EmployeeDAO();
    private AreaDAO areaDAO = new AreaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account acc = (Account) request.getSession().getAttribute("account");
        Employee emp = empDAO.getById(acc.getEmployeeId());

        try {
            // Tạm thời gán cứng ID bãi xe theo employee để test giao diện
            int currentSiteId = emp.getSiteId();
            SiteDetailDTO overviewData = buildSiteOverview(currentSiteId);
            request.setAttribute("overview", overviewData);

            // 1. GỌI CÁC HÀM NỘI BỘ (PRIVATE) ĐỂ LẤY DỮ LIỆU DTO
            request.setAttribute("stats", getDashboardStats(currentSiteId));
            request.setAttribute("recentLogs", getRecentActivities(currentSiteId, 10));
            
            
            // 2. FORWARD TỚI JSP
            request.getRequestDispatcher("/WEB-INF/views/dashboard/staff.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải Dashboard");
        }
    }

    // =========================================================
    // CÁC HÀM NỘI BỘ (PRIVATE METHODS) XỬ LÝ LOGIC
    // =========================================================
    /**
     * Hàm tính toán và đóng gói Thống kê bãi xe
     */
    private DashboardStatsDTO getDashboardStats(int siteId) {
        String siteName = siteDAO.getById(siteId).getSiteName();
        String empName = empDAO.getById(2).getFullName();
        int total = siteDAO.getById(siteId).getTotalSlots();

        // Gọi thẳng SessionDAO để đếm số lượng ACTIVE hiện tại
        int occupied = sessionDAO.countActiveSessions(siteId);
        int available = Math.max(0, total - occupied);

        return new DashboardStatsDTO(siteName, empName, total, occupied, available);
    }

    /**
     * Hàm lấy nhật ký từ Database và biến đổi thành DTO hiển thị (Chuẩn Clean
     * Architecture)
     */
    private List<RecentActivityDTO> getRecentActivities(int siteId, int limit) {
        List<ParkingSession> rawLogs = sessionDAO.getRecentLogs(siteId, limit, "");
        List<RecentActivityDTO> dtoList = new ArrayList<>();

        // CẬP NHẬT: Thêm định dạng Ngày/Tháng/Năm (dd/MM/yyyy)
        // Dùng HH:mm (24h) thường chuyên nghiệp hơn cho bãi xe, nếu thích AM/PM thì dùng hh:mm a
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (ParkingSession session : rawLogs) {
            String formattedTime = "--/--/---- --:--";

            // Lấy thời gian tùy thuộc vào việc xe đang đỗ hay đã ra
            if ("parked".equalsIgnoreCase(session.getSessionState()) && session.getEntryTime() != null) {
                formattedTime = session.getEntryTime().format(timeFormatter);
            } else if ("completed".equalsIgnoreCase(session.getSessionState()) && session.getExitTime() != null) {
                formattedTime = session.getExitTime().format(timeFormatter);
            }

            // DTO giờ đây cực kỳ mỏng nhẹ, Controller không còn chứa code Giao diện (CSS) nữa
            dtoList.add(new RecentActivityDTO(
                    session.getLicensePlate(),
                    formattedTime,
                    session.getSessionState()
            ));
        }
        return dtoList;
    }

    private SiteDetailDTO buildSiteOverview(int siteId) {
        // Lấy Entity Site
        ParkingSite site = siteDAO.getById(siteId);

        // 1. Lấy danh sách Entity Khu vực từ DAO
        List<ParkingArea> rawAreas = areaDAO.getAreasBySite(siteId);

        // Chuẩn bị 2 cấu trúc dữ liệu để nhét vào SiteDetailDTO
        List<AreaDetailDTO> areaList = new ArrayList<>();
        Map<VehicleType, Integer> slotPerVehicleMap = new HashMap<>();

        // 2. Vòng lặp lắp ráp và tính toán dữ liệu
        for (ParkingArea area : rawAreas) {
            // Đếm số xe đang đỗ và tính chỗ trống
            int occupiedSlots = sessionDAO.countActiveSessionsByArea(area.getAreaId());
            int availableInArea = area.getTotalSlots() - occupiedSlots;

            // TẠO ĐỐI TƯỢNG VEHICLE TYPE (Dùng chung cho cả Map và AreaDetailDTO)
            // Lưu ý: Đảm bảo class VehicleType đã có hàm equals() và hashCode() theo vehicleTypeId
            VehicleType vt = new VehicleType(area.getVehicleTypeId(), area.getVehicleTypeName());

            // CỘNG DỒN VÀO MAP
            int currentAvailable = slotPerVehicleMap.getOrDefault(vt, 0);
            slotPerVehicleMap.put(vt, currentAvailable + availableInArea);

            // ĐÓNG GÓI VÀO DTO CHI TIẾT KHU VỰC
            AreaDetailDTO dto = new AreaDetailDTO(
                    area.getAreaId(),
                    area.getAreaName(),
                    vt, // Truyền luôn object vt vừa tạo ở trên vào đây cho đồng bộ
                    area.getTotalSlots(),
                    occupiedSlots
            );
            areaList.add(dto);
        }

        // 3. Khởi tạo DTO tổng hợp
        SiteDetailDTO overview = new SiteDetailDTO(
                site.getSiteName(),
                site.getAddress(),
                site.getSiteStatus(), // Dựa theo DB của bạn là operating_state
                areaList
        );

        // 4. Nạp cái Map vừa tính toán xong vào DTO
        overview.setSlotPerVehicle(slotPerVehicleMap);

        return overview;
    }
}
