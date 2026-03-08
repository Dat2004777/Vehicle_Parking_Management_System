<%-- 
    Document   : list (Employee)
    Created on : 24 Feb 2026, 00:26:12
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý nhân viên - Admin Parking</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <style>
            :root {
                --bs-primary: #3b82f6; /* Đồng bộ màu xanh với bãi xe */
                --bs-primary-rgb: 59, 130, 246;
                --bg-body: #f8fafc;
                --bg-sidebar: #ffffff;
                --sidebar-width: 260px; /* Khớp với bãi xe */
                --text-main: #334155;
                --text-dark: #0f172a;
                --text-muted: #64748b;
                --border-color: #e2e8f0;
            }

            body {
                font-family: 'Inter', sans-serif;
                background-color: var(--bg-body);
                color: var(--text-main);
                overflow-x: hidden;
            }

            /* ==================== MAIN CONTENT ==================== */
            .main-content {
                margin-left: var(--sidebar-width);
                min-height: 100vh;
                transition: margin-left 0.3s ease-in-out;
                display: flex;
                flex-direction: column;
            }

            /* Header (Top Bar) - GIỐNG BÃI XE */
            .top-header {
                height: 80px;
                background-color: #ffffff;
                border-bottom: 1px solid var(--border-color);
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 0 2rem;
                position: sticky;
                top: 0;
                z-index: 1030;
            }

            .page-title {
                font-size: 1.25rem;
                font-weight: 700;
                color: #0f172a;
                margin: 0;
            }

            /* Content Area */
            .content-area {
                padding: 2rem;
                flex-grow: 1;
            }

            /* Filter Section (Cùng 1 hàng ngang) */
            .filter-section {
                display: flex;
                gap: 1rem;
                margin-bottom: 1.5rem;
                align-items: center;
                flex-wrap: wrap; /* Tránh vỡ layout trên màn hình nhỏ */
            }

            .search-box {
                position: relative;
                flex-grow: 1; /* Cho ô search chiếm phần không gian còn lại */
                min-width: 250px;
            }
            .search-box i {
                position: absolute;
                left: 12px;
                top: 50%;
                transform: translateY(-50%);
                color: #94a3b8;
            }
            .search-box .form-control {
                padding-left: 2.5rem;
                border-radius: 8px;
                border-color: #cbd5e1;
                padding-top: 0.6rem;
                padding-bottom: 0.6rem;
                font-size: 0.875rem;
            }
            .search-box .form-control:focus {
                border-color: var(--bs-primary);
                box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
            }

            .filter-select {
                min-width: 160px;
                width: auto;
                border-radius: 8px;
                border-color: #cbd5e1;
                color: var(--text-main);
                padding-top: 0.6rem;
                padding-bottom: 0.6rem;
                font-size: 0.875rem;
                cursor: pointer;
            }
            .filter-select:focus {
                border-color: var(--bs-primary);
                box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
            }

            /* Table Card */
            .table-card {
                background-color: #fff;
                border-radius: 12px;
                border: 1px solid var(--border-color);
                box-shadow: 0 1px 2px rgba(0,0,0,0.01);
                overflow: hidden;
                padding: 1.5rem;
            }

            /* Table Scroll Container (Giống Bãi Xe) */
            .table-scroll-container {
                max-height: 500px;
                overflow-y: auto;
                position: relative;
                border-radius: 8px;
            }
            .table-scroll-container table thead {
                position: sticky;
                top: 0;
                z-index: 10;
                background-color: #ffffff;
                box-shadow: 0 1px 0 var(--border-color);
            }
            .table-scroll-container::-webkit-scrollbar {
                width: 6px;
            }
            .table-scroll-container::-webkit-scrollbar-track {
                background: #f1f5f9;
            }
            .table-scroll-container::-webkit-scrollbar-thumb {
                background: #cbd5e1;
                border-radius: 10px;
            }
            .table-scroll-container::-webkit-scrollbar-thumb:hover {
                background: #94a3b8;
            }

            .table {
                margin-bottom: 0;
                vertical-align: middle;
            }
            .table th {
                font-size: 0.75rem;
                font-weight: 600;
                color: #94a3b8;
                text-transform: uppercase;
                letter-spacing: 0.05em;
                padding: 1rem 1.5rem;
                border-bottom: 1px solid var(--border-color);
                background-color: transparent;
                white-space: nowrap;
            }
            .table td {
                padding: 1rem 1.5rem;
                border-bottom: 1px solid #f1f5f9;
                color: var(--text-main);
                font-size: 0.875rem;
                white-space: nowrap;
            }
            .table tbody tr:last-child td {
                border-bottom: none;
            }

            /* Hiệu ứng cho hàng trong bảng có thể click */
            .clickable-row {
                cursor: pointer;
                transition: all 0.2s ease-in-out;
            }
            .clickable-row:hover {
                background-color: #f8fafc !important;
                transform: translateY(-1px);
                box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
            }
            .clickable-row td {
                border-bottom: 1px solid transparent;
            }

            /* User Column Styles */
            .user-info {
                display: flex;
                align-items: center;
                gap: 12px;
            }
            .avatar-initials {
                width: 36px;
                height: 36px;
                border-radius: 50%;
                background-color: #e2e8f0;
                color: #475569;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: 600;
                font-size: 0.875rem;
            }
            .avatar-img {
                width: 36px;
                height: 36px;
                border-radius: 50%;
                object-fit: cover;
            }
            .user-name {
                font-weight: 600;
                color: var(--text-dark);
                margin-bottom: 0.1rem;
                font-size: 0.95rem;
            }
            .user-email {
                font-size: 0.75rem;
                color: var(--text-muted);
            }

            /* Role Badges */
            .role-badge {
                display: inline-flex;
                align-items: center;
                padding: 0.35rem 0.75rem;
                border-radius: 20px;
                font-size: 0.75rem;
                font-weight: 500;
                border: 1px solid transparent;
            }
            .role-red {
                background-color: #fef2f2; /* Nền đỏ cực nhạt */
                color: #dc2626; /* Chữ đỏ đậm */
                border-color: #fecaca; /* Viền đỏ nhạt */
            }
            .role-blue {
                background-color: #eff6ff;
                color: #2563eb;
                border-color: #bfdbfe;
            }
            .role-purple {
                background-color: #faf5ff;
                color: #9333ea;
                border-color: #e9d5ff;
            }
            .role-orange {
                background-color: #fff7ed;
                color: #ea580c;
                border-color: #fed7aa;
            }

            /* Status Dots */
            .status-wrapper {
                display: inline-flex;
                align-items: center;
                gap: 8px;
                font-weight: 500;
            }
            .status-dot {
                width: 8px;
                height: 8px;
                border-radius: 50%;
            }
            .status-dot.active {
                background-color: #10b981;
            }
            .status-dot.inactive {
                background-color: #cbd5e1;
            }

            .status-text-active {
                color: #334155;
            }
            .status-text-inactive {
                color: #94a3b8;
            }

            /* ==================== RESPONSIVE ==================== */
            #mobileToggle {
                display: none;
            }

            .sidebar-overlay {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                /* THÊM DÒNG NÀY ĐỂ CÓ MÀU XÁM MỜ KHI ACTIVE */
                background-color: rgba(15, 23, 42, 0.5);
                z-index: 1035; /* Đảm bảo nó nằm dưới Sidebar (1040) nhưng trên Main Content (1030) */
                opacity: 0;
                transition: opacity 0.3s ease;
            }

            @media (max-width: 991.98px) {
                .sidebar {
                    transform: translateX(-100%);
                }
                .sidebar.active {
                    transform: translateX(0);
                }
                .main-content {
                    margin-left: 0;
                }
                #mobileToggle {
                    display: block;
                    background: none;
                    border: none;
                    font-size: 1.5rem;
                    color: #0f172a;
                    padding: 0;
                    margin-right: 15px;
                }
                .top-header {
                    padding: 0 1.5rem;
                }
                .content-area {
                    padding: 1.5rem;
                }
                .sidebar-overlay.active {
                    display: block;
                    opacity: 1;
                }
            }

            @media (max-width: 767.98px) {
                .filter-section {
                    flex-direction: column;
                    align-items: stretch;
                }
                .search-box {
                    width: 100%;
                }
                .filter-select {
                    width: 100%;
                }
                /* THAY THẾ CHỖ NÀY */
                .btn-add-mobile {
                    width: 100%;
                }
            }
        </style>

    </head>
    <body>

        <jsp:include page="../../layout/admin-sidebar.jsp">
            <jsp:param name="activePage" value="adminEmployee" />
        </jsp:include>

        <div class="sidebar-overlay" id="sidebarOverlay"></div>

        <main class="main-content">
            <header class="top-header">
                <div class="d-flex align-items-center">
                    <button id="mobileToggle" aria-label="Toggle menu">
                        <i class="bi bi-list"></i>
                    </button>
                    <h1 class="page-title">Quản lý nhân viên</h1>
                </div>
            </header>

            <div class="content-area">

                <div class="filter-section">
                    <div class="search-box">
                        <i class="bi bi-search"></i>
                        <input value="${param.employeeSearch}" id="employeeSearch" name="employeeSearch" type="text" class="form-control" placeholder="Tìm theo tên hoặc mã nhân viên...">
                    </div>

                    <select name="roleFilter" class="form-select filter-select">
                        <option value="" selected>Tất cả chức vụ</option>
                        <c:forEach var="role" items="${roles}">
                            <option value="${role.name()}" ${param.role == role.name() ? 'selected' : ''}>
                                ${role.label}
                            </option>
                        </c:forEach>
                    </select>

                    <select name="statusFilter" class="form-select filter-select">
                        <option value="" selected>Tất cả trạng thái</option>
                        <option value="1">Đang làm việc</option>
                        <option value="2">Đang nghỉ</option>
                    </select>

                    <a href="${ctx}/employee/add" class="btn btn-primary text-decoration-none">
                        <i class="bi bi-plus-lg"></i> Thêm nhân viên mới
                    </a>
                </div>

                <div class="table-card">
                    <div class="table-responsive">
                        <div class="table-scroll-container">
                            <table class="table table-borderless align-middle text-nowrap">
                                <thead>
                                    <tr>
                                        <th>HỌ VÀ TÊN</th>
                                        <th>MÃ NV</th>
                                        <th>CHỨC VỤ</th>
                                        <th>BÃI XE PHỤ TRÁCH</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="employee" items="${listEmployees}">
                                        <tr class="clickable-row" onclick="window.location.href = '${ctx}/employee/detail?employeeId=${employee.employeeId}'">
                                            <td>
                                                <div class="user-info">
                                                    <div class="avatar-initials" style="color: #6366f1; background-color: #e0e7ff;">${employee.getAvatarInitials()}</div>
                                                    <div>
                                                        <div class="user-name">${employee.getName()}</div>
                                                        <div class="user-email">${employee.phone}</div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>${employee.getDisplayEmployeeId()}</td>
                                            <td><span class="role-badge ${employee.role == 'ADMIN' ? 'role-red' : 
                                                                          employee.role == 'MANAGER' ? 'role-orange' : 'role-blue'}">${employee.role.label}</span></td>
                                            <td>${employee.siteName}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </main>

        <jsp:include page="../../layout/toast-message.jsp" />

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <script>
                                            // --- Mobile Sidebar Toggle Logic (Đồng bộ chuẩn) ---
                                            const mobileToggle = document.getElementById('mobileToggle');
                                            const sidebar = document.querySelector('.sidebar'); // Lấy class .sidebar từ JSP layout nếu có
                                            const overlay = document.getElementById('sidebarOverlay');

                                            function toggleMenu() {
                                                // Đảm bảo class active đồng nhất với layout sidebar
                                                sidebar.classList.toggle('active');
                                                overlay.classList.toggle('active');

                                                if (sidebar.classList.contains('active')) {
                                                    document.body.style.overflow = 'hidden';
                                                } else {
                                                    document.body.style.overflow = '';
                                                }
                                            }

                                            if (mobileToggle) {
                                                mobileToggle.addEventListener('click', toggleMenu);
                                            }
                                            if (overlay) {
                                                overlay.addEventListener('click', toggleMenu);
                                            }

                                            document.getElementById('employeeSearch').addEventListener('change', function () {
                                                const query = this.value.trim();
                                                const currentUrl = new URL(window.location.href);

                                                // Sử dụng URLSearchParams để quản lý param chuyên nghiệp hơn
                                                if (query) {
                                                    currentUrl.searchParams.set('employeeSearch', query); // Đặt tên param khớp với Servlet của bạn
                                                } else {
                                                    currentUrl.searchParams.delete('employeeSearch'); // Xóa nếu ô search trống
                                                }

                                                // Chuyển hướng trang
                                                window.location.href = currentUrl.toString();
                                            });
        </script>
    </body>
</html>