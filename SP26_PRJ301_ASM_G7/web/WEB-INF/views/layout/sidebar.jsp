<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
    /* ==================== SIDEBAR ==================== */
    .sidebar {
        width: var(--sidebar-width);
        background-color: #ffffff;
        border-right: 1px solid #e2e8f0;
        height: 100vh;
        position: fixed;
        top: 0;
        left: 0;
        z-index: 1040;
        transition: transform 0.3s ease-in-out;
        display: flex;
        flex-direction: column;
    }

    .sidebar-header {
        padding: 1.5rem;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .brand-logo {
        width: 32px;
        height: 32px;
        background-color: var(--bs-primary);
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
        color: var(--text-muted);
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
        color: var(--bs-primary);
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

<!-- Overlay for mobile sidebar -->
<div class="sidebar-overlay" id="sidebarOverlay"></div>

<!-- Sidebar -->
<aside class="sidebar" id="sidebar">
    <div class="sidebar-header">
        <div class="brand-logo">P</div>
        <span class="brand-text">ParkAdmin</span>
    </div>

    <div class="sidebar-nav">
        <a href="${ctx}/dashboard" class="nav-item-link active">
            <i class="bi bi-grid-1x2"></i> Bảng điều khiển
        </a>

        <div class="nav-section-title">QUẢN LÝ</div>
        <a href="${ctx}/site" class="nav-item-link">
            <i class="bi bi-car-front"></i> Quản lý bãi xe
        </a>
        <a href="#" class="nav-item-link">
            <i class="bi bi-people"></i> Nhân viên
        </a>
        <a href="#" class="nav-item-link">
            <i class="bi bi-card-heading"></i> Vé tháng
        </a>

        <div class="nav-section-title">BÁO CÁO</div>
        <a href="#" class="nav-item-link">
            <i class="bi bi-bar-chart"></i> Phân tích
        </a>
        <a href="#" class="nav-item-link">
            <i class="bi bi-receipt"></i> Giao dịch
        </a>
    </div>

    <div class="user-profile-section mt-auto">
        <img src="https://ui-avatars.com/api/?name=Alex+Morgan&background=e2e8f0&color=334155" alt="Avatar" class="user-avatar">
        <div class="flex-grow-1 overflow-hidden">
            <div class="fw-bold text-dark text-truncate" style="font-size: 0.875rem;">${account.username}</div>
            <div class="text-muted text-truncate" style="font-size: 0.75rem;">Quản trị viên cấp cao</div>
        </div>
        <a href="#" class="text-muted ms-auto" aria-label="Đăng xuất"><i class="bi bi-box-arrow-right fs-5"></i></a>
    </div>
</aside>