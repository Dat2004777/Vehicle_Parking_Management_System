<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    /* ==================== SIDEBAR TRONG OFFCANVAS ==================== */
    .sidebar {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        background-color: #ffffff;
    }

    .sidebar-header {
        padding: 1.5rem;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .sidebar-header-left {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .brand-logo {
        width: 32px;
        height: 32px;
        background-color: #0d6efd; /* Đổi màu xanh primary Bootstrap chuẩn */
        color: white;
        border-radius: 8px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        font-weight: bold;
        font-size: 1.1rem;
    }

    .brand-text {
        font-size: 1.25rem;
        font-weight: 700;
        color: #0f172a;
    }

    .nav-section-title {
        font-size: 0.75rem;
        font-weight: 600;
        color: #94a3b8;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        padding: 1.5rem 1.5rem 0.5rem;
    }

    .sidebar-nav {
        padding: 0 1rem;
        flex-grow: 1;
        overflow-y: auto;
    }

    .nav-item-link {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 0.75rem 1rem;
        color: #64748b;
        text-decoration: none;
        border-radius: 8px;
        font-weight: 500;
        margin-bottom: 0.25rem;
        transition: all 0.2s;
    }

    .nav-item-link:hover {
        background-color: #f1f5f9;
        color: #0f172a;
    }

    .nav-item-link.active {
        background-color: #eff6ff;
        color: #0d6efd;
    }

    .user-profile-section {
        padding: 1rem;
        margin: 1rem;
        background-color: #f8fafc;
        border-radius: 12px;
        display: flex;
        align-items: center;
        gap: 12px;
        border: 1px solid #e2e8f0;
    }

    .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        object-fit: cover;
    }
</style>

<div class="offcanvas offcanvas-start border-0 shadow" tabindex="-1" id="sidebarOffcanvas" style="width: 280px;">
    <button type="button" class="btn-close position-absolute top-0 end-0 mt-4 me-3" 
            data-bs-dismiss="offcanvas" aria-label="Close" style="z-index: 1050;"></button>
    <div class="offcanvas-body p-0">

        <aside class="sidebar" id="sidebar">

            <div class="sidebar-header">
                <div class="sidebar-header-left">
                    <div class="brand-logo">P</div>
                    <span class="brand-text">ParkStaff</span>
                </div>
            </div>

            <div class="sidebar-nav">
                <a href="${ctx}/dashboard" class="nav-item-link ${param.activepage == 'dashboard' || empty param.activepage ? 'active' : ''}">
                    <i class="bi bi-grid-1x2"></i> Bảng điều khiển
                </a>

                <div class="nav-section-title">QUẢN LÝ</div>
                <a href="${ctx}/search" class="nav-item-link ${param.activepage == 'search' ? 'active' : ''}">
                    <i class="bi bi-search"></i> Truy vấn phương tiện
                </a>
                <a href="${ctx}/subscription" class="nav-item-link ${param.activepage == 'subscription' ? 'active' : ''}">
                    <i class="bi bi-people"></i> Quản lý vé tháng
                </a>
                <a href="${ctx}/parking/history" class="nav-item-link ${param.activepage == 'history' ? 'active' : ''}">
                    <i class="bi bi-clock-history"></i> Lịch sử 
                </a>
            </div>

            <div class="user-profile-section mt-auto">
                <img src="https://ui-avatars.com/api/?name=${account.username}&background=e2e8f0&color=334155" alt="Avatar" class="user-avatar">
                <div class="flex-grow-1 overflow-hidden">
                    <div class="fw-bold text-dark text-truncate" style="font-size: 0.875rem;">${account.username}</div>
                    <div class="text-muted text-truncate" style="font-size: 0.75rem;">Nhân viên trực bãi</div>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="text-muted ms-auto" aria-label="Đăng xuất"><i class="bi bi-box-arrow-right fs-5"></i></a>
            </div>

        </aside>

    </div>
</div>