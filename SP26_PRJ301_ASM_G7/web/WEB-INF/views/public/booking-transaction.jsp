<%-- 
    Document   : booking-transaction
    Created on : Feb 28, 2026, 10:29:46 PM
    Author     : ADMIN
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <title>Booking Confirmation Page</title>
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

            .badge-warning-custom {
                background-color: #fff7ed;
                color: #c2410c;
                border: 2px solid #ffedd5;
                padding: 8px 16px;
                font-weight: 500;
            }

            .badge-success-custom {
                background-color: #f0fdf4;
                color: #15803d;
                border: 2px solid #dcfce7;
                padding: 8px 16px;
                font-weight: 500;
            }

            .badge-warning-custom {
                background-color: #f1f5f9;
                color: #64748b;
                border: 2px solid #e2e8f0;
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
        </style>
    </head>
    <body class="bg-light">
        <!--Header-->
        <%@include file="/WEB-INF/views/layout/header.jsp" %>
        
        <!--Layout-->
        <%@include file="/WEB-INF/views/layout/customer-layout.jsp" %>
        <div class="container py-4">
            <nav aria-label="breadcrumb" class="mb-3">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/sites">Tìm kiếm</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/sites">Đặt chỗ</a></li>
                    <li class="breadcrumb-item">Xác nhận</li>
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
                                                <span class="badge-feature">${v.vehicle.vehicleName}</span>
                                            </c:forEach>
                                        </c:if>
                                    </div>

                                    <h5 class="fw-bold mt-4 mb-3">Trạng thái hoạt động</h5>
                                    <p class="mb-1 text-muted"><i class="bi bi-clock"></i> Giờ mở cửa: <strong>06:00 -
                                            23:00</strong></p>
                                            <c:choose>
                                                <c:when test="${requestScope.site.siteStatus eq 'OPERATING'}">
                                            <p class="mb-0 text-muted mt-3">
                                                <span class="badge-feature badge-success-custom p-2">${requestScope.site.siteStatus.label}</span>
                                            </p>
                                        </c:when>

                                        <c:when test="${requestScope.site.siteStatus eq 'MAINTENANCE'}">
                                            <p class="mb-0 text-muted mt-3">
                                                <span class="badge-feature badge-warning-custom p-2">${requestScope.site.siteStatus.label}</span>
                                            </p>
                                        </c:when>

                                        <c:otherwise>
                                            <p class="mb-0 text-muted mt-3">
                                                <span class="badge-feature badge-danger-custom p-2">${requestScope.site.siteStatus.label}</span>
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
                    <form>

                        <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                            <div class="card-header bg-light bg-opacity-50 border-0 py-3 px-4">
                                <h5 class="fw-bold mb-0 d-flex align-items-center" style="color: #1a2b4b;">
                                    <i class="bi bi-cart-fill me-2 text-primary"></i> Tóm tắt đơn hàng
                                </h5>
                            </div>

                            <div class="card-body p-4">
                                <div class="mb-4">
                                    <div class="d-flex justify-content-between mb-3">
                                        <span class="text-muted">Giờ vào:</span>
                                        <span class="fw-bold text-dark">${requestScope.inDateTime}</span>
                                    </div>
                                    <div class="d-flex justify-content-between mb-3">
                                        <span class="text-muted">Giờ ra:</span>
                                        <span class="fw-bold text-dark">${requestScope.outDateTime}</span>
                                    </div>
                                    <div class="d-flex justify-content-between">
                                        <span class="text-muted">Thời hạn:</span>
                                        <span class="fw-bold text-dark">${requestScope.hours} giờ</span>
                                    </div>
                                </div>

                                <hr class="border-secondary opacity-10 mb-4" style="border-style: dashed;">

                                <div class="rounded-3 p-3 mb-4" style="background-color: #f8faff;">
                                    <div class="d-flex justify-content-between mb-2">
                                        <span class="text-muted">Tạm tính:</span>
                                        <span class="text-dark">
                                            <fmt:formatNumber value="${requestScope.price}" type="number" groupingUsed="true"/> đ
                                        </span>
                                    </div>
                                    <div class="d-flex justify-content-between mb-3 border-bottom pb-2">
                                        <span class="text-muted">Phí dịch vụ:</span>
                                        <span class="text-dark">0đ</span>
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center mt-3">
                                        <span class="fw-bold fs-5 text-primary">Tổng cộng:</span>
                                        <span class="fw-bold fs-3 text-primary">
                                            <fmt:formatNumber value="${requestScope.totalPrice}" type="number" groupingUsed="true"/> đ
                                        </span>
                                    </div>
                                </div>

                                <button
                                    type="button"
                                    id="playBtn"
                                    class="btn btn-primary w-100 py-3 fw-bold rounded-3 shadow-sm d-flex align-items-center justify-content-center"
                                    style="background-color: #1d61e7; border: none;">
                                    <i class="bi bi-cash-stack me-2"></i> Thanh toán ngay
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!--Footer-->
        <%@ include file="/WEB-INF/views/layout/footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.getElementById("playBtn").addEventListener("click", async function () {

                const btn = this;
                const contextPath = "${pageContext.request.contextPath}";
                const siteId = "${requestScope.site.siteId}";

                // Loading state
                const originalHTML = btn.innerHTML;
                btn.disabled = true;
                btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span> Đang xử lý...';

                try {

                    const response = await fetch(contextPath + "/api/payment?type=booking", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded"
                        },
                        body: new URLSearchParams({
                            siteId: siteId
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
                            window.location.href = contextPath + "/sites?action=booking";
                        }, 2000);

                    } else {

                        showToast("error", data.message || "Đặt chỗ thất bại!");

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
