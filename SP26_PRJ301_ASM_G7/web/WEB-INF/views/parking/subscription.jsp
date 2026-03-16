<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý Vé tháng | ParkEasy</title>

        <jsp:include page="/WEB-INF/views/layout/layout.jsp"/>
        <style>
            :root {
                --bs-body-bg: #f8fafc;
                --bs-primary: #1e40af;
                --text-main: #0f172a;
                --text-muted: #475569;
                --card-border: #cbd5e1;
                --navbar-height: 70px;
            }

            body {
                font-family: 'Inter', sans-serif;
                color: var(--text-main);
                background-color: var(--bs-body-bg);
                padding-top: var(--navbar-height);
                padding-bottom: 50px;
            }

            .section-card {
                background: white;
                border-radius: 12px;
                border: 1.5px solid var(--card-border);
                padding: 1.5rem;
                margin-bottom: 1.5rem;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            }
            .section-card:focus-within {
                border-color: var(--bs-primary);
                box-shadow: 0 0 0 3px rgba(30, 64, 175, 0.1);
            }

            .step-badge {
                width: 32px;
                height: 32px;
                background: #eff6ff;
                color: var(--bs-primary);
                border-radius: 8px;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                font-weight: 800;
                margin-right: 12px;
            }

            .package-grid {
                display: grid;
                grid-template-columns: repeat(3, 1fr);
                gap: 1rem;
            }
            .package-item {
                border: 2px solid var(--card-border);
                border-radius: 12px;
                padding: 1rem;
                cursor: pointer;
                text-align: center;
                position: relative;
                transition: all 0.2s;
            }
            .package-item.active {
                border-color: var(--bs-primary);
                background: #f0f7ff;
                transform: translateY(-2px);
                box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);
            }
            .package-item .price {
                font-weight: 800;
                color: var(--bs-primary);
                display: block;
                margin-top: 5px;
            }

            .summary-sticky {
                position: sticky;
                top: calc(var(--navbar-height) + 20px);
            }
            .total-amount {
                font-size: 1.75rem;
                font-weight: 800;
                color: #dc2626;
            }

            @keyframes spin {
                100% {
                    transform: rotate(360deg);
                }
            }
            .spin-animation {
                animation: spin 1s linear infinite;
            }
            .loading-spinner {
                display: none;
                width: 1.2rem;
                height: 1.2rem;
            }

            .badge-soft-success {
                background: #dcfce7;
                color: #166534;
                border: 1px solid #86efac;
                font-weight: 700;
                padding: 5px 12px;
                border-radius: 6px;
            }
            .badge-soft-warning {
                background: #fef3c7;
                color: #92400e;
                border: 1px solid #fcd34d;
                font-weight: 700;
                padding: 5px 12px;
                border-radius: 6px;
            }
        </style>
    </head>
    <body>

        <jsp:include page="/WEB-INF/views/layout/staff-header.jsp" />

        <c:set var="errMsg" value="${sessionScope.errorMsg}" />
        <c:set var="sucMsg" value="${sessionScope.successMsg}" />
        <c:set var="oldSubscription" value="${sessionScope.oldSubscription}" />
        <c:remove var="errorMsg" scope="session"/>
        <c:remove var="successMsg" scope="session"/>
        <c:remove var="oldSubscription" scope="session"/>

        <jsp:include page="/WEB-INF/views/layout/sidebar.jsp">
                <jsp:param name="activepage" value="subscription" />
            </jsp:include>

        <div class="container">
            <div class="d-flex justify-content-between align-items-center mt-4 mb-4">
                <h4 class="fw-bold mb-0 text-dark"><i class="bi bi-card-checklist me-2 text-primary"></i>Quản lý Vé tháng</h4>
                <a href="${ctx}/dashboard" class="btn btn-outline-secondary fw-bold">Quay lại</a>
            </div>

            <form id="subForm" action="${pageContext.request.contextPath}/staff/subscription" method="POST">

                <input type="hidden" id="appContext" value="${pageContext.request.contextPath}">

                <input type="hidden" name="actionType" id="actionType" value="create">
                <input type="hidden" name="oldSubscriptionId" id="oldSubscriptionId" value="">

                <div class="row">
                    <div class="col-lg-8">

                        <div class="section-card">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h6 class="fw-bold mb-0 text-uppercase"><span class="step-badge">1</span>Thông tin khách hàng</h6>
                                <span id="customerStatus" class="badge-soft-warning">MỚI</span>
                            </div>
                            <div class="row g-3">
                                <div class="col-md-5">
                                    <label class="form-label fw-bold small">Số điện thoại</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-white"><i class="bi bi-telephone-fill text-primary"></i></span>
                                        <input type="text" name="phone" id="phoneInput" class="form-control fw-bold" placeholder="09xxxxxxxx" required>
                                        <span class="input-group-text bg-white loading-spinner" id="phoneLoader">
                                            <div class="spinner-border spinner-border-sm text-primary" role="status"></div>
                                        </span>
                                    </div>
                                </div>
                                <div class="col-md-7">
                                    <label class="form-label fw-bold small">Họ tên chủ xe</label>
                                    <input type="text" name="fullName" id="nameInput" class="form-control fw-bold" required>
                                </div>
                            </div>
                        </div>

                        <div class="section-card">
                            <h6 class="fw-bold mb-4 text-uppercase"><span class="step-badge">2</span>Thông tin phương tiện</h6>

                            <div id="existingVehicles" class="mb-4 d-none">
                                <div id="hasVehicleSection">
                                    <label class="form-label fw-bold small text-primary mb-2">Chọn từ xe đã đăng ký:</label>
                                    <div class="d-flex gap-2 flex-wrap" id="vehicleList"></div>
                                </div>
                                <div id="noVehicleMsg" class="d-none">
                                    <span class="text-muted small fst-italic">
                                        <i class="bi bi-info-circle me-1"></i>Khách hàng chưa có phương tiện hoặc vé tháng cũ.
                                    </span>
                                </div>
                                <hr class="border-secondary opacity-25 mt-3">
                            </div>

                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-bold small">Biển số xe</label>
                                    <input type="text" name="plate" id="plateInput" class="form-control fw-bold text-uppercase fs-5" placeholder="29A-123.45" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-bold small">Loại xe</label>
                                    <select class="form-select fw-bold" name="vehicleTypeId" id="vehicleType">
                                        <c:forEach items="${vehicleTypes}" var="v">
                                            <option value="${v.vehicleTypeId}">${v.vehicleTypeName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="section-card">
                            <h6 class="fw-bold mb-4 text-uppercase"><span class="step-badge">3</span>Cấu hình vé tháng</h6>

                            <div class="package-grid mb-4">
                                <c:forEach items="${availablePackages}" var="pkgType" varStatus="loop">

                                    <c:set var="monthVal" value="${pkgType == 'monthly' ? 1 : (pkgType == 'quarterly' ? 3 : (pkgType == 'half-yearly' ? 6 : (pkgType == 'yearly' ? 12 : 1)))}" />

                                    <div class="package-item ${loop.first ? 'active' : ''}">
                                        <input type="radio" name="configId" value="" data-type="${pkgType}" class="d-none" ${loop.first ? 'checked' : ''}>
                                        <span class="fw-bold">${monthVal} THÁNG</span>
                                        <span class="price">0 đ</span>
                                    </div>

                                </c:forEach>
                            </div>

                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-bold small text-muted">Ngày bắt đầu</label>
                                    <input type="date" name="startDate" id="startDate" class="form-control fw-bold" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-bold small text-muted">Mã thẻ vật lý (RFID)</label>
                                    <div class="input-group">
                                        <input type="text" name="cardId" id="cardIdInput" class="form-control fw-bold border-primary" placeholder="Quét thẻ..." required>
                                        <button class="btn btn-primary" type="button" id="btnRandomCard">
                                            <i class="bi bi-shuffle"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-4">
                        <div class="summary-sticky">
                            <div class="section-card border-primary">
                                <h5 class="fw-bold mb-4 text-primary">THANH TOÁN</h5>

                                <div class="d-flex justify-content-between mb-3">
                                    <span class="fw-medium text-muted">Phí gói cước:</span>
                                    <span id="sumBasePrice" class="fw-bold text-dark">0 đ</span>
                                </div>
                                <div class="d-flex justify-content-between mb-3">
                                    <span class="fw-medium text-muted">Phí phát hành thẻ:</span>
                                    <span id="sumCardPrice" class="fw-bold text-dark">0 đ</span>
                                </div>
                                <hr class="border-primary opacity-50">
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <span class="fw-bold fs-5">TỔNG CỘNG</span>
                                    <span class="total-amount" id="sumTotal">0 đ</span>
                                </div>

                                <button type="submit" class="btn btn-primary w-100 py-3 fw-bold shadow">
                                    <i class="bi bi-printer-fill me-2"></i>XÁC NHẬN & IN BIÊN LAI
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <script type="application/json" id="priceMapData">
            {
            <c:forEach items="${priceConfigs}" var="p" varStatus="loop">
                "${p.vehicleTypeId}_${p.type}": {
                "configId": ${p.configId},
                "basePrice": ${p.basePrice}
                }${!loop.last ? "," : ""}
            </c:forEach>
            }


        </script>

        <!--        <script id="oldSubDataIsland" type="application/json">
        <c:choose>
            <c:when test="${not empty oldSubscription}">
                {
                "actionType": "${oldSubscription.actionType}",
                "phone": "${oldSubscription.phone}",
                "fullName": "${oldSubscription.fullName}",
                "plate": "${oldSubscription.plate}",
                "cardId": "${oldSubscription.cardId}",
                "vehicleTypeId": "${oldSubscription.vehicleTypeId}",
                "configId": "${oldSubscription.configId}",
                "startDate": "${oldSubscription.startDate}",
                "oldSubId": "${oldSubscription.oldSubId}"
                }
            </c:when>
            <c:otherwise>null</c:otherwise>
        </c:choose>
    </script>-->

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

        <script>
            document.addEventListener('DOMContentLoaded', function () {

                // Đọc dữ liệu môi trường từ HTML
                const ctx = document.getElementById('appContext').value;
                const priceMapStr = document.getElementById('priceMapData').textContent;
                const priceMapObj = JSON.parse(priceMapStr || '{}');

                const el = {
                    form: document.getElementById('subForm'),
                    actionType: document.getElementById('actionType'),
                    oldSubscriptionId: document.getElementById('oldSubscriptionId'),

                    phoneInput: document.getElementById('phoneInput'),
                    nameInput: document.getElementById('nameInput'),
                    plateInput: document.getElementById('plateInput'),
                    vehicleType: document.getElementById('vehicleType'),
                    startDate: document.getElementById('startDate'),
                    cardIdInput: document.getElementById('cardIdInput'),

                    btnRandom: document.getElementById('btnRandomCard'),
                    btnSubmit: document.querySelector('button[type="submit"]'),

                    phoneLoader: document.getElementById('phoneLoader'),
                    customerStatus: document.getElementById('customerStatus'),
                    existingVehicles: document.getElementById('existingVehicles'),
                    hasVehicleSection: document.getElementById('hasVehicleSection'),
                    noVehicleMsg: document.getElementById('noVehicleMsg'),
                    vehicleList: document.getElementById('vehicleList'),
                    packageItems: document.querySelectorAll('.package-item'),

                    sumBasePrice: document.getElementById('sumBasePrice'),
                    sumCardPrice: document.getElementById('sumCardPrice'),
                    sumTotal: document.getElementById('sumTotal'),

                    lastValidPhone: ''

                };

                // =========================================================
// LOGIC KHÔI PHỤC DỮ LIỆU CÓ GẮN DEBUGGER
// =========================================================
//                try {
//                    const islandElement = document.getElementById('oldSubDataIsland');
//                    if (!islandElement) {
//                        console.error("DEBUG 1: Không tìm thấy thẻ <script id='oldSubDataIsland'> trên HTML.");
//                        return;
//                    }
//
//                    const rawData = islandElement.textContent.trim();
//                    console.log("DEBUG 2: Dữ liệu thô JSTL in ra là:", rawData);
//
//                    if (rawData === "null" || rawData === "") {
//                        console.log("DEBUG 3: Session không có dữ liệu cũ (Form chạy bình thường).");
//                        return;
//                    }
//
//                    const oldData = JSON.parse(rawData);
//                    console.log("DEBUG 4: Đã chuyển thành Object JS thành công:", oldData);
//
//                    // Bắt đầu điền dữ liệu
//                    if (el.phoneInput)
//                        el.phoneInput.value = oldData.phone || '';
//                    if (el.nameInput)
//                        el.nameInput.value = oldData.fullName || '';
//                    if (el.plateInput)
//                        el.plateInput.value = oldData.plate || '';
//                    if (el.cardIdInput)
//                        el.cardIdInput.value = oldData.cardId || '';
//
//                    console.log("DEBUG 5: Đã đổ xong Text. Bắt đầu xử lý select/radio...");
//
//                    if (el.vehicleType)
//                        el.vehicleType.value = oldData.vehicleTypeId || '';
//
//                    // Phải chờ HTML render xong option mới chọn được radio
//                    setTimeout(() => {
//                        updatePricesByMemory();
//                        console.log("DEBUG 6: Đã chạy xong updatePricesByMemory.");
//
//                        if (oldData.configId) {
//                            // Cố gắng tìm thẻ input theo cả value lẫn name
//                            const savedRadio = document.querySelector(`input[value="${oldData.configId}"]`);
//                            if (savedRadio) {
//                                const parentItem = savedRadio.closest('.package-item');
//                                if (parentItem) {
//                                    parentItem.click();
//                                    console.log("DEBUG 7: Đã tự động chọn lại gói cước ID:", oldData.configId);
//                                }
//                            } else {
//                                console.warn("DEBUG 7-LỖI: Không tìm thấy thẻ input nào có value =", oldData.configId);
//                            }
//                        }
//                    }, 100);
//
//                } catch (error) {
//                    console.error("DEBUG CRITICAL: Lỗi vỡ code JS trong quá trình khôi phục!", error);
//                }

                function lookupCustomer() {
                    if (!el.phoneInput)
                        return;
                    const phone = el.phoneInput.value.trim();

                    if (phone === '') {
                        el.lastValidPhone = '';
                        resetToNewCustomer();
                        return;
                    }

                    const phoneRegex = /^0\d{9}$/;
                    if (!phoneRegex.test(phone)) {
                        showToast('warning', 'SĐT không hợp lệ! Vui lòng nhập 10 số bắt đầu bằng 0.');
                        el.lastValidPhone = '';
                        resetToNewCustomer();
                        setTimeout(() => {
                            if (el.phoneInput)
                                el.phoneInput.focus();
                        }, 50);
                        return;
                    }

                    if (phone === el.lastValidPhone && el.customerStatus.innerText !== "MỚI")
                        return;

                    el.lastValidPhone = phone;
                    if (el.phoneLoader)
                        el.phoneLoader.style.display = 'block';

                    fetch('${pageContext.request.contextPath}/api/customer/lookup?phone=' + phone)
                            .then(res => res.json())
                            .then(data => {
                                if (data.exists) {
                                    if (el.nameInput)
                                        el.nameInput.value = data.name;
                                    el.nameInput.readOnly = true;
                                    if (el.customerStatus) {
                                        el.customerStatus.innerText = "KHÁCH CŨ";
                                        el.customerStatus.className = "badge-soft-success";
                                    }
                                    if (data.vehicles)
                                        renderVehicleButtons(data.vehicles);
                                    showToast('success', 'Đã nhận diện: ' + data.name);
                                } else {
                                    resetToNewCustomer();
                                }
                            })
                            .catch(e => {
                                showToast('error', 'Lỗi kết nối API tra cứu!');
                            })
                            .finally(() => {
                                if (el.phoneLoader)
                                    el.phoneLoader.style.display = 'none';
                            });
                }

                function renderVehicleButtons(vehicles) {
                    if (!el.existingVehicles || !el.vehicleList || !el.noVehicleMsg || !el.hasVehicleSection)
                        return;

                    el.existingVehicles.classList.remove('d-none');
                    el.vehicleList.innerHTML = '';

                    if (vehicles.length === 0) {
                        el.noVehicleMsg.classList.remove('d-none');
                        el.hasVehicleSection.classList.add('d-none');
                        if (el.plateInput)
                            el.plateInput.value = '';
                        if (el.startDate)
                            el.startDate.value = new Date().toISOString().split('T')[0];
                        if (el.sumCardPrice)
                            el.sumCardPrice.innerText = ' đ';
                        calculateTotal();
                        return;
                    }

                    el.noVehicleMsg.classList.add('d-none');
                    el.hasVehicleSection.classList.remove('d-none');

                    vehicles.forEach((v, index) => {
                        const btn = document.createElement('button');
                        btn.type = 'button';
                        btn.className = v.hasSub ? 'btn btn-primary fw-bold btn-sm mb-2 me-2' : 'btn btn-outline-dark fw-bold btn-sm mb-2 me-2';
                        btn.innerHTML = `<i class="bi bi-car-front me-1"></i> ${v.plate}${v.hasSub ? ' (Gia hạn)' : ''}`;

                        btn.onclick = () => applyVehicleSelection(v, btn);
                        el.vehicleList.appendChild(btn);

                        if (index === 0)
                            applyVehicleSelection(v, btn);
                    });
                }

                function applyVehicleSelection(vehicle, btnElement) {
                    if (el.plateInput)
                        el.plateInput.value = vehicle.plate;
                    if (el.vehicleType)
                        el.vehicleType.value = vehicle.typeId;

                    const today = new Date();

                    if (vehicle.hasSub) {
                        if (el.actionType)
                            el.actionType.value = 'renew';
                        if (el.oldSubscriptionId)
                            el.oldSubscriptionId.value = vehicle.subscriptionId || '';

                        const expiryDate = new Date(vehicle.expiryDate);
                        if (expiryDate >= today) {
                            const nextStart = new Date(expiryDate);
                            nextStart.setDate(nextStart.getDate() + 1);
                            if (el.startDate)
                                el.startDate.value = nextStart.toISOString().split('T')[0];
                        } else {
                            if (el.startDate)
                                el.startDate.value = today.toISOString().split('T')[0];
                        }
                        if (el.sumCardPrice)
                            el.sumCardPrice.innerText = '0 đ';
                    } else {
                        if (el.actionType)
                            el.actionType.value = 'create';
                        if (el.oldSubscriptionId)
                            el.oldSubscriptionId.value = '';
                        if (el.startDate)
                            el.startDate.value = today.toISOString().split('T')[0];
                        if (el.sumCardPrice)
                            el.sumCardPrice.innerText = '0 đ';
                    }

                    if (btnElement) {
                        const allBtns = el.vehicleList.querySelectorAll('button');
                        allBtns.forEach(b => {
                            b.classList.remove('border-3', 'border-warning');
                            b.style.transform = 'scale(1)';
                        });
                        btnElement.classList.add('border-3', 'border-warning');
                        btnElement.style.transform = 'scale(1.05)';
                    }

                    updatePricesByMemory();
                }

                function updatePricesByMemory() {
                    if (!el.vehicleType || el.vehicleType.selectedIndex === -1)
                        return;

                    const vId = el.vehicleType.value;

                    el.packageItems.forEach(item => {
                        const input = item.querySelector('input');
                        const pkgType = input.getAttribute('data-type'); // Lấy "monthly", "quarterly"...

                        // Tìm cấu hình tương ứng trong dữ liệu Data Island
                        const key = vId + "_" + pkgType;
                        const configData = priceMapObj[key];

                        if (configData) {
                            // NẾU CÓ CẤU HÌNH: Hiển thị giá và gán configId vào input để submit
                            item.querySelector('.price').innerText = configData.basePrice.toLocaleString('vi-VN') + ' đ';
                            input.value = configData.configId;
                            item.style.display = 'block';
                        } else {
                            // NẾU LOẠI XE NÀY KHÔNG CÓ GÓI NÀY: Ẩn đi
                            item.querySelector('.price').innerText = '---';
                            input.value = '';
                            item.style.display = 'none';
                        }
                    });

                    // Lấy giá gói đang Active đổ sang Cột Thanh Toán
                    const activePriceLabel = document.querySelector('.package-item.active .price');
                    if (activePriceLabel && el.sumBasePrice) {
                        el.sumBasePrice.innerText = activePriceLabel.innerText === '---' ? '0 đ' : activePriceLabel.innerText;
                    }

                    calculateTotal();
                }

                function calculateTotal() {
                    if (!el.sumBasePrice || !el.sumCardPrice || !el.sumTotal)
                        return;
                    const parseCurrency = (text) => parseInt(text.replace(/\D/g, ''), 10) || 0;

                    const total = parseCurrency(el.sumBasePrice.innerText) + parseCurrency(el.sumCardPrice.innerText);
                    el.sumTotal.innerText = total.toLocaleString('vi-VN') + ' đ';
                }

                function resetToNewCustomer() {
                    if (el.customerStatus) {
                        el.customerStatus.innerText = "MỚI";
                        el.customerStatus.className = "badge-soft-warning";
                    }
                    if (el.existingVehicles)
                        el.existingVehicles.classList.add('d-none');
                    if (el.vehicleList)
                        el.vehicleList.innerHTML = '';

                    if (el.nameInput)
                        el.nameInput.value = '';
                        el.nameInput.readOnly = false; 
                    
                    if (el.plateInput)
                        el.plateInput.value = '';
                    if (el.actionType)
                        el.actionType.value = 'create';
                    if (el.oldSubscriptionId)
                        el.oldSubscriptionId.value = '';
                    if (el.startDate)
                        el.startDate.value = new Date().toISOString().split('T')[0];
                    if (el.sumCardPrice)
                        el.sumCardPrice.innerText = '0 đ';
                    updatePricesByMemory();
                }

                function fetchRandomCard() {
                    const icon = el.btnRandom ? el.btnRandom.querySelector('i') : null;
                    if (icon)
                        icon.className = 'bi bi-arrow-repeat spin-animation d-inline-block';

                    fetch('${pageContext.request.contextPath}/api/parking/random-card')
                            .then(r => r.json())
                            .then(data => {
                                if (data.success && el.cardIdInput) {
                                    el.cardIdInput.value = data.cardId;
                                    showToast('success', 'Đã cấp thẻ: ' + data.cardId);
                                    if (el.plateInput)
                                        el.plateInput.focus();
                                } else {
                                    showToast('warning', data.message || 'Hết thẻ trống!');
                                }
                            })
                            .finally(() => {
                                if (icon)
                                    icon.className = 'bi bi-shuffle';
                            });
                }

                if (el.phoneInput)
                    el.phoneInput.addEventListener('blur', lookupCustomer);
                if (el.vehicleType)
                    el.vehicleType.addEventListener('change', updatePricesByMemory);
                if (el.btnRandom)
                    el.btnRandom.addEventListener('click', fetchRandomCard);

                el.packageItems.forEach(btn => {
                    btn.addEventListener('click', function () {
                        el.packageItems.forEach(p => p.classList.remove('active'));
                        this.classList.add('active');
                        this.querySelector('input').checked = true;
                        updatePricesByMemory();
                    });
                });

                document.addEventListener('keydown', function (e) {
                    if (e.key === 'Tab' && document.activeElement === el.cardIdInput) {
                        if (!el.cardIdInput.value.trim()) {
                            e.preventDefault();
                            fetchRandomCard();
                        }
                    }
                    if (e.key === 'Enter' && e.target.type !== 'submit') {
                        e.preventDefault();
                        const flow = [el.phoneInput, el.nameInput, el.plateInput, el.vehicleType, el.cardIdInput, el.startDate];
                        const currentIndex = flow.indexOf(document.activeElement);

                        if (currentIndex > -1 && currentIndex < flow.length - 1) {
                            const nextField = flow[currentIndex + 1];
                            nextField.focus();
                            if (nextField.tagName === 'INPUT')
                                nextField.select();
                        } else {
                            if (el.btnSubmit)
                                el.btnSubmit.focus();
                        }
                    }
                });

                if (el.phoneInput)
                    el.phoneInput.focus();
                if (el.startDate)
                    el.startDate.value = new Date().toISOString().split('T')[0];
                if (el.vehicleType && el.vehicleType.options.length > 0) {
                    setTimeout(updatePricesByMemory, 50);
                }
            });
        </script>
    </body>
</html>