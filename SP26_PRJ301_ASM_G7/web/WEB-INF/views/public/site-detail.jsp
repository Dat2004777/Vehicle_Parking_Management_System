<%-- 
    Document   : site-detail
    Created on : Feb 28, 2026, 7:03:59 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<c:set var="isDisabled" value="${requestScope.site.siteState ne 'OPERATING'}"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Site Detail Page</title>
        <!-- Google Fonts: Inter -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

        <!-- Bootstrap 5 CSS -->
        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <title>Site Detail Page</title>
        <style>
            :root {
                --bs-primary: #1a56db;
                --bs-primary-rgb: 26, 86, 219;
                --bs-font-sans-serif: 'Inter', sans-serif;
                --text-main: #374151;
                --text-muted: #6b7280;
                --bg-warning: #fff7ed;
                --text-warning: #c2410c;
            }

            body {
                font-family: var(--bs-font-sans-serif);
                color: var(--text-main);
                -webkit-font-smoothing: antialiased;
            }

            .card {
                border: none;
                border-radius: 12px;
                overflow: hidden;
            }

            .hero-section {
                background: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url('https://images.unsplash.com/photo-1506521781263-d8422e82f27a?auto=format&fit=crop&q=80&w=1000');
                background-size: cover;
                background-position: center;
                height: 250px;
                display: flex;
                flex-direction: column;
                justify-content: flex-end;
                padding: 20px;
                color: white;
            }

            .badge-feature {
                background-color: #f1f3f5;
                color: #495057;
                border-radius: 20px;
                padding: 8px 15px;
                font-size: 0.9rem;
                margin-right: 8px;
                margin-bottom: 8px;
                display: inline-block;
            }

            .map-placeholder {
                background: #e9ecef;
                border-radius: 12px;
                height: 200px;
                display: flex;
                align-items: center;
                justify-content: center;
                position: relative;
            }

            .price-summary {
                background-color: #f8f9fa;
                border-radius: 8px;
                padding: 15px;
            }

            .total-price {
                color: #0d6efd;
                font-weight: bold;
                font-size: 1.5rem;
            }

            .btn-confirm {
                background-color: #0d6efd;
                border: none;
                padding: 12px;
                border-radius: 8px;
                font-weight: 600;
                width: 100%;
            }

            .note-box {
                background-color: #e7f1ff;
                border-radius: 12px;
                padding: 15px;
                border-left: 5px solid #0d6efd;
            }

            /* Màu Cam - Cảnh báo (Warning) */
            .badge-warning-custom {
                background-color: #fff7ed;
                color: #c2410c;
                border: 2px solid #ffedd5;
                font-weight: 500;
                display: inline-block;
            }

            /* Màu Xanh lá - Thành công (Success) */
            .badge-success-custom {
                background-color: #f0fdf4;
                color: #15803d;
                border: 2px solid #dcfce7;
                font-weight: 500;
            }

            /* Màu Xanh dương hoặc Xám - Thông tin (Info/Secondary) 
               (Tôi sửa lại class bị trùng của bạn thành 'info') */
            .badge-info-custom {
                background-color: #f1f5f9;
                color: #475569;
                border: 2px solid #e2e8f0;
                font-weight: 500;
            }

            /* Màu Đỏ - Nguy hiểm/Lỗi (Danger) */
            .badge-danger-custom {
                background-color: #fef2f2;
                color: #b91c1c;
                border: 2px solid #fee2e2;
                font-weight: 500;
            }

            .custom-input-group .form-control {
                font-size: 0.9rem;
                border-color: #dee2e6;
            }
            .custom-input-group .form-control:focus {
                box-shadow: none;
                border-color: #dee2e6;
                background-color: #f8faff;
            }
            .custom-input-group .input-group-text {
                border-color: #dee2e6;
            }
            /* Loại bỏ viền xanh mặc định của Bootstrap khi focus để tạo cảm giác liền mạch */
            .custom-input-group:focus-within {
                ring: 1px solid #1d61e7;
                border-radius: 0.375rem;
            }
        </style>
    </head>
    <body class="bg-light">
        <!--Header-->
        <jsp:include page="/WEB-INF/views/layout/header.jsp">
            <jsp:param name="activePage" value="${param.action}"/>
        </jsp:include>


        <div class="container py-4">
            <nav aria-label="breadcrumb" class="mb-3">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/sites">Tìm kiếm</a></li>
                    <li class="breadcrumb-item">Đặt chỗ</li>
                </ol>
            </nav>

            <div class="row g-4">
                <div class="col-lg-8">
                    <div class="card shadow-sm mb-4">
                        <div class="hero-section">
                            <h2 class="fw-bold">${requestScope.site.siteName}</h2>
                            <p class="mb-0"><i class="bi bi-geo-alt-fill"></i> ${requestScope.site.address}</p>
                        </div>

                        <div class="card-body p-4">
                            <div class="row">
                                <div class="col-md-6 mb-4">
                                    <h5 class="fw-bold mb-3">Khu vực bãi để xe</h5>
                                    <div class="d-flex flex-wrap">
                                        <c:if test="${not empty vehicles}">
                                            <c:forEach var="v" items="${vehicles}">
                                                <span class="badge-feature">${v.vehicle.vehicleName.label}</span>
                                            </c:forEach>
                                        </c:if>
                                    </div>
                                    <h5 class="fw-bold mt-4 mb-3">Trạng thái hoạt động</h5>
                                    <p class="mb-1 text-muted"><i class="bi bi-clock"></i> Giờ mở cửa: <strong>06:00 - 23:00</strong></p>
                                    <c:choose>
                                        <c:when test="${requestScope.site.siteState eq 'OPERATING'}">
                                            <p class="mb-0 text-muted mt-3">
                                                <span class="badge-feature badge-success-custom p-2">${requestScope.site.siteState.label}</span>
                                            </p>
                                        </c:when>

                                        <c:when test="${requestScope.site.siteState eq 'MAINTENANCE'}">
                                            <p class="mb-0 text-muted mt-3">
                                                <span class="badge-feature badge-warning-custom p-2">${requestScope.site.siteState.label}</span>
                                            </p>
                                        </c:when>

                                        <c:otherwise>
                                            <p class="mb-0 text-muted mt-3">
                                                <span class="badge-feature badge-danger-custom p-2">${requestScope.site.siteState.label}</span>
                                            </p>
                                        </c:otherwise>
                                    </c:choose>

                                </div>

                                <div class="col-md-6">
                                    <h5 class="fw-bold mb-3">Vị trí trên bản đồ</h5>
                                    <div class="map-placeholder overflow-hidden">
                                        <c:url var="mapUrl" value="https://maps.google.com/maps">
                                            <c:param name="q" value="${requestScope.site.address}" />
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
                            </div>
                        </div>
                    </div>

                    <div class="note-box d-flex align-items-start shadow-sm">
                        <i class="bi bi-info-circle-fill text-primary me-3 fs-4"></i>
                        <div>
                            <h6 class="fw-bold">Lưu ý khi đặt chỗ</h6>
                            <p class="mb-0 small text-muted">Vui lòng đến đúng giờ đã đặt để đảm bảo vị trí đỗ xe của bạn.
                                Nếu bạn đến trễ quá 15 phút, hệ thống có thể tự động hủy đặt chỗ để nhường chỗ cho người
                                khác.</p>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <span class="text-danger d-block mt-2 text-center">${dateError}</span>
                    <div class="card shadow-sm p-4">
                        <form id="bookingForm" action="${pageContext.request.contextPath}/sites/site-detail?siteId=${requestScope.site.siteId}" method="post">
<!--                            <input type="hidden" name="siteId" value="${requestScope.site.siteId}"/>-->
                            <h5 class="fw-bold mb-1">Thiết lập đặt chỗ</h5>
                            <p class="text-muted small mb-4">Vui lòng chọn thời gian gửi xe</p>

                            <div class="mb-3">
                                <label class="form-label small fw-bold text-secondary">Giờ vào dự kiến</label>
                                <div class="input-group custom-input-group shadow-sm">
                                    <span class="input-group-text bg-white border-end-0">
                                        <i class="bi bi-box-arrow-in-right text-primary"></i>
                                    </span>
                                    <input id="dateIn" name="dateIn" type="date" class="form-control border-start-0 border-end-0 ps-0" required ${isDisabled ? "disabled" : ""}>
                                    <span class="input-group-text bg-white border-start-0 border-end-0 px-1 text-muted">|</span>
                                    <input name="timeIn" type="time" class="form-control border-start-0 ps-1" required ${isDisabled ? "disabled" : ""}>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label class="form-label small fw-bold text-secondary">Giờ ra dự kiến</label>
                                <div class="input-group custom-input-group shadow-sm">
                                    <span class="input-group-text bg-white border-end-0">
                                        <i class="bi bi-box-arrow-in-left text-danger"></i>
                                    </span>
                                    <input id="dateOut" name="dateOut" type="date" class="form-control border-start-0 border-end-0 ps-0" required ${isDisabled ? "disabled" : ""}>
                                    <span class="input-group-text bg-white border-start-0 border-end-0 px-1 text-muted">|</span>
                                    <input id="timeOut" name="timeOut" type="time" class="form-control border-start-0 ps-1" required ${isDisabled ? "disabled" : ""}>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label>Loại xe</label>
                                <select name="typeVehicle" class="form-select" id="vehicleSelect" ${isDisabled ? "disabled" : ""}>

                                    <c:forEach var="v" items="${vehicles}">
                                        <option value="${v.vehicle.vehicleTypeId}" data-price="${v.basePrice}">
                                            ${v.vehicle.vehicleName.label}
                                        </option>
                                    </c:forEach>

                                </select>
                            </div>
                            <div class="price-summary mb-4">
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Tổng thời gian</span>
                                    <span class="fw-bold" id="hours">0 giờ</span>
                                    <input type="hidden" id="hour" name="hour" />
                                </div>
                                <div class="progress mb-2" style="height: 6px;">
                                    <div class="progress-bar w-100" role="progressbar"></div>
                                </div>
                                <small class="text-muted d-block text-end">Tự động tính toán</small>
                            </div>

                            <div class="d-flex justify-content-between mb-2">
                                <span class="text-muted">Đơn giá</span>
                                <span class="fw-bold" id="displayPrice"></span>
                                <input type="hidden" id="price" name="price" />
                            </div>
                            <div class="d-flex justify-content-between mb-4">
                                <span class="text-muted">Phí dịch vụ</span>
                                <span class="text-success fw-bold">Miễn phí</span>
                            </div>

                            <hr>

                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <div>
                                    <span class="text-muted d-block">Tổng cộng</span>
                                    <span class="total-price" id="totalPriceText">0đ</span>
                                    <input type="hidden" id="totalPrice" name="totalPrice" >
                                </div>
                                <small class="text-muted">Đã bao gồm VAT</small>
                            </div>
                            <c:choose>
                                <c:when test="${requestScope.site.siteState eq 'OPERATING'}">
                                    <button type="submit" class="btn btn-primary btn-confirm shadow-sm">
                                        Xác nhận đặt chỗ <i class="bi bi-arrow-right ms-2"></i>
                                    </button>
                                </c:when>

                                <c:otherwise>
                                    <button type="submit" class="btn btn-primary btn-confirm shadow-sm" disabled>
                                        Xác nhận đặt chỗ <i class="bi bi-arrow-right ms-2"></i>
                                    </button>
                                </c:otherwise>
                            </c:choose>

                            <p class="text-center mt-3 x-small text-muted" style="font-size: 0.75rem;">
                                Bằng việc xác nhận, bạn đồng ý với <a href="#" class="text-decoration-none">Điều khoản sử
                                    dụng</a> và <a href="#" class="text-decoration-none">Chính sách hủy</a> của ParkEasy.
                            </p>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!--Footer-->
        <%@ include file="/WEB-INF/views/layout/footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            const today = new Date();
            const yyyy = today.getFullYear();
            const mm = String(today.getMonth() + 1).padStart(2, '0');
            const dd = String(today.getDate()).padStart(2, '0');

            const minDate = yyyy + '-' + mm + '-' + dd;
            console.log(minDate);

            // Set min cho input date
            document.getElementById("dateIn").setAttribute("min", minDate);
            document.getElementById("dateOut").setAttribute("min", minDate);

            const vehicleSelect = document.getElementById("vehicleSelect");
            const priceDisplay = document.getElementById("displayPrice");
            const hiddenPrice = document.getElementById("price");

            function updatePrice() {
                const selectedOption = vehicleSelect.options[vehicleSelect.selectedIndex];

                const basePrice = parseInt(selectedOption.getAttribute("data-price"));

                priceDisplay.innerText = basePrice.toLocaleString() + " đ";
                hiddenPrice.value = basePrice;

                calculate();
            }

            // Khi đổi loại xe
            vehicleSelect.addEventListener("change", updatePrice);

            // Khi load trang (để hiện giá mặc định)
            window.onload = updatePrice;

            function calculate() {
                const dateIn = document.getElementById("dateIn").value;
                const timeIn = document.getElementsByName("timeIn")[0].value;

                const dateOut = document.getElementById("dateOut").value;
                const timeOut = document.getElementsByName("timeOut")[0].value;

                if (!dateIn || !timeIn || !dateOut || !timeOut)
                    return;

                const start = new Date(dateIn);
                const end = new Date(dateOut);

                const [hourIn, minuteIn] = timeIn.split(":");
                const [hourOut, minuteOut] = timeOut.split(":");

                start.setHours(hourIn, minuteIn, 0);
                end.setHours(hourOut, minuteOut, 0);

                const diffMs = end.getTime() - start.getTime();
                const hours = Math.floor((diffMs / (1000 * 60 * 60)));

                if (hours > 0) {
                    const basePrice = parseFloat(document.getElementById("price").value) || 0;
                    const total = hours * basePrice;

                    document.getElementById("hours").innerText = hours + " giờ";
                    document.getElementById("totalPriceText").innerText =
                            total.toLocaleString() + " đ";

                    document.getElementById("hour").value = hours;
                    document.getElementById("totalPrice").value = total;
                }
            }

            document.querySelectorAll("input[type=date], input[type=time]")
                    .forEach(input => {
                        input.addEventListener("change", calculate);
                    });

//            document.getElementById("bookingForm").addEventListener("submit", function (e) {
//                e.preventDefault(); // chặn submit mặc định
//
//                const formData = new FormData(this);
//                const contextPath = ${pageContext.request.contextPath};
//                console.log(contextPath);
//                const siteId = ${requestScope.site.siteId};
//                fetch(contextPath + `/api/booking/preview?siteId=` + siteId, {
//                    method: "POST",
//                    body: formData
//                })
//                .then(res => res.json())
//                .then(data => {
//                    if (data.error) {
//                        document.getElementById("dateError").textContent = "Lỗi xác nhận dữ liệu";
//                        return;
//                    }
//
//                    // Chuyển sang trang confirm bằng JS
//                    localStorage.setItem("bookingPreview", JSON.stringify(data));
//                    window.location.href = "${pageContext.request.contextPath}/payment?action=booking&siteId=${requestScope.site.siteId}";
//
//                })
//                .catch(err => {
//                    alert("Lỗi hệ thống");
//                });
//            });
        </script>
    </body>
</html>
