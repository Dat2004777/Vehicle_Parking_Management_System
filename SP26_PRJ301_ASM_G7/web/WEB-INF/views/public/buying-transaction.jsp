<%-- 
    Document   : buying-transaction
    Created on : Mar 1, 2026, 8:38:07 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Buying Confirmation Page</title>
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
                --primary-color: #0d6efd;
                --bs-primary: #1a56db;
                --bs-primary-rgb: 26, 86, 219;
                --bs-font-sans-serif: 'Inter', sans-serif;
                --text-main: #374151;
                --text-muted: #6b7280;
            }

            body {
                background-color: #f9fafb;
                font-family: var(--bs-font-sans-serif);
                color: var(--text-main);
                -webkit-font-smoothing: antialiased;
            }

            .main-content-card {
                background: white;
                padding: 2rem;
                border-radius: 1.5rem;
                border: 1px solid #e5e7eb;
                box-shadow: 0 1px 3px rgba(0,0,0,0.05);
            }
            .selected-badge {
                position: absolute;
                top: -10px;
                right: -10px;
                background: white;
                color: var(--primary-color);
                font-size: 20px;
                line-height: 1;
                border-radius: 50%;
            }
            .pricing-card {
                background: white;
                transition: all 0.3s ease;
                cursor: pointer;
            }
            .pricing-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
            }

            .pricing-card.active {
                border: 2px solid var(--primary-color) !important;
            }
            /* Các bước thực hiện */
            .step-number {
                width: 28px;
                height: 28px;
                background-color: var(--primary-color);
                color: white;
                border-radius: 50%;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                font-size: 14px;
                font-weight: bold;
                margin-right: 12px;
            }

            .badge-promo {
                position: absolute;
                top: 10px;
                right: 10px;
                background: #dcfce7;
                color: #166534;
                font-size: 11px;
                font-weight: 700;
                padding: 2px 8px;
                border-radius: 1rem;
            }

            /* Map Placeholder */
            .map-box {
                background: #e5e7eb;
                height: 200px;
                display: flex;
                align-items: center;
                justify-content: center;
                background-image: radial-gradient(#cbd5e1 1px, transparent 1px);
                background-size: 15px 15px;
            }

            .site-display-input {
                width: 100%;
                border: none;
                background: #eef2ff;
                padding: 12px 14px;
                border-radius: 0.75rem;
                font-weight: 600;
                color: #1a56db;
                font-size: 0.95rem;
            }

            .site-display-input:focus {
                outline: none;
            }
            /* Biển số xe input */
            #licenseInput {
                letter-spacing: 1px;
            }

            /* Custom Responsive */
            @media (max-width: 991.98px) {
                .main-content-card {
                    padding: 1.5rem;
                }
            }
        </style>
    </head>
    <body>
        <!--Header-->
        <%@include file="/WEB-INF/views/layout/header.jsp" %>

        <!--Toast Layout-->
        <%@include file="/WEB-INF/views/layout/customer-layout.jsp" %>
        <div class="container my-5">
            <div class="row g-4">
                <div class="col-lg-8">
                    <div class="main-content-card h-100">
                        <header class="mb-4">
                            <h1 class="h3 fw-bold mb-1">Đăng ký vé tháng</h1>
                            <p class="text-muted">Giải pháp đỗ xe dài hạn tiết kiệm và tiện lợi cho phương tiện của bạn.</p>
                        </header>

                        <section class="mb-5">
                            <div class="d-flex align-items-center mb-4">
                                <span class="step-number me-2">1</span>
                                <h5 class="mb-0 fw-bold">Chọn gói vé</h5>
                            </div>

                            <div class="row g-3">
                                <div class="col-md-4">
                                    <div class="pricing-card active h-100 p-3 border rounded-3 position-relative">
                                        <input type="radio" name="planType" value="month" checked hidden>
                                        <div class="selected-badge"><i class="fa-solid fa-circle-check"></i></div>
                                        <p class="text-secondary small mb-1">Vé tháng</p>
                                        <h4 class="fw-bold">
                                            <span id="price-month"></span>
                                            <small class="text-muted fs-6">/tháng</small>
                                        </h4>
                                        <ul class="list-unstyled mt-3 small">
                                            <li class="mb-2 text-success"><i class="fa-solid fa-check me-2"></i>Ra vào tự do
                                            </li>
                                            <li class="text-success"><i class="fa-solid fa-check me-2"></i>Hỗ trợ 24/7</li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="pricing-card h-100 p-3 border rounded-3 position-relative">
                                        <input type="radio" name="planType" value="quarter" hidden>
                                        <span
                                            class="badge bg-success-subtle text-success position-absolute top-0 end-0 m-2">-10%</span>
                                        <p class="text-secondary small mb-1">Vé quý</p>
                                        <h4 class="fw-bold">
                                            <span id="price-quarter"></span>
                                            <small class="text-muted fs-6">/3 tháng</small>
                                        </h4>
                                        <ul class="list-unstyled mt-3 small">
                                            <li class="mb-2 text-success"><i class="fa-solid fa-check me-2"></i>Tiết kiệm 150k
                                            </li>
                                            <li class="text-success"><i class="fa-solid fa-check me-2"></i>Ra vào tự do</li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="pricing-card h-100 p-3 border rounded-3 position-relative">
                                        <input type="radio" name="planType" value="year" hidden>
                                        <span
                                            class="badge bg-primary-subtle text-primary position-absolute top-0 end-0 m-2 text-uppercase">Best</span>
                                        <p class="text-secondary small mb-1">Vé năm</p>
                                        <h4 class="fw-bold">
                                            <span id="price-year"></span>
                                            <small class="text-muted fs-6">/năm</small>
                                        </h4>
                                        <ul class="list-unstyled mt-3 small">
                                            <li class="mb-2 text-success"><i class="fa-solid fa-check me-2"></i>Giảm 2 tháng
                                            </li>
                                            <li class="text-success"><i class="fa-solid fa-check me-2"></i>Vị trí ưu tiên</li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <section class="mb-5">
                            <h5 class="fw-bold mb-4 d-flex align-items-center"><span class="step-number">2</span> Thông tin đăng ký</h5>
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold fs-6">Bãi đỗ xe</label>
                                    <input type="text" name="siteName" class="site-display-input" value="${site.siteName}" readonly=""/>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold fs-6">Loại phương tiện</label>
                                    <div class="d-flex gap-2">
                                        <c:forEach var="v" items="${vehicles}" varStatus="status">
                                            <input type="radio"
                                                   class="btn-check"
                                                   name="vtype"
                                                   value="${v.vehicle.vehicleTypeId}"
                                                   data-price="${v.basePrice}"
                                                   id="${v.vehicle.vehicleName}"
                                                   ${status.first ? "checked" : ""}/>

                                            <label class="btn btn-outline-primary flex-grow-1 py-2" for="${v.vehicle.vehicleName}">
                                                <c:choose>
                                                    <c:when test="${v.vehicle.vehicleName eq 'CAR'}">
                                                        <i class="bi bi-car-front me-2"></i>
                                                        ${v.vehicle.vehicleName.label}
                                                    </c:when>
                                                    <c:when test="${v.vehicle.vehicleName eq 'MOTORBIKE'}">
                                                        <i class="bi bi-bicycle me-2"></i>
                                                        ${v.vehicle.vehicleName.label}
                                                    </c:when>
                                                </c:choose>
                                            </label>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="col-12 mt-4">
                                    <label class="form-label fw-semibold fs-6">Biển số xe</label>
                                    <input id="licensePlate" type="text" class="form-control bg-light border-1 py-2 fs-5 text-uppercase fw-bold" placeholder="VÍ DỤ: 51F-123.45" required>
                                </div>
                            </div>
                        </section>

                        <div class="map-box position-relative overflow-hidden rounded-4 shadow-sm mx-auto" 
                             style="height: 280px; max-width: 700px;">

                            <c:url var="mapUrl" value="https://maps.google.com/maps">
                                <c:param name="q" value="${requestScope.site.address}" />
                                <c:param name="output" value="embed" />
                            </c:url>

                            <iframe 
                                src="${mapUrl}" 
                                class="w-100 h-100 border-0"
                                allowfullscreen="" 
                                loading="lazy">
                            </iframe>

                            <div class="position-absolute bottom-0 start-0 m-2">
                                <div class="bg-white px-2 py-1 rounded-2 shadow-sm" style="font-size: 0.75rem; font-weight: 600;">
                                    <i class="bi bi-geo-alt-fill text-danger"></i> ${requestScope.site.address}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="card border-0 shadow-sm rounded-4 overflow-hidden position-sticky" style="top: 20px;border-color: #e5e7eb; background-color: #ffffff;">
                        <div class="card-header bg-white border-0 py-3 px-4">
                            <h5 class="fw-bold mb-0 d-flex align-items-center text-dark">
                                <i class="bi bi-cart-fill me-2 text-primary"></i> Tóm tắt đơn hàng
                            </h5>
                        </div>

                        <div class="card-body p-4 pt-0 mt-3">
                            <div class="summary-info mb-4">
                                <div class="d-flex justify-content-between mb-3">
                                    <span class="text-muted">Gói đăng ký:</span>
                                    <span class="fw-bold text-dark">Vé tháng </span>
                                </div>
                                <div class="d-flex justify-content-between mb-3">
                                    <span class="text-muted">Biển số xe:</span>
                                    <span class="fw-bold text-dark"></span>
                                </div>
                                <div class="d-flex justify-content-between mb-3">
                                    <span class="text-muted">Bãi đỗ xe:</span>
                                    <span class="fw-bold text-dark">${requestScope.site.siteName}</span>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <span class="text-muted">Thời hạn:</span>
                                    <span class="fw-bold text-dark" id="summary-expiry"></span>
                                </div>
                            </div>

                            <hr class="border-secondary opacity-10 mb-4" style="border-style: dashed;">

                            <div class="rounded-3 p-3 mb-4" style="background-color: #f8faff;">
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted small">Tạm tính:</span>
                                    <span class="text-dark fw-medium" id="summary-subtotal"><fmt:formatNumber value="${requestScope.price}" type="number" groupingUsed="true"/>đ</span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted small">Phí dịch vụ:</span>
                                    <span class="text-dark fw-medium">0đ</span>
                                </div>
                                <div class="d-flex justify-content-between mb-3 border-bottom pb-2">
                                    <span class="text-muted small">Giảm giá:</span>
                                    <span class="text-dark fw-medium"></span>
                                </div>
                                <div class="d-flex justify-content-between align-items-center mt-3">
                                    <span class="fw-bold text-dark">Tổng cộng:</span>
                                    <span class="fw-bold fs-4 text-primary" id="summary-total"><fmt:formatNumber value="${requestScope.price}" type="number" groupingUsed="true"/>đ</span>
                                </div>
                            </div>

                            <button class="btn btn-primary w-100 py-3 fw-bold rounded-3 shadow-sm d-flex align-items-center justify-content-center"
                                    id="playBtn"
                                    style="background-color: #1d61e7; border: none;">
                                <i class="bi bi-cash-stack me-2"></i> Thanh toán ngay
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--Footer-->
        <%@ include file="/WEB-INF/views/layout/footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            const discountRates = {
                month: 1,
                quarter: 0.9,
                year: 0.83
            };

            // 1. BỔ SUNG HÀM NÀY - Đây là hàm bạn đang thiếu
            function getBasePrice() {
                const selectedRadio = document.querySelector('input[name="vtype"]:checked');
                return selectedRadio ? parseFloat(selectedRadio.dataset.price) : 0;
            }

            // 2. Hàm định dạng tiền 
            function formatVND(amount) {
                return new Intl.NumberFormat('vi-VN', {
                    style: 'currency',
                    currency: 'VND',
                }).format(amount).replace(/₫/g, 'đ');
            }

            function updatePricingCards() {
                const basePrice = getBasePrice();
                if (!basePrice)
                    return;

                // Cập nhật giá lên 3 card chọn gói
                document.getElementById('price-month').innerText = (basePrice / 1000) + 'k';
                document.getElementById('price-quarter').innerText = ((basePrice * 3 * discountRates.quarter) / 1000).toLocaleString() + 'k';
                document.getElementById('price-year').innerText = ((basePrice * 12 * discountRates.year) / 1000).toLocaleString() + 'k';

                updateSummary();
            }
            function formatDate(date) {
                const d = date.getDate().toString().padStart(2, '0');
                const m = (date.getMonth() + 1).toString().padStart(2, '0');
                const y = date.getFullYear();
                return d + "/" + m + "/" + y;
            }

            function updateSummary() {
                const activeCard = document.querySelector('.pricing-card.active');
                const basePrice = getBasePrice();
                const licenseInput = document.getElementById("licensePlate");
                const licensePlate = licenseInput ? licenseInput.value : "";

                let total = 0, discount = 0, planLabel = "", durationType = "";

                // Xác định loại gói
                if (activeCard.innerText.includes('Vé tháng')) {
                    total = basePrice;
                    planLabel = "Vé tháng";
                    durationType = "month";
                } else if (activeCard.innerText.includes('Vé quý')) {
                    total = basePrice * 3 * discountRates.quarter;
                    discount = (basePrice * 3) - total;
                    planLabel = "Vé quý";
                    durationType = "quarter";
                } else {
                    total = basePrice * 12 * discountRates.year;
                    discount = (basePrice * 12) - total;
                    planLabel = "Vé năm";
                    durationType = "year";
                }

                // --- LOGIC TÍNH THỜI HẠN CHỐT CUỐI THÁNG ---
                const startDate = new Date();
                let endDate = new Date();

                if (durationType === "month") {
                    // Lấy ngày cuối cùng của tháng hiện tại
                    // (Tháng kế tiếp, ngày 0 => Tự động lùi về ngày cuối tháng trước)
                    endDate = new Date(startDate.getFullYear(), startDate.getMonth() + 1, 0);
                } else if (durationType === "quarter") {
                    // Hết quý (3 tháng tính từ tháng hiện tại)
                    endDate = new Date(startDate.getFullYear(), startDate.getMonth() + 3, 0);
                } else {
                    // Hết năm (12 tháng tính từ tháng hiện tại)
                    endDate = new Date(startDate.getFullYear(), startDate.getMonth() + 12, 0);
                }

                const dateRangeStr = formatDate(startDate) + " - " + formatDate(endDate);

                // Đổ dữ liệu ra UI
                const expiryElem = document.getElementById('summary-expiry');
                if (expiryElem)
                    expiryElem.innerText = dateRangeStr;

                // Cập nhật các nhãn khác
                const summaryLabels = document.querySelectorAll('.summary-info span.fw-bold');
                if (summaryLabels.length >= 2) {
                    summaryLabels[0].innerText = planLabel;
                    summaryLabels[1].innerText = licensePlate.toUpperCase() || "Chưa nhập";
                }

                document.getElementById('summary-subtotal').innerText = formatVND(total + discount);
                document.getElementById('summary-total').innerText = formatVND(total);

                const discountDisplay = document.querySelector('.border-bottom.pb-2 span:last-child');
                if (discountDisplay)
                    discountDisplay.innerText = "-" + formatVND(discount);
            }

            // --- Xử lý sự kiện ---
            const cards = document.querySelectorAll(".pricing-card");
            cards.forEach(card => {
                card.addEventListener('click', function () {
                    cards.forEach(c => {
                        c.classList.remove('active');
                        const badge = c.querySelector('.selected-badge');
                        if (badge)
                            badge.remove();
                    });
                    this.classList.add('active');

                    const radio = this.querySelector('input[name="planType"]');
                    radio.checked = true;
                    this.insertAdjacentHTML('afterbegin', '<div class="selected-badge"><i class="fa-solid fa-circle-check"></i></div>');
                    updateSummary();
                });
            });

            document.querySelectorAll('input[name="vtype"]').forEach(radio => {
                radio.addEventListener('change', updatePricingCards);
            });

            document.getElementById("licensePlate").addEventListener('input', updateSummary);

            // Khởi tạo
            window.onload = function () {
                updatePricingCards();
            };

            //Dùng API để gọi payment

            document.getElementById("playBtn").addEventListener("click", async function () {

                const btn = this;
                const contextPath = "${pageContext.request.contextPath}";
                const siteId = "${requestScope.site.siteId}";
                const vehicleId = document.querySelector('input[name="vtype"]:checked').value;
                const planType = document.querySelector('input[name="planType"]:checked').value;
                const licensePlate = document.getElementById("licensePlate").value;
                const vehicleRadio = document.querySelector('input[name="vtype"]:checked');
                const planRadio = document.querySelector('input[name="planType"]:checked');

                if (!licensePlate.trim()) {
                    showToast("error", "Vui lòng nhập biển số xe!");
                    return;
                }


                if (!vehicleRadio || !planRadio) {
                    showToast("error", "Vui lòng chọn loại xe và gói vé!");
                    return;
                }

                if (!licensePlate.trim()) {
                    showToast("error", "Vui lòng nhập biển số xe!");
                    return;
                }
                // Loading state
                const originalHTML = btn.innerHTML;
                btn.disabled = true;
                btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span> Đang xử lý...';

                try {

                    const response = await fetch(contextPath + "/api/payment?type=buying", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded"
                        },
                        body: new URLSearchParams({
                            siteId: siteId,
                            vehicleId: vehicleId,
                            planType: planType,
                            licensePlate: licensePlate
                        })
                    });

                    // Nếu servlet redirect (ví dụ session hết hạn)
                    if (response.redirected) {
                        window.location.href = response.url;
                        return;
                    }

                    const data = await response.json();

                    if (data.success) {

                        showToast("success", data.message || "Thanh toán thành công!");

                        // đợi user thấy toast
                        setTimeout(() => {
                            window.location.href = contextPath + "/sites?action=buying";
                        }, 2000);

                    } else {

                        showToast("error", data.message || "Mua vé thất bại!");

                        // enable lại nút
                        btn.disabled = false;
                        btn.innerHTML = originalHTML;
                    }

                } catch (err) {

                    console.error(err);
                    showToast("error", "Lỗi kết nối hệ thống!");

                    btn.disabled = false;
                    btn.innerHTML = originalHTML;
                }

            });
        </script>
    </body>
</html>
