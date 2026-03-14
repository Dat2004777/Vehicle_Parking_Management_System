<%-- 
    Document   : customerSidebar
    Created on : Mar 10, 2026, 8:10:16 PM
    Author     : ADMIN
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
    .sidebar-link.active {
        background-color: #eeefff !important;
        border-left: 4px solid #3f3d8f !important;
        color: #3f3d8f !important;
        font-weight: 600;
    }

    .btn-purple {
        background-color: #3f3d8f;
        color: #fff;
    }


    .btn-purple:hover {
        background-color: #2e2c6e;
        color: #fff;
    }

    /* Avatar chữ cái đầu */
    .profile-avatar {
        width: 80px;
        height: 80px;
        margin: 0 auto;
        background-color: var(--bs-primary);
        color: white;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 600;
        font-size: 2rem;
        box-shadow:
            0 0 0 4px rgba(255, 255, 255, 1),
            0 0 15px 5px rgba(99, 88, 238, 0.4);
    }

    .list-group-item {
        background-color: white;
    }
</style>

<!--Lấy giá trị để activePage-->
<c:set var="currentPage" value="${param.activePage}" />


<div class="col-lg-3">

    <!-- Profile Card -->
    <div class="card shadow-sm text-center p-4 mb-3">
        <h5 class="fw-bold mb-4 text-center fs-3" style="color: #111827;">Hồ sơ của tôi</h5>
        <div class="position-relative d-inline-block">
            <div class="profile-avatar">
                <c:out value="${fn:substring(sessionScope.customer.firstname, 0, 1)}${fn:substring(sessionScope.customer.lastname, 0, 1)}" />
            </div>
        </div>

        <h6 class="mt-3 mb-0 fw-bold mb-2">
            ${sessionScope.customer.firstname} ${sessionScope.customer.lastname}
        </h6>
        <span class="text-muted small">
            Số ví còn lại:
        </span>
        <div class="fw-bold text-danger">
            <fmt:formatNumber value="${sessionScope.customer.walletAmount}" type="number"/> VND
        </div>
    </div>

    <!-- Menu -->
    <div class="card shadow-sm">
        <div class="list-group list-group-flush">

            <a href="${pageContext.request.contextPath}/customer-info"
               class="list-group-item list-group-item-action sidebar-link
               ${currentPage == 'customer-info' ? 'active' : ''}">
                <i class="bi bi-person-circle me-2"></i> Thông tin cá nhân
            </a>

            <a href="${pageContext.request.contextPath}/customer-info/history-subscriptions"
               class="list-group-item list-group-item-action sidebar-link
               ${currentPage == 'history-subscriptions' ? 'active' : ''}">
                <i class="bi bi-ticket-perforated me-2"></i> Vé tháng của tôi
            </a>

            <a href="${pageContext.request.contextPath}/customer-info/history-bookings"
               class="list-group-item list-group-item-action sidebar-link
               ${currentPage == 'history-bookings' ? 'active' : ''}">
                <i class="bi bi-calendar-event me-2"></i> Lịch sử đặt chỗ
            </a>
            <!--
                        <a href="${pageContext.request.contextPath}/history-transactions"
                           class="list-group-item list-group-item-action sidebar-link
            ${currentPage == 'history-transactions' ? 'active' : ''}">
             <i class="bi bi-credit-card me-2"></i> Lịch sử thanh toán
         </a>-->

            <a href="${pageContext.request.contextPath}/logout"
               class="list-group-item list-group-item-action text-danger">
                <i class="bi bi-box-arrow-right me-2"></i> Đăng xuất
            </a>

        </div>
    </div>
</div>
