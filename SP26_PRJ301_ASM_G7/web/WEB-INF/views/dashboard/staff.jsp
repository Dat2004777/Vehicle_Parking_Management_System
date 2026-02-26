<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Cổng Nhân Viên - Kiểm Soát Xe</title>

        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Google Fonts: Inter -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

        <style>
            :root {
                --bs-body-bg: #f3f4f6;
                --bs-primary: #1a56db;
                --bs-primary-rgb: 26, 86, 219;
                --bs-font-sans-serif: 'Inter', sans-serif;
                --text-main: #374151;
                --text-muted: #6b7280;
                --navbar-height: 70px;
            }
            body {
                font-family: var(--bs-font-sans-serif);
                color: var(--text-main);
                -webkit-font-smoothing: antialiased;
                background-color: var(--bs-body-bg);
                padding-top: var(--navbar-height);
            }

            /* NAVBAR HEADER */
            .navbar-custom {
                height: var(--navbar-height);
                box-shadow: 0 1px 2px rgba(0,0,0,0.05);
                background-color: #fff;
            }
            .header-clock {
                font-weight: 600;
                color: #475569;
                background: #f1f5f9;
                padding: 8px 16px;
                border-radius: 8px;
                font-variant-numeric: tabular-nums;
            }

            /* THỐNG KÊ (STATS CARDS) */
            .stat-card {
                border: none;
                border-radius: 16px;
                background: #fff;
                box-shadow: 0 4px 6px -1px rgba(0,0,0,0.02);
                padding: 1.5rem;
                display: flex;
                align-items: center;
                justify-content: space-between;
            }
            .stat-icon {
                width: 48px;
                height: 48px;
                border-radius: 12px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 1.5rem;
            }

            /* MAIN FORM CARD */
            .form-card {
                border: none;
                border-radius: 20px;
                background: #fff;
                box-shadow: 0 10px 15px -3px rgba(0,0,0,0.05);
                padding: 2.5rem;
            }

            /* TAB SWITCH (Vào bãi / Ra bãi) */
            .tab-switch {
                display: flex;
                background: #f8fafc;
                border-radius: 12px;
                padding: 6px;
                margin-bottom: 2rem;
            }
            .tab-switch button {
                flex: 1;
                border: none;
                background: transparent;
                padding: 12px;
                border-radius: 8px;
                font-weight: 600;
                color: #64748b;
                transition: all 0.2s;
            }
            /* TAB SWITCH (Vào bãi / Ra bãi) */
            .tab-switch {
                display: flex;
                background: #f8fafc;
                border-radius: 12px;
                padding: 6px;
                margin-bottom: 2rem;
            }
            .tab-switch button {
                flex: 1;
                border: none;
                background: transparent;
                padding: 12px;
                border-radius: 8px;
                font-weight: 600;
                color: #64748b;
                transition: all 0.2s;
            }

            /* Màu nền xanh dương, chữ trắng cho toàn bộ Tab VÀO BÃI */
            .tab-switch button#tabIn.active {
                background: var(--bs-primary);
                color: #fff;
                box-shadow: 0 4px 12px rgba(37, 99, 235, 0.3);
            }

            /* Màu nền xanh lá, chữ trắng cho toàn bộ Tab RA BÃI */
            .tab-switch button#tabOut.active {
                background: #ef4444;
                color: #fff;
                box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
            }

            /* INPUT FIELDS */
            .input-group-custom {
                position: relative;
                margin-bottom: 1.5rem;
            }
            .input-group-custom .form-label {
                font-size: 0.85rem;
                font-weight: 600;
                color: #475569;
                margin-bottom: 0.5rem;
            }
            .input-group-custom input {
                background: #f8fafc;
                border: 2px solid #e2e8f0;
                border-radius: 12px;
                padding: 16px 16px 16px 48px;
                font-size: 1.1rem;
                font-weight: 500;
                width: 100%;
                transition: all 0.2s;
            }
            .input-group-custom input:focus {
                outline: none;
                border-color: var(--bs-primary);
                background: #fff;
                box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1);
            }
            .input-group-custom .icon-left {
                position: absolute;
                bottom: 18px;
                left: 16px;
                color: #94a3b8;
                font-size: 1.2rem;
            }

            /* Container giới hạn chiều cao cho danh sách khu vực */
            .scrollable-area-list {
                max-height: 280px; /* Bạn có thể tăng giảm số này tùy theo độ cao màn hình muốn hiển thị */
                overflow-y: auto;
                overflow-x: hidden;
                padding-right: 8px; /* Tránh để thanh cuộn đè lên nội dung */
            }

            /* Custom Scrollbar cho Webkit (Chrome, Safari, Edge) */
            .scrollable-area-list::-webkit-scrollbar {
                width: 6px;
            }
            .scrollable-area-list::-webkit-scrollbar-track {
                background: #f1f5f9;
                border-radius: 8px;
            }
            .scrollable-area-list::-webkit-scrollbar-thumb {
                background: #cbd5e1;
                border-radius: 8px;
            }
            .scrollable-area-list::-webkit-scrollbar-thumb:hover {
                background: #94a3b8;
            }

            /* NÚT XÁC NHẬN */
            .btn-submit {
                background: var(--bs-primary);
                color: #fff;
                border: none;
                border-radius: 12px;
                padding: 16px;
                font-size: 1.1rem;
                font-weight: 600;
                width: 100%;
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 8px;
                transition: all 0.2s;
            }
            .btn-submit:hover {
                background: #1d4ed8;
                transform: translateY(-2px);
            }
            .btn-submit.btn-checkout {
                background: #10b981; /* Màu xanh lá cho nút ra bãi */
            }
            .btn-submit.btn-checkout:hover {
                background: #059669;
            }

            /* OFF CANVAS SIDEBAR (Nav) */
            .sidebar-nav .nav-link {
                color: #475569;
                font-weight: 500;
                padding: 12px 20px;
                border-radius: 8px;
                margin-bottom: 4px;
            }
            .sidebar-nav .nav-link:hover, .sidebar-nav .nav-link.active {
                background: #eff6ff;
                color: var(--bs-primary);
            }
        </style>
    </head>
    <body>

        <!--        header-->
        <nav class="navbar navbar-expand-lg navbar-custom fixed-top px-3 px-md-4">
            <div class="container-fluid p-0 d-flex justify-content-between align-items-center">

                <div class="d-flex align-items-center gap-3">
                    <button class="btn btn-light border-0 bg-light" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarOffcanvas">
                        <i class="bi bi-list fs-4"></i>
                    </button>
                    <div class="d-sm-block">
                        <div class="fw-bold fs-5" style="line-height: 1.2; color: #1e293b;">${stats.siteName}</div>
                    </div>
                </div>

                <div class="header-clock d-md-flex align-items-center gap-2">
                    <i class="bi bi-clock"></i>
                    <span id="realtimeClock">00:00:00</span> - <span id="realtimeDate">--/--/----</span>
                </div>

                <div class="d-flex align-items-center gap-4">

                </div>
            </div>
        </nav>

        <main class="container-fluid d-flex align-items-center justify-content-center" style="max-width: 1400px; min-height: calc(100vh - 80px); padding: 2rem 15px;">
            <%-- [QUAN TRỌNG] Trích xuất message ra biến cục bộ và xóa session ngay lập tức --%>
            <c:set var="errMsg" value="${sessionScope.errorMsg}" />
            <c:set var="sucMsg" value="${sessionScope.successMsg}" />
            <c:remove var="errorMsg" scope="session"/>
            <c:remove var="successMsg" scope="session"/>
            <div class="row g-4 w-100 align-items-center">

                <div class="col-12 d-lg-none order-1">
                    <div class="card shadow-sm border-0" style="border-radius: 12px;">
                        <div class="card-body p-3">
                            <div class="d-flex justify-content-between align-items-end">
                                <div>
                                    <div class="text-muted fw-bold mb-1" style="font-size: 0.75rem; text-transform: uppercase;">
                                        Chỗ còn trống
                                    </div>
                                    <div class="fw-bold text-primary" style="font-size: 2.2rem; line-height: 1;">
                                        ${stats.availableSpaces != null ? stats.availableSpaces : '0'}
                                    </div>
                                </div>
                                <div class="text-end">
                                    <div class="text-muted fw-bold mb-1" style="font-size: 0.75rem; text-transform: uppercase;">
                                        Đã đỗ / Tổng
                                    </div>
                                    <div class="fw-bold text-dark" style="font-size: 1.1rem; line-height: 1;">
                                        ${stats.occupiedSpaces != null ? stats.occupiedSpaces : '0'} <span class="text-muted fw-normal" style="font-size: 0.85rem;">/ ${stats.totalCapacity != null ? stats.totalCapacity : '0'}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <c:if test="${not empty errMsg or not empty sucMsg}">
                        <div class="alert-wrapper mb-2" style="max-height: 120px; overflow-y: auto; scrollbar-width: thin;">
                            <c:if test="${not empty errMsg}">
                                <div class="alert alert-danger alert-dismissible fade show d-flex align-items-start mb-0 py-2 px-3 shadow-sm" role="alert" style="border-radius: 10px; border-left: 4px solid #dc3545;">
                                    <i class="bi bi-exclamation-triangle-fill fs-5 me-2 text-danger mt-1"></i>
                                    <div class="flex-grow-1 pe-3">
                                        <span style="font-size: 0.9rem; line-height: 1.3; display: block;" class="mt-1 fw-medium">${errMsg}</span>
                                    </div>
                                    <button type="button" class="btn-close p-2 m-1" data-bs-dismiss="alert" aria-label="Close" style="font-size: 0.65rem;"></button>
                                </div>
                            </c:if>
                            <c:if test="${not empty sucMsg}">
                                <div class="alert alert-success alert-dismissible fade show d-flex align-items-start mb-0 py-2 px-3 shadow-sm" role="alert" style="border-radius: 10px; border-left: 4px solid #198754;">
                                    <i class="bi bi-check-circle-fill fs-5 me-2 text-success mt-1"></i>
                                    <div class="flex-grow-1 pe-3">
                                        <span style="font-size: 0.9rem; line-height: 1.3; display: block;" class="mt-1 fw-medium">${sucMsg}</span>
                                    </div>
                                    <button type="button" class="btn-close p-2 m-1" data-bs-dismiss="alert" aria-label="Close" style="font-size: 0.65rem;"></button>
                                </div>
                            </c:if>
                        </div>
                    </c:if>
                </div>

                <div class="col-lg-8 order-2 order-lg-1">

                    <div class="form-card shadow-lg p-4 p-xl-5 mx-auto" style="border-radius: 16px; background-color: #fff; max-width: 700px;">
                        <div class="w-100">

                            <div class="tab-switch mb-4" id="formTabs">
                                <button type="button" class="active" id="tabIn" onclick="switchMode('in')">
                                    VÀO BÃI <span class="fw-bold opacity-75 ms-1">[F1]</span>
                                </button>
                                <button type="button" id="tabOut" onclick="switchMode('out')">
                                    RA BÃI <span class="fw-bold opacity-75 ms-1">[F2]</span>
                                </button>
                            </div>

                            <form action="${ctx}/parking/checkin" method="POST" id="mainGateForm">
                                <input type="hidden" name="actionType" id="actionType" value="checkin">

                                <div class="input-group-custom mb-4 position-relative">
                                    <label class="form-label text-secondary fw-bold" style="font-size: 0.85rem;">MÃ SỐ THẺ</label>
                                    <div class="d-flex shadow-sm" style="border-radius: 8px;">
                                        <div class="position-relative flex-grow-1">
                                            <i class="bi bi-credit-card-2-front icon-left"></i>
                                            <input type="text" id="cardId" name="cardId" placeholder="Quét thẻ hoặc nhập mã số..." required autofocus autocomplete="off" class="form-control-lg fs-5 w-100" style="border: 1px solid #e2e8f0; border-right: none; border-top-right-radius: 0; border-bottom-right-radius: 0; padding-left: 3rem;">
                                        </div>

                                        <button type="button" id="btnRandomCard" class="btn btn-primary px-4" onclick="fetchRandomCard()" style="border-top-left-radius: 0; border-bottom-left-radius: 0; z-index: 1;" title="Lấy thẻ trống ngẫu nhiên">
                                            <i class="bi bi-shuffle fs-5"></i>
                                        </button>
                                    </div>
                                </div>

                                <div class="input-group-custom mb-4">
                                    <label class="form-label text-secondary fw-bold" style="font-size: 0.85rem;">BIỂN SỐ XE</label>
                                    <i class="bi bi-123 icon-left"></i>
                                    <input type="text" id="licensePlate" name="licensePlate" placeholder="NHẬP BIỂN SỐ (VD: 30A-123.45)" required autocomplete="off" class="form-control-lg fs-5 text-uppercase">
                                </div>

                                <div id="vehicleTypeContainer" class="mb-4">
                                    <label class="form-label text-secondary fw-bold" style="font-size: 0.85rem;">LOẠI XE <span class="text-muted fw-normal text-lowercase">(Dành cho vé lượt)</span></label>

                                    <c:set var="hasSelected" value="false" />
                                    <input type="hidden" name="vehicleTypeId" id="vehicleTypeId" value="-1">

                                    <div class="d-flex gap-3">
                                        <c:forEach var="entry" items="${overview.slotPerVehicle}">

                                            <c:set var="vType" value="${entry.key}" />
                                            <c:set var="availableSlots" value="${entry.value}" />
                                            <c:set var="isAvailable" value="${availableSlots > 0}" />

                                            <c:choose>
                                                <c:when test="${vType.vehicleTypeName.toLowerCase().contains('motorbike')}">
                                                    <c:set var="icon" value="bi-bicycle" />
                                                    <c:set var="label" value="XE MÁY" />
                                                </c:when>
                                                <c:when test="${vType.vehicleTypeName.toLowerCase().contains('car')}">
                                                    <c:set var="icon" value="bi-car-front-fill" />
                                                    <c:set var="label" value="Ô TÔ" />
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="icon" value="bi-truck" />
                                                    <c:set var="label" value="${vType.vehicleTypeName}" />
                                                </c:otherwise>
                                            </c:choose>

                                            <c:choose>
                                                <c:when test="${isAvailable}">
                                                    <div id="boxType_${vType.vehicleTypeId}" 
                                                         class="vehicle-box flex-fill py-3 text-center fw-bold border rounded-3 cursor-pointer ${!hasSelected ? 'bg-primary border-primary text-white active-box' : 'bg-light border-secondary text-secondary'}" 
                                                         onclick="selectVehicleType(${vType.vehicleTypeId}, 'boxType_${vType.vehicleTypeId}')" 
                                                         style="cursor: pointer; transition: all 0.2s; user-select: none;">

                                                        <i class="bi ${icon} fs-4 d-block mb-1"></i> ${label}
                                                        <small class="d-block fw-normal subtitle-text ${!hasSelected ? 'text-white-50' : 'text-muted'}" style="font-size: 0.7rem;">(Còn ${availableSlots} chỗ)</small>
                                                    </div>

                                                    <c:if test="${!hasSelected}">
                                                        <script>document.getElementById('vehicleTypeId').value = '${vType.vehicleTypeId}';</script>
                                                        <c:set var="hasSelected" value="true" />
                                                    </c:if>
                                                </c:when>

                                                <c:otherwise>
                                                    <div class="flex-fill py-3 text-center fw-bold border rounded-3" 
                                                         style="background-color: #e2e8f0; opacity: 0.6; cursor: not-allowed; user-select: none; border-color: #cbd5e1;">

                                                        <i class="bi ${icon} fs-4 d-block mb-1 text-muted"></i> 
                                                        <span class="text-muted">${label} <small class="d-block fw-normal text-danger" style="font-size: 0.7rem;">(Đầy)</small></span>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>

                                        </c:forEach>
                                    </div>

                                    <c:if test="${!hasSelected}">
                                        <div class="alert alert-danger mt-3 py-2 text-center" style="font-size: 0.85rem;">
                                            <i class="bi bi-exclamation-triangle-fill me-1"></i> Bãi xe đã hết sức chứa cho mọi loại xe!
                                        </div>
                                    </c:if>
                                </div>

                                <div class="d-flex align-items-center gap-2 mb-4 mt-3">
                                    <i class="bi bi-info-circle text-muted"></i>
                                    <small class="text-muted">Nhấn phím <b>Enter</b> để xác nhận và gửi thông tin.</small>
                                </div>

                                <button type="button" id="btnSubmitForm" class="btn-submit py-3 fs-5" style="background-color: #3b82f6;" onclick="submitForm()">
                                    <span id="btnSubmitText">XÁC NHẬN VÀO</span> <i id="btnSubmitIcon" class="bi bi-box-arrow-in-right ms-2"></i>
                                </button>
                            </form>



                        </div>

                    </div>

                </div>

                <div class="col-lg-4 order-3 order-lg-2 d-flex flex-column gap-3">
                    <c:if test="${not empty errMsg or not empty sucMsg}">
                        <div class="alert-wrapper d-none d-lg-block" style="max-height: 120px; overflow-y: auto; scrollbar-width: thin;">
                            <c:if test="${not empty errMsg}">
                                <div class="alert alert-danger alert-dismissible fade show d-flex align-items-start mb-0 py-2 px-3 shadow-sm" role="alert" style="border-radius: 10px; border-left: 4px solid #dc3545;">
                                    <i class="bi bi-exclamation-triangle-fill fs-5 me-2 text-danger mt-1"></i>
                                    <div class="flex-grow-1 pe-3">
                                        <span style="font-size: 0.95rem; line-height: 1.3; display: block;" class="mt-1 fw-medium">${errMsg}</span>
                                    </div>
                                    <button type="button" class="btn-close p-2 m-1" data-bs-dismiss="alert" aria-label="Close" style="font-size: 0.65rem;"></button>
                                </div>
                            </c:if>
                            <c:if test="${not empty sucMsg}">
                                <div class="alert alert-success alert-dismissible fade show d-flex align-items-start mb-0 py-2 px-3 shadow-sm" role="alert" style="border-radius: 10px; border-left: 4px solid #198754;">
                                    <i class="bi bi-check-circle-fill fs-5 me-2 text-success mt-1"></i>
                                    <div class="flex-grow-1 pe-3">
                                        <span style="font-size: 0.95rem; line-height: 1.3; display: block;" class="mt-1 fw-medium">${sucMsg}</span>
                                    </div>
                                    <button type="button" class="btn-close p-2 m-1" data-bs-dismiss="alert" aria-label="Close" style="font-size: 0.65rem;"></button>
                                </div>
                            </c:if>
                        </div>
                    </c:if>
                    <div class="alert-wrapper mt-lg-0 mt-3" style="max-height: 120px; overflow-y: auto; scrollbar-width: thin;">

                        <%-- Bắt lỗi (Error) --%>
                        <c:if test="${not empty sessionScope.errorMsg}">
                            <div class="alert alert-danger alert-dismissible fade show d-flex align-items-start mb-0 py-2 px-3 shadow-sm" role="alert" style="border-radius: 10px; border-left: 4px solid #dc3545;">
                                <i class="bi bi-exclamation-triangle-fill fs-5 me-2 text-danger mt-1"></i>
                                <div class="flex-grow-1 pe-3">
                                    <span style="font-size: 1rem; line-height: 1.3; display: block;" class="mt-1">${sessionScope.errorMsg}</span>
                                </div>
                                <button type="button" class="btn-close p-2 m-1" data-bs-dismiss="alert" aria-label="Close" style="font-size: 0.65rem;"></button>
                            </div>
                            <%-- Xóa message khỏi session để không bị hiện lại khi F5 --%>
                            <c:remove var="errorMsg" scope="session"/>
                        </c:if>

                        <%-- Báo thành công (Success) --%>
                        <c:if test="${not empty sessionScope.successMsg}">
                            <div class="alert alert-success alert-dismissible fade show d-flex align-items-start mb-0 py-2 px-3 shadow-sm" role="alert" style="border-radius: 10px; border-left: 4px solid #198754;">
                                <i class="bi bi-check-circle-fill fs-5 me-2 text-success mt-1"></i>
                                <div class="flex-grow-1 pe-3">
                                    <span style="font-size: 1rem; line-height: 1.3; display: block;" class="mt-1">${sessionScope.successMsg}</span>
                                </div>
                                <button type="button" class="btn-close p-2 m-1" data-bs-dismiss="alert" aria-label="Close" style="font-size: 0.65rem;"></button>
                            </div>
                            <%-- Xóa message khỏi session --%>
                            <c:remove var="successMsg" scope="session"/>
                        </c:if>
                    </div>

                    <div class="card shadow-sm border-0 d-none d-lg-block" style="border-radius: 16px;">
                        <div class="card-body p-3 p-xl-4">

                            <div class="d-flex justify-content-between align-items-end mb-2">
                                <div>
                                    <div class="text-muted fw-bold mb-1" style="font-size: 0.75rem; text-transform: uppercase;">Chỗ còn trống</div>
                                    <div class="fw-bold text-primary" style="font-size: 2.5rem; line-height: 1;">${stats.availableSpaces != null ? stats.availableSpaces : '0'}</div>
                                </div>
                                <div class="text-end">
                                    <div class="text-muted fw-bold mb-1" style="font-size: 0.75rem; text-transform: uppercase;">Đã đỗ / Tổng</div>
                                    <div class="fw-bold text-dark" style="font-size: 1.25rem; line-height: 1;">
                                        ${stats.occupiedSpaces != null ? stats.occupiedSpaces : '0'} <span class="text-muted fw-normal" style="font-size: 0.9rem;">/ ${stats.totalCapacity != null ? stats.totalCapacity : '0'}</span>
                                    </div>
                                </div>
                            </div>

                            <hr class="text-muted opacity-25 my-3">

                            <div class="scrollable-area-list">
                                <div class="row g-3 mb-2">

                                    <c:if test="${empty overview.areas}">
                                        <div class="col-12 text-center text-muted py-4">
                                            <i>Chưa có dữ liệu phân khu cho bãi xe này.</i>
                                        </div>
                                    </c:if>

                                    <c:forEach items="${overview.areas}" var="area">

                                        <c:choose>
                                            <c:when test="${area.vehicleType.vehicleTypeName.toLowerCase().contains('car')}">
                                                <c:set var="borderColor" value="#3b82f6" />
                                                <c:set var="iconClass" value="bi-car-front-fill" />
                                                <c:set var="textColor" value="text-primary" />
                                            </c:when>
                                            <c:when test="${area.vehicleType.vehicleTypeName.toLowerCase().contains('motorbike')}">
                                                <c:set var="borderColor" value="#eab308" />
                                                <c:set var="iconClass" value="bi-bicycle" />
                                                <c:set var="textColor" value="text-warning" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="borderColor" value="#94a3b8" />
                                                <c:set var="iconClass" value="bi-p-circle-fill" />
                                                <c:set var="textColor" value="text-secondary" />
                                            </c:otherwise>
                                        </c:choose>

                                        <div class="col-6">
                                            <div class="border rounded p-3 h-100 zone-card" style="border-left: 4px solid ${borderColor} !important;">
                                                <div class="text-muted fw-bold mb-2 text-uppercase" style="font-size: 0.8rem;">
                                                    <i class="bi ${iconClass} me-2 ${textColor}"></i>${area.areaName}
                                                </div>

                                                <div class="d-flex justify-content-between align-items-baseline">
                                                    <c:choose>
                                                        <c:when test="${area.availableSlots <= 0}">
                                                            <div class="fw-bold text-danger fs-5" style="line-height: 1;">Hết chỗ</div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="fw-bold ${textColor} fs-3" style="line-height: 1;">${area.availableSlots}</div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <div class="text-muted fw-medium" style="font-size: 0.8rem;">${area.occupiedSlots}/${area.totalSlots}</div>
                                                </div>
                                            </div>
                                        </div>

                                    </c:forEach>

                                </div> 
                            </div>
                        </div>
                    </div>

                    <div class="card shadow-sm border-0 flex-grow-1" style="border-radius: 16px; display: flex; flex-direction: column;">

                        <div class="card-header bg-white border-bottom py-2 px-3 d-flex justify-content-between align-items-center" style="border-top-left-radius: 16px; border-top-right-radius: 16px;">
                            <h6 class="mb-0 fw-bold text-dark" style="font-size: 0.9rem;"><i class="bi bi-clock-history me-2"></i>Hoạt động gần đây</h6>
                            <span class="badge bg-primary-subtle text-primary rounded-pill" style="font-size: 0.7rem;">Real-time</span>
                        </div>

                        <div class="card-body p-0" style="max-height: 280px; overflow-y: auto;">
                            <div class="list-group list-group-flush">

                                <c:forEach var="log" items="${recentLogs}">
                                    <c:choose>
                                        <c:when test="${log.sessionState == 'parked' || log.sessionState == 'PARKED'}">
                                            <c:set var="actionName" value="Xe vào bãi" />
                                            <c:set var="badgeClass" value="bg-primary-subtle text-primary" />
                                            <c:set var="icon" value="bi-arrow-down-circle" />
                                            <c:set var="statusText" value="Đang đỗ" />
                                        </c:when>
                                        <c:when test="${log.sessionState == 'completed' || log.sessionState == 'COMPLETED'}">
                                            <c:set var="actionName" value="Xe ra bãi" />
                                            <c:set var="badgeClass" value="bg-success-subtle text-success" />
                                            <c:set var="icon" value="bi-arrow-up-circle" />
                                            <c:set var="statusText" value="Đã ra" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="actionName" value="Lỗi Dữ liệu" />
                                            <c:set var="badgeClass" value="bg-secondary-subtle text-secondary" />
                                            <c:set var="icon" value="bi-question-circle" />
                                            <c:set var="statusText" value="Lỗi" />
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="list-group-item px-3 py-2 border-bottom">
                                        <div class="d-flex justify-content-between align-items-center mb-1">
                                            <span class="fw-bold text-dark" style="font-size: 0.85rem;">${log.licensePlate}</span>
                                            <span class="badge ${badgeClass}" style="font-size: 0.7rem;">${statusText}</span>
                                        </div>
                                        <div class="d-flex align-items-center justify-content-between">
                                            <span class="${badgeClass} fw-medium px-2 rounded" style="font-size: 0.7rem; padding-top: 2px; padding-bottom: 2px;">
                                                <i class="bi ${icon} me-1"></i> ${actionName}
                                            </span>
                                            <small class="text-muted" style="font-size: 0.75rem;">${log.formattedTime}</small>
                                        </div>
                                    </div>
                                </c:forEach>

                                <c:if test="${empty recentLogs}">
                                    <div class="py-4 text-center text-muted">
                                        <i class="bi bi-inbox fs-2 d-block mb-1 text-light"></i>
                                        <small style="font-size: 0.8rem;">Chưa có dữ liệu.</small>
                                    </div>
                                </c:if>

                            </div>
                        </div>

                        <div class="card-footer bg-white border-top text-center py-2" style="border-bottom-left-radius: 16px; border-bottom-right-radius: 16px;">
                            <a href="${ctx}/parking/history" class="text-decoration-none fw-bold text-primary" style="font-size: 0.8rem;">
                                Xem toàn bộ <i class="bi bi-arrow-right ms-1"></i>
                            </a>
                        </div>

                    </div>
                </div>

            </div>
            <div class="offcanvas offcanvas-start border-0 shadow" tabindex="-1" id="sidebarOffcanvas" style="width: 280px;">

                <div class="offcanvas-header border-bottom">
                    <h5 class="fw-bold mb-0 text-success"><i class="bi bi-p-square-fill me-2"></i>Smart Parking</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
                </div>

                <div class="offcanvas-body d-flex flex-column p-3">
                    <jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>
                </div>

            </div>

        </main>


        <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
        <script>
                                    // Bọc toàn bộ code để đảm bảo HTML đã load xong 100% mới chạy JS
                                    document.addEventListener('DOMContentLoaded', function () {

                                        // 1. Khai báo Elements
                                        const form = document.getElementById('mainGateForm');
                                        const actionType = document.getElementById('actionType');
                                        const tabIn = document.getElementById('tabIn');
                                        const tabOut = document.getElementById('tabOut');
                                        const cardIdInput = document.getElementById('cardId');
                                        const plateInput = document.getElementById('licensePlate');
                                        const btnSubmit = document.getElementById('btnSubmitForm');

                                        const ctx = "${ctx}";

                                        // Tự động focus vào ô Mã thẻ khi vừa vào trang
                                        if (cardIdInput)
                                            cardIdInput.focus();

                                        // 2. Logic Đổi Tab (Vào/Ra)
                                        window.switchMode = function (mode) {
                                            const vehicleContainer = document.getElementById('vehicleTypeContainer');
                                            const btnRandom = document.getElementById('btnRandomCard'); // Lấy nút Random Card

                                            if (mode === 'in') {
                                                tabIn.classList.add('active');
                                                tabOut.classList.remove('active');
                                                actionType.value = 'checkin';
                                                form.action = ctx + '/parking/checkin';

                                                btnSubmit.style.backgroundColor = '#3b82f6'; // Nút Xanh
                                                btnSubmit.innerHTML = '<span>XÁC NHẬN VÀO</span> <i class="bi bi-box-arrow-in-right ms-2"></i>';

                                                // HIỆN LẠI cụm chọn loại xe và Nút Random
                                                if (vehicleContainer)
                                                    vehicleContainer.classList.remove('d-none');
                                                if (btnRandom)
                                                    btnRandom.style.display = ''; // Để rỗng để trả về trạng thái flex ban đầu

                                            } else {
                                                tabOut.classList.add('active');
                                                tabIn.classList.remove('active');
                                                actionType.value = 'checkout';
                                                form.action = ctx + '/parking/checkout';

                                                btnSubmit.style.backgroundColor = '#ef4444'; // Nút Đỏ
                                                btnSubmit.innerHTML = '<span>XÁC NHẬN RA</span> <i class="bi bi-box-arrow-right ms-2"></i>';

                                                // ẨN cụm chọn loại xe và Nút Random đi cho màn hình Check-out gọn gàng
                                                if (vehicleContainer)
                                                    vehicleContainer.classList.add('d-none');
                                                if (btnRandom)
                                                    btnRandom.style.display = 'none'; // Ẩn nút random
                                            }

                                            // Xóa trắng & Tự động Focus lại ô đầu tiên (Mã thẻ) để sẵn sàng quét
                                            cardIdInput.value = '';
                                            plateInput.value = '';
                                            cardIdInput.focus();
                                        };

                                        window.submitForm = function () {
                                            if (cardIdInput.value.trim() === '' && plateInput.value.trim() === '') {
                                                alert('Vui lòng quét thẻ hoặc nhập biển số!');
                                                cardIdInput.focus();
                                                return;
                                            }
                                            form.submit();
                                        };

                                        // ==========================================
// BẮT SỰ KIỆN PHÍM TẮT TOÀN CỤC (HOTKEYS)
// ==========================================
                                        document.addEventListener('keydown', function (e) {

                                            // 1. Phím F1: Chuyển sang Check-in VÀ tự động lấy thẻ Random
                                            if (e.key === 'F1') {
                                                e.preventDefault(); // Chặn popup Help mặc định của trình duyệt
                                                switchMode('in');
                                            }

                                            // 2. Phím F2: Chuyển sang Check-out
                                            else if (e.key === 'F2') {
                                                e.preventDefault();
                                                switchMode('out');
                                            }

                                            // 3. Phím Space: Lấy thẻ Random
                                            else if (e.code === 'Tab') {
                                                // Kỹ thuật an toàn: Kiểm tra xem user có đang gõ text vào ô Input không
                                                e.preventDefault(); // Chặn hành vi cuộn trang xuống của phím Space
                                                const actionType = document.getElementById('actionType').value;
                                                if (actionType === 'checkin') {
                                                    fetchRandomCard();
                                                }

                                            }

                                            // 4. Phím Enter: Xác nhận (Submit Form)
                                            else if (e.key === 'Enter') {
                                                e.preventDefault(); // Chặn hành vi submit mặc định để qua bước validate của mình

                                                // UX thông minh: 
                                                // - Nếu nhân viên vừa quét xong thẻ (đang đứng ở ô cardId) -> Enter nhảy sang ô Biển số
                                                // - Nếu không phải -> Thực hiện Submit luôn
                                                const cardIdInput = document.getElementById('cardId');
                                                const plateInput = document.getElementById('licensePlate');

                                                if (document.activeElement === cardIdInput && cardIdInput.value.trim() !== '') {
                                                    plateInput.focus();
                                                } else {
                                                    submitForm();
                                                }
                                            }
                                        });

                                        // 5. Auto focus thông minh & an toàn
// (Chỉ focus lại nếu click ra ngoài các vùng tương tác)
                                        document.addEventListener('click', function (e) {
                                            // Đã thêm .vehicle-box để nhận diện hộp chọn xe là một vùng tương tác hợp lệ
                                            const isInteractiveArea = e.target.closest('button, a, input, .offcanvas, .vehicle-box');

                                            if (!isInteractiveArea) {
                                                if (cardIdInput.value === '') {
                                                    cardIdInput.focus();
                                                } else {
                                                    plateInput.focus();
                                                }
                                            }
                                        });

// 6. Gắn thẳng hàm đổi màu vào "window" để HTML gọi được
                                        window.selectVehicleType = function (typeId, boxId) {
                                            const hiddenInput = document.getElementById('vehicleTypeId');
                                            if (hiddenInput) {
                                                hiddenInput.value = typeId;
                                            }

                                            const allBoxes = document.querySelectorAll('.vehicle-box');

                                            // 1. Reset tất cả các hộp về trạng thái CHƯA CHỌN (Màu xám nhạt bg-light)
                                            allBoxes.forEach(box => {
                                                box.classList.remove('bg-primary', 'border-primary', 'text-white', 'active-box');
                                                box.classList.add('bg-light', 'border-secondary', 'text-secondary');

                                                const subtitle = box.querySelector('.subtitle-text');
                                                if (subtitle) {
                                                    subtitle.classList.remove('text-white-50');
                                                    subtitle.classList.add('text-muted');
                                                }
                                            });

                                            // 2. Kích hoạt hộp VỪA BẤM thành trạng thái ĐÃ CHỌN (Màu xanh bg-primary)
                                            const selectedBox = document.getElementById(boxId);
                                            if (selectedBox) {
                                                selectedBox.classList.remove('bg-light', 'border-secondary', 'text-secondary');
                                                selectedBox.classList.add('bg-primary', 'border-primary', 'text-white', 'active-box');

                                                const selectedSubtitle = selectedBox.querySelector('.subtitle-text');
                                                if (selectedSubtitle) {
                                                    selectedSubtitle.classList.remove('text-muted');
                                                    selectedSubtitle.classList.add('text-white-50');
                                                }
                                            }

                                            // 3. Logic Smart Focus sau khi click chọn xong
                                            // Nếu chưa quét thẻ -> Trỏ về ô Thẻ. Nếu đã có thẻ -> Trỏ sang ô Biển số
                                            if (cardIdInput.value.trim() === '') {
                                                cardIdInput.focus();
                                            } else {
                                                plateInput.focus();
                                            }
                                        };

                                        // 7. Đồng hồ Realtime
                                        function updateClock() {
                                            const clockEl = document.getElementById('realtimeClock');
                                            const dateEl = document.getElementById('realtimeDate');

                                            if (clockEl && dateEl) {
                                                const now = new Date();
                                                clockEl.innerText = now.toLocaleTimeString('vi-VN', {hour12: false});
                                                dateEl.innerText = now.toLocaleDateString('vi-VN', {day: '2-digit', month: '2-digit', year: 'numeric'});
                                            }
                                        }
                                        setInterval(updateClock, 1000);
                                        updateClock();
                                    });

                                    // Hàm gọi API lấy thẻ rảnh ngẫu nhiên
                                    window.fetchRandomCard = function () {
                                        const icon = document.querySelector('#btnRandomCard i');

                                        // Đổi icon thành vòng xoay (Loading) cho chuyên nghiệp
                                        icon.className = 'bi bi-arrow-repeat spin-animation';

                                        // Gọi API Backend (Bạn nhớ sửa URL ctx cho đúng với route của bạn)
                                        fetch('${pageContext.request.contextPath}/api/parking/random-card')
                                                .then(response => response.json())
                                                .then(data => {
                                                    if (data.success) {
                                                        // Điền thẻ vào ô input và tự nhảy sang ô Biển số
                                                        document.getElementById('cardId').value = data.cardId;
                                                        document.getElementById('licensePlate').focus();
                                                    } else {
                                                        alert(data.message || 'Hệ thống báo: Đã hết thẻ trống!');
                                                    }
                                                })
                                                .catch(error => {
                                                    console.error('Lỗi khi lấy thẻ ngẫu nhiên:', error);
                                                    alert('Lỗi kết nối đến máy chủ!');
                                                })
                                                .finally(() => {
                                                    // Trả lại icon Shuffle ban đầu
                                                    icon.className = 'bi bi-shuffle fs-5';
                                                });
                                    };
        </script>
    </body>
</html>