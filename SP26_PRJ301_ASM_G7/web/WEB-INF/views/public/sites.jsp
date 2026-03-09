<%-- 
    Document   : booking
    Created on : 24 Feb 2026, 00:23:29
    Author     : Admin
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Google Fonts: Inter -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

        <!-- Bootstrap 5 CSS -->
        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <title>Sites Page</title>
        <style>
            :root {
                --primary-color: #2563eb;
                --bs-primary: #1a56db;
                --bs-primary-rgb: 26, 86, 219;
                --bs-font-sans-serif: 'Inter', sans-serif;
                --text-main: #374151;
                --text-muted: #6b7280;
                --text-dark: #1e293b;
                --bg-warning: #fff7ed;
                --text-warning: #c2410c;
            }

            body {
                font-family: var(--bs-font-sans-serif);
                color: var(--text-main);
                background-color: #f3f4f6;
                -webkit-font-smoothing: antialiased;
            }

            /* Cấu trúc Card */
            .parking-card {
                border-radius: 20px;
                border: 1px solid #e2e8f0 !important;
                transition: transform 0.2s ease;
                overflow: hidden;
            }

            .parking-card:hover {
                transform: translateY(-4px);
            }

            /* Chia tỷ lệ 30/70 */
            .map-container {
                flex: 0 0 32%;
                /* Ảnh chiếm 32% */
                background-color: #f8fafc;
            }

            .info-container {
                flex: 1;
                /* Thông tin chiếm phần còn lại */
            }

            /* Image styling */
            .image-wrapper {
                position: relative;
                min-height: 220px;
            }

            .distance-tag {
                position: absolute;
                top: 25px;
                left: 25px;
                background: white;
                padding: 4px 12px;
                border-radius: 8px;
                font-size: 0.8rem;
                font-weight: 600;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }

            /* Typography & Elements */
            .card-title {
                font-size: 1.25rem;
                color: #0f172a;
            }

            .badge-warning-custom {
                background-color: var(--bg-warning);
                color: var(--text-warning);
                border: 1px solid #ffedd5;
                padding: 8px 16px;
                font-weight: 500;
            }

            .badge-success-custom {
                background-color: #f0fdf4;
                color: #15803d;
                border: 1px solid #dcfce7;
                padding: 8px 16px;
                font-weight: 500;
            }

            .badge-danger-custom {
                background-color:#FFFBE6;
                color: #D48806;
                border: 1px solid #dcfce7;
                padding: 8px 16px;
                font-weight: 500;
            }

            .badge-light-custom {
                background-color: #f1f5f9;
                color: #64748b;
                border: 1px solid #e2e8f0;
                padding: 8px 16px;
                font-weight: 500;
            }

            .btn-book {
                background-color: var(--primary-color);
                border-radius: 12px;
                font-weight: 600;
                border: none;
            }

            .form-control:focus, .form-select:focus {
                box-shadow: none;
                border-color: #cbd5e1;
            }
            /* --- MOBILE RESPONSIVE --- */
            @media (max-width: 767.98px) {

                /* Ẩn hoàn toàn cột chứa ảnh/map */
                .map-container {
                    display: none !important;
                }

                .info-container {
                    padding: 1.5rem !important;
                }

                .card-title {
                    font-size: 1.1rem;
                }

                .btn{
                    width: 100%;
                }
            }

            /* Styling cho Search & Filter */
            .search-input-group {
                border-radius: 10px;
                overflow: hidden;
            }

            .search-input-group .input-group-text,
            .search-input-group .form-control,
            .search-input-group .form-select {
                border: 1px solid #e2e8f0;
                font-size: 0.95rem;
                height: 48px;
            }

            .search-btn {
                border-radius: 10px;
                background-color: #2563eb;
            }

            /* Filter Pills */
            .filter-dropdown {
                cursor: pointer;
                font-size: 0.85rem;
                color: #1e293b;
                border: 1px solid #e2e8f0 !important;
            }

            .filter-pill {
                background-color: #f1f5f9;
                border-radius: 10px;
                font-size: 0.85rem;
                font-weight: 500;
                color: #475569;
                padding: 6px 16px;
                border: 1px solid transparent;
                transition: all 0.2s;
            }

            .filter-pill:hover {
                background-color: #e2e8f0;
                border-color: #cbd5e1;
            }

            .vr {
                width: 1px;
                height: 24px;
                background-color: #e2e8f0;
                opacity: 1;
            }

            /* Responsive cho Filter */
            @media (max-width: 768px) {
                .filter-pill {
                    flex-grow: 1;
                    /* Trên mobile các nút filter dãn đều */
                    text-align: center;
                }
            }
        </style>
    </head>
    <body>
        <!--Footer-->
        <%@include file="/WEB-INF/views/layout/header.jsp" %>

        <div class="bg-light py-4 border-bottom">
            <div class="container">
                <form action="${pageContext.request.contextPath}/sites" method="get">
                    <input type="hidden" name="action" value="${param.action}">
                    <div class="bg-white rounded-4 shadow-sm px-4 py-4 mx-auto" style="max-width: 1300px;">
                        <div class="row g-2 mb-3 align-items-end">
                            <div class="col-lg-5 col-md-12">
                                <div class="input-group search-input-group shadow-sm">
                                    <span class="input-group-text bg-white border-end-0"><i class="bi bi-search"></i></span>
                                    <input name="siteAddress" value="${param.siteAddress}" type="text" class="form-control border-start-0" placeholder="Nhập địa điểm">
                                </div>
                            </div>

                            <div class="col-lg-5 col-md-12">
                                <div class="input-group search-input-group shadow-sm">
                                    <span class="input-group-text bg-white border-end-0"><i class="bi bi-geo-alt"></i></span>
                                    <select name="region" class="form-select border-start-0">
                                        <option value="">-- Tất cả khu vực --</option>
                                        <c:forEach var="region" items="${regions}">
                                            <option value="${region}" ${param.region == region ? 'selected' : ''}>
                                                <c:choose>
                                                    <c:when test="${region == 'north'}">Miền Bắc</c:when>
                                                    <c:when test="${region == 'middle'}">Miền Trung</c:when>
                                                    <c:when test="${region == 'south'}">Miền Nam</c:when>
                                                </c:choose>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <div class="col-lg-2 col-md-12">
                                <button type="submit" class="btn btn-primary w-100 search-btn fw-bold d-flex align-items-center justify-content-center" style="height: 48px;">
                                    <i class="bi bi-search me-2"></i>Tìm kiếm
                                </button>
                            </div>
                        </div>
                        <div class="d-flex flex-wrap gap-2 align-items-center">

                            <!-- Trạng thái -->
                            <div class="filter-dropdown bg-white border rounded-pill px-2 py-1 shadow-sm">
                                <select name="status" class="form-select border-0 shadow-none small fw-medium" onchange="this.form.submit()">
                                    <option value="">Trạng thái: Tất cả</option>
                                    <option value="operating" ${param.status == 'operating' ? 'selected' : ''}> Hoạt động</option>
                                </select>
                            </div>

                            <!-- Loại xe -->
                            <div class="filter-dropdown bg-white border rounded-pill px-2 py-1 shadow-sm">
                                <select name="vehicleType" class="form-select border-0 shadow-none small fw-medium" onchange="this.form.submit()">
                                    <option value="">Loại xe: Tất cả</option>
                                    <c:forEach var="vehicle" items="${vehicles}">
                                            <option value="${vehicle.vehicleTypeId}" ${param.vehicleType == vehicle.vehicleTypeId ? 'selected' : ''}>
                                                ${vehicle.vehicleName.label} 
                                            </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="container mt-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div class="d-flex align-items-center">
                    <span class="text-muted me-2 fs-6">Sắp xếp:</span>
                    <div class="position-relative d-flex align-items-center">
                        <select class="form-select border-0 fw-bold p-0 pe-5 shadow-none bg-transparent" 
                                style="width: auto; cursor: pointer; appearance: none; -webkit-appearance: none;">
                            <option>Đề xuất</option>
                            <option>Giá</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="container py-5">
            <div class="row">
                <div class="col-12">
                    <c:forEach var="s" items="${sites}">
                        <div class="card border shadow-sm parking-card mb-4" onclick="goToSiteDetail(${s.siteId})">
                            <div class="d-flex align-items-stretch">
                                <div class="map-container">
                                    <div class="image-wrapper p-3 h-100">
                                        <c:url var="mapUrl" value="https://maps.google.com/maps">
                                            <c:param name="q" value="${s.address}" />
                                            <c:param name="output" value="embed" />
                                        </c:url>

                                        <iframe 
                                            width="100%" 
                                            height="100%" 
                                            src="${mapUrl}" 
                                            style="border:0;" 
                                            allowfullscreen="" 
                                            loading="lazy"
                                            class="rounded-4">
                                        </iframe>
                                    </div>
                                </div>

                                <div class="info-container p-4">
                                    <div class="d-flex flex-column h-100 justify-content-between">
                                        <div>
                                            <div class="d-flex justify-content-between align-items-start">
                                                <h4 class="card-title fw-bold fs-3 mt-3">${s.siteName}</h4>
                                                <div class="rating-box">
                                                    <c:if test="${s.siteState eq 'OPERATING'}">
                                                        <i class="bi bi-box"></i>
                                                        <span class=" fw-bold text-dark small"> Còn ${s.totalSlots} slots</span>
                                                    </c:if>
                                                </div>
                                            </div>
                                            <p class="text-secondary mt-2"><i class="bi bi-map me-2"></i>${s.address}</p>
                                            <p class="text-secondary mb-3"><i class="bi bi-geo-alt me-2"></i>${s.region.label}</p>

                                            <c:choose>
                                                <c:when test="${s.siteState eq 'OPERATING'}">
                                                    <span class="badge badge-success-custom ">${s.siteState.label}</span>
                                                </c:when>
                                                <c:when test="${s.siteState eq 'MAINTENANCE'}">
                                                    <span class="badge badge-danger-custom">${s.siteState.label}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-warning-custom">${s.siteState.label}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <c:choose>
                                            <c:when test="${param.action eq 'booking'}">
                                                <c:choose>
                                                    <c:when test="${s.siteState eq 'OPERATING'}">
                                                        <div class="d-flex justify-content-end mt-4">
                                                            <a href="${pageContext.request.contextPath}/sites/site-detail?action=${param.action}&siteId=${s.siteId}" class="btn btn-primary btn-book px-4 py-2"  onclick="event.stopPropagation()">Đặt ngay</a>
                                                        </div>
                                                    </c:when>

                                                    <c:otherwise>
                                                        <div class="d-flex justify-content-end mt-4">
                                                            <button class="btn btn-primary btn-book px-4 py-2" disabled>Đặt ngay</button>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>

                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${s.siteState eq 'OPERATING'}">
                                                        <div class="d-flex justify-content-end mt-4">
                                                            <a href="${pageContext.request.contextPath}/sites/site-detail?action=${param.action}&siteId=${s.siteId}" class="btn btn-primary btn-book px-4 py-2"  onclick="event.stopPropagation()">Mua thẻ</a>
                                                        </div>
                                                    </c:when>

                                                    <c:otherwise>
                                                        <div class="d-flex justify-content-end mt-4">
                                                            <button class="btn btn-primary btn-book px-4 py-2" disabled>Mua thẻ</button>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                        <%--<c:choose>--%>
                                        <%--<c:when test="${s.siteStatus eq 'OPERATING'}">--%>
                                        <!--                                                <div class="d-flex justify-content-end mt-4">
                                                                                            <a href="${pageContext.request.contextPath}/sites/site-detail?siteId=${s.siteId}" class="btn btn-primary btn-book px-4 py-2"  onclick="event.stopPropagation()">Đặt ngay</a>
                                                                                        </div>-->
                                        <%--</c:when>--%>

                                        <%--<c:otherwise>--%>
                                        <!--                                                <div class="d-flex justify-content-end mt-4">
                                                                                            <button class="btn btn-primary btn-book px-4 py-2" disabled>Đặt ngay</button>
                                                                                        </div>-->
                                        <%--</c:otherwise>--%>
                                        <%--</c:choose>--%>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        <!--Footer-->
        <%@ include file="/WEB-INF/views/layout/footer.jsp" %>
        <!-- Bootstrap JS Bundle -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            function goToSiteDetail(siteId) {
                window.location.href =
                        '${pageContext.request.contextPath}/sites/site-detail?action=${param.action}&siteId=' + siteId;
            }
        </script>
    </body>
</html>
