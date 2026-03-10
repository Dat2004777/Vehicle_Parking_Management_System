<%-- 
    Document   : customerToast
    Created on : Mar 5, 2026, 7:19:00 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
<title>JSP Page</title>
<style>
    :root {
        --bs-body-bg: #f3f4f6;
        --bs-primary: #1a56db;
        --bs-primary-rgb: 26, 86, 219;
        --bs-font-sans-serif: 'Inter', sans-serif;
        --text-main: #374151;
        --text-muted: #6b7280;
    }

    body {
        font-family: var(--bs-font-sans-serif);
        color: var(--text-main);
        -webkit-font-smoothing: antialiased;
        background-color: var(--bs-body-bg);
    }

    /* Toast Style */

    .custom-toast {
        border-radius: 10px;
        border-left: 4px solid transparent;
        background: #fff;
        min-width: 320px;
        overflow: hidden;
    }

    .custom-toast .btn-close {
        font-size: 0.65rem;
    }

    /* progress bar */

    .custom-toast::after {
        content: "";
        display: block;
        height: 3px;
        width: 100%;
        background: currentColor;
        animation: toastProgress 5s linear;
    }
    
    .card {
        background-color: #fff;
    }

    @keyframes toastProgress {
        from {
            width: 100%
        }

        to {
            width: 0%
        }
    }

    /* Desktop animation */

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
        animation: slideInFromRight 0.35s ease-out;
    }

    /* Mobile */

    @media (max-width:575px) {

        .toast-container {
            width: 100%;
            padding: 1rem !important;
            top: var(--navbar-height) !important;
            right: 0 !important;
            left: 0 !important;
        }

        .custom-toast {
            width: 100%;
        }

        @keyframes slideDown {
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
            animation: slideDown .35s ease-out;
        }

    }
</style>
<!-- Toast container -->

<div class="toast-container position-fixed end-0 p-4" style="z-index:1055; top:var(--navbar-height)">

    <div id="systemToast" class="toast custom-toast shadow-lg" role="alert">

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

    const toastEl = document.getElementById("systemToast");
    const toastIcon = document.getElementById("toastIcon");
    const toastText = document.getElementById("toastText");

    let toastInstance = null;

    document.addEventListener("DOMContentLoaded", () => {

        toastInstance = new bootstrap.Toast(toastEl, {
            delay: 5000
        });

    });

    function showToast(type, message) {

        if (!toastInstance) {
            toastInstance = new bootstrap.Toast(toastEl, {delay: 5000});
        }

        /* reset */

        toastIcon.className = "bi fs-5 me-2";
        toastEl.style.borderLeftColor = "transparent";

        /* type */

        switch (type) {

            case "success":

                toastEl.style.borderLeftColor = "#198754";
                toastIcon.classList.add("bi-check-circle-fill", "text-success");

                break;

            case "error":

                toastEl.style.borderLeftColor = "#dc3545";
                toastIcon.classList.add("bi-x-circle-fill", "text-danger");

                break;

            default:

                toastEl.style.borderLeftColor = "#ffc107";
                toastIcon.classList.add("bi-exclamation-triangle-fill", "text-warning");

        }

        /* message */

        toastText.innerHTML = message;

        /* show */

        toastInstance.show();

    }

</script>
