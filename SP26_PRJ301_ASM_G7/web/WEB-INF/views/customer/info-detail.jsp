<%-- 
    Document   : info-detail
    Created on : Feb 26, 2026, 10:08:37 PM
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
        <title>Account Profile</title>
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

        </style>
    </head>
    <body>
        <%@include file="/WEB-INF/views/layout/header.jsp" %>

        <div class="py-5">
            <div class="container">
                <div class="row g-4">

                    <jsp:include page="/WEB-INF/views/layout/customerSidebar.jsp">
                        <jsp:param name="activePage" value="customer-info"/>
                    </jsp:include>

                    <div class="col-lg-9">
                        <div class="card border-1 shadow-sm rounded-4 p-4 p-lg-5">
                            <form method="post" action="customer-info">

                                <div class="mb-5">
                                    <h5 class="fw-bold mb-1">Thông tin liên lạc</h5>
                                    <p class="text-muted small mb-4 text-danger">
                                        Sử dụng địa chỉ email cố định để nhận thông báo quan trọng.
                                    </p>

                                    <div class="row g-4">
                                        <div class="col-md-6">
                                            <label class="form-label fw-semibold small">Họ và tên</label>
                                            <input id="customerName" name="customerName" type="text" class="form-control"
                                                   value="${customer.lastname} ${customer.firstname}" required>
                                            <span id="nameError" class="text-danger d-block mt-2">${errorName}</span>
                                        </div>

                                        <div class="col-md-6">
                                            <label class="form-label fw-semibold small">Địa chỉ Email</label>
                                            <input type="email" class="form-control"
                                                   value="${customer.email}" readonly>
                                        </div>

                                        <div class="col-md-6">
                                            <label class="form-label fw-semibold small">Số điện thoại</label>
                                            <input id="phone" name="phone" type="text" class="form-control bg-light"
                                                   value="${customer.phone}" required>
                                            <span id="phoneError" class="text-danger d-block mt-2">${errorPhone}</span>
                                        </div>

                                        <div class="col-md-6 d-flex align-items-end justify-content-md-end">
                                            <button id="updateBtn" type="submit" name="action" value="updateProfile" class="btn btn-primary px-4 me-1">
                                                Cập nhật
                                            </button>
                                            <button type="reset" class="btn btn-outline-secondary px-4" name="action" value="reset">Hủy</button>
                                        </div>
                                    </div>
                                </div>

                                <hr class="my-5">

                                <div>
                                    <h5 class="fw-bold mb-1">Đổi mật khẩu</h5>
                                    <p class="text-muted small mb-4">
                                        Cập nhật mật khẩu thường xuyên để bảo vệ tài khoản của bạn.
                                    </p>

                                    <div class="row g-4">
                                        <div class="col-12">
                                            <label class="form-label fw-semibold small">Mật khẩu hiện tại</label>
                                            <input id="oldPass"name="oldPass" type="password" class="form-control">
                                            <span id="errorOldPass" class="text-danger d-block mt-2">${errorOldPass}</span>
                                        </div>

                                        <div class="col-12">
                                            <label class="form-label fw-semibold small">Mật khẩu mới</label>
                                            <input id="newPass" name="newPass" type="password" class="form-control">
                                            <span id="errorNewPass" class="text-danger d-block mt-2">${errorNewPass}</span>
                                        </div>

                                        <div class="col-12">
                                            <label class="form-label fw-semibold small">Xác nhận mật khẩu mới</label>
                                            <input id="confirmPass" name=""confirmPass type="password" class="form-control">
                                            <span id="errorConfirmPass" class="text-danger d-block mt-2">${errorConfirmPass}</span>
                                        </div>

                                        <div class="col-12 text-md-end">
                                            <button id="changePassBtn" type="submit" name="action" value="changePassword" class="btn btn-dark px-4">
                                                Lưu thay đổi
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--Footer-->
        <%@ include file="/WEB-INF/views/layout/footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            const contextPath = "${pageContext.request.contextPath}";
            
            document.addEventListener("DOMContentLoaded", function () {

                // ===== PROFILE SECTION =====
                const nameInput = document.getElementById("customerName");
                const phoneInput = document.getElementById("phone");
                const updateBtn = document.getElementById("updateBtn");

                const originalName = nameInput.value;
                const originalPhone = phoneInput.value;

                // 👉 disable ban đầu
                updateBtn.disabled = true;

                function checkProfileChange() {
                    if (nameInput.value !== originalName ||
                            phoneInput.value !== originalPhone) {
                        updateBtn.disabled = false;
                    } else {
                        updateBtn.disabled = true;
                    }
                }

                nameInput.addEventListener("input", checkProfileChange);
                phoneInput.addEventListener("input", checkProfileChange);


                // ===== PASSWORD SECTION =====
                const oldPass = document.getElementById("oldPass");
                const newPass = document.getElementById("newPass");
                const confirmPass = document.getElementById("confirmPass");
                const changePassBtn = document.getElementById("changePassBtn");

                // 👉 disable ban đầu
                changePassBtn.disabled = true;

                function checkPasswordChange() {
                    if (oldPass.value !== "" ||
                            newPass.value !== "" ||
                            confirmPass.value !== "") {
                        changePassBtn.disabled = false;
                    } else {
                        changePassBtn.disabled = true;
                    }
                }

                oldPass.addEventListener("input", checkPasswordChange);
                newPass.addEventListener("input", checkPasswordChange);
                confirmPass.addEventListener("input", checkPasswordChange);

            });

            //Validate Phone
            document.addEventListener("DOMContentLoaded", function () {

                const form = document.querySelector("form");

                // ===== PHONE VALIDATION =====
                const phoneInput = document.getElementById("phone");
                const phoneError = document.getElementById("phoneError");
                const phoneRegex = /^0\d{9}$/;

                phoneInput.addEventListener("input", () => {
                    if (!phoneRegex.test(phoneInput.value.trim())) {
                        phoneError.textContent = "Số điện thoại không hợp lệ";
                        phoneInput.classList.add("is-invalid");
                    } else {
                        phoneError.textContent = "";
                        phoneInput.classList.remove("is-invalid");
                    }
                });

                // ===== RESET =====
                form.addEventListener("reset", () => {
                    setTimeout(() => {
                        phoneInput.classList.remove("is-invalid", "is-valid");
                        phoneError.textContent = "";
                    }, 0);
                });

            });

            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.get("success") === "false") {
                Swal.fire({
                    icon: 'error',
                    title: 'Fail to update!',
                    confirmButtonText: 'Try Again'
                }).then(() => {
                    window.location.href = contextPath + "/customer-info";
                });
            } else if (urlParams.get("success") === "true") {
                Swal.fire({
                    icon: 'success',
                    title: 'Update Successfully!',
                    confirmButtonText: 'OK'
                }).then(() => {
                    window.location.href = contextPath + "/customer-info";
                });
            }
        </script>
    </body>
</html>

