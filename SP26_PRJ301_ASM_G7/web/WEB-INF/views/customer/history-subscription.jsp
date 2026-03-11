<%-- 
    Document   : history-subscription
    Created on : Mar 10, 2026, 8:07:11 PM
    Author     : ADMIN
--%>

<%-- 
    Document   : history-subscription
    Created on : Mar 10, 2026, 8:07:11 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <title>History Subscriptions</title>
        <style>
            :root {
                --bs-primary: #1a56db;
                --bs-primary-rgb: 26, 86, 219;
                --bs-font-sans-serif: 'Inter', sans-serif;
                --text-main: #374151;
                --text-muted: #6b7280;
            }

            body {
                font-family: var(--bs-font-sans-serif);
                color: var(--text-main);
                background-color: #f3f4f6; /* Màu nền nhẹ giúp Card nổi bật hơn */
                -webkit-font-smoothing: antialiased;
            }

            .password-wrapper{
                position: relative;
            }

            .toggle-password {
                position: absolute;
                right: 15px;
                top: 50%;
                transform: translateY(-50%);
                cursor: pointer;
                color: #9ca3af;
            }
            .plan-card{
                border:1px solid #e5e7eb;
                border-radius:12px;
                padding:18px;
                cursor:pointer;
                transition:all .2s ease;
                background:#fff;
            }

            .plan-card:hover{
                border-color:#1a56db;
                transform:translateY(-3px);
                box-shadow:0 6px 18px rgba(0,0,0,0.08);
            }

            .plan-card.active{
                border:2px solid #1a56db;
                background:#eff6ff;
            }

            .plan-card .price{
                font-size:20px;
                font-weight:700;
                color:#1a56db;
            }
        </style>
    </head>
    <body>
        <%@include file="/WEB-INF/views/layout/header.jsp" %>
        <%@include file="/WEB-INF/views/layout/customer-layout.jsp" %>
        <div class="container py-5">
            <div class="row g-4">
                <!--Side bar-->
                <jsp:include page="/WEB-INF/views/layout/customerSidebar.jsp">
                    <jsp:param name="activePage" value="history-subscriptions"/>
                </jsp:include>

                <!--Content-->  
                <div class="col-lg-9">
                    <!--History Tickets Tab-->
                    <div id="tickets">
                        <div class="card p-4 shadow-sm">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h5 class="fw-bold text-purple mb-0">Vé tháng hiện tại</h5>
                                <a href="${pageContext.request.contextPath}/sites?action=buying" class="btn btn-outline-primary btn-sm px-3">+ Đăng ký mới</a>
                            </div>
                            <c:choose>
                                <c:when test="${empty requestScope.subscriptions}">
                                    <div class="text-center py-5 border rounded bg-white shadow-sm">
                                        <div class="mb-3">
                                            <i class="bi bi-ticket-perforated text-muted" style="font-size: 3rem;"></i>
                                        </div>
                                        <h6 class="text-secondary fw-bold">Hiện tại bạn chưa đăng ký vé tháng nào!</h6>
                                        <p class="small text-muted mb-4">Đăng ký vé tháng để tiết kiệm chi phí và hưởng nhiều ưu đãi hơn.</p>
                                    </div>
                                </c:when>

                                <c:otherwise>
                                    <c:forEach items="${requestScope.subscriptions}" var="sub">

                                        <%-- TRƯỜNG HỢP: ĐANG HOẠT ĐỘNG --%>
                                        <c:if test="${sub.subscription.subState eq 'active'}">
                                            <form>
                                                <div class="card border border-primary p-3 mb-3 shadow-sm" style="border-width: 2px !important;">
                                                    <div class="row align-items-center">
                                                        <div class="col-12 col-md-8 mb-3 mb-md-0">
                                                            <h6 class="fw-bold">
                                                                ${sub.subscription.vehicleTypeId == 1 ? 'Vé Ô Tô' : 'Vé Xe Máy'} - Bãi xe ${sub.siteName}
                                                                <span class="ms-2 badge bg-success-subtle text-success fw-bold">Đang hoạt động</span>
                                                            </h6>
                                                            <p class="mb-1 small text-muted">
                                                                <i class="bi ${sub.subscription.vehicleTypeId == 1 ? 'bi-car-front' : 'bi-bicycle'}"></i> 
                                                                Biển số: ${sub.subscription.licensePlate}
                                                            </p>
                                                            <p class="mb-0 small text-muted">
                                                                <i class="bi bi-clock"></i> Hết hạn:
                                                                <span class="text-danger fw-bold">
                                                                    <fmt:parseDate value="${sub.subscription.endDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                                                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" />
                                                                </span>
                                                                (Còn ${sub.dayRemain} ngày)
                                                            </p>
                                                        </div>
                                                        <div class="col-12 col-md-4 text-md-end">
                                                            <a 
                                                                data-id="${sub.subscription.subscriptionId}"
                                                                data-site-id="${sub.siteId}"
                                                                data-site="${sub.siteName}"
                                                                data-plate="${sub.subscription.licensePlate}"
                                                                data-vehicle="${sub.subscription.vehicleTypeId == 1 ? 'Ô tô' : 'Xe máy'}"
                                                                data-enddate="<fmt:formatDate value='${parsedDate}' pattern='dd/MM/yyyy' />"
                                                                data-baseprice="${sub.basePrice}"
                                                                data-bs-toggle="modal"
                                                                data-bs-target="#renewModal"
                                                                class="btn btn-purple btn-sm w-md-auto renew-btn">
                                                                Gia hạn ngay
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </c:if>

                                        <%-- TRƯỜNG HỢP: HẾT HẠN --%>
                                        <c:if test="${sub.subscription.subState eq 'expired'}">
                                            <div class="card border p-3 shadow-sm bg-light">
                                                <div class="row align-items-center">
                                                    <div class="col-12 col-md-8 mb-3 mb-md-0">
                                                        <h6 class="fw-bold text-secondary">
                                                            ${sub.subscription.vehicleTypeId == 1 ? 'Vé Ô Tô' : 'Vé Xe Máy'} - Bãi xe ${sub.siteName}
                                                            <span class="ms-2 badge bg-danger-subtle text-danger fw-bold">Đã hết hạn</span>
                                                        </h6>
                                                        <p class="mb-1 small text-muted">
                                                            <i class="bi ${sub.subscription.vehicleTypeId == 1 ? 'bi-car-front' : 'bi-bicycle'}"></i>
                                                            Biển số: ${sub.subscription.licensePlate}
                                                        </p>
                                                        <p class="mb-0 small text-muted">
                                                            <i class="bi bi-clock"></i> Hết hạn: 
                                                            <fmt:parseDate value="${sub.subscription.endDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateExp" type="both" />
                                                            <fmt:formatDate value="${parsedDateExp}" pattern="dd/MM/yyyy" />
                                                        </p>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- ===== RENEW SUBSCRIPTION MODAL ===== -->
        <div class="modal fade" id="renewModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered modal-lg">
                <div class="modal-content border-0 shadow-lg">

                    <div class="modal-header border-0 pb-0">
                        <h5 class="modal-title fw-bold">
                            <i class="bi bi-arrow-repeat me-2 text-primary"></i>
                            Gia hạn vé tháng
                        </h5>
                        <button class="btn-close" data-bs-dismiss="modal"></button>
                    </div>

                    <div class="modal-body pt-2">

                        <!-- THÔNG TIN VÉ -->
                        <div class="bg-light rounded p-3 mb-4">

                            <div class="row g-3">

                                <div class="col-md-6">
                                    <small class="text-muted">Bãi xe</small>
                                    <div id="renewSite" class="fw-semibold"></div>
                                </div>

                                <div class="col-md-6">
                                    <small class="text-muted">Loại xe</small>
                                    <div id="renewVehicle" class="fw-semibold"></div>
                                </div>

                                <div class="col-md-6">
                                    <small class="text-muted">Biển số</small>
                                    <div id="renewPlate" class="fw-semibold"></div>
                                </div>

                                <div class="col-md-6">
                                    <small class="text-muted">Hết hạn hiện tại</small>
                                    <div id="renewEndDate" class="fw-bold text-danger"></div>
                                </div>

                            </div>

                        </div>

                        <!-- CHỌN GÓI -->
                        <h6 class="fw-bold mb-3">Chọn gói gia hạn</h6>

                        <div class="row g-3">

                            <!-- 1 MONTH -->
                            <div class="col-md-4">
                                <div class="plan-card active" data-plan="month" data-price="100000">

                                    <div class="fw-bold mb-2">
                                        <i class="bi bi-calendar"></i> 1 tháng
                                    </div>

                                    <div class="text-muted small mb-2">
                                        Gia hạn cơ bản
                                    </div>

                                    <div class="price">
                                    </div>

                                </div>
                            </div>

                            <!-- QUARTER -->
                            <div class="col-md-4">
                                <div class="plan-card" data-plan="quarter" data-price="210000">

                                    <div class="fw-bold mb-2">
                                        <i class="bi bi-calendar3"></i> 3 tháng
                                    </div>

                                    <div class="badge bg-success-subtle text-success mb-2">
                                        Tiết kiệm 10%
                                    </div>

                                    <div class="price">
                                    </div>

                                </div>
                            </div>

                            <!-- YEAR -->
                            <div class="col-md-4">
                                <div class="plan-card" data-plan="year" data-price="996000">

                                    <div class="fw-bold mb-2">
                                        <i class="bi bi-calendar4"></i> 12 tháng
                                    </div>

                                    <div class="badge bg-warning-subtle text-warning mb-2">
                                        Tiết kiệm 20%
                                    </div>

                                    <div class="price">
                                    </div>

                                </div>
                            </div>

                        </div>

                        <!-- TỔNG TIỀN -->
                        <div class="mt-4 p-3 rounded bg-primary-subtle d-flex justify-content-between align-items-center">

                            <span class="fw-semibold">
                                Tổng thanh toán
                            </span>

                            <span id="renewPrice"
                                  class="fw-bold fs-4 text-primary">
                            </span>

                        </div>

                    </div>

                    <div class="modal-footer border-0 pt-0">

                        <button class="btn btn-light px-4"
                                data-bs-dismiss="modal">
                            Huỷ
                        </button>

                        <button class="btn btn-primary px-4"
                                id="confirmRenew">
                            <i class="bi bi-check-circle me-1"></i>
                            Xác nhận gia hạn
                        </button>

                    </div>

                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            let selectedPlan = "month";
            let currentSubscriptionId = null;
            let currentBasePrice = 0;
            let currentSiteId = null;

            function formatVND(price) {
                return price.toLocaleString("vi-VN") + "đ";
            }

            function calculatePrice(plan) {

                if (plan === "month") {
                    return currentBasePrice;
                }

                if (plan === "quarter") {
                    return currentBasePrice * 3 * 0.9;
                }

                if (plan === "year") {
                    return currentBasePrice * 12 * 0.83;
                }

            }

            document.querySelectorAll(".renew-btn").forEach(btn => {

                btn.addEventListener("click", function () {

                    currentSubscriptionId = this.dataset.id;
                    currentBasePrice = parseInt(this.dataset.baseprice);
                    currentSiteId = this.dataset.siteId;

                    document.getElementById("renewSite").innerText = this.dataset.site;
                    document.getElementById("renewVehicle").innerText = this.dataset.vehicle;
                    document.getElementById("renewPlate").innerText = this.dataset.plate;
                    document.getElementById("renewEndDate").innerText = this.dataset.enddate;

                    document.querySelectorAll(".plan-card").forEach(card => {

                        let plan = card.dataset.plan;
                        let price = calculatePrice(plan);

                        card.querySelector(".price").innerText = formatVND(price);

                    });
                    // reset plan
                    selectedPlan = "month";

                    let price = calculatePrice("month");

                    document.getElementById("renewPrice").innerText =
                            formatVND(price);

                });

            });


            document.querySelectorAll(".plan-card").forEach(card => {

                card.addEventListener("click", function () {

                    document.querySelectorAll(".plan-card").forEach(c => c.classList.remove("active"));

                    this.classList.add("active");

                    selectedPlan = this.dataset.plan;

                    let price = calculatePrice(selectedPlan);

                    document.getElementById("renewPrice").innerText =
                            formatVND(price);

                });

            });

            document.getElementById("confirmRenew").addEventListener("click", async function () {

                const btn = this;
                const contextPath = "${pageContext.request.contextPath}";

                const subscriptionId = currentSubscriptionId;
                const planType = selectedPlan;

                // Loading state
                const originalHTML = btn.innerHTML;
                btn.disabled = true;
                btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span> Đang xử lý...';

                try {

                    const response = await fetch(contextPath + "/customer-info/history-subscriptions?action=renew", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded"
                        },
                        body: new URLSearchParams({
                            subscriptionId: subscriptionId,
                            siteId: currentSiteId,
                            planType: planType
                        })
                    });

                    const data = await response.json();

                    if (data.success) {
                        // đóng modal
                        const modal = bootstrap.Modal.getInstance(document.getElementById("renewModal"));
                        modal.hide();

                        showToast("success", data.message || "Gia hạn thành công!");

                        setTimeout(() => {
                            location.reload();
                        }, 1500);

                    } else {
                        // đóng modal
                        const modal = bootstrap.Modal.getInstance(document.getElementById("renewModal"));
                        modal.hide();
                        
                        showToast("error", data.message || "Gia hạn thất bại!");

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

