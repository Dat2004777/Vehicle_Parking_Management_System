//package controller.filter;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import java.io.IOException;
//import utils.UrlConstants;
//
//// Bắt mọi request tới hệ thống để kiểm duyệt
//@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})
//public class AuthorizationFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // Khởi tạo filter (có thể để trống)
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//        HttpSession session = req.getSession(false);
//
//        String uri = req.getRequestURI();
//        String contextPath = req.getContextPath();
//        
//        // Cắt bỏ contextPath để lấy chính xác path nghiệp vụ (VD: /manager/dashboard)
//        String path = uri.substring(contextPath.length()); 
//
//        // 1. Cho phép các tài nguyên Public và trang Login đi qua không cần kiểm duyệt
//        if (path.startsWith("/public") || path.startsWith("/assets") 
//                || path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/api")
//                || path.equals("/login") || path.equals("/")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // 2. Kiểm tra xác thực (Authentication): Đã đăng nhập chưa?
//        if (session == null || session.getAttribute("account") == null) {
//            // Chưa đăng nhập thì đá về trang Login
//            res.sendRedirect(contextPath + "/login"); 
//            return;
//        }
//
//        // 3. Phân quyền (Authorization): Kiểm tra Role Prefix
//        String rolePrefix = (String) session.getAttribute("rolePrefix");
//
//        // Nhóm các đường dẫn cần bảo vệ theo Role
//        boolean isProtectedPath = path.startsWith(UrlConstants.URL_ADMIN) || 
//                                  path.startsWith(UrlConstants.URL_STAFF) || 
//                                  path.startsWith(UrlConstants.URL_MANAGER);
//
//        if (isProtectedPath) {
//            if (rolePrefix == null || !path.startsWith(rolePrefix)) {
//                
//                // CÁCH 2: Forward thẳng tới trang giao diện 404 của bạn
//                req.getRequestDispatcher("/404-error.jsp").forward(req, res);
//                return;
//            }
//        }
//
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//        // Dọn dẹp tài nguyên (có thể để trống)
//    }
//}