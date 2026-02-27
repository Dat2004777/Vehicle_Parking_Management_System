<%-- 
    Document   : historySubscriptions
    Created on : Feb 27, 2026, 12:40:57 PM
    Author     : ADMIN
--%>

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
        <title>History Subscriptions</title>
        <style>
            :root {
                --bs-primary: #1a56db;
                --bs-primary-rgb: 26, 86, 219;
                --bs-font-sans-serif: 'Inter', sans-serif;
                --text-main: #374151;
                --text-muted: #6b7280;
            }

            body {
                font-family: var(--bs-font-sans-serif);
                color: var(--text-main);
                background-color: #f3f4f6; /* Màu nền nhẹ giúp Card nổi bật hơn */
                -webkit-font-smoothing: antialiased;
            }

            .password-wrapper{
                position: relative;
            }

            .toggle-password {
                position: absolute;
                right: 15px;
                top: 50%;
                transform: translateY(-50%);
                cursor: pointer;
                color: #9ca3af;
            }
        </style>
    </head>
    <body>
        <%@include file="/WEB-INF/views/layout/header.jsp" %>

        <div class="container py-5">
            <div class="row g-4">
                <!--Side bar-->
                 <jsp:include page="/WEB-INF/views/layout/customerSidebar.jsp">
                        <jsp:param name="activePage" value="history-subscriptions"/>
                    </jsp:include>

                <!--Content-->  
                <div class="col-lg-9">
                    <!--History Tickets Tab-->
                    <div id="tickets">
                        <div class="card p-4 shadow-sm">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h5 class="fw-bold text-purple mb-0">Vé tháng hiện tại</h5>
                                <button class="btn btn-outline-primary btn-sm px-3">+ Đăng ký mới</button>
                            </div>

                            <div class="card border border-primary p-3 mb-3 shadow-sm"
                                 style="border-width: 2px !important;">

                                <div class="row align-items-center">

                                    <!-- Nội dung -->
                                    <div class="col-12 col-md-8 mb-3 mb-md-0">

                                        <h6 class="fw-bold">
                                            Vé Ô tô - Bãi FPT Cầu Giấy
                                            <span class="ms-2 badge bg-success-subtle text-success fw-bold">
                                                Đang hoạt động
                                            </span>
                                        </h6>

                                        <p class="mb-1 small text-muted">
                                            <i class="bi bi-car-front"></i> Biển số: 30A-123.45
                                        </p>

                                        <p class="mb-0 small text-muted">
                                            <i class="bi bi-clock"></i>
                                            Hết hạn:
                                            <span class="text-danger fw-bold">
                                                28/02/2026
                                            </span>
                                            (Còn 30 ngày)
                                        </p>
                                    </div>

                                    <!-- Button -->
                                    <div class="col-12 col-md-4 text-md-end">
                                        <button class="btn btn-purple btn-sm w-md-auto">
                                            Gia hạn ngay
                                        </button>
                                    </div>

                                </div>
                            </div>

                            <div class="card border p-3 shadow-sm bg-light">

                                <div class="row align-items-center">

                                    <div class="col-12 col-md-8 mb-3 mb-md-0">

                                        <h6 class="fw-bold text-secondary">
                                            Vé Xe máy - Bãi FPT Hòa Lạc
                                            <span class="ms-2 badge bg-danger-subtle text-danger fw-bold">
                                                Đã hết hạn
                                            </span>
                                        </h6>

                                        <p class="mb-1 small text-muted">
                                            <i class="bi bi-bicycle"></i>
                                            Biển số: 29H1-999.99
                                        </p>

                                        <p class="mb-0 small text-muted">
                                            <i class="bi bi-clock"></i>
                                            Hết hạn: 01/01/2026
                                        </p>
                                    </div>

                                    <div class="col-12 col-md-4 text-md-end">
                                        <button class="btn btn-outline-secondary btn-sm w-md-auto">
                                            Mua lại
                                        </button>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
