<%@page contentType="text/html" pageEncoding="UTF-8"%>

<link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
<!--<link href="${pageContext.request.contextPath}/assets/js/bootstrap.min.js" rel="stylesheet">-->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

<style>
    :root {
        --bs-body-bg: #f3f4f6;
        --bs-primary: #1a56db;
        --bs-primary-rgb: 26, 86, 219;
        --bs-font-sans-serif: 'Inter', sans-serif;
        --text-main: #374151;
        --text-muted: #6b7280;
        --navbar-height: 70px;
    }

    body {
        font-family: var(--bs-font-sans-serif);
        color: var(--text-main);
        -webkit-font-smoothing: antialiased;
        background-color: var(--bs-body-bg);
        padding-top: var(--navbar-height);
    }

    /* --- Custom Badges --- */
    .badge-soft-success {
        background-color: #dcfce7;
        color: #15803d;
        border: 1px solid #bbf7d0;
        font-weight: 600;
        padding: 6px 12px;
        border-radius: 20px;
    }
    .badge-soft-warning {
        background-color: #fff7ed;
        color: #c2410c;
        border: 1px solid #fed7aa;
        font-weight: 600;
        padding: 6px 12px;
        border-radius: 20px;
    }
    .badge-soft-danger  {
        background-color: #fef2f2;
        color: #b91c1c;
        border: 1px solid #fecaca;
        font-weight: 600;
        padding: 6px 12px;
        border-radius: 20px;
    }
    .badge-outline      {
        background-color: #f8fafc;
        color: var(--text-main);
        border: 1px solid #cbd5e1;
        font-weight: 600;
        padding: 6px 12px;
        border-radius: 20px;
    }

    /* --- Toast Custom & Animation --- */
    .custom-toast {
        border-radius: 10px !important;
        border-left: 4px solid transparent;
        background-color: #ffffff;
        min-width: 300px;
        transition: none !important;
    }
    .custom-toast .btn-close {
        font-size: 0.65rem;
        padding: 0.5rem;
        margin: 0.25rem;
    }

    @keyframes slideInFromRight {
        0% {
            transform: translateX(120%);
            opacity: 0;
        }
        100% {
            transform: translateX(0);
            opacity: 1;
        }
    }
    .custom-toast.show {
        animation: slideInFromRight 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275) forwards;
    }

    /* Responsive Toast cho Mobile */
    @media (max-width: 575.98px) {
        .toast-container {
            width: 100%;
            padding: 1rem !important;

            /* Sửa top: 0 thành top: var(--navbar-height) */
            top: var(--navbar-height) !important;

            bottom: auto !important;
            right: 0 !important;
            left: 0 !important;
        }
        .custom-toast {
            width: 100%;
            max-width: 100%;
        }

        @keyframes slideDownFromTop {
            0% {
                transform: translateY(-100%);
                opacity: 0;
            }
            100% {
                transform: translateY(0);
                opacity: 1;
            }
        }
        .custom-toast.show {
            animation: slideDownFromTop 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275) forwards;
        }
    }
</style>

<div class="toast-container position-fixed end-0 p-4" style="z-index: 1055; top: var(--navbar-height)">
    <div id="systemToast" class="toast custom-toast shadow-lg" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex align-items-center py-2 px-3 bg-white" style="border-radius: 0 10px 10px 0;">
            <i id="toastIcon" class="bi fs-5 me-2 mt-1"></i>
            <div class="flex-grow-1 pe-3">
                <span id="toastText" style="font-size: 0.9rem; line-height: 1.3; display: block;" class="mt-1 fw-medium text-dark"></span>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<script>
    // Khai báo sẵn các phần tử DOM để tăng hiệu năng (không phải tìm lại mỗi lần gọi hàm)
    const toastEl = document.getElementById('systemToast');
    const toastIcon = document.getElementById('toastIcon');
    const toastText = document.getElementById('toastText');

    // Khởi tạo đối tượng Toast 1 lần duy nhất để tránh lỗi hiển thị
    // Đã cấu hình thời gian hiển thị mặc định là 5000ms (5 giây)
    let bsToastInstance = null;

    document.addEventListener("DOMContentLoaded", () => {
        bsToastInstance = bootstrap.Toast.getOrCreateInstance(toastEl, {delay: 5000});
    });

    /**
     * Hiển thị thông báo Toast
     * @param {string} type - 'success', 'error', 'warning'
     * @param {string} message - Nội dung thông báo
     */
    function showToast(type, message) {
        if (!bsToastInstance) {
            bsToastInstance = bootstrap.Toast.getOrCreateInstance(toastEl, {delay: 5000});
        }

        // 1. Reset class (sạch sẽ giao diện)
        toastEl.style.borderColor = 'transparent';
        toastIcon.className = 'bi fs-5 me-2 mt-1';

        // 2. Định cấu hình màu sắc & Icon
        switch (type) {
            case 'success':
                toastEl.style.borderLeftColor = '#198754';
                toastIcon.classList.add('bi-check-circle-fill', 'text-success');
                break;
            case 'error':
                toastEl.style.borderLeftColor = '#dc3545';
                toastIcon.classList.add('bi-exclamation-triangle-fill', 'text-danger');
                break;
            default: // warning
                toastEl.style.borderLeftColor = '#ffc107';
                toastIcon.classList.add('bi-exclamation-circle-fill', 'text-warning');
                break;
        }

        // 3. Gắn chữ và hiển thị
        toastText.innerHTML = message;
        bsToastInstance.show();
    }
</script>


<div class="modal fade" id="paymentModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content" style="border-radius: 12px; border: none;">
            <div class="modal-header bg-light">
                <h5 class="modal-title fw-bold text-primary"><i class="bi bi-receipt"></i> Hóa đơn Check-out</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <div class="d-flex justify-content-between mb-2">
                    <span class="text-muted">Mã thẻ:</span>
                    <span id="modalCardId" class="fw-bold">---</span>
                </div>
                <div class="d-flex justify-content-between mb-2">
                    <span class="text-muted">Biển số:</span>
                    <span id="modalPlate" class="fw-bold text-uppercase text-primary">---</span>
                </div>
                <div class="d-flex justify-content-between mb-2">
                    <span class="text-muted">Giờ vào:</span>
                    <span id="modalTimeIn">---</span>
                </div>
                <div class="d-flex justify-content-between mb-3 border-bottom pb-3">
                    <span class="text-muted">Thời gian đỗ:</span>
                    <span id="modalDuration">---</span>
                </div>

                <div class="d-flex justify-content-between align-items-center mt-3">
                    <span class="fs-5 fw-bold text-dark">Thành tiền:</span>
                    <span id="modalFee" class="fs-4 fw-bold text-danger">0 đ</span>
                </div>
            </div>
            <div class="modal-footer bg-light border-0" style="border-radius: 0 0 12px 12px;">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy bỏ</button>

                <button type="button" class="btn btn-success px-4" id="btnConfirmPayment">
                    <i class="bi bi-cash-coin"></i> Xác nhận Thu tiền
                </button>
            </div>
        </div>
    </div>
</div>