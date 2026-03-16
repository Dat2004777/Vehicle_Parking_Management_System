<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="type" required="false" type="java.lang.String" %>
<%@ attribute name="message" required="false" type="java.lang.String" %>

<style>
    /* --- CSS Toast Core --- */
    .custom-toast {
        border-radius: 10px;
        border-left: 4px solid transparent;
        background: #fff;
        min-width: 320px;
        overflow: hidden; /* Quan tr?ng ?? Progress Bar không tràn ra ngoài */
    }

    .custom-toast .btn-close {
        font-size: 0.65rem;
    }

    /* Progress Bar logic: S? d?ng currentColor ?? ??ng b? màu v?i Icon */
    .custom-toast::after {
        content: "";
        display: block;
        height: 3px;
        width: 100%;
        background: currentColor; 
        opacity: 0.6; /* Làm m? thanh progress m?t chút cho tinh t? */
    }
    
    /* Animation ch?y thanh progress trong 5s (kh?p v?i delay c?a Toast) */
    .custom-toast.showing::after,
    .custom-toast.show::after {
        animation: toastProgress 5s linear forwards;
    }

    @keyframes toastProgress {
        from { width: 100%; }
        to { width: 0%; }
    }

    /* Animation tr??t t? ph?i (Desktop) */
    @keyframes slideInFromRight {
        0% { transform: translateX(120%); opacity: 0; }
        100% { transform: translateX(0); opacity: 1; }
    }
    .custom-toast.show {
        animation: slideInFromRight 0.35s ease-out;
    }

    /* Responsive Mobile */
    @media (max-width: 575px) {
        .toast-container {
            width: 100%;
            padding: 1rem !important;
            top: var(--navbar-height, 70px) !important;
            right: 0 !important;
            left: 0 !important;
        }
        .custom-toast { width: 100%; }
        @keyframes slideDown {
            0% { transform: translateY(-100%); opacity: 0; }
            100% { transform: translateY(0); opacity: 1; }
        }
        .custom-toast.show {
            animation: slideDown .35s ease-out;
        }
    }
</style>

<div class="toast-container position-fixed end-0 p-4" style="z-index:1055; top:var(--navbar-height, 70px)">
    <div id="systemToast" class="toast custom-toast shadow-lg" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex align-items-center py-2 px-3">
            <i id="toastIcon" class="bi fs-5 me-2"></i>
            <div class="flex-grow-1">
                <span id="toastText" class="fw-medium text-dark" style="font-size:.9rem"></span>
            </div>
            <button class="btn-close" data-bs-dismiss="toast"></button>
        </div>
    </div>
</div>

<script>
    let bsToastInstance = null;

    function showToast(type, message) {
        const toastEl = document.getElementById('systemToast');
        const toastIcon = document.getElementById('toastIcon');
        const toastText = document.getElementById('toastText');

        if (!bsToastInstance) {
            bsToastInstance = bootstrap.Toast.getOrCreateInstance(toastEl, {delay: 5000});
        }

        // 1. Reset tr?ng thái
        toastEl.style.borderLeftColor = 'transparent';
        toastEl.style.color = 'inherit'; // Reset currentColor
        toastIcon.className = 'bi fs-5 me-2';

        // 2. C?u hình theo Type (C?p nh?t style.color ?? thanh ::after nh?n màu)
        const config = {
            success: { border: '#198754', color: '#198754', icon: 'bi-check-circle-fill' },
            error: { border: '#dc3545', color: '#dc3545', icon: 'bi-exclamation-triangle-fill' },
            warning: { border: '#ffc107', color: '#ffc107', icon: 'bi-exclamation-circle-fill' }
        };

        const settings = config[type] || config['warning'];
        
        toastEl.style.borderLeftColor = settings.border;
        toastEl.style.color = settings.color; // Quy?t ??nh màu c?a progress bar qua currentColor
        toastIcon.classList.add(settings.icon);
        toastIcon.style.color = settings.color;

        // 3. Hi?n th? n?i dung
        toastText.innerHTML = message;
        
        // Restart animation b?ng cách xóa/thêm class show
        toastEl.classList.remove('show');
        setTimeout(() => {
            bsToastInstance.show();
        }, 50);
    }
</script>

<c:if test="${not empty message}">
    <script>
        document.addEventListener("DOMContentLoaded", () => {
            showToast('${empty type ? "success" : type}', '${message}');
        });
    </script>
</c:if>