<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Cổng Nhân Viên - Kiểm Soát Xe</title>
        <jsp:include page="/WEB-INF/views/layout/layout.jsp"/>
        <style>
            body {
                padding-top: var(--navbar-height);
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
                max-height: 140px; /* Bạn có thể tăng giảm số này tùy theo độ cao màn hình muốn hiển thị */
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
        <jsp:include page="/WEB-INF/views/layout/staff-header.jsp" />


        <main class="container-fluid d-flex align-items-center justify-content-center" style="max-width: 1400px; min-height: calc(100vh - 80px); padding: 2rem 15px;">
            <%-- [QUAN TRỌNG] Trích xuất message ra biến cục bộ và xóa session ngay lập tức --%>
            <c:set var="errMsg" value="${sessionScope.errorMsg}" />
            <c:set var="sucMsg" value="${sessionScope.successMsg}" />
            <c:set var="oldCard" value="${sessionScope.oldCardId}" />
            <c:set var="oldPlate" value="${sessionScope.oldPlate}" />
            <c:remove var="errorMsg" scope="session"/>
            <c:remove var="successMsg" scope="session"/>
            <c:remove var="oldCardId" scope="session"/>
            <c:remove var="oldPlate" scope="session"/>

            <div class="row g-4 w-100 align-items-center">

                <div class="col-12 d-lg-none order-1 pt-5">
                    <div class="card shadow-sm border-0" style="border-radius: 12px">
                        <div class="card-body p-3">
                            <div class="d-flex justify-content-between align-items-end">
                                <div>
                                    <div class="text-muted fw-bold mb-1" style="font-size: 0.75rem; text-transform: uppercase;">Chỗ còn trống</div>
                                    <div class="fw-bold text-primary" style="font-size: 2.2rem; line-height: 1;">${overview.totalAvailable != null ? overview.totalAvailable : '0'}</div>
                                </div>
                                <div class="text-end">
                                    <div class="text-muted fw-bold mb-1" style="font-size: 0.75rem; text-transform: uppercase;">Đã đỗ / Tổng</div>
                                    <div class="fw-bold text-dark" style="font-size: 1.1rem; line-height: 1;">
                                        ${overview.totalOccupied != null ? overview.totalOccupied : '0'} <span class="text-muted fw-normal" style="font-size: 0.85rem;">/ ${overview.totalCapacity != null ? overview.totalCapacity : '0'}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-8 order-2 order-lg-1">
                    <div class="form-card shadow-lg p-4 p-xl-5 mx-auto" style="border-radius: 16px; background-color: #fff; max-width: 700px;">
                        <div class="w-100">

                            <div class="tab-switch mb-4" id="formTabs">
                                <button type="button" class="active" id="tabIn" data-mode="in">
                                    VÀO BÃI <span class="fw-bold opacity-75 ms-1">[F1]</span>
                                </button>
                                <button type="button" id="tabOut" data-mode="out">
                                    RA BÃI <span class="fw-bold opacity-75 ms-1">[F2]</span>
                                </button>
                            </div>

                            <form action="${ctx}/parking/checkin" method="POST" id="mainGateForm">
                                <input type="hidden" name="actionType" id="actionType" value="checkin">
                                <input type="hidden" id="estimatedFeeInput" name="estimatedFee">
                                <div class="input-group-custom mb-4 position-relative">
                                    <label class="form-label text-secondary fw-bold" style="font-size: 0.85rem;">MÃ SỐ THẺ</label>
                                    <div class="d-flex shadow-sm" style="border-radius: 8px;">
                                        <div class="position-relative flex-grow-1">
                                            <i class="bi bi-credit-card-2-front icon-left"></i>
                                            <input type="text" id="cardId" name="cardId" value="${oldCard}" placeholder="Quét thẻ hoặc nhập mã số..." required autocomplete="off" class="form-control-lg fs-5 w-100" style="border: 1px solid #e2e8f0; border-right: none; border-top-right-radius: 0; border-bottom-right-radius: 0; padding-left: 3rem;">
                                        </div>

                                        <button type="button" id="btnRandomCard" class="btn btn-primary px-4" style="border-top-left-radius: 0; border-bottom-left-radius: 0; z-index: 1;" title="Lấy thẻ trống ngẫu nhiên">
                                            <i class="bi bi-shuffle fs-5"></i>
                                        </button>
                                    </div>
                                </div>

                                <div class="input-group-custom mb-4">
                                    <label class="form-label text-secondary fw-bold" style="font-size: 0.85rem;">BIỂN SỐ XE</label>
                                    <i class="bi bi-123 icon-left"></i>
                                    <input type="text" id="licensePlate" name="licensePlate" value="${oldPlate}" placeholder="NHẬP BIỂN SỐ (VD: 30A-123.45)" required autocomplete="off" class="form-control-lg fs-5 text-uppercase">
                                </div>

                                <div id="vehicleTypeContainer" class="mb-4">
                                    <label class="form-label text-secondary fw-bold" style="font-size: 0.85rem;">LOẠI XE </label>
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
                                                         data-type-id="${vType.vehicleTypeId}"
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

                                <button type="button" id="btnSubmitForm" class="btn-submit py-3 fs-5" style="background-color: #3b82f6;">
                                    <span id="btnSubmitText">XÁC NHẬN VÀO</span> <i id="btnSubmitIcon" class="bi bi-box-arrow-in-right ms-2"></i>
                                </button>
                            </form>

                        </div>
                    </div>
                </div>

                <div class="col-lg-4 order-3 order-lg-2 d-flex flex-column gap-3">

                    <div class="card shadow-sm border-0 d-none d-lg-block" style="border-radius: 16px;">
                        <div class="card-body p-3 p-xl-4">
                            <div class="d-flex justify-content-between align-items-end mb-2">
                                <div>
                                    <div class="text-muted fw-bold mb-1" style="font-size: 0.75rem; text-transform: uppercase;">Chỗ còn trống</div>
                                    <div class="fw-bold text-primary" style="font-size: 2.5rem; line-height: 1;">${overview.totalAvailable != null ? overview.totalAvailable : '0'}</div>
                                </div>
                                <div class="text-end">
                                    <div class="text-muted fw-bold mb-1" style="font-size: 0.75rem; text-transform: uppercase;">Đã đỗ / Tổng</div>
                                    <div class="fw-bold text-dark" style="font-size: 1.25rem; line-height: 1;">
                                        ${overview.totalOccupied != null ? overview.totalOccupied : '0'} <span class="text-muted fw-normal" style="font-size: 0.9rem;">/ ${overview.totalCapacity != null ? overview.totalCapacity : '0'}</span>
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
                                            <c:set var="badgeClass" value="badge-soft-success" />
                                            <c:set var="icon" value="bi-arrow-down-circle" />
                                            <c:set var="statusText" value="Đang đỗ" />
                                        </c:when>
                                        <c:when test="${log.sessionState == 'completed' || log.sessionState == 'COMPLETED'}">
                                            <c:set var="actionName" value="Xe ra bãi" />
                                            <c:set var="badgeClass" value="badge-soft-warning" />
                                            <c:set var="icon" value="bi-arrow-up-circle" />
                                            <c:set var="statusText" value="Đã ra" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="actionName" value="Lỗi Dữ liệu" />
                                            <c:set var="badgeClass" value="badge-soft-danger" />
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
                    <jsp:include page="/WEB-INF/views/layout/sidebar.jsp">
                        <jsp:param name="activepage" value="dashboard" />
                    </jsp:include>
                </div>
            </div>

            <c:if test="${not empty errMsg or not empty sucMsg}">
                <script>
                    document.addEventListener("DOMContentLoaded", function () {

                    <c:if test="${not empty errMsg}">

                        showToast('error', `${errMsg}`);
                    </c:if>

                    <c:if test="${not empty sucMsg}">
                        showToast('success', `${sucMsg}`);
                    </c:if>

                    });
                </script>
            </c:if>

        </main>


        <script>
            document.addEventListener('DOMContentLoaded', function () {

                // ==========================================
                // KHỐI 1: CACHE DOM ELEMENTS
                // ==========================================
                const el = {
                    form: document.getElementById('mainGateForm'),
                    actionType: document.getElementById('actionType'),
                    tabIn: document.getElementById('tabIn'),
                    tabOut: document.getElementById('tabOut'),
                    cardIdInput: document.getElementById('cardId'),
                    plateInput: document.getElementById('licensePlate'),
                    btnSubmit: document.getElementById('btnSubmitForm'),
                    btnRandom: document.getElementById('btnRandomCard'),
                    vehicleContainer: document.getElementById('vehicleTypeContainer'),
                    hiddenVehicleTypeId: document.getElementById('vehicleTypeId'),
                    vehicleBoxes: document.querySelectorAll('.vehicle-box'),
                    clockTime: document.getElementById('realtimeClock'),
                    clockDate: document.getElementById('realtimeDate'),

                    // --- CÁC PHẦN TỬ CỦA MODAL THANH TOÁN ---
                    paymentModal: document.getElementById('paymentModal'),
                    btnConfirmPayment: document.getElementById('btnConfirmPayment'),

                    ctx: "${ctx}"
                };

                // ==========================================
                // KHỐI 2: EVENT HANDLERS (Logic Cốt Lõi)
                // ==========================================

                function handleSwitchMode(mode, isUserClick = true) {
                    if (!el.tabIn || !el.tabOut || !el.form || !el.actionType)
                        return;

                    if (mode === 'in') {
                        el.tabIn.classList.add('active');
                        el.tabOut.classList.remove('active');
                        el.actionType.value = 'checkin';
                        el.form.action = el.ctx + '/parking/checkin';

                        if (el.btnSubmit) {
                            el.btnSubmit.style.backgroundColor = '#3b82f6';
                            el.btnSubmit.innerHTML = '<span>XÁC NHẬN VÀO</span> <i class="bi bi-box-arrow-in-right ms-2"></i>';
                        }

                        if (el.vehicleContainer)
                            el.vehicleContainer.classList.remove('d-none');
                        if (el.btnRandom)
                            el.btnRandom.style.display = '';
                    } else {
                        el.tabOut.classList.add('active');
                        el.tabIn.classList.remove('active');
                        el.actionType.value = 'checkout';
                        el.form.action = el.ctx + '/parking/checkout';

                        if (el.btnSubmit) {
                            el.btnSubmit.style.backgroundColor = '#ef4444';
                            el.btnSubmit.innerHTML = '<span>XÁC NHẬN RA</span> <i class="bi bi-box-arrow-right ms-2"></i>';
                        }

                        if (el.vehicleContainer)
                            el.vehicleContainer.classList.add('d-none');
                        if (el.btnRandom)
                            el.btnRandom.style.display = 'none';
                    }

                    if (isUserClick) {
                        if (el.cardIdInput) {
                            el.cardIdInput.value = '';
                            el.cardIdInput.focus();
                        }
                        if (el.plateInput)
                            el.plateInput.value = '';
                }
                }

                // --- HÀM XỬ LÝ SUBMIT (ĐÃ CẬP NHẬT EVENT) ---
                function handleFormSubmit(event) {
                    // Chặn submit mặc định của nút (Vì nút đang nằm trong form)
                    if (event) {
                        event.preventDefault();
                    }

                    if (el.cardIdInput && el.plateInput) {
                        if (el.cardIdInput.value.trim() === '' && el.plateInput.value.trim() === '') {
                            showToast('warning', 'Vui lòng quét thẻ hoặc nhập biển số!');
                            el.cardIdInput.focus();
                            return;
                        }
                    }

                    if (el.actionType) {
                        sessionStorage.setItem("lastActiveTab", JSON.stringify(el.actionType.value === 'checkout' ? 'out' : 'in'));
                    }

                    // RẼ NHÁNH LOGIC:
                    if (el.actionType && el.actionType.value === 'checkout') {
                        // 1. [XE RA] Dừng submit, gọi API để lấy thông tin cước phí
                        fetchCheckoutFee(el.cardIdInput.value.trim(), el.plateInput.value.trim());
                    } else {
                        // 2. [XE VÀO] Cứ submit form để Server xử lý (Mở cửa barie)
                        if (el.form)
                            el.form.submit();
                    }
                }

                // Biến toàn cục để quản lý bộ đếm thời gian thanh toán
                let paymentTimeout;

                function fetchCheckoutFee(cardId, plateNumber) {
                    // 1. Xóa bộ đếm cũ nếu có (trường hợp quét thẻ liên tục)
                    if (paymentTimeout)
                        clearTimeout(paymentTimeout);

                    const params = new URLSearchParams();
                    params.append('type', 'session');
                    params.append('cardId', cardId);
                    params.append('plateNumber', plateNumber);
                    params.append('siteId', ${staff.siteId});

                    fetch('${pageContext.request.contextPath}/api/parking/estimate-price', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                        body: params
                    })
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    // 2. Cập nhật thông tin hiển thị lên Modal
                                    document.getElementById('modalCardId').innerText = data.cardId || cardId;
                                    document.getElementById('modalPlate').innerText = data.plateNumber || plateNumber;
                                    document.getElementById('modalTimeIn').innerText = data.timeIn || '---';
                                    document.getElementById('modalDuration').innerText = data.duration || '---';
                                    document.getElementById('modalFee').innerText = data.price || '0 VNĐ';

                                    // 3. Lưu phí dự kiến vào input hidden để gửi lên Backend đối chiếu lúc Submit
                                    // Điều này giúp Backend kiểm tra xem giá có bị nhảy sau khi hiện Modal không
                                    const feeInput = document.getElementById('estimatedFeeInput');
                                    if (feeInput) {
                                        feeInput.value = data.fee;
                                    }

                                    // 4. Khởi tạo và hiển thị Modal (Bootstrap 5)
                                    const modalEl = document.getElementById('paymentModal');
                                    const myModal = bootstrap.Modal.getOrCreateInstance(modalEl);
                                    myModal.show();

                                    // 5. THIẾT LẬP THỜI GIAN CHỜ (TIMEOUT)
                                    // Nếu sau 5 phút (300.000ms) không bấm Xác nhận, Modal tự đóng để tránh sai lệch giá
                                    paymentTimeout = setTimeout(() => {
                                        myModal.hide();
                                        showToast('warning', '⏳ Phiên thanh toán đã hết hạn (quá 5 phút). Vui lòng quét lại thẻ!');
                                        // Reset form nếu cần thiết
                                        const mainForm = document.getElementById('checkoutForm');
                                        if (mainForm)
                                            mainForm.reset();
                                    }, 300000);

                                } else {
                                    // Hiển thị thông báo lỗi từ Backend (Thẻ không tồn tại, sai bãi, v.v.)
                                    showToast('error', data.message || 'Không tìm thấy lượt đỗ xe!');
                                }
                            })
                            .catch(error => {
                                console.error('Lỗi Fetch API:', error);
                                showToast('error', 'Lỗi kết nối máy chủ! Vui lòng thử lại.');
                            });
                }

// --- HÀM 2.2: CHỐT ĐƠN (Submit thẳng lên CheckOutController) ---
                function processCheckoutPayment(event) {
                    if (event)
                        event.preventDefault();

                    const btn = el.btnConfirmPayment;
                    if (!btn)
                        return;

                    // 1. Vô hiệu hóa nút để tránh bấm nhiều lần
                    btn.disabled = true;
                    btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span> Đang xử lý...';

                    // 2. Lấy instance của Modal
                    const modalInstance = bootstrap.Modal.getInstance(el.paymentModal);

                    if (!modalInstance) {
                        // LẮNG NGHE SỰ KIỆN: Khi nào modal ẩn hẳn thì mới Submit form
                        // Điều này giúp tránh xung đột UI và đảm bảo logic chạy mượt mà
                        el.paymentModal.addEventListener('hidden.bs.modal', function () {
                            if (el.form) {
                                el.form.submit();
                            }
                        }, {once: true}); // { once: true } đảm bảo sự kiện này chỉ chạy 1 lần duy nhất

                        // 3. Ra lệnh ẩn Modal (sẽ kích hoạt sự kiện hidden bên trên)
                        modalInstance.hide();
                    } else {
                        //Phòng hờ nếu không tìm thấy instance thì submit luôn
                        if (el.form)
                            el.form.submit();
                    }
                }

                function handleVehicleSelect(boxElement) {
                    const typeId = boxElement.getAttribute('data-type-id');
                    if (el.hiddenVehicleTypeId)
                        el.hiddenVehicleTypeId.value = typeId;

                    el.vehicleBoxes.forEach(box => {
                        box.classList.remove('bg-primary', 'border-primary', 'text-white', 'active-box');
                        box.classList.add('bg-light', 'border-secondary', 'text-secondary');
                        const subtitle = box.querySelector('.subtitle-text');
                        if (subtitle) {
                            subtitle.classList.remove('text-white-50');
                            subtitle.classList.add('text-muted');
                        }
                    });

                    boxElement.classList.remove('bg-light', 'border-secondary', 'text-secondary');
                    boxElement.classList.add('bg-primary', 'border-primary', 'text-white', 'active-box');

                    const selectedSubtitle = boxElement.querySelector('.subtitle-text');
                    if (selectedSubtitle) {
                        selectedSubtitle.classList.remove('text-muted');
                        selectedSubtitle.classList.add('text-white-50');
                    }

                    if (el.cardIdInput && el.plateInput) {
                        if (el.cardIdInput.value.trim() === '')
                            el.cardIdInput.focus();
                        else
                            el.plateInput.focus();
                    }
                }

                function fetchRandomCard() {
                    if (!el.btnRandom)
                        return;
                    const icon = el.btnRandom.querySelector('i');
                    if (icon)
                        icon.className = 'bi bi-arrow-repeat spin-animation';

                    fetch('${pageContext.request.contextPath}/api/parking/random-card')
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    if (el.cardIdInput)
                                        el.cardIdInput.value = data.cardId;
                                    if (el.plateInput)
                                        el.plateInput.focus();
                                } else {
                                    showToast('warning', data.message || 'Hệ thống báo: Đã hết thẻ trống!');
                                }
                            })
                            .catch(error => {
                                console.error('Lỗi lấy thẻ:', error);
                                showToast('error', 'Lỗi kết nối đến máy chủ!');
                            })
                            .finally(() => {
                                if (icon)
                                    icon.className = 'bi bi-shuffle fs-5';
                            });
                }


                // ==========================================
                // KHỐI 3: EVENT REGISTRATION (Gắn sự kiện)
                // ==========================================

                if (el.tabIn)
                    el.tabIn.addEventListener('click', () => handleSwitchMode('in'));
                if (el.tabOut)
                    el.tabOut.addEventListener('click', () => handleSwitchMode('out'));

                // Gắn sự kiện nút Submit Form (truyền event vào hàm)
                if (el.btnSubmit) {
                    el.btnSubmit.addEventListener('click', function (e) {
                        handleFormSubmit(e);
                    });
                }

                // Gắn sự kiện nút Random Card
                if (el.btnRandom)
                    el.btnRandom.addEventListener('click', fetchRandomCard);

                // Gắn sự kiện Hộp xe
                el.vehicleBoxes.forEach(box => {
                    box.addEventListener('click', function () {
                        if (this.style.cursor === 'not-allowed')
                            return;
                        handleVehicleSelect(this);
                    });
                });

                // Gắn sự kiện nút XÁC NHẬN THU TIỀN trên Modal
                if (el.btnConfirmPayment) {
                    el.btnConfirmPayment.addEventListener('click', function (e) {
                        processCheckoutPayment(e);
                    });
                }


                // ==========================================
                // KHỐI 4: GLOBAL LISTENERS (Bàn phím & Smart Focus)
                // ==========================================

                document.addEventListener('keydown', function (e) {
                    if (e.key === 'F1') {
                        e.preventDefault();
                        handleSwitchMode('in');
                    } else if (e.key === 'F2') {
                        e.preventDefault();
                        handleSwitchMode('out');
                    } else if (e.code === 'Tab') {
                        e.preventDefault();
                        if (el.actionType && el.actionType.value === 'checkin') {
                            fetchRandomCard();
                        }
                    } else if (e.key === 'Enter') {
                        e.preventDefault();

                        // Nếu cái Modal thanh toán đang mở thì bấm Enter là tự động Xác nhận thu tiền luôn
                        if (el.paymentModal && el.paymentModal.classList.contains('show')) {
                            processCheckoutPayment(e);
                            return;
                        }

                        if (document.activeElement === el.cardIdInput && el.cardIdInput.value.trim() !== '') {
                            if (el.plateInput)
                                el.plateInput.focus();
                        } else {
                            handleFormSubmit(e); // Truyền e vào
                        }
                    }
                });

                document.addEventListener('click', function (e) {
                    // Không kích hoạt Smart Focus nếu click vào Modal hoặc nút tắt Toast
                    const isInteractiveArea = e.target.closest('button, a, input, .offcanvas, .vehicle-box, .modal, .toast');
                    if (!isInteractiveArea && el.cardIdInput && el.plateInput) {
                        if (el.cardIdInput.value === '')
                            el.cardIdInput.focus();
                        else
                            el.plateInput.focus();
                    }
                });


                // ==========================================
                // KHỐI 5: INITIALIZATION (Khởi chạy trang)
                // ==========================================

                const savedTab = JSON.parse(sessionStorage.getItem("lastActiveTab"));
                if (savedTab) {
                    handleSwitchMode(savedTab, false);
                    sessionStorage.removeItem("lastActiveTab");
                }

                if (el.cardIdInput && document.activeElement !== el.cardIdInput) {
                    el.cardIdInput.focus();
                }

            });
        </script>
    </body>
</html>