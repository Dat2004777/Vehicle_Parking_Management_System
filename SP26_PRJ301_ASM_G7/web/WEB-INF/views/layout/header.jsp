<%@page contentType="text/html" pageEncoding="UTF-8"%>

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
</style>

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
                    <a class="nav-link active" href="">Trang chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Đặt chỗ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Mua vé tháng</a>
                </li>
            </ul>
            <div class="d-flex align-items-center mt-3 mt-lg-0">
                <a href="#" class="btn-login">Đăng nhập</a>
                <a href="#" class="btn btn-primary btn-register">Đăng ký</a>
            </div>
        </div>
    </div>
</nav>