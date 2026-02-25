<%@page contentType="text/html" pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-lg navbar-white bg-white fixed-top px-3 px-md-4 border-bottom" style="height: 70px;">
    <div class="container-fluid p-0 d-flex justify-content-between align-items-center">
        <div class="d-flex align-items-center gap-3">
            <button class="btn btn-light border-0 shadow-sm" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarOffcanvas">
                <i class="bi bi-list fs-4"></i>
            </button>
            <div class="d-none d-sm-block">
                <div class="fw-bold fs-5 text-dark" style="line-height: 1.2;">${overview.siteName}</div>
            </div>
        </div>

        <div class="d-flex align-items-center gap-3 border-start ps-4">
            <div class="text-end d-none d-lg-block">
                <div class="fw-bold text-dark" style="font-size: 0.95rem;">${sessionScope.user.fullName != null ? sessionScope.user.fullName : 'Nhân viên'}</div>
            </div>
            <img src="https://ui-avatars.com/api/?name=Staff&background=eff6ff&color=2563eb" alt="Avatar" class="rounded-circle" width="40" height="40">
        </div>
    </div>
</nav>