<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Không tìm thấy trang | Smart Parking</title>
    
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        :root {
            --bs-primary: #1a56db;
            --bs-body-bg: #f8fafc;
            --text-main: #334155;
        }
        body {
            font-family: 'Inter', sans-serif;
            background-color: var(--bs-body-bg);
            color: var(--text-main);
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
            overflow: hidden; /* Ngăn cuộn trang */
        }
        .error-container {
            text-align: center;
            max-width: 500px;
            padding: 2rem;
            animation: fadeIn 0.5s ease-in-out;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }
        .error-icon {
            font-size: 5.5rem;
            color: #f59e0b; /* Màu cam cảnh báo giống cọc tiêu */
            margin-bottom: 0.5rem;
            filter: drop-shadow(0 10px 8px rgba(245, 158, 11, 0.2));
        }
        .error-code {
            font-size: 7rem;
            font-weight: 800;
            line-height: 1;
            color: #cbd5e1; /* Màu xám nhạt để làm nền */
            margin-bottom: 1rem;
            letter-spacing: -2px;
        }
        .btn-home {
            background-color: var(--bs-primary);
            color: #fff;
            font-weight: 600;
            padding: 0.75rem 1.5rem;
            border-radius: 10px;
            transition: all 0.2s;
            text-decoration: none;
        }
        .btn-home:hover {
            background-color: #1e40af;
            color: #fff;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(26, 86, 219, 0.3);
        }
        .btn-back {
            background-color: #fff;
            color: #64748b;
            border: 1px solid #e2e8f0;
            font-weight: 500;
            padding: 0.75rem 1.5rem;
            border-radius: 10px;
            transition: all 0.2s;
        }
        .btn-back:hover {
            background-color: #f1f5f9;
            color: #334155;
        }
    </style>
</head>
<body>

    <div class="error-container">
        <div class="error-icon">
            <i class="bi bi-cone-striped"></i>
        </div>
        
        <h1 class="error-code">404</h1>
        <h3 class="fw-bold mb-3 text-dark">Lạc đường rồi tài xế ơi!</h3>
        <p class="text-muted mb-4" style="font-size: 0.95rem; line-height: 1.6;">
            Khu vực bạn đang tìm kiếm không tồn tại, đã bị di dời hoặc bạn không có quyền truy cập vào bãi đỗ này.
        </p>
        
        <div class="d-flex flex-column flex-sm-row justify-content-center gap-3">
            <a href="${pageContext.request.contextPath}/" class="btn-home d-inline-flex align-items-center justify-content-center gap-2">
                <i class="bi bi-house-door-fill"></i> Về bảng điều khiển
            </a>
            
            <button onclick="history.back()" class="btn-back d-inline-flex align-items-center justify-content-center gap-2">
                <i class="bi bi-arrow-left"></i> Quay lại bước trước
            </button>
        </div>
    </div>

</body>
</html>