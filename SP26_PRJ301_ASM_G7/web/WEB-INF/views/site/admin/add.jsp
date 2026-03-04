<%-- 
    Document   : add
    Created on : Feb 28, 2026, 1:43:06 AM
    Author     : dat20
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thêm Bãi Xe Mới</title>

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
                --bs-primary: #2563eb; /* Xanh dương đậm chuẩn theo thiết kế */
                --bs-primary-rgb: 37, 99, 235;
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

            /* ==================== SIDEBAR ==================== */
            .sidebar {
                width: var(--sidebar-width);
                background-color: #ffffff;
                border-right: 1px solid var(--border-color);
                height: 100vh;
                position: fixed;
                top: 0;
                left: 0;
                z-index: 1040;
                transition: transform 0.3s ease-in-out;
                display: flex;
                flex-direction: column;
            }

            .sidebar-header {
                padding: 1.5rem;
                display: flex;
                align-items: center;
                gap: 12px;
            }

            .brand-logo {
                width: 36px;
                height: 36px;
                background-color: var(--bs-primary);
                color: white;
                border-radius: 8px;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                font-weight: 700;
                font-size: 1.25rem;
            }

            .brand-info {
                display: flex;
                flex-direction: column;
            }
            .brand-text {
                font-size: 1.125rem;
                font-weight: 700;
                color: #0f172a;
                line-height: 1.2;
            }
            .brand-subtext {
                font-size: 0.75rem;
                color: var(--text-muted);
            }

            .sidebar-nav {
                padding: 1rem;
                flex-grow: 1;
                overflow-y: auto;
            }

            .nav-item-link {
                display: flex;
                align-items: center;
                gap: 12px;
                padding: 0.75rem 1rem;
                color: #475569;
                text-decoration: none;
                border-radius: 8px;
                font-weight: 600;
                font-size: 0.95rem;
                margin-bottom: 0.35rem;
                transition: all 0.2s;
            }

            .nav-item-link:hover {
                background-color: #f1f5f9;
                color: #0f172a;
            }

            .nav-item-link.active {
                background-color: #eff6ff;
                color: var(--bs-primary);
            }
            .nav-item-link i {
                font-size: 1.1rem;
            }

            .user-profile-section {
                padding: 1rem;
                border-top: 1px solid var(--border-color);
                display: flex;
                align-items: center;
                gap: 12px;
                background-color: #fff;
            }

            .user-avatar {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                object-fit: cover;
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

            .breadcrumb-custom {
                display: flex;
                align-items: center;
                gap: 8px;
                font-size: 0.875rem;
                color: var(--text-muted);
                margin: 0;
            }
            .breadcrumb-custom .active {
                color: #0f172a;
                font-weight: 500;
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

            /* Content Area */
            .content-area {
                padding: 2rem;
                flex-grow: 1;
                max-width: 950px;
                margin: 0 auto;
                width: 100%;
            }

            .page-header {
                margin-bottom: 2rem;
            }
            .page-title {
                font-size: 1.25rem;
                font-weight: 700;
                color: #0f172a;
                margin: 0;
            }
            .page-subtitle {
                color: var(--text-muted);
                font-size: 0.95rem;
                margin-bottom: 0;
            }

            /* --- Form Card --- */
            .custom-card {
                background-color: #ffffff;
                border-radius: 12px;
                border: 1px solid var(--border-color);
                box-shadow: 0 1px 2px rgba(0,0,0,0.02);
                padding: 2.5rem;
                margin-bottom: 1.5rem;
            }

            /* Form Controls */
            .form-label {
                font-size: 0.875rem;
                font-weight: 600;
                color: #334155;
                margin-bottom: 0.5rem;
            }

            .form-label-price {
                font-size: 0.875rem;
                font-weight: 600;
                color: #334155;
                margin-bottom: 0.25rem;
            }

            .form-control, .form-select {
                border: 1px solid #cbd5e1;
                border-radius: 8px;
                padding: 0.6rem 1rem;
                font-size: 0.95rem;
                color: #0f172a;
                transition: all 0.2s;
                box-shadow: none !important;
            }

            .form-control::placeholder {
                color: #94a3b8;
            }

            .form-control:focus, .form-select:focus {
                border-color: var(--bs-primary);
                box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1) !important;
            }

            /* Custom Input Group for Address */
            .input-group-custom {
                display: flex;
                align-items: center;
                border: 1px solid #cbd5e1;
                border-radius: 8px;
                background-color: #fff;
                transition: all 0.2s;
                overflow: hidden;
            }
            .input-group-custom:focus-within {
                border-color: var(--bs-primary);
                box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
            }
            .input-group-custom .icon-wrapper {
                padding: 0.6rem 0.5rem 0.6rem 1rem;
                color: #94a3b8;
                font-size: 1.1rem;
            }
            .input-group-custom .form-control {
                border: none;
                box-shadow: none !important;
                padding-left: 0.5rem;
                background: transparent;
            }

            /* --- Dynamic Vehicle & Pricing Section --- */
            .vehicle-config-block {
                background-color: #f8fafc;
                border: 1px solid #e2e8f0;
                border-radius: 8px;
                padding: 1.5rem;
                margin-bottom: 1rem;
                position: relative;
                transition: all 0.3s ease;
            }

            .vehicle-config-block:hover {
                border-color: #cbd5e1;
            }

            .btn-remove-vehicle {
                color: #94a3b8;
                background: none;
                border: none;
                font-size: 1.25rem;
                padding: 0;
                transition: color 0.2s;
            }
            .btn-remove-vehicle:hover {
                color: #ef4444;
            }

            .pricing-section {
                margin-top: 1.5rem;
                padding-top: 1.5rem;
                border-top: 1px dashed #cbd5e1;
                /* Animation cho việc hiện lên */
                animation: slideDown 0.3s ease-out forwards;
            }

            .section-title-small {
                font-size: 1rem;
                font-weight: 700;
                color: #0f172a;
                margin-bottom: 1rem;
            }

            @keyframes slideDown {
                from {
                    opacity: 0;
                    transform: translateY(-10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            /* Map Placeholder Graphic */
            .map-placeholder {
                background-color: #e2e8f0;
                background-image: radial-gradient(circle at 50% 0%, #f1f5f9 0%, transparent 70%);
                border: 2px dashed #cbd5e1;
                border-radius: 12px;
                height: 250px;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                color: #475569;
                text-align: center;
                position: relative;
                overflow: hidden;
            }
            .map-icon {
                font-size: 2.5rem;
                color: var(--bs-primary);
                margin-bottom: 0.5rem;
                z-index: 2;
            }
            .map-text {
                font-size: 0.875rem;
                font-weight: 500;
                z-index: 2;
            }

            /* Action Buttons */
            .form-actions {
                display: flex;
                justify-content: flex-end;
                gap: 1rem;
                margin-top: 2.5rem;
                padding-top: 1.5rem;
                border-top: 1px solid var(--border-color);
            }

            .btn-cancel {
                background-color: #ffffff;
                border: 1px solid #cbd5e1;
                color: #475569;
                font-weight: 600;
                padding: 0.6rem 1.5rem;
                border-radius: 8px;
                transition: all 0.2s;
            }
            .btn-cancel:hover {
                background-color: #f8fafc;
                color: #0f172a;
            }

            .btn-save {
                background-color: var(--bs-primary);
                border: none;
                color: white;
                font-weight: 600;
                padding: 0.6rem 1.5rem;
                border-radius: 8px;
                transition: all 0.2s;
            }
            .btn-save:hover {
                background-color: #1d4ed8;
            }

            /* --- Alert Box --- */
            .info-alert {
                background-color: #eff6ff;
                border-radius: 8px;
                padding: 1.25rem;
                display: flex;
                align-items: flex-start;
                gap: 1rem;
            }
            .info-alert i {
                color: var(--bs-primary);
                font-size: 1.25rem;
                margin-top: -2px;
            }
            .info-alert p {
                margin: 0;
                color: #334155;
                font-size: 0.9rem;
                line-height: 1.5;
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

                .breadcrumb-custom {
                    display: none;
                }
            }

            @media (max-width: 576px) {
                .custom-card {
                    padding: 1.5rem;
                }
                .form-actions {
                    flex-direction: column;
                }
                .form-actions button {
                    width: 100%;
                }
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
                    <h1 class="page-title">Thêm bãi xe mới</h1>
                </div>
            </header>

            <!-- Content Area -->
            <div class="content-area">

                <div class="page-header">
                    <p class="page-subtitle">Vui lòng điền các thông tin chi tiết dưới đây để đăng ký bãi đỗ xe mới vào hệ thống.</p>
                </div>

                <!-- Form Card -->
                <div class="custom-card">
                    <form action="${ctx}/site/add" method="post" id="addParkingForm">
                        <div class="row g-4">
                            <!-- Tên bãi xe -->
                            <div class="col-12">
                                <label class="form-label">Tên bãi xe</label>
                                <input name="siteName" type="text" class="form-control" placeholder="Ví dụ: Bãi xe ParkEasy Quận 1" value="${savedData.siteName}" required>
                            </div>

                            <!-- Địa chỉ -->
                            <div class="col-12">
                                <label class="form-label">Địa chỉ</label>
                                <div class="input-group-custom">
                                    <div class="icon-wrapper"><i class="bi bi-geo-alt-fill"></i></div>
                                    <input name="siteAddress" type="text" class="form-control" placeholder="Nhập địa chỉ chi tiết" value="${savedData.siteAddress}" required>
                                </div>
                            </div>

                            <!-- Khu vực & Nhân viên -->
                            <div class="col-12 col-md-6">
                                <label class="form-label">Khu vực</label>
                                <select name="siteRegion" class="form-select">
                                    <option value="0" selected disabled>Chọn khu vực</option>
                                    <c:forEach items="${formData.regions}" var="region">
                                        <option value="${region.name().toLowerCase()}" ${region.name().toLowerCase() == savedData.siteRegion ? 'selected' : ''}>${region.label}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-12 col-md-6">
                                <label class="form-label">Nhân viên quản lý</label>
                                <select name="siteManager" class="form-select">
                                    <option value="0" selected>Chưa có nhân viên</option>
                                    <c:forEach items="${formData.availableManagers}" var="employee">
                                        <option value="${employee.employeeId}" ${employee.employeeId == savedData.siteManagerId ? 'selected' : ''}>${employee.getName()}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Trạng thái ban đầu -->
                            <div class="col-12 col-md-6">
                                <label class="form-label">Trạng thái ban đầu</label>
                                <select name="siteState" class="form-select">
                                    <c:forEach items="${formData.states}" var="state">
                                        <option value="${state.name().toLowerCase()}" ${state.name().toLowerCase() == savedData.siteState ? 'selected'  : ''}>${state.label}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- ============================================== -->
                            <!-- KHU VỰC ĐỘNG: Cấu hình sức chứa & Giá vé       -->
                            <!-- ============================================== -->
                            <div class="col-12 mt-5">
                                <h5 class="section-title-small">Cấu hình sức chứa theo loại xe</h5>

                                <!-- Container chứa các khối cấu hình -->
                                <div id="vehicleConfigContainer">
                                    <c:choose>
                                        <%-- Nếu có dữ liệu cũ thì render lại các block đó --%>
                                        <c:when test="${not empty savedData.vehicleConfigs}">
                                            <c:forEach items="${savedData.vehicleConfigs}" var="config" varStatus="status">
                                                <!-- Khối cấu hình mặc định (có thể thêm/xóa) -->
                                                <div class="vehicle-config-block">
                                                    <div class="row g-3 align-items-end">
                                                        <div class="col-12 col-md-5">
                                                            <label class="form-label">Loại xe</label>
                                                            <select name="vehicleType" class="form-select vehicle-type-select">
                                                                <option value="" selected disabled>Chọn loại xe</option>
                                                                <c:forEach items="${formData.vehicles}" var="vehicle">
                                                                    <option value="${vehicle.vehicleId}" ${vehicle.vehicleId == config.vehicleTypeId ? 'selected' : ''}>${vehicle.vehicleName.label}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                        <div class="col-10 col-md-6">
                                                            <label class="form-label">Số lượng (Sức chứa)</label>
                                                            <input name="capacity" type="number" class="form-control" placeholder="Ví dụ: 500" min="0" value="${config.capacity}">
                                                        </div>
                                                        <div class="col-2 col-md-1 text-center pb-1">
                                                            <button type="button" class="btn-remove-vehicle" title="Xóa loại xe này"><i class="bi bi-trash-fill"></i></button>
                                                        </div>
                                                    </div>

                                                    <!-- Thiết lập giá vé (Ẩn mặc định, hiển thị bằng JS) -->
                                                    <div class="pricing-section">
                                                        <h6 class="form-label mb-3">Thiết lập giá vé</h6>
                                                        <div class="row g-3">
                                                            <div class="col-12 col-md-6">
                                                                <label class="ms-2 form-label-price fw-light text-muted">Giá vé theo giờ (VNĐ)</label>
                                                                <input name="hourlyPrice" type="text" class="form-control mt-2" placeholder="Ví dụ: 12.000đ" value="${config.hourlyPrice}">
                                                            </div>
                                                            <div class="col-12 col-md-6">
                                                                <label class="ms-2 form-label-price fw-light text-muted">Giá vé theo tháng (VNĐ)</label>
                                                                <input name="monthlyPrice" type="text" class="form-control mt-2" placeholder="Ví dụ: 120.000đ" value="${config.monthlyPrice}">
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </c:when>

                                        <%-- Nếu KHÔNG có dữ liệu (lần đầu vào trang) thì render 1 block trống mặc định --%>
                                        <c:otherwise>
                                            <!-- Khối cấu hình mặc định (có thể thêm/xóa) -->
                                            <div class="vehicle-config-block">
                                                <div class="row g-3 align-items-end">
                                                    <div class="col-12 col-md-5">
                                                        <label class="form-label">Loại xe</label>
                                                        <select name="vehicleType" class="form-select vehicle-type-select">
                                                            <option value="" selected disabled>Chọn loại xe</option>
                                                            <c:forEach items="${formData.vehicles}" var="vehicle">
                                                                <option value="${vehicle.vehicleId}">${vehicle.vehicleName.label}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                    <div class="col-10 col-md-6">
                                                        <label class="form-label">Số lượng (Sức chứa)</label>
                                                        <input name="capacity" type="number" class="form-control" placeholder="Ví dụ: 500" min="0" required>
                                                    </div>
                                                    <div class="col-2 col-md-1 text-center pb-1">
                                                        <button type="button" class="btn-remove-vehicle" title="Xóa loại xe này"><i class="bi bi-trash-fill"></i></button>
                                                    </div>
                                                </div>

                                                <!-- Thiết lập giá vé (Ẩn mặc định, hiển thị bằng JS) -->
                                                <div class="pricing-section d-none">
                                                    <h6 class="form-label mb-3">Thiết lập giá vé</h6>
                                                    <div class="row g-3">
                                                        <div class="col-12 col-md-6">
                                                            <label class="ms-2 form-label-price fw-light text-muted">Giá vé theo giờ (VNĐ)</label>
                                                            <input name="hourlyPrice" type="text" class="form-control mt-2" placeholder="Ví dụ: 12.000đ" required>
                                                        </div>
                                                        <div class="col-12 col-md-6">
                                                            <label class="ms-2 form-label-price fw-light text-muted">Giá vé theo tháng (VNĐ)</label>
                                                            <input name="monthlyPrice" type="text" class="form-control mt-2" placeholder="Ví dụ: 120.000đ" required>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <!-- Nút thêm khối cấu hình mới -->
                                <button type="button" id="btnAddVehicle" class="btn btn-link text-decoration-none fw-semibold p-0 mt-2 d-inline-flex align-items-center gap-1">
                                    <i class="bi bi-plus-circle-fill"></i> Thêm loại xe
                                </button>
                            </div>
                            <!-- ============================================== -->
                        </div>

                        <!-- Buttons -->
                        <div class="form-actions">
                            <a type="button" class="btn btn-cancel" href="${ctx}/site">Hủy</a>
                            <button type="submit" class="btn btn-save">Thêm bãi xe</button>
                        </div>
                    </form>
                </div>

                <!-- Info Alert -->
                <div class="info-alert">
                    <i class="bi bi-info-circle-fill"></i>
                    <p>Sau khi bãi xe được tạo, bạn có thể thiết lập bảng giá chi tiết trong phần cài đặt của từng bãi xe.</p>
                </div>

            </div>
        </main>

        <jsp:include page="../../layout/toast-message.jsp" />

        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <script>
            // --- Logic đóng mở Sidebar trên điện thoại ---
            const mobileToggle = document.getElementById('mobileToggle');
            const sidebar = document.getElementById('sidebar');
            const overlay = document.getElementById('sidebarOverlay');
            function toggleMenu() {
                sidebar.classList.toggle('active');
                overlay.classList.toggle('active');
                if (sidebar.classList.contains('active')) {
                    document.body.style.overflow = 'hidden';
                } else {
                    document.body.style.overflow = '';
                }
            }

            mobileToggle.addEventListener('click', toggleMenu);
            overlay.addEventListener('click', toggleMenu);
            // --- Logic Xử lý Ẩn/Hiện Giá Vé và Thêm/Xóa Loại Xe ---
            const container = document.getElementById('vehicleConfigContainer');
            const btnAdd = document.getElementById('btnAddVehicle');
            // 1. Hàm cập nhật lại các tùy chọn (Disable loại xe đã chọn)
            function updateVehicleOptions() {
                const selects = document.querySelectorAll('.vehicle-type-select');
                const selectedValues = [];
                // Lấy tất cả các ID loại xe đã được chọn
                selects.forEach(select => {
                    if (select.value !== "") {
                        selectedValues.push(select.value);
                    }
                });
                // Duyệt qua từng ô select để disable/enable các option
                selects.forEach(select => {
                    const options = select.querySelectorAll('option');
                    options.forEach(option => {
                        if (option.value === "")
                            return; // Bỏ qua dòng "Chọn loại xe"

                        // Nếu loại xe này đã bị chọn ở một ô KHÁC -> Disable nó
                        if (selectedValues.includes(option.value) && option.value !== select.value) {
                            option.disabled = true;
                        } else {
                            option.disabled = false;
                        }
                    });
                });
                // 2. Ẩn nút "Thêm loại xe" nếu đã thêm hết các loại xe có thể
                // Lấy tổng số lượng loại xe có trong DB (đếm số option của 1 select bất kỳ, trừ đi dòng placeholder)
                const totalAvailableVehicles = selects[0].querySelectorAll('option').length - 1;
                if (selects.length >= totalAvailableVehicles) {
                    btnAdd.style.display = 'none'; // Ẩn nút thêm
                } else {
                    btnAdd.style.display = 'inline-flex'; // Hiện lại nút thêm
                }
            }

            // Hàm kiểm tra và hiển thị khu vực Giá vé khi Chọn loại xe
            function handleSelectChange(event) {
                if (event.target.classList.contains('vehicle-type-select')) {
                    const parentBlock = event.target.closest('.vehicle-config-block');
                    const pricingSection = parentBlock.querySelector('.pricing-section');
                    // Nếu người dùng đã chọn một giá trị (khác rỗng)
                    if (event.target.value !== "") {
                        pricingSection.classList.remove('d-none');
                    } else {
                        pricingSection.classList.add('d-none');
                    }

                    // Cập nhật lại dropdown mỗi khi người dùng thay đổi lựa chọn
                    updateVehicleOptions();
                }
            }

            // Hàm xóa một khối cấu hình
            function handleRemoveClick(event) {
                const btnRemove = event.target.closest('.btn-remove-vehicle');
                if (btnRemove) {
                    const parentBlock = btnRemove.closest('.vehicle-config-block');
                    // Kiểm tra, ít nhất phải giữ lại 1 khối cấu hình
                    if (container.children.length > 1) {
                        parentBlock.remove();
                        // Cập nhật lại dropdown (giải phóng loại xe vừa xóa)
                        updateVehicleOptions();
                    } else {
                        alert("Phải có ít nhất một cấu hình loại xe!");
                    }
                }
            }

            // Gắn sự kiện (Event Delegation) cho container
            container.addEventListener('change', handleSelectChange);
            container.addEventListener('click', handleRemoveClick);
            // Nút Thêm loại xe
            btnAdd.addEventListener('click', () => {
                // Lấy HTML của khối đầu tiên làm mẫu
                const firstBlock = container.querySelector('.vehicle-config-block');
                const newBlock = firstBlock.cloneNode(true); // Clone node

                // Reset các giá trị trong khối mới
                const select = newBlock.querySelector('.vehicle-type-select');
                select.value = "";
                const inputs = newBlock.querySelectorAll('input');
                inputs.forEach(input => input.value = "");
                // Ẩn lại phần giá vé ở khối mới
                const pricingSection = newBlock.querySelector('.pricing-section');
                pricingSection.classList.add('d-none');
                // Thêm khối mới vào container
                container.appendChild(newBlock);
                // Cập nhật lại dropdown cho ô vừa thêm
                updateVehicleOptions();
            });
            // Chạy cập nhật lần đầu tiên khi trang vừa load xong
            updateVehicleOptions();

        </script>
    </body>
</html>
