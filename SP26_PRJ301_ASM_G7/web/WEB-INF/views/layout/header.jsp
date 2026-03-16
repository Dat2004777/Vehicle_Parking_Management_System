<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" />
<style>

    /* --- Header / Navbar --- */
    .navbar {
        padding-top: 1rem;
        padding-bottom: 1rem;
        border-bottom: 1px solid #f3f4f6;
        background-color: #fff;
    }
    .navbar-brand .logo-icon {
        background-color: var(--bs-primary);
        color: white;
        width: 32px;
        height: 32px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        border-radius: 8px;
        font-weight: bold;
        font-size: 1.1rem;
        margin-right: 8px;
    }
    .navbar-brand span.text-dark {
        color: #111827 !important;
        font-weight: 700;
    }
    .nav-link {
        font-weight: 500;
        color: #4b5563;
        font-size: 0.95rem;
        padding: 0.5rem 1rem !important;
    }
    .nav-link:hover, .nav-link.active {
        color: var(--bs-primary);
    }
    .btn-login {
        color: #4b5563;
        font-weight: 600;
        text-decoration: none;
        margin-right: 1.5rem;
    }
    .btn-login:hover {
        color: var(--bs-primary);
    }
    .btn-register {
        font-weight: 600;
        padding: 0.5rem 1.25rem;
        border-radius: 6px;
    }

    /* Avatar chữ cái đầu */
    .avatar-circle {
        width: 40px;
        height: 40px;
        background-color: var(--bs-primary);
        color: white;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 600;
        font-size: 0.9rem;
        cursor: pointer;
    }

    /* Dropdown khi hover hoặc click */
    .user-menu-container {
        position: relative;
        display: inline-block;
    }

    .user-dropdown {
        display: none;
        position: absolute;
        top: 110%;
        right: 0;
        left: auto;   /* thêm dòng này */
        background: white;
        min-width: 200px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        border-radius: 8px;
        padding: 0.5rem 0;
        z-index: 1050;
    }

    .user-dropdown a {
        display: block;
        padding: 0.7rem 1.2rem;
        color: #374151;
        text-decoration: none;
        font-size: 0.9rem;
        transition: background 0.2s;
    }

    .user-dropdown a:hover {
        background-color: #f3f4f6;
        color: var(--bs-primary);
    }

    @media (max-width: 991px) {

        .user-menu-container {
            flex-direction: column;   /* QUAN TRỌNG */
        }

        .user-dropdown {
            position: relative;
            right: auto;
            left: 0;
            width: 100%;
            margin-top: 10px;
        }

    }

    /* Hiển thị dropdown khi hover vào container */
    .user-menu-container:hover .user-dropdown {
        display: block;
    }

    .user-name-text {
        margin-right: 10px;
        font-weight: 500;
        color: #4b5563;
    }

    .avatar-wrapper {
        cursor: pointer;
        padding: 5px;
        transition: all 0.3s ease;
    }

    .arrow-icon {
        font-size: 0.8rem;
        color: #6b7280;
        transition: transform 0.3s ease;
    }

    /* Khi hover vào container, mũi tên sẽ xoay 180 độ */
    .user-menu-container:hover .arrow-icon {
        transform: rotate(180deg);
        color: var(--bs-primary);
    }

    /* Căn chỉnh lại menu để không bị lệch */
    .user-dropdown {
        top: 110%; /* Đẩy xuống một chút so với header */
    }
</style>

<c:set var="currentPage" value="${param.activePage}" />

<!-- Header / Navbar -->
<nav class="navbar navbar-expand-lg sticky-top">
    <div class="container">
        <!-- Logo -->
        <a class="navbar-brand d-flex align-items-center" href="">
            <span class="logo-icon">P</span>
            <span class="text-dark fs-4">ParkEasy</span>
        </a>

        <!-- Mobile Toggle Button -->
        <button class="navbar-toggler border-0 shadow-none" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
                aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Nav Links & Auth Buttons -->
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mx-auto mb-2 mb-lg-0 gap-lg-3">
                <li class="nav-item">
                    <a class="nav-link ${currentPage == 'homePage' ? 'active' : ''}" href="${pageContext.request.contextPath}">Trang chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${currentPage == 'booking' ? 'active' : ''}" href="${pageContext.request.contextPath}/sites?action=booking">Đặt chỗ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${currentPage == 'buying' ? 'active' : ''}" href="${pageContext.request.contextPath}/sites?action=buying">Mua vé tháng</a>
                </li>
            </ul>
            <c:choose>
                <%-- TRƯỜNG HỢP CHƯA ĐĂNG NHẬP --%>
                <c:when test="${empty sessionScope.account}">
                    <div class="d-flex align-items-center mt-3 mt-lg-0">
                        <a href="${pageContext.request.contextPath}/login" class="btn-login">Đăng nhập</a>
                        <a href="${pageContext.request.contextPath}/signup" class="btn btn-primary btn-register">Đăng ký</a>
                    </div>
                </c:when>

                <%-- TRƯỜNG HỢP ĐÃ ĐĂNG NHẬP --%>
                <c:otherwise>
                    <div class="user-menu-container d-flex align-items-center">
                        <span class="user-name-text d-none d-lg-block">Xin chào, ${sessionScope.account.username}</span>

                        <a href="${pageContext.request.contextPath}/customer-info" class="avatar-wrapper d-flex align-items-center gap-2 text-decoration-none">
                            <div class="avatar-circle">
                                <c:out value="${fn:substring(sessionScope.customer.firstname, 0, 1)}${fn:substring(sessionScope.customer.lastname, 0, 1)}" />
                            </div>
                            <i class="fa-solid fa-angle-down arrow-icon"></i>
                        </a>

                        <div class="user-dropdown">
                            <div class="px-3 py-2 border-bottom mb-1">
                                <small class="text-muted d-block">Tài khoản</small>
                                <strong>${sessionScope.account.username}</strong>
                            </div>
                            <a href="${pageContext.request.contextPath}/customer-info"><i class="fa-regular fa-user me-2"></i> Hồ sơ</a>
                            <a href="${pageContext.request.contextPath}/logout" class="text-danger border-top mt-1">
                                <i class="fa-solid fa-right-from-bracket me-2"></i> Đăng xuất
                            </a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>
