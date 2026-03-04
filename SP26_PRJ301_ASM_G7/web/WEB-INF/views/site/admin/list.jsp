<%-- 
    Document   : list.jsp
    Created on : Feb 25, 2026, 1:00:10 AM
    Author     : dat20
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Bãi Đỗ Xe - ParkAdmin</title>

        <!-- Google Fonts: Inter -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

        <!-- Bootstrap 5 CSS -->
        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <style>
            :root {
                --bs-primary: #3b82f6;
                --bs-primary-rgb: 59, 130, 246;
                --bg-body: #f8fafc;
                --sidebar-width: 260px;
                --text-main: #334155;
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

            /* Header (Top Bar) */
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

            .search-container {
                position: relative;
                width: 320px;
            }

            .search-container .bi-search {
                position: absolute;
                left: 12px;
                top: 50%;
                transform: translateY(-50%);
                color: #94a3b8;
            }

            .search-input {
                width: 100%;
                padding: 0.5rem 1rem 0.5rem 2.5rem;
                background-color: #f1f5f9;
                border: 1px solid transparent;
                border-radius: 8px;
                font-size: 0.875rem;
                transition: all 0.2s;
                color: var(--text-main);
            }

            .search-input:focus {
                outline: none;
                background-color: #ffffff;
                border-color: #cbd5e1;
                box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
            }

            .header-actions {
                display: flex;
                align-items: center;
                gap: 1.25rem;
            }

            .btn-icon {
                background: none;
                border: none;
                color: var(--text-muted);
                font-size: 1.25rem;
                position: relative;
                padding: 0;
                cursor: pointer;
                transition: color 0.2s;
            }
            .btn-icon:hover {
                color: #0f172a;
            }

            .notification-dot {
                position: absolute;
                top: -2px;
                right: -2px;
                width: 8px;
                height: 8px;
                background-color: #ef4444;
                border-radius: 50%;
                border: 2px solid white;
            }

            /* Content Area */
            .content-area {
                padding: 2rem;
                flex-grow: 1;
            }

            /* --- Summary Cards --- */
            .summary-card {
                background-color: #ffffff;
                border: 1px solid var(--border-color);
                border-radius: 12px;
                padding: 1.25rem;
                display: flex;
                align-items: center;
                gap: 1rem;
                height: 100%;
                box-shadow: 0 1px 2px rgba(0,0,0,0.01);
            }
            .summary-icon {
                width: 48px;
                height: 48px;
                border-radius: 10px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 1.5rem;
                flex-shrink: 0;
            }
            /* Icon Colors */
            .icon-total {
                background-color: #eff6ff;
                color: #3b82f6;
            }
            .icon-active {
                background-color: #dcfce7;
                color: #10b981;
            }
            .icon-full {
                background-color: #fee2e2;
                color: #ef4444;
            }
            .icon-maint {
                background-color: #fff7ed;
                color: #f97316;
            }

            .summary-title {
                font-size: 0.875rem;
                color: var(--text-muted);
                font-weight: 500;
                margin-bottom: 0.25rem;
            }
            .summary-value {
                font-size: 1.5rem;
                font-weight: 700;
                color: #0f172a;
                line-height: 1;
            }

            /* --- Main Data Card & Table --- */
            .custom-card {
                background-color: #ffffff;
                border-radius: 16px;
                border: 1px solid var(--border-color);
                box-shadow: 0 1px 3px rgba(0,0,0,0.02);
                padding: 1.5rem;
            }

            .table-controls .btn-outline {
                border: 1px solid var(--border-color);
                color: var(--text-main);
                background-color: #fff;
                font-weight: 500;
                font-size: 0.875rem;
                padding: 0.4rem 0.8rem;
                display: inline-flex;
                align-items: center;
                gap: 0.5rem;
                border-radius: 6px;
            }
            .table-controls .btn-outline:hover {
                background-color: #f8fafc;
                border-color: #cbd5e1;
            }

            .table {
                margin-bottom: 0;
            }
            .table th {
                font-size: 0.75rem;
                font-weight: 600;
                color: #94a3b8;
                text-transform: uppercase;
                border-bottom: 1px solid var(--border-color);
                padding: 1rem 0.75rem;
                background-color: transparent;
            }
            .table td {
                padding: 1.25rem 0.75rem;
                vertical-align: middle;
                border-bottom: 1px solid #f1f5f9;
                color: var(--text-main);
                font-size: 0.875rem;
            }
            .table tbody tr:last-child td {
                border-bottom: none;
            }

            /* Type Icons */
            .type-icon {
                width: 40px;
                height: 40px;
                border-radius: 8px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: 700;
                font-size: 1.1rem;
                flex-shrink: 0;
            }
            .type-p-blue {
                background-color: #eff6ff;
                color: #3b82f6;
            }
            .type-star-orange {
                background-color: #fff7ed;
                color: #f97316;
            }
            .type-moto-green {
                background-color: #dcfce7;
                color: #10b981;
            }

            .lot-name {
                font-weight: 700;
                color: #0f172a;
                margin-bottom: 0.2rem;
            }
            .lot-sub {
                font-size: 0.75rem;
                color: var(--text-muted);
            }

            /* Badges */
            .custom-badge {
                display: inline-flex;
                align-items: center;
                gap: 6px;
                padding: 4px 12px;
                border-radius: 20px;
                font-size: 0.75rem;
                font-weight: 600;
            }
            .badge-active {
                background-color: #dcfce7;
                color: #16a34a;
            }
            .badge-maintenance {
                background-color: #fef3c7; /* Nền vàng nhạt */
                color: #d97706;            /* Chữ vàng cam đậm, cùng màu với dot-yellow */
            }
            .badge-full {
                background-color: #fee2e2;
                color: #dc2626;
            }
            .dot {
                width: 6px;
                height: 6px;
                border-radius: 50%;
            }
            .dot-green {
                background-color: #16a34a;
            }
            .dot-red {
                background-color: #dc2626;
            }

            /* Màu chấm tròn cho Bảo trì */
            .dot-yellow {
                background-color: #d97706; /* Màu vàng cam đậm (Amber-600) để nổi bật trên nền sáng */
            }

            /* Màu text cho Bảo trì */
            .text-warning-custom {
                color: #f59e0b !important; /* Màu vàng cam (Amber-500) */
            }

            .text-success-custom {
                color: #10b981 !important;
            }
            .text-danger-custom {
                color: #ef4444 !important;
            }

            /* Action Buttons */
            .action-btns .btn-icon-sm {
                color: #94a3b8;
                background: none;
                border: none;
                padding: 4px 8px;
                font-size: 1rem;
                transition: color 0.2s;
            }
            .action-btns .btn-icon-sm:hover {
                color: var(--bs-primary);
            }
            .action-btns .btn-icon-sm.delete:hover {
                color: #ef4444;
            }

            /* Pagination */
            .pagination-container {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding-top: 1.5rem;
                border-top: 1px solid var(--border-color);
                margin-top: 0.5rem;
            }
            .btn-page {
                background-color: #fff;
                border: 1px solid var(--border-color);
                color: var(--text-main);
                padding: 0.4rem 1rem;
                border-radius: 6px;
                font-size: 0.875rem;
                font-weight: 500;
                transition: all 0.2s;
            }
            .btn-page:hover {
                background-color: #f8fafc;
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
                background-color: rgba(15, 23, 42, 0.5);
                z-index: 1035;
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

                .search-container {
                    width: 200px;
                }
            }

            @media (max-width: 767.98px) {
                .search-container {
                    width: 100%;
                }
                .table-controls {
                    flex-direction: column;
                    align-items: flex-start !important;
                    gap: 1rem;
                }
            }

            /* Hiệu ứng cho hàng trong bảng có thể click */
            .clickable-row {
                cursor: pointer; /* Đổi chuột thành hình bàn tay */
                transition: all 0.2s ease-in-out; /* Làm mượt animation */
            }

            .clickable-row:hover {
                background-color: #f8fafc !important; /* Đổi màu nền sang xám rất nhạt (chuẩn Tailwind) */
                transform: translateY(-1px); /* Nổi dòng lên 1 pixel tạo cảm giác 3D */
                box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05); /* Thêm bóng đổ siêu nhẹ */
            }

            /* Đảm bảo các viền không bị vỡ khi dòng nổi lên */
            .clickable-row td {
                border-bottom: 1px solid transparent;
            }
        </style>
    </head>
    <body>

        <jsp:include page="../../layout/admin-sidebar.jsp">
            <jsp:param name="activePage" value="adminSite" />
        </jsp:include>

        <!-- Main Content -->
        <main class="main-content">
            <!-- Header -->
            <header class="top-header">
                <div class="d-flex align-items-center">
                    <button id="mobileToggle" aria-label="Toggle menu">
                        <i class="bi bi-list"></i>
                    </button>
                    <h1 class="page-title">Danh sách bãi xe</h1>
                </div>

            </header>

            <!-- Content Area -->
            <div class="content-area">

                <!-- Header Section -->
                <div class="d-flex flex-column flex-md-row align-items-md-center mb-4 gap-3">
                    <div class="search-container">
                        <i class="bi bi-search"></i>
                        <input id="siteSearchQuery" name="siteSearchQuery" type="text" class="search-input" placeholder="Tìm kiếm bãi xe, địa chỉ..." value="${param.siteSearchQuery}">
                    </div>
                    <a href="${ctx}/site/add" class="btn btn-primary d-flex align-items-center gap-2 px-3 fw-medium">
                        <i class="bi bi-plus-lg"></i> Thêm bãi xe mới
                    </a>
                </div>

                <!-- Summary Cards Row -->
                <div class="row g-3 mb-4">
                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="summary-card">
                            <div class="summary-icon icon-total"><i class="bi bi-building"></i></div>
                            <div>
                                <div class="summary-title">Tổng số bãi xe</div>
                                <div class="summary-value">${siteStateDTO.totalSites}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="summary-card">
                            <div class="summary-icon icon-active"><i class="bi bi-check-circle"></i></div>
                            <div>
                                <div class="summary-title">Đang hoạt động</div>
                                <div class="summary-value">${siteStateDTO.operatingSites}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="summary-card">
                            <div class="summary-icon icon-full"><i class="bi bi-x-circle"></i></div>
                            <div>
                                <div class="summary-title">Đóng cửa</div>
                                <div class="summary-value">${siteStateDTO.closedSites}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="summary-card">
                            <div class="summary-icon icon-maint"><i class="bi bi-tools"></i></div>
                            <div>
                                <div class="summary-title">Bảo trì</div>
                                <div class="summary-value">${siteStateDTO.maintenanceSites}</div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Main Table Card -->
                <div class="custom-card">

                    <!-- Table Controls -->
                    <!--                    <div class="table-controls d-flex justify-content-between align-items-center mb-3">
                                            <div class="d-flex gap-2">
                                                <button class="btn-outline">
                                                    <i class="bi bi-funnel"></i> Lọc
                                                </button>
                                                <button class="btn-outline">
                                                    <i class="bi bi-sort-down"></i> Sắp xếp
                                                </button>
                                            </div>
                                            <span class="text-muted" style="font-size: 0.875rem;">Hiển thị 1-5 trong số 12 bãi xe</span>
                                        </div>-->

                    <!-- Table -->
                    <div class="table-responsive">
                        <table class="table table-borderless align-middle text-nowrap">
                            <thead>
                                <tr>
                                    <th>TÊN BÃI XE</th>
                                    <th>ĐỊA CHỈ</th>
                                    <th class="text-center">TRỐNG</th>
                                    <th class="text-center">TỔNG CHỖ</th>
                                    <th>TRẠNG THÁI</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="site" items="${parkingSites}">
                                    <tr class="clickable-row" onclick="window.location.href = '${ctx}/site/detail?siteId=${site.siteId}'">
                                        <td>
                                            <div class="d-flex align-items-center gap-3">
                                                <div class="type-icon type-p-blue">P</div>
                                                <div>
                                                    <div class="lot-name">${site.siteName}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="text-muted">
                                            <i class="bi bi-geo-alt me-1"></i> ${site.address}
                                        </td>
                                        <c:set var="densityData" value="${densityMap[site.siteId]}" />

                                        <td class="text-center fw-bold ${
                                            densityData != null && densityData.densityPercentage > 90 ? 'text-danger-custom' : 
                                                densityData != null && densityData.densityPercentage > 75 ? 'text-warning-custom' : 
                                                'text-success-custom'
                                            }">
                                            ${densityData != null ? densityData.availableCapacity : 0}
                                        </td>
                                        <td class="text-center fw-bold text-dark">
                                            ${densityData != null ? densityData.maxCapacity : 0}
                                        </td>
                                        <td>
                                            <span class="custom-badge ${site.siteState == 'OPERATING' ? 'badge-active' : 
                                                                        site.siteState == 'CLOSED' ? 'badge-full' : 'badge-maintenance'}">
                                                <span class="dot ${site.siteState == 'OPERATING' ? 'dot-green' : 
                                                                   site.siteState == 'CLOSED' ? 'dot-red' : 'dot-yellow'}">
                                                </span> 
                                                ${site.siteState.label}
                                            </span>
                                        </td>
                                    </tr>
                                    <!--                                <iframe 
                                                                        width="100%" 
                                                                        height="300" 
                                                                        frameborder="0" 
                                                                        scrolling="no" 
                                                                        marginheight="0" 
                                                                        marginwidth="0" 
                                                                        style="border:0;"
                                                                        title="Bản đồ vị trí bãi xe"
                                                                        allowfullscreen="" 
                                                                        loading="lazy"
                                                                        src="https://maps.google.com/maps?q=${site.address}&hl=vi&z=16&output=embed">
                                                                    </iframe>-->
                                    <!--                                <c:url value="https://maps.google.com/maps" var="mapUrl">
                                        <c:param name="q" value="${site.address}" />
                                        <c:param name="hl" value="vi" />
                                        <c:param name="z" value="16" />
                                        <c:param name="output" value="embed" />
                                    </c:url>
    
                                    <iframe 
                                        width="100%" 
                                        height="300" 
                                        style="border:0; border-radius: 8px;" 
                                        src="${mapUrl}">
                                    </iframe>-->
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- Pagination -->
                    <div class="pagination-container">
                        <div class="d-flex gap-2">
                            <button class="btn-page" disabled>Trước</button>
                            <button class="btn-page">Tiếp</button>
                        </div>
                        <div class="text-muted" style="font-size: 0.875rem;">
                            Trang <span class="fw-bold text-dark">1</span> của <span class="fw-bold text-dark">3</span>
                        </div>
                    </div>

                </div>

            </div>
        </main>

        <jsp:include page="../../layout/toast-message.jsp" />

        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <script>
                                        // --- Mobile Sidebar Toggle Logic ---
                                        const mobileToggle = document.getElementById('mobileToggle');
                                        const sidebar = document.getElementById('sidebar');
                                        const overlay = document.getElementById('sidebarOverlay');

                                        function toggleMenu() {
                                            sidebar.classList.toggle('active');
                                            overlay.classList.toggle('active');

                                            // Prevent body scroll when menu is open on mobile
                                            if (sidebar.classList.contains('active')) {
                                                document.body.style.overflow = 'hidden';
                                            } else {
                                                document.body.style.overflow = '';
                                            }
                                        }

                                        mobileToggle.addEventListener('click', toggleMenu);
                                        overlay.addEventListener('click', toggleMenu);
        </script>

        <script>
            document.getElementById('siteSearchQuery').addEventListener('change', function () {
                const query = this.value.trim();
                const currentUrl = new URL(window.location.href);

                // Sử dụng URLSearchParams để quản lý param chuyên nghiệp hơn
                if (query) {
                    currentUrl.searchParams.set('siteSearchQuery', query); // Đặt tên param khớp với Servlet của bạn
                } else {
                    currentUrl.searchParams.delete('siteSearchQuery'); // Xóa nếu ô search trống
                }

                // Chuyển hướng trang
                window.location.href = currentUrl.toString();
            });
        </script>
    </body>
</html>
