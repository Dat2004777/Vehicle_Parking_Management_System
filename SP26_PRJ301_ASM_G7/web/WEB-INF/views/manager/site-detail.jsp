<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết bãi xe của tôi</title>

        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <style>
            :root {
                --bs-primary: #3b82f6;
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

            .main-content {
                margin-left: var(--sidebar-width);
                min-height: 100vh;
                transition: margin-left 0.3s ease-in-out;
                display: flex;
                flex-direction: column;
            }
            .top-header {
                height: 80px;
                background-color: #ffffff;
                border-bottom: 1px solid var(--border-color);
                display: flex;
                align-items: center;
                padding: 0 2rem;
                position: sticky;
                top: 0;
                z-index: 1030;
            }
            .content-area {
                padding: 2.5rem 2rem;
                flex-grow: 1;
                max-width: 1000px;
                margin: 0 auto;
                width: 100%;
            }

            .page-title-header {
                font-size: 1.25rem;
                font-weight: 700;
                color: #0f172a;
                margin: 0;
            }
            .page-title {
                font-size: 1.5rem;
                font-weight: 700;
                color: #0f172a;
                margin-bottom: 0.5rem;
            }
            .page-subtitle {
                color: var(--text-muted);
                font-size: 0.95rem;
                margin-bottom: 2rem;
            }

            .custom-card {
                background-color: #ffffff;
                border-radius: 12px;
                border: 1px solid var(--border-color);
                padding: 2.5rem;
                margin-bottom: 1.5rem;
                box-shadow: 0 1px 3px rgba(0,0,0,0.02);
            }

            .form-label {
                font-size: 0.875rem;
                font-weight: 600;
                color: #334155;
                margin-bottom: 0.5rem;
            }

            /* Làm mờ và khóa các ô Input cho Manager */
            .form-control:disabled, .form-select:disabled {
                background-color: #f8fafc;
                opacity: 1;
                color: #475569;
                cursor: not-allowed;
                border-color: #e2e8f0;
                font-weight: 500;
            }

            .form-control, .form-select {
                border: 1px solid #cbd5e1;
                border-radius: 8px;
                padding: 0.6rem 1rem;
                font-size: 0.95rem;
            }

            .input-group-custom {
                display: flex;
                align-items: center;
                border: 1px solid #e2e8f0;
                border-radius: 8px;
                background-color: #f8fafc;
                overflow: hidden;
            }
            .input-group-custom .icon-wrapper {
                padding: 0.6rem 0.5rem 0.6rem 1rem;
                color: #94a3b8;
                font-size: 1.1rem;
            }
            .input-group-custom .form-control {
                border: none;
                background: transparent;
                padding-left: 0.5rem;
            }

            .vehicle-config-block {
                background-color: #ffffff;
                border: 1px solid #e2e8f0;
                border-radius: 8px;
                padding: 1.5rem;
                margin-bottom: 1rem;
                box-shadow: 0 1px 2px rgba(0,0,0,0.02);
            }
            .pricing-section {
                margin-top: 1.5rem;
                padding-top: 1.5rem;
                border-top: 1px dashed #cbd5e1;
            }
            .section-title-small {
                font-size: 1.1rem;
                font-weight: 700;
                color: #0f172a;
                margin-bottom: 1rem;
            }

            .map-placeholder {
                background-color: #e2e8f0;
                border: 2px dashed #cbd5e1;
                border-radius: 12px;
                height: 300px;
                display: flex;
                align-items: center;
                justify-content: center;
                overflow: hidden;
            }

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
        </style>
    </head>
    <body>

        <jsp:include page="../layout/admin-sidebar.jsp">
            <jsp:param name="activePage" value="adminSite" />
        </jsp:include>

        <div class="sidebar-overlay" id="sidebarOverlay"></div>

        <main class="main-content">
            <header class="top-header">
                <div class="d-flex align-items-center w-100">
                    <button id="mobileToggle" aria-label="Toggle menu"><i class="bi bi-list"></i></button>
                    <h1 class="page-title-header ms-2">Bãi đỗ xe của tôi</h1>
                </div>
            </header>

            <div class="content-area">
                <h1 class="page-title">Thông tin chi tiết bãi xe</h1>
                <p class="page-subtitle">Xem thông tin và cấu hình bãi xe bạn đang phụ trách. (Chỉ Admin mới có quyền chỉnh sửa thông số này)</p>

                <div class="custom-card">
                    <div class="row g-4">
                        <div class="col-12">
                            <label class="form-label">Tên bãi xe</label>
                            <input type="text" class="form-control text-primary fw-bold fs-5" value="${parkingSite.siteName}" disabled>
                        </div>

                        <div class="col-12">
                            <label class="form-label">Địa chỉ</label>
                            <div class="input-group-custom">
                                <div class="icon-wrapper"><i class="bi bi-geo-alt-fill"></i></div>
                                <input type="text" class="form-control" value="${parkingSite.address}" disabled>
                            </div>
                        </div>

                        <div class="col-12 col-md-6">
                            <label class="form-label">Khu vực</label>
                            <select class="form-select" disabled>
                                <c:forEach items="${formData.regions}" var="region">
                                    <option value="${region.name()}" ${region.name() eq parkingSite.region.name() ? 'selected' : ''}>${region.label}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-12 col-md-6">
                            <label class="form-label">Trạng thái hoạt động</label>
                            <select class="form-select text-success fw-bold" disabled>
                                <c:forEach items="${formData.states}" var="state">
                                    <option value="${state.name()}" ${state.name() eq parkingSite.siteState.name() ? 'selected' : ''}>${state.label}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-12 mt-5">
                            <h5 class="section-title-small"><i class="bi bi-gear-wide-connected me-2 text-primary"></i>Sức chứa & Giá vé hiện tại</h5>

                            <div class="mt-3">
                                <c:forEach items="${vehicleConfigDTOs}" var="config">
                                    <div class="vehicle-config-block">
                                        <div class="row g-3 align-items-end">
                                            <div class="col-12 col-md-6">
                                                <label class="form-label">Loại xe</label>
                                                <select class="form-select" disabled>
                                                    <c:forEach items="${formData.vehicles}" var="v">
                                                        <option value="${v.vehicleId}" ${v.vehicleId eq config.vehicleTypeId ? 'selected' : ''}>${v.vehicleName.label}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div class="col-12 col-md-6">
                                                <label class="form-label">Số lượng (Sức chứa)</label>
                                                <input type="text" class="form-control" value="${config.capacity} chỗ" disabled>
                                            </div>
                                        </div>

                                        <div class="pricing-section">
                                            <h6 class="form-label mb-3">Mức giá áp dụng</h6>
                                            <div class="row g-3">
                                                <div class="col-12 col-md-6">
                                                    <label class="form-label text-muted fw-light">Giá vé theo giờ (VNĐ)</label>
                                                    <input type="text" class="form-control mt-1 text-danger fw-semibold" value="<fmt:formatNumber value='${config.hourlyPrice}' pattern='#,###'/> đ" disabled>
                                                </div>
                                                <div class="col-12 col-md-6">
                                                    <label class="form-label text-muted fw-light">Giá vé theo tháng (VNĐ)</label>
                                                    <input type="text" class="form-control mt-1 text-danger fw-semibold" value="<fmt:formatNumber value='${config.monthlyPrice}' pattern='#,###'/> đ" disabled>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>   
                            </div>
                        </div>

                        <div class="col-12 mt-4">
                            <label class="form-label">Vị trí trên bản đồ</label>
                            <div class="map-placeholder">
                                <c:url value="https://maps.google.com/maps" var="mapUrl">
                                    <c:param name="q" value="${parkingSite.address}" />
                                    <c:param name="hl" value="vi" />
                                    <c:param name="z" value="16" />
                                    <c:param name="output" value="embed" />
                                </c:url>
                                <iframe width="100%" height="100%" style="border:0;" src="${mapUrl}"></iframe>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
        <script>
            const mobileToggle = document.getElementById('mobileToggle');
            const sidebar = document.querySelector('.sidebar');
            const overlay = document.getElementById('sidebarOverlay');

            function toggleMenu() {
                if (sidebar)
                    sidebar.classList.toggle('active');
                if (overlay)
                    overlay.classList.toggle('active');
                if (document.body)
                    document.body.style.overflow = sidebar.classList.contains('active') ? 'hidden' : '';
            }

            if (mobileToggle)
                mobileToggle.addEventListener('click', toggleMenu);
            if (overlay)
                overlay.addEventListener('click', toggleMenu);
        </script>
    </body>
</html>