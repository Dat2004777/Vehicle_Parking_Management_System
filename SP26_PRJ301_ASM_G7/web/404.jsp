<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>404 - Trang không tồn tại</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet"/>
    
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #fafbfc; /* Màu nền xám trắng siêu nhạt */
            color: #1e293b;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        /* Navbar Tùy chỉnh */
        .navbar-custom {
            background-color: transparent;
            padding: 1rem 0;
        }
        .nav-link-custom {
            color: #475569;
            font-weight: 500;
            font-size: 0.95rem;
            padding: 0.5rem 1rem;
            transition: color 0.2s;
        }
        .nav-link-custom:hover {
            color: #2563eb;
        }

        /* Khối Graphic 404 CSS */
        .graphic-404-box {
            background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
            border-radius: 24px;
            width: 100%;
            max-width: 480px;
            height: 220px;
            margin: 0 auto;
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            overflow: hidden;
            box-shadow: inset 0 2px 4px rgba(255, 255, 255, 0.8), 0 4px 10px rgba(0,0,0,0.02);
        }
        .text-404 {
            font-size: 8rem;
            font-weight: 800;
            color: #e2e8f0;
            letter-spacing: -2px;
            line-height: 1;
            position: relative;
            z-index: 1;
        }
        .icon-car-404 {
            position: absolute;
            top: 20%;
            font-size: 4rem;
            color: #cbd5e1;
            opacity: 0.5;
            z-index: 0;
        }
        .icon-ban-404 {
            position: absolute;
            font-size: 3.5rem;
            color: #94a3b8;
            z-index: 2;
            bottom: 25%;
        }

        /* Nút bấm tùy chỉnh */
        .btn-custom-outline {
            background-color: #ffffff;
            color: #334155;
            border: 1px solid #e2e8f0;
            transition: all 0.2s;
        }
        .btn-custom-outline:hover {
            background-color: #f8fafc;
            border-color: #cbd5e1;
        }

        /* Liên kết hỗ trợ phía dưới */
        .footer-links a {
            color: #64748b;
            text-decoration: none;
            font-size: 0.85rem;
            font-weight: 500;
            transition: color 0.2s;
        }
        .footer-links a:hover {
            color: #2563eb;
        }
        
        .footer-text {
            color: #94a3b8;
            font-size: 0.85rem;
        }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-custom container px-4 mt-2">
        <a class="navbar-brand d-flex align-items-center gap-2 fw-bold text-dark fs-5" href="#">
            <span class="text-primary fs-4 d-flex"><i class="bi bi-p-square-fill"></i></span>
            ParkEasy
        </a>
        
        <button class="navbar-toggler border-0 shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <i class="bi bi-list fs-2"></i>
        </button>

        <div class="collapse navbar-collapse justify-content-center" id="navbarContent">
            <div class="navbar-nav gap-2 gap-lg-4 my-3 my-lg-0 text-center">
                <a class="nav-link-custom text-decoration-none" href="#">Trang chủ</a>
                <a class="nav-link-custom text-decoration-none" href="#">Dịch vụ</a>
                <a class="nav-link-custom text-decoration-none" href="#">Bản đồ</a>
                <a class="nav-link-custom text-decoration-none" href="#">Hỗ trợ</a>
            </div>
            
            <div class="d-lg-none mt-2 text-center">
                <a href="#" class="btn btn-primary px-4 rounded-3 fw-medium w-100">Đăng nhập</a>
            </div>
        </div>
        
        <div class="d-none d-lg-block ms-auto">
            <a href="#" class="btn btn-primary px-4 py-2 rounded-3 fw-medium">Đăng nhập</a>
        </div>
    </nav>

    <main class="flex-grow-1 d-flex align-items-center justify-content-center py-5">
        <div class="container text-center px-3">
            
            <div class="graphic-404-box mb-5">
                <i class="bi bi-car-front-fill icon-car-404"></i>
                <div class="text-404">4<span style="opacity: 0;">0</span>4</div>
                <i class="bi bi-slash-circle-fill icon-ban-404"></i>
            </div>

            <h1 class="fw-bold mb-3 text-dark" style="font-size: 2.25rem;">Rất tiếc! Trang bạn tìm kiếm không tồn tại</h1>
            <p class="text-muted mx-auto mb-5" style="max-width: 500px; font-size: 1.05rem; line-height: 1.6;">
                Có vẻ như bạn đã đi lạc vào một khu vực đỗ xe không có thực. Hãy để chúng tôi đưa bạn trở lại lộ trình chính xác.
            </p>

            <div class="d-flex flex-column flex-sm-row justify-content-center gap-3 mb-5">
                <a href="#" class="btn btn-primary px-4 py-3 rounded-3 fw-medium d-flex justify-content-center align-items-center gap-2 shadow-sm">
                    <i class="bi bi-house-door-fill"></i> Quay lại Trang chủ
                </a>
                <a href="#" class="btn btn-custom-outline px-4 py-3 rounded-3 fw-medium d-flex justify-content-center align-items-center gap-2 shadow-sm">
                    <i class="bi bi-headset"></i> Liên hệ hỗ trợ
                </a>
            </div>

            <div class="footer-links d-flex flex-wrap justify-content-center gap-4">
                <a href="#"><i class="bi bi-map"></i> Bản đồ bãi đỗ</a>
                <a href="#"><i class="bi bi-question-circle"></i> Câu hỏi thường gặp</a>
                <a href="#"><i class="bi bi-search"></i> Tìm kiếm vị trí</a>
            </div>

        </div>
    </main>

    <footer class="py-4 mt-auto text-center border-top">
        <div class="container">
            <p class="footer-text mb-0">© 2024 Hệ thống Quản lý Đỗ xe Thông minh. Bảo lưu mọi quyền.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>