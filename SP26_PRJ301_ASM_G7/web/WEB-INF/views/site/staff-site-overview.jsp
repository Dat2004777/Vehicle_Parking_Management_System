<%@page import="model.ParkingSite"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Sơ đồ bãi xe - Overview</title>

        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">

        <style>
            :root {
                --bs-body-bg: #f3f4f6;
                --bs-primary: #1a56db;
                --bs-font-sans-serif: 'Inter', sans-serif;
                --navbar-height: 70px;
            }
            body {
                font-family: var(--bs-font-sans-serif);
                background-color: var(--bs-body-bg);
                padding-top: var(--navbar-height);
            }
            .panel-container {
                max-width: 900px;
                margin: 0 auto;
            }
            .card-hero {
                border: none;
                border-radius: 20px;
                background: #fff;
                box-shadow: 0 10px 25px rgba(0,0,0,0.05);
            }
            .badge-custom {
                font-weight: 600;
                padding: 8px 16px;
                border-radius: 30px;
            }
            .zone-card {
                background: #f8fafc;
                transition: all 0.2s;
                border: 1px solid #e2e8f0;
            }
            .zone-card:hover {
                transform: translateY(-3px);
                box-shadow: 0 5px 15px rgba(0,0,0,0.08);
                background: #fff;
            }
            .giant-number {
                font-size: 5rem;
                letter-spacing: -2px;
            }
        </style>
    </head>
    <body>

        <jsp:include page="/WEB-INF/views/layout/staff-header.jsp" />

        <div class="offcanvas offcanvas-start border-0 shadow" tabindex="-1" id="sidebarOffcanvas" style="width: 280px;">
            <div class="offcanvas-header border-bottom">
                <h5 class="fw-bold mb-0 text-success"><i class="bi bi-p-square-fill me-2"></i>Smart Parking</h5>
                <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
            </div>
            <div class="offcanvas-body d-flex flex-column p-3">
                <jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />
            </div>
        </div>

        <main class="container-fluid py-4 panel-container">

            <nav aria-label="breadcrumb" class="mb-4">
                <ol class="breadcrumb small">
                    <li class="breadcrumb-item"><a href="${ctx}/dashboard" class="text-decoration-none text-muted">Bảng điều khiển</a></li>
                    <li class="breadcrumb-item active fw-medium text-primary">Sơ đồ bãi xe</li>
                </ol>
            </nav>

            <div class="card card-hero">
                <div class="card-header bg-white border-bottom-0 pt-4 px-4 px-xl-5 d-flex justify-content-between align-items-center">
                    <div>
                        <h1 class="fw-bold mb-1 text-dark fs-3">${not empty overview.siteName ? overview.siteName : 'Bãi Đỗ Xe'}</h1>
                        <p class="text-muted mb-0 small"><i class="bi bi-geo-alt-fill text-danger me-1"></i>${overview.address}</p>
                    </div>
                    <div>
                        <c:choose>
                            <c:when test="${overview.operatingState.name() == 'ACTIVE'}">
                                <span class="badge bg-success badge-custom shadow-sm"><i class="bi bi-door-open-fill me-1"></i> MỞ CỬA</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-danger badge-custom shadow-sm"><i class="bi bi-door-closed-fill me-1"></i> ĐÓNG CỬA</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="card-body px-4 px-xl-5 py-4">
                    <div class="d-flex justify-content-between align-items-end mb-4">
                        <div>
                            <div class="text-muted fw-bold small text-uppercase">Chỗ còn trống</div>
                            <div class="fw-bold text-primary giant-number">${overview.totalAvailable}</div>
                        </div>
                        <div class="text-end">
                            <div class="text-muted fw-bold small text-uppercase">Đã đỗ / Tổng</div>
                            <div class="fw-bold text-dark fs-2">
                                ${overview.totalOccupied} <span class="text-muted fw-normal fs-5">/ ${overview.totalCapacity}</span>
                            </div>
                        </div>
                    </div>

                    <hr class="opacity-10 mb-4">

                    <div class="row g-3">
                        <c:forEach items="${overview.areas}" var="area">
                            <c:choose>
                                <c:when test="${area.vehicleType.vehicleTypeName.toLowerCase().contains('car')}">
                                    <c:set var="color" value="#3b82f6" /><c:set var="icon" value="bi-car-front-fill" /><c:set var="txt" value="text-primary" />
                                </c:when>
                                <c:otherwise>
                                    <c:set var="color" value="#eab308" /><c:set var="icon" value="bi-bicycle" /><c:set var="txt" value="text-warning" />
                                </c:otherwise>
                            </c:choose>

                            <div class="col-6">
                                <div class="rounded p-3 h-100 zone-card" style="border-left: 4px solid ${color} !important;">
                                    <div class="text-muted fw-bold mb-2 text-uppercase small">
                                        <i class="bi ${icon} me-2 ${txt}"></i>${area.areaName}
                                    </div>
                                    <div class="d-flex justify-content-between align-items-baseline">
                                        <c:choose>
                                            <c:when test="${area.availableSlots <= 0}">
                                                <div class="fw-bold text-danger fs-5">Hết chỗ</div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="fw-bold ${txt} fs-3">${area.availableSlots}</div>
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="text-muted fw-medium small">${area.occupiedSlots}/${area.totalSlots}</div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div> 
                </div>
            </div>
        </main>

        <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
    </body>
</html>