<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Test Toast Message</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <style>
            /* --- CSS của Toast --- */
            .custom-toast {
                border-radius: 10px !important;
                border-left: 4px solid transparent;
                background-color: #ffffff;
                min-width: 300px;
                transition: none !important; /* Bỏ transition mặc định để dùng animation trượt */
            }

            .custom-toast .btn-close {
                font-size: 0.65rem;
                padding: 0.5rem;
                margin: 0.25rem;
            }

            /* Hiệu ứng trượt từ phải sang (trên màn hình lớn) */
            @keyframes slideInFromRight {
                0% { transform: translateX(120%); opacity: 0; }
                100% { transform: translateX(0); opacity: 1; }
            }
            .custom-toast.show {
                animation: slideInFromRight 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275) forwards;
            }

            /* Trên điện thoại: Toast sẽ nằm ở trên cùng, chiếm full chiều ngang */
            @media (max-width: 575.98px) {
                .toast-container {
                    width: 100%;
                    padding: 1rem !important;
                    top: 0 !important;
                    bottom: auto !important;
                    right: 0 !important;
                    left: 0 !important;
                }
                .custom-toast {
                    width: 100%;
                    max-width: 100%;
                }
                
                /* Đổi sang trượt từ trên xuống cho điện thoại */
                @keyframes slideDownFromTop {
                    0% { transform: translateY(-100%); opacity: 0; }
                    100% { transform: translateY(0); opacity: 1; }
                }
                .custom-toast.show {
                    animation: slideDownFromTop 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275) forwards;
                }
            }
        </style>
    </head>
    <body>
        <div class="container mt-5">
            <h2 class="mb-4">Trình quản lý Bãi đỗ xe</h2>
            <div class="d-flex gap-3">
                <button type="button" class="btn btn-success" onclick="showToast('success', 'Lưu giao dịch thành công. Thu: 15.000đ!')">
                    <i class="bi bi-check-circle"></i> Test Success
                </button>

                <button type="button" class="btn btn-danger" onclick="showToast('error', 'Lỗi: Biển số xe không khớp lúc vào bãi!')">
                    <i class="bi bi-x-circle"></i> Test Error
                </button>

                <button type="button" class="btn btn-warning" onclick="showToast('warning', 'Cảnh báo: Bãi xe máy sắp hết chỗ trống!')">
                    <i class="bi bi-exclamation-triangle"></i> Test Warning
                </button>
            </div>
        </div>


        <div class="toast-container position-fixed top-0 end-0 p-4" style="z-index: 1055;">
            <div id="systemToast" class="toast custom-toast shadow-lg" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex align-items-start py-2 px-3 bg-white" style="border-radius: 0 10px 10px 0;">
                    <i id="toastIcon" class="bi fs-5 me-2 mt-1"></i>

                    <div class="flex-grow-1 pe-3">
                        <span id="toastText" style="font-size: 0.9rem; line-height: 1.3; display: block;" class="mt-1 fw-medium text-dark">
                            Nội dung thông báo
                        </span>
                    </div>

                    <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <script>
            function showToast(type, message) {
                const toastEl = document.getElementById('systemToast');
                const toastIcon = document.getElementById('toastIcon');
                const toastText = document.getElementById('toastText');

                // 1. Reset class
                toastEl.style.borderColor = 'transparent';
                toastIcon.className = 'bi fs-5 me-2 mt-1'; 

                // 2. Định dạng theo Type
                if (type === 'success') {
                    toastEl.style.borderLeftColor = '#198754'; 
                    toastIcon.classList.add('bi-check-circle-fill', 'text-success');
                } else if (type === 'error') {
                    toastEl.style.borderLeftColor = '#dc3545'; 
                    toastIcon.classList.add('bi-exclamation-triangle-fill', 'text-danger');
                } else {
                    toastEl.style.borderLeftColor = '#ffc107'; 
                    toastIcon.classList.add('bi-exclamation-circle-fill', 'text-warning');
                }

                // 3. Gắn chữ
                toastText.innerHTML = message;

                // 4. Khởi tạo và Show bằng Bootstrap API
                const bsToast = new bootstrap.Toast(toastEl, { delay: 10000 });
                bsToast.show();
            }
        </script>
    </body>
</html>