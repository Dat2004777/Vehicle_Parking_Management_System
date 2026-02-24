<%-- 
    Document   : dashboard.jsp
    Created on : Feb 24, 2026, 9:30:19 PM
    Author     : dat20
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tổng quan hệ thống - ParkAdmin</title>

        <!-- Google Fonts: Inter -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <style>
            :root {
                --bs-primary: #3b82f6; /* Màu xanh dương chủ đạo */
                --bs-primary-rgb: 59, 130, 246;
                --bg-body: #f8fafc; /* Màu nền xám nhạt */
                --sidebar-width: 260px;
                --text-main: #334155;
                --text-muted: #64748b;
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

            /* Header (Top Bar) */
            .top-header {
                height: 80px;
                background-color: #ffffff;
                border-bottom: 1px solid #e2e8f0;
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 0 2rem;
                position: sticky;
                top: 0;
                z-index: 1030;
            }

            .page-title {
                font-size: 1.25rem;
                font-weight: 700;
                color: #0f172a;
                margin: 0;
            }

            .search-container {
                position: relative;
                width: 320px;
            }

            .search-container .bi-search {
                position: absolute;
                left: 12px;
                top: 50%;
                transform: translateY(-50%);
                color: #94a3b8;
            }

            .search-input {
                width: 100%;
                padding: 0.5rem 1rem 0.5rem 2.5rem;
                background-color: #f1f5f9;
                border: 1px solid transparent;
                border-radius: 8px;
                font-size: 0.875rem;
                transition: all 0.2s;
            }

            .search-input:focus {
                outline: none;
                background-color: #ffffff;
                border-color: #cbd5e1;
                box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
            }

            .header-actions {
                display: flex;
                align-items: center;
                gap: 1.25rem;
            }

            .btn-icon {
                background: none;
                border: none;
                color: var(--text-muted);
                font-size: 1.25rem;
                position: relative;
                padding: 0;
                cursor: pointer;
                transition: color 0.2s;
            }
            .btn-icon:hover {
                color: #0f172a;
            }

            .notification-dot {
                position: absolute;
                top: -2px;
                right: -2px;
                width: 8px;
                height: 8px;
                background-color: #ef4444;
                border-radius: 50%;
                border: 2px solid white;
            }

            /* Content Area */
            .content-area {
                padding: 2rem;
                flex-grow: 1;
            }

            .custom-card {
                background-color: #ffffff;
                border-radius: 16px;
                border: 1px solid #e2e8f0;
                box-shadow: 0 1px 3px rgba(0,0,0,0.02);
                padding: 1.5rem;
                height: 100%;
            }

            /* Stat Cards */
            .stat-card-title {
                font-size: 0.875rem;
                color: var(--text-muted);
                font-weight: 500;
                margin-bottom: 0.5rem;
            }

            .stat-card-value {
                font-size: 2rem;
                font-weight: 700;
                color: #0f172a;
                margin-bottom: 0.5rem;
            }

            .stat-icon {
                width: 48px;
                height: 48px;
                border-radius: 12px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 1.5rem;
            }
            .icon-blue {
                background-color: #eff6ff;
                color: #3b82f6;
            }
            .icon-orange {
                background-color: #fff7ed;
                color: #f97316;
            }
            .icon-purple {
                background-color: #faf5ff;
                color: #a855f7;
            }

            .trend-badge {
                display: inline-flex;
                align-items: center;
                gap: 4px;
                padding: 4px 8px;
                border-radius: 6px;
                font-size: 0.75rem;
                font-weight: 600;
                background-color: #dcfce7;
                color: #16a34a;
            }

            /* Progress Bars */
            .custom-progress {
                height: 6px;
                background-color: #f1f5f9;
                border-radius: 3px;
                overflow: hidden;
                margin-top: 8px;
            }
            .custom-progress-bar {
                height: 100%;
                border-radius: 3px;
            }

            .pg-orange {
                background-color: #f97316;
            }
            .pg-red {
                background-color: #ef4444;
            }
            .pg-blue {
                background-color: #3b82f6;
            }
            .pg-green {
                background-color: #10b981;
            }

            /* Chart Header */
            .card-header-flex {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 1.5rem;
            }
            .card-title-h3 {
                font-size: 1.125rem;
                font-weight: 700;
                color: #0f172a;
                margin: 0;
            }

            /* Table Styles */
            .table-responsive {
                margin-top: 1rem;
            }
            .table {
                margin-bottom: 0;
            }
            .table th {
                font-size: 0.75rem;
                font-weight: 600;
                color: #94a3b8;
                text-transform: uppercase;
                border-bottom: 1px solid #e2e8f0;
                padding: 1rem;
                background-color: transparent;
            }
            .table td {
                padding: 1rem;
                vertical-align: middle;
                border-bottom: 1px solid #f1f5f9;
                color: #334155;
                font-size: 0.875rem;
            }

            .avatar-box {
                width: 40px;
                height: 40px;
                background-color: #f1f5f9;
                border-radius: 8px;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #64748b;
                font-size: 1.25rem;
            }

            /* Action Badges in Table */
            .badge-action {
                display: inline-flex;
                align-items: center;
                gap: 6px;
                padding: 4px 10px;
                border-radius: 6px;
                font-size: 0.75rem;
                font-weight: 600;
            }
            .action-entry {
                background-color: #dcfce7;
                color: #16a34a;
            }
            .action-exit {
                background-color: #eff6ff;
                color: #2563eb;
            }
            .action-pass {
                background-color: #f3e8ff;
                color: #9333ea;
            }

            /* Status Dots */
            .status-indicator {
                display: inline-flex;
                align-items: center;
                gap: 6px;
                font-weight: 500;
            }
            .dot {
                width: 8px;
                height: 8px;
                border-radius: 50%;
            }
            .dot-green {
                background-color: #10b981;
            }

            /* Link */
            .link-primary {
                color: var(--bs-primary);
                text-decoration: none;
                font-weight: 500;
                font-size: 0.875rem;
            }
            .link-primary:hover {
                text-decoration: underline;
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
                background-color: rgba(15, 23, 42, 0.5);
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
                    color: #0f172a;
                    padding: 0;
                    margin-right: 15px;
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

                .search-container {
                    width: 200px;
                }
            }

            @media (max-width: 767.98px) {
                .search-container {
                    display: none;
                } /* Hide search on mobile header to save space */
                .stat-card-value {
                    font-size: 1.75rem;
                }
            }
        </style>
    </head>
    <body>

        <%@include file="../layout/sidebar.jsp" %>

        <!-- Main Content -->
        <main class="main-content">
            <!-- Header -->
            <header class="top-header">
                <div class="d-flex align-items-center">
                    <button id="mobileToggle" aria-label="Toggle menu">
                        <i class="bi bi-list"></i>
                    </button>
                    <h1 class="page-title">Tổng quan hệ thống</h1>
                </div>

                <div class="header-actions">
                    <div class="search-container">
                        <i class="bi bi-search"></i>
                        <input type="text" class="search-input" placeholder="Tìm biển số, người dùng, vé...">
                    </div>

                    <button class="btn-icon">
                        <i class="bi bi-bell"></i>
                        <span class="notification-dot"></span>
                    </button>
                    <button class="btn-icon">
                        <i class="bi bi-gear"></i>
                    </button>
                </div>
            </header>

            <!-- Content Area -->
            <div class="content-area">

                <!-- Top Stats Row -->
                <div class="row g-4 mb-4">
                    <!-- Stat 1 -->
                    <div class="col-12 col-md-4">
                        <div class="custom-card">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div class="stat-card-title">Tổng doanh thu</div>
                                <div class="stat-icon icon-blue"><i class="bi bi-cash-stack"></i></div>
                            </div>
                            <div class="stat-card-value">$12,450</div>
                            <div class="d-flex align-items-center gap-2 text-muted" style="font-size: 0.875rem;">
                                <span class="trend-badge"><i class="bi bi-arrow-up-right"></i> +12.5%</span> 
                                <span>so với tháng trước</span>
                            </div>
                        </div>
                    </div>

                    <!-- Stat 2 -->
                    <div class="col-12 col-md-4">
                        <div class="custom-card">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div class="stat-card-title">Xe đang đỗ</div>
                                <div class="stat-icon icon-orange"><i class="bi bi-car-front"></i></div>
                            </div>
                            <div class="stat-card-value mb-2">843</div>
                            <div>
                                <div class="d-flex justify-content-between text-muted" style="font-size: 0.75rem;">
                                    <span>Tỷ lệ lấp đầy</span>
                                    <span class="fw-bold text-dark">85%</span>
                                </div>
                                <div class="custom-progress">
                                    <div class="custom-progress-bar pg-orange" style="width: 85%"></div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Stat 3 -->
                    <div class="col-12 col-md-4">
                        <div class="custom-card">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div class="stat-card-title">Vé đã bán</div>
                                <div class="stat-icon icon-purple"><i class="bi bi-card-heading"></i></div>
                            </div>
                            <div class="stat-card-value">120</div>
                            <div class="d-flex align-items-center gap-2 text-muted" style="font-size: 0.875rem;">
                                <span class="trend-badge"><i class="bi bi-arrow-up-right"></i> +4.2%</span> 
                                <span>Đăng ký mới</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Chart and Occupancy Row -->
                <div class="row g-4 mb-4">
                    <!-- Chart Column -->
                    <div class="col-12 col-lg-8">
                        <div class="custom-card d-flex flex-column">
                            <div class="card-header-flex">
                                <h3 class="card-title-h3">Xu hướng doanh thu</h3>
                                <div class="btn-group" role="group">
                                    <input type="radio" class="btn-check" name="btnradio" id="btnWeek" autocomplete="off" checked>
                                    <label class="btn btn-outline-secondary btn-sm" for="btnWeek">Theo tuần</label>

                                    <input type="radio" class="btn-check" name="btnradio" id="btnMonth" autocomplete="off">
                                    <label class="btn btn-outline-secondary btn-sm" for="btnMonth">Theo tháng</label>
                                </div>
                            </div>
                            <div class="flex-grow-1" style="position: relative; min-height: 250px;">
                                <canvas id="revenueChart"></canvas>
                            </div>
                        </div>
                    </div>

                    <!-- Occupancy Column -->
                    <div class="col-12 col-lg-4">
                        <div class="custom-card">
                            <h3 class="card-title-h3 mb-4">Mật độ bãi xe</h3>

                            <div class="mb-4">
                                <div class="d-flex justify-content-between text-sm mb-1">
                                    <span class="fw-medium text-dark">Nhà xe phía Bắc (A)</span>
                                    <span class="text-muted small">230/250</span>
                                </div>
                                <div class="custom-progress">
                                    <div class="custom-progress-bar pg-red" style="width: 92%"></div>
                                </div>
                            </div>

                            <div class="mb-4">
                                <div class="d-flex justify-content-between text-sm mb-1">
                                    <span class="fw-medium text-dark">Bãi xe phía Nam (B)</span>
                                    <span class="text-muted small">145/300</span>
                                </div>
                                <div class="custom-progress">
                                    <div class="custom-progress-bar pg-blue" style="width: 48%"></div>
                                </div>
                            </div>

                            <div class="mb-4">
                                <div class="d-flex justify-content-between text-sm mb-1">
                                    <span class="fw-medium text-dark">Tầng hầm (VIP)</span>
                                    <span class="text-muted small">45/50</span>
                                </div>
                                <div class="custom-progress">
                                    <div class="custom-progress-bar pg-orange" style="width: 90%"></div>
                                </div>
                            </div>

                            <div>
                                <div class="d-flex justify-content-between text-sm mb-1">
                                    <span class="fw-medium text-dark">Khu vực khách</span>
                                    <span class="text-muted small">12/80</span>
                                </div>
                                <div class="custom-progress">
                                    <div class="custom-progress-bar pg-green" style="width: 15%"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Recent Activity Table -->
                <div class="custom-card">
                    <div class="card-header-flex mb-0">
                        <h3 class="card-title-h3">Hoạt động gần đây</h3>
                        <a href="#" class="link-primary">Xem tất cả <i class="bi bi-arrow-right"></i></a>
                    </div>

                    <div class="table-responsive">
                        <table class="table table-borderless text-nowrap">
                            <thead>
                                <tr>
                                    <th>XE / CHỦ SỞ HỮU</th>
                                    <th>VỊ TRÍ</th>
                                    <th>HÀNH ĐỘNG</th>
                                    <th>THỜI GIAN</th>
                                    <th>TRẠNG THÁI</th>
                                    <th>SỐ TIỀN</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Row 1 -->
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-3">
                                            <div class="avatar-box"><i class="bi bi-car-front-fill"></i></div>
                                            <div>
                                                <div class="fw-bold text-dark">ABC-1234</div>
                                                <div class="text-muted" style="font-size: 0.75rem;">BMW X5 - Đen</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td>Nhà xe phía Bắc (A)</td>
                                    <td><span class="badge-action action-entry"><i class="bi bi-box-arrow-in-right"></i> Vào</span></td>
                                    <td>24 Th10, 08:30 Sáng</td>
                                    <td><span class="status-indicator"><span class="dot dot-green"></span>Hoạt động</span></td>
                                    <td class="fw-bold text-dark">-</td>
                                </tr>

                                <!-- Row 2 -->
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-3">
                                            <div class="avatar-box"><i class="bi bi-person-fill"></i></div>
                                            <div>
                                                <div class="fw-bold text-dark">XYZ-9876</div>
                                                <div class="text-muted" style="font-size: 0.75rem;">Honda Civic - Bạc</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td>Bãi xe phía Nam (B)</td>
                                    <td><span class="badge-action action-exit"><i class="bi bi-box-arrow-right"></i> Ra</span></td>
                                    <td>24 Th10, 08:15 Sáng</td>
                                    <td><span class="status-indicator"><span class="dot dot-green"></span>Đã thanh toán</span></td>
                                    <td class="fw-bold text-dark">$12.50</td>
                                </tr>

                                <!-- Row 3 -->
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-3">
                                            <div class="avatar-box"><i class="bi bi-car-front-fill"></i></div>
                                            <div>
                                                <div class="fw-bold text-dark">LMN-4567</div>
                                                <div class="text-muted" style="font-size: 0.75rem;">Tesla Model 3 - Trắng</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td>Tầng hầm (VIP)</td>
                                    <td><span class="badge-action action-pass"><i class="bi bi-patch-check-fill"></i> Vé</span></td>
                                    <td>24 Th10, 08:05 Sáng</td>
                                    <td><span class="status-indicator"><span class="dot dot-green"></span>Đã xác minh</span></td>
                                    <td class="fw-bold text-dark">Tháng</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
        </main>

        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

        <script>
            // --- Mobile Sidebar Toggle Logic ---
            const mobileToggle = document.getElementById('mobileToggle');
            const sidebar = document.getElementById('sidebar');
            const overlay = document.getElementById('sidebarOverlay');

            function toggleMenu() {
                sidebar.classList.toggle('active');
                overlay.classList.toggle('active');

                // Prevent body scroll when menu is open on mobile
                if (sidebar.classList.contains('active')) {
                    document.body.style.overflow = 'hidden';
                } else {
                    document.body.style.overflow = '';
                }
            }

            mobileToggle.addEventListener('click', toggleMenu);
            overlay.addEventListener('click', toggleMenu);

            // --- Chart.js Setup ---
            const ctx = document.getElementById('revenueChart').getContext('2d');

            // Tạo gradient màu xanh cho biểu đồ
            let gradient = ctx.createLinearGradient(0, 0, 0, 300);
            gradient.addColorStop(0, 'rgba(59, 130, 246, 0.25)'); // Màu xanh nhạt trên cùng
            gradient.addColorStop(1, 'rgba(59, 130, 246, 0)');    // Mờ dần xuống dưới

            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
                    datasets: [{
                            label: 'Doanh thu',
                            data: [2500, 3800, 5200, 4100, 4800, 6500, 7200],
                            borderColor: '#3b82f6', // Màu viền xanh
                            backgroundColor: gradient,
                            borderWidth: 3,
                            pointBackgroundColor: '#ffffff',
                            pointBorderColor: '#3b82f6',
                            pointBorderWidth: 2,
                            pointRadius: 4,
                            pointHoverRadius: 6,
                            fill: true,
                            tension: 0.4 // Độ cong mượt mà của đường line
                        }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {display: false},
                        tooltip: {
                            backgroundColor: '#0f172a',
                            padding: 12,
                            titleFont: {family: 'Inter', size: 13},
                            bodyFont: {family: 'Inter', size: 14, weight: 'bold'},
                            displayColors: false,
                            callbacks: {
                                label: function (context) {
                                    return '$' + context.parsed.y;
                                }
                            }
                        }
                    },
                    scales: {
                        y: {
                            display: false, // Ẩn cột dọc (Y-axis) giống thiết kế
                            beginAtZero: true
                        },
                        x: {
                            grid: {
                                display: false, // Ẩn lưới sọc dọc
                                drawBorder: false
                            },
                            ticks: {
                                font: {family: 'Inter', size: 12},
                                color: '#94a3b8'
                            }
                        }
                    }
                }
            });
        </script>
    </body>
</html>
