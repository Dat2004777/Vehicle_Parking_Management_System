<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index: 1080">
    <div id="successToast" class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
                <i class="bi bi-check-circle-fill me-2"></i>
                <span id="successMsg">${sessionScope.successMessage}</span>
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    </div>

    <div id="errorToast" class="toast align-items-center text-white bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                <span id="errorMsg">${errorMessage}${sessionScope.errorMessage}</span>
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        // 1. Kiểm tra và hiện Success Message
        const successContent = "${sessionScope.successMessage}";
        if (successContent && successContent.trim() !== "") {
            const sToast = new bootstrap.Toast(document.getElementById('successToast'));
            sToast.show();
            // Xóa session sau khi hiện để tránh F5 lại hiện tiếp
    <% session.removeAttribute("successMessage"); %>
        }

        // 2. Kiểm tra và hiện Error Message (từ cả request và session)
        const errorContent = "${errorMessage}${sessionScope.errorMessage}";
        if (errorContent && errorContent.trim() !== "") {
            const eToast = new bootstrap.Toast(document.getElementById('errorToast'));
            eToast.show();
    <% session.removeAttribute("errorMessage"); %>
        }
    });

    // Hàm gọi thủ công nếu cần
    function showToast(message, isSuccess = true) {
        const id = isSuccess ? 'successToast' : 'errorToast';
        const msgId = isSuccess ? 'successMsg' : 'errorMsg';
        document.getElementById(msgId).innerText = message;
        new bootstrap.Toast(document.getElementById(id)).show();
    }
</script>