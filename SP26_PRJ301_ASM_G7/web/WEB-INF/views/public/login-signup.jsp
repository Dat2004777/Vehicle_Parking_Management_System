<%-- 
    Document   : login
    Created on : 24 Feb 2026, 00:22:33
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth/auth.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css" />
        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <!-- Google Fonts: Inter -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

        <!-- JS Bootstrap -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.css">
        <title>JSP Page</title>
    </head>
    <body class="bg-light">
        <div class="auth-wrapper ${authMode == 'signup' ? 'right-panel-active' : ''}" id="auth-wrapper">

            <!-- === SIGN UP FORM (Reveals on the right side) === -->
            <div class="form-container sign-up-container">
                <div class="auth-header">
                    <div class="brand-logo">P</div>
                    <span class="fs-5 fw-bold text-dark">ParkEasy</span>
                </div>

                <div class="my-auto pb-4">
                    <h2 class="form-title">Đăng ký tài khoản</h2>
                    <p class="form-subtitle">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login" id="show-login">Đăng nhập</a></p>

                    <form action="${pageContext.request.contextPath}/signup" method="post">

                        <!-- Username -->
                        <div class="mb-3">
                            <label class="form-label fw-bold">Username</label>
                            <input id="username" name="username" value="${username}" type="text" class="form-control" placeholder="Enter Username" required>
                            <span id="usernameError" class="text-danger d-block mt-2">${errorUsername}</span>
                        </div>

                        <!-- First Name / Last Name-->
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">First Name</label>
                                <input name="firstname" type="text" class="form-control" placeholder="First Name" required>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold">Last Name</label>
                                <input name="lastname" type="text" class="form-control" placeholder="Last Name" required>
                            </div>
                        </div>

                        <!-- Email -->
                        <div class="mb-3">
                            <label class="form-label fw-bold">Email</label>
                            <input name="email" 
                                  value="${email}" id="email" type="email" class="form-control" placeholder="Enter Email" required>
                            <span id="emailError" class="text-danger d-block mt-2">${errorEmail}</span>
                        </div>

                        <!-- Phone -->
                        <div class="mb-3">
                            <label class="form-label fw-bold">Số điện thoại</label>
                            <input name="phone" id="phone" type="tel" class="form-control" placeholder="Enter Phone Number" required>
                            <span id="phoneError" class="text-danger d-block mt-2">${errorPhone}</span>
                        </div>

                        <!-- Password -->
                        <div class="mb-3">
                            <label class="form-label fw-bold">Mật khẩu</label>
                            <div class="password-wrapper">
                                <input id="password" name="password_1" type="password" class="form-control" placeholder="Enter Your Password" required>
                                <i class="bi bi-eye toggle-password" onclick="togglePassword(this)"></i>
                            </div>
                            <span id="passwordError" class="text-danger d-block mt-2">${errorPass}</span>
                        </div>

                        <!-- Confirm Password -->
                        <div class="mb-3">
                            <label class="form-label fw-bold">Xác nhận mật khẩu</label>
                            <div class="password-wrapper">
                                <input id="confirmPassword" name="password_2" type="password" class="form-control" placeholder="Confirm Your Password" required>
                                <i class="bi bi-eye toggle-password" onclick="togglePassword(this)"></i>
                            </div>
                            <span id="confirmPasswordError" class="text-danger d-block mt-2">${errorPassConfirm}</span>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">Đăng ký</button>
                    </form>
                </div>
            </div>

            <!-- === SIGN IN FORM (Visible by default on the left) === -->
            <div class="form-container sign-in-container">
                <div class="auth-header">
                    <div class="brand-logo">P</div>
                    <span class="fs-5 fw-bold text-dark">ParkEasy</span>
                </div>

                <div class="my-auto pb-4">
                    <h2 class="form-title">Chào mừng trở lại</h2>
                    <p class="form-subtitle">Chưa có tài khoản? <a href="${pageContext.request.contextPath}/signup" id="show-register">Đăng ký ngay</a></p>

                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="mb-4">
                            <label class="form-label fw-bold">Tên đăng nhập</label>
                            <input name="username" type="text" class="form-control" placeholder="Username" value="${username}"required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">Mật khẩu</label>
                            <div class="password-wrapper">
                                <input name="password" type="password" class="form-control" placeholder="Enter Password" required>
                                <i class="bi bi-eye toggle-password" onclick="togglePassword(this)"></i>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <a href="#" class="text-primary text-decoration-none fw-medium"
                               style="font-size: 0.875rem;">Quay lại trang chủ</a>
                               
                            <a href="#" class="text-primary text-decoration-none fw-medium"
                               style="font-size: 0.875rem;">Quên mật khẩu?</a>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
                    </form>

                    <!--Error message-->
                    <div class="text-center mt-3">
                        <span class="text-danger fw-semibold">${errorMessage}</span>
                    </div>
                </div>
            </div>

            <!-- === SLIDING OVERLAY PANEL (Image) === -->
            <div class="overlay-container">
                <div class="overlay">

                    <div class="overlay-nav">
                        <a href="${pageContext.request.contextPath}">Trang chủ</a>
                        <a href="#">Đặt chỗ</a>
                    </div>

                    <div class="overlay-panel overlay-left">
                        <div class="mt-auto">
                            <h2>Quản lý bãi đỗ xe thông minh</h2>
                            <p>Trải nghiệm đặt chỗ nhanh chóng, an toàn và tiện lợi với hệ thống ParkEasy. Kết nối ngay hôm
                                nay.</p>
                        </div>
                    </div>
                    <div class="overlay-panel overlay-right">
                        <div class="mt-auto">
                            <h2>Quản lý bãi đỗ xe thông minh</h2>
                            <p>Trải nghiệm đặt chỗ nhanh chóng, an toàn và tiện lợi với hệ thống ParkEasy. Kết nối ngay hôm
                                nay.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            const contextPath = "${pageContext.request.contextPath}";
        </script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="${pageContext.request.contextPath}/assets/js/auth/auth.js"></script>
    </body>
</html>
