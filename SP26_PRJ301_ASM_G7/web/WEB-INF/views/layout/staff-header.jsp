<%@page contentType="text/html" pageEncoding="UTF-8"%>

<nav class="navbar navbar-expand-lg navbar-custom fixed-top px-3 px-md-4">
    <div class="container-fluid p-0 d-flex justify-content-between align-items-center">

        <div class="d-flex align-items-center gap-3">
            <button class="btn btn-light border-0 bg-light" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarOffcanvas">
                <i class="bi bi-list fs-4"></i>
            </button>
            <div class="d-sm-block">
                <div class="fw-bold fs-5" style="line-height: 1.2; color: #1e293b;">${site.siteName}</div>
            </div>
        </div>

        <div class="header-clock d-md-flex align-items-center gap-2 d-none">
            <i class="bi bi-clock"></i>
            <span id="realtimeClock">00:00:00</span> - <span id="realtimeDate">--/--/----</span>
        </div>

        <div class="d-flex align-items-center gap-4">

        </div>
    </div>
</nav>

<style>
    /* NAVBAR HEADER */
    .navbar-custom {
        height: var(--navbar-height);
        box-shadow: 0 1px 2px rgba(0,0,0,0.05);
        background-color: #fff;
    }
    .header-clock {
        font-weight: 600;
        color: #475569;
        background: #f1f5f9;
        padding: 8px 16px;
        border-radius: 8px;
        font-variant-numeric: tabular-nums;
    }

</style>            
<script>
    document.addEventListener('DOMContentLoaded', function () {

        // 1. Khai báo lấy phần tử HTML
        const clockTimeEl = document.getElementById('realtimeClock');
        const clockDateEl = document.getElementById('realtimeDate');

        // 2. Hàm cập nhật thời gian
        function updateClock() {
            if (clockTimeEl && clockDateEl) {
                const now = new Date();
                clockTimeEl.innerText = now.toLocaleTimeString('vi-VN', {hour12: false});
                clockDateEl.innerText = now.toLocaleDateString('vi-VN', {day: '2-digit', month: '2-digit', year: 'numeric'});
            }
        }

        // 3. Chạy đồng hồ
        setInterval(updateClock, 1000);
        updateClock(); // Gọi ngay 1 lần để không bị chờ 1 giây đầu
    });
</script>
