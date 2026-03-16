<%-- 
    Document   : history-bookings
    Created on : Mar 12, 2026, 7:49:27 PM
    Author     : ADMIN
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <title>History Booking Page</title>
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
                background-color: #f3f4f6;
                -webkit-font-smoothing: antialiased;
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
            .booking-detail {
                display: flex;
                flex-direction: column;
                gap: 16px;
            }

            .detail-row {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 10px 14px;
                background: #f9fafb;
                border-radius: 10px;
            }

            .detail-label {
                color: #6b7280;
                font-weight: 500;
            }

            .detail-value {
                color: #111827;
                font-weight: 500;
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
                    <jsp:param name="activePage" value="history-bookings"/>
                </jsp:include>

                <!--Content-->  
                <div class="col-lg-9">
                    <!--History Bookings Tab-->
                    <div id="bookings">
                        <div class="card p-4 shadow-sm">
                            <h5 class="fw-bold text-purple mb-4">Lịch sử Đặt chỗ</h5>
                            <div class="table-responsive">
                                <form>
                                    <table class="table align-middle">
                                        <thead>
                                            <tr>
                                                <th>Mã Booking</th>
                                                <th>Bãi xe</th>
                                                <th>Thời gian đến</th>
                                                <th>Trạng thái</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="h" items="${histories}">
                                                <tr>
                                                    <td class="fw-bold ${h.state eq 'upcoming' ? '' : 'text-muted'}">
                                                        BK-<fmt:formatNumber value="${h.booking.bookingId}" pattern="0000"/>
                                                    </td>
                                                    <td class="${h.state eq 'upcoming' ? '' : 'text-muted'}">
                                                        ${h.siteName}
                                                    </td>
                                                    <td class="${h.state eq 'upcoming' ? '' : 'text-muted'}">
                                                        ${h.booking.timeIn}
                                                    </td>
                                                    <c:choose>
                                                        <c:when test="${h.state eq 'upcoming'}">
                                                            <td>
                                                                <span class="ms-2 badge bg-warning-subtle text-warning fw-bold ">Sắp
                                                                    tới
                                                                </span>
                                                            </td>

                                                        </c:when>
                                                        <c:when test="${h.state eq 'completed'}">
                                                            <td>
                                                                <span class="ms-2 badge bg-success-subtle text-success fw-bold">Hoàn
                                                                    thành
                                                                </span>
                                                            </td>
                                                        </c:when>
                                                    </c:choose>
                                                    <td>
                                                        <button  type="button"
                                                                 class="btn btn-outline-secondary btn-sm btn-detail"
                                                                 data-id="${h.booking.bookingId}"
                                                                 data-site="${h.siteName}"
                                                                 data-time="${h.booking.timeIn}"
                                                                 data-amount="${h.booking.bookingAmount}"
                                                                 data-state="${h.state eq 'upcoming' ? 'Sắp tới' : 'Hoàn thành'}"
                                                                 data-bs-toggle="modal"
                                                                 data-bs-target="#bookingDetailModal">
                                                            Chi tiết
                                                        </button>
                                                    </td> 
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--Pop Up-->
        <div class="modal fade" id="bookingDetailModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content shadow-lg border-0 rounded-4">

                    <!-- Header -->
                    <div class="modal-header border-0 pb-0">
                        <h5 class="modal-title fw-bold text-primary">
                            <i class="bi bi-ticket-perforated me-2"></i>
                            Chi tiết Booking
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>

                    <!-- Body -->
                    <div class="modal-body pt-2">

                        <div class="booking-detail">

                            <div class="detail-row">
                                <span class="detail-label">Mã Booking</span>
                                <span class="detail-value fw-bold" id="mBookingId"></span>
                            </div>

                            <div class="detail-row">
                                <span class="detail-label">Bãi xe</span>
                                <span class="detail-value" id="mSite"></span>
                            </div>

                            <div class="detail-row">
                                <span class="detail-label">Thời gian đến</span>
                                <span class="detail-value" id="mTime"></span>
                            </div>

                            <div class="detail-row">
                                <span class="detail-label">Số tiền</span>
                                <span class="detail-value fw-bold fs-4 text-primary" id="mAmount"></span>
                            </div>

                            <div class="detail-row">
                                <span class="detail-label">Trạng thái</span>
                                <span class="badge rounded-pill px-3 py-2"
                                      id="mStateBadge"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--Footer-->
        <%@ include file="/WEB-INF/views/layout/footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.querySelectorAll(".btn-detail").forEach(btn => {

                btn.addEventListener("click", function () {

                    document.getElementById("mBookingId").innerText =
                            "BK-" + this.dataset.id;

                    document.getElementById("mSite").innerText =
                            this.dataset.site;

                    document.getElementById("mTime").innerText =
                            formatTime(this.dataset.time);

                    document.getElementById("mAmount").innerText =
                            formatVND(this.dataset.amount);

                    let badge = document.getElementById("mStateBadge");
                    let state = this.dataset.state;

                    badge.innerText = state;

                    if (state === "Hoàn thành") {
                        badge.className = "badge bg-success rounded-pill px-3 py-2";
                    } else {
                        badge.className = "badge bg-warning text-dark rounded-pill px-3 py-2";
                    }
                });
            });

            function formatVND(amount) {
                return Number(amount).toLocaleString('vi-VN') + " VND";
            }
            function formatTime(timeStr) {

                // đổi T thành space
                let formatted = timeStr.replace("T", " ");

                // tách date và time
                let parts = formatted.split(" ");

                let date = parts[0];
                let time = parts[1];

                // bỏ số 0 ở đầu giờ
                let [hour, minute] = time.split(":");
                hour = parseInt(hour);

                return date + " " + hour + ":" + minute;
            }
        </script>
    </body>
</html>
