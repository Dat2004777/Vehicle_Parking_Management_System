<%-- 
    Document   : add (Employee)
    Created on : 24 Feb 2026, 00:25:29
    Author     : Admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm nhân viên mới - ParkEasy</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <style>
            :root {
                --bs-primary: #3b82f6; /* Đồng bộ màu xanh với danh sách */
                --bs-primary-rgb: 59, 130, 246;
                --bg-body: #f8fafc;
                --bg-sidebar: #ffffff;
                --sidebar-width: 260px; /* Đồng bộ độ rộng sidebar */
                --text-main: #334155;
                --text-dark: #0f172a;
                --text-muted: #64748b;
                --border-color: #e2e8f0;
            }

            body {
                font-family: 'Inter', sans-serif;
                background-color: var(--bg-body);
                color: var(--text-main);
                overflow-x: hidden;
            }

            /* ==================== MAIN CONTENT ==================== */
            .main-content {
                margin-left: var(--sidebar-width);
                min-height: 100vh;
                transition: margin-left 0.3s ease-in-out;
                display: flex;
                flex-direction: column;
            }

            /* Top Header (Giống hệt list.jsp) */
            .top-header {
                height: 80px;
                background-color: #ffffff;
                border-bottom: 1px solid var(--border-color);
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 0 2rem;
                position: sticky;
                top: 0;
                z-index: 1030;
            }

            .header-title-link {
                color: var(--text-dark);
                text-decoration: none;
                display: flex;
                align-items: center;
                gap: 1rem;
                font-size: 1.25rem;
                font-weight: 700;
                transition: color 0.2s;
                margin: 0;
            }
            .header-title-link:hover {
                color: var(--bs-primary);
            }
            .header-title-link i {
                color: var(--text-muted);
            }

            /* Content Area */
            .content-area {
                padding: 2.5rem 2rem;
                flex-grow: 1;
                max-width: 900px;
                width: 100%;
                margin: 0 auto;
            }

            .page-title {
                font-size: 1.5rem;
                font-weight: 700;
                color: var(--text-dark);
                margin-bottom: 0.5rem;
            }
            .page-subtitle {
                color: var(--text-muted);
                font-size: 0.95rem;
                margin-bottom: 2rem;
            }
            .page-title-header{
                font-size: 1.25rem;
                font-weight: 700;
                color: #0f172a;
                margin: 0;
            }

            /* --- Cards & Form Styling --- */
            .custom-card {
                background-color: #ffffff;
                border-radius: 12px;
                border: 1px solid var(--border-color);
                padding: 2rem;
                margin-bottom: 1.5rem;
                box-shadow: 0 1px 2px rgba(0,0,0,0.01); /* Thêm box-shadow nhẹ cho đồng bộ */
            }

            .form-label {
                font-size: 0.875rem;
                font-weight: 600;
                color: #334155;
                margin-bottom: 0.5rem;
            }

            .form-control, .form-select {
                border: 1px solid #cbd5e1;
                border-radius: 8px;
                padding: 0.6rem 1rem;
                font-size: 0.95rem;
                color: #0f172a;
                transition: all 0.2s;
                box-shadow: none !important;
            }

            .form-control::placeholder {
                color: #94a3b8;
            }
            .form-control:focus, .form-select:focus {
                border-color: var(--bs-primary);
                box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1) !important; /* Hiệu ứng focus chuẩn */
            }

            /* Upload Zone */
            .avatar-upload-zone {
                border: 2px dashed #cbd5e1;
                border-radius: 12px;
                background-color: #f8fafc;
                height: 130px;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                cursor: pointer;
                transition: all 0.2s;
                color: #64748b;
            }
            .avatar-upload-zone:hover {
                border-color: var(--bs-primary);
                background-color: #eff6ff;
                color: var(--bs-primary);
            }
            .upload-hint {
                font-size: 0.75rem;
                color: #94a3b8;
                text-align: center;
                margin-top: 0.5rem;
                line-height: 1.4;
            }

            /* Section Title (Security) */
            .section-title-sm {
                font-size: 0.75rem;
                font-weight: 700;
                color: #94a3b8;
                text-transform: uppercase;
                letter-spacing: 0.05em;
                margin-bottom: 1.25rem;
            }

            /* Password Input Wrapper */
            .password-wrapper {
                position: relative;
            }
            .password-wrapper .form-control {
                padding-right: 2.5rem;
            }
            .password-wrapper i {
                position: absolute;
                right: 1rem;
                top: 50%;
                transform: translateY(-50%);
                color: #94a3b8;
                cursor: pointer;
                transition: color 0.2s;
            }
            .password-wrapper i:hover {
                color: #475569;
            }

            /* Action Buttons Area */
            .action-footer {
                display: flex;
                justify-content: flex-end;
                align-items: center;
                gap: 1rem;
                margin-top: 2rem;
                padding-bottom: 2rem;
            }
            .btn-delete {
                background-color: #ef4444; /* Đỏ tươi (Red-500) */
                border: none;
                color: white;
                font-weight: 600;
                padding: 0.6rem 1.5rem;
                border-radius: 8px;
                transition: all 0.2s;
                cursor: pointer;
            }

            .btn-delete:hover {
                color: #fef3c7;
                background-color: #dc2626; /* Đỏ đậm hơn khi di chuột (Red-600) */
                box-shadow: 0 4px 6px -1px rgba(220, 38, 38, 0.2); /* Đổ bóng đỏ nhẹ */
            }

            /* Thêm hiệu ứng khi nhấn vào (Active) */
            .btn-delete:active {
                background-color: #b91c1c;
                transform: scale(0.98);
            }
            /* Định dạng nút Hủy giống trang Chi tiết bãi xe */
            .btn-cancel {
                background-color: #ffffff;
                border: 1px solid #cbd5e1;
                color: #475569;
                font-weight: 600;
                text-decoration: none;
                padding: 0.6rem 1.5rem;
                border-radius: 8px;
                transition: all 0.2s;
            }
            .btn-cancel:hover {
                background-color: #f8fafc;
                color: #0f172a;
                border-color: #94a3b8; /* Thêm hiệu ứng đổi màu viền nhẹ khi hover */
            }

            /* Sửa lại btn-save dùng class btn-primary của Bootstrap cho đồng bộ 100% */
            .btn-save {
                background-color: var(--bs-primary);
                color: white;
                font-weight: 500; /* Khớp với font-weight trang list */
                padding: 0.6rem 1.5rem;
                border-radius: 8px;
                border: none;
                display: flex;
                align-items: center;
                gap: 0.5rem;
                transition: background-color 0.2s;
            }
            .btn-save:hover {
                background-color: #2563eb; /* Màu hover của Tailwind Blue-600 */
                color: white;
            }

            /* Multi-select customization */
            select[multiple] {
                height: 120px;
                padding: 0.5rem;
            }
            select[multiple] option {
                padding: 0.4rem 0.5rem;
                border-radius: 4px;
                margin-bottom: 2px;
            }
            select[multiple] option:checked {
                background-color: #eff6ff linear-gradient(0deg, #eff6ff 0%, #eff6ff 100%);
                color: var(--bs-primary);
            }

            /* ==================== RESPONSIVE ==================== */
            #mobileToggle {
                display: none;
            }

            .sidebar-overlay {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background-color: rgba(15, 23, 42, 0.5); /* Overlay mờ */
                z-index: 1035;
                opacity: 0;
                transition: opacity 0.3s ease;
            }

            @media (max-width: 991.98px) {
                .sidebar {
                    transform: translateX(-100%);
                }
                .sidebar.active {
                    transform: translateX(0);
                }
                .main-content {
                    margin-left: 0;
                }
                #mobileToggle {
                    display: block;
                    background: none;
                    border: none;
                    font-size: 1.5rem;
                    color: var(--text-dark);
                    padding: 0;
                }
                .top-header {
                    padding: 0 1.5rem;
                }
                .content-area {
                    padding: 1.5rem;
                }
                .sidebar-overlay.active {
                    display: block;
                    opacity: 1;
                }
            }

            @media (max-width: 767.98px) {
                .custom-card {
                    padding: 1.5rem;
                }
                .header-title-link span {
                    font-size: 1.1rem;
                }
                .action-footer {
                    flex-direction: column-reverse;
                    align-items: stretch;
                    gap: 0.5rem;
                }
                .btn-save, .btn-cancel {
                    text-align: center;
                    justify-content: center;
                }
            }
        </style>
    </head>
    <body>

        <jsp:include page="../../layout/admin-sidebar.jsp">
            <jsp:param name="activePage" value="adminEmployee" />
        </jsp:include>

        <div class="sidebar-overlay" id="sidebarOverlay"></div>

        <main class="main-content">
            <header class="top-header">
                <div class="d-flex align-items-center gap-3 w-100">
                    <button id="mobileToggle" aria-label="Toggle menu">
                        <i class="bi bi-list"></i>
                    </button>
                    <h1 class="page-title-header">Thêm nhân viên mới</h1>
                    <!--<button class="btn btn-delete ms-auto" type="button" id="btnDeleteEmployee">Xóa nhân viên</button>-->
                </div>
            </header>

            <div class="content-area">

                <div class="page-header">
                    <h1 class="page-title">Thông tin nhân viên</h1>
                    <p class="page-subtitle">Vui lòng điền đầy đủ thông tin để tạo tài khoản nhân viên mới trong hệ thống.</p>
                </div>

                <form action="${ctx}/employee/add" method="POST">
                    <div class="custom-card">
                        <div class="row g-4">
                            <div class="col-12 col-md-12 col-lg-12">
                                <div class="row g-3">
                                    <div class="col-12 col-sm-6">
                                        <label class="form-label">Họ và tên đệm</label>
                                        <input value="${lastName}" type="text" name="lastName" class="form-control" placeholder="Nguyễn Văn" required>
                                    </div>
                                    <div class="col-12 col-sm-6">
                                        <label class="form-label">Tên</label>
                                        <input value="${firstName}" type="text" name="firstName" class="form-control" placeholder="An" required>
                                    </div>
                                    <div class="col-12 col-sm-6">
                                        <label class="form-label">Số điện thoại</label>
                                        <input value="${phone}" type="tel" name="phone" class="form-control" placeholder="0901 234 567" required>
                                    </div>
                                    <div class="col-12 col-sm-6">
                                        <label class="form-label">Bãi xe phụ trách</label>
                                        <select name="siteId" class="form-select">
                                            <option value="0" selected>Không có bãi xe</option>
                                            <c:forEach var="site" items="${listSites}">
                                                <option value="${site.siteId}" ${site.siteId == saveSiteId ? 'selected' : ''}>${site.siteName}</option>
                                            </c:forEach>
                                        </select>
                                        <div class="form-text text-muted ms-2" style="font-size: 0.75rem; margin-top: 0.5rem;">
                                            Lưu ý: Bạn có thể gán hoặc thay đổi bãi xe sau khi tạo tài khoản.
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="custom-card">
                        <h3 class="section-title-sm">Bảo mật tài khoản</h3>
                        <div class="row g-4">
                            <div class="col-12 col-md-6">
                                <label class="form-label">Mật khẩu đăng nhập</label>
                                <div class="password-wrapper">
                                    <input type="password" name="password" class="form-control" placeholder="Nhập mật khẩu" required>
                                    <i class="bi bi-eye-fill toggle-password"></i>
                                </div>
                            </div>
                            <div class="col-12 col-md-6">
                                <label class="form-label">Xác nhận mật khẩu</label>
                                <div class="password-wrapper">
                                    <input type="password" name="confirmPassword" class="form-control" placeholder="Nhập lại mật khẩu" required>
                                    <i class="bi bi-eye-fill toggle-password"></i>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="action-footer">
                        <a href="${ctx}/employee" class="btn-cancel">Hủy</a>
                        <button type="submit" class="btn btn-primary btn-save">
                            <i class="bi bi-floppy"></i> Lưu nhân viên
                        </button>
                    </div>

                </form>
            </div>
        </main>

        <jsp:include page="../../layout/toast-message.jsp" />

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <script>
            // --- Mobile Sidebar Toggle Logic ---
            const mobileToggle = document.getElementById('mobileToggle');
            const sidebar = document.querySelector('.sidebar');
            const overlay = document.getElementById('sidebarOverlay');

            function toggleMenu() {
                sidebar.classList.toggle('active');
                overlay.classList.toggle('active');
                document.body.style.overflow = sidebar.classList.contains('active') ? 'hidden' : '';
            }

            if (mobileToggle)
                mobileToggle.addEventListener('click', toggleMenu);
            if (overlay)
                overlay.addEventListener('click', toggleMenu);

            // --- Logic Ẩn/Hiện mật khẩu ---
            const togglePasswordIcons = document.querySelectorAll('.toggle-password');

            togglePasswordIcons.forEach(icon => {
                icon.addEventListener('click', function () {
                    const input = this.previousElementSibling;
                    if (input.type === 'password') {
                        input.type = 'text';
                        this.classList.remove('bi-eye-fill');
                        this.classList.add('bi-eye-slash-fill');
                    } else {
                        input.type = 'password';
                        this.classList.remove('bi-eye-slash-fill');
                        this.classList.add('bi-eye-fill');
                    }
                });
            });
        </script>
    </body>
</html>