<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tổng quan - Manager</title>

        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <style>
            :root {
                --bs-primary: #3b82f6;
                --bg-body: #f8fafc;
                --sidebar-width: 260px;
                --text-main: #334155;
                --text-dark: #0f172a;
                --text-muted: #64748b;
                --border-color: #e2e8f0;
            }

            body {
                font-family: 'Inter', sans-serif;
                background-color: var(--bg-body);
                color: var(--text-main);
                overflow-x: hidden;
            }

            .main-content {
                margin-left: var(--sidebar-width);
                min-height: 100vh;
                display: flex;
                flex-direction: column;
                transition: margin-left 0.3s ease;
            }
            .top-header {
                height: 80px;
                background-color: #ffffff;
                border-bottom: 1px solid var(--border-color);
                display: flex;
                align-items: center;
                padding: 0 2rem;
                position: sticky;
                top: 0;
                z-index: 1030;
            }
            .page-title-header {
                font-size: 1.25rem;
                font-weight: 700;
                color: var(--text-dark);
                margin: 0;
            }

            .content-area {
                padding: 2.5rem 2rem;
                flex-grow: 1;
                max-width: 1400px;
                margin: 0 auto;
                width: 100%;
            }

            /* Welcome Banner */
            .welcome-banner {
                background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
                border-radius: 16px;
                padding: 2rem;
                color: white;
                margin-bottom: 2rem;
                box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.2);
            }
            .welcome-title {
                font-size: 1.75rem;
                font-weight: 700;
                margin-bottom: 0.5rem;
            }
            .welcome-subtitle {
                font-size: 1rem;
                opacity: 0.9;
                margin: 0;
            }

            /* Stats Cards */
            .stat-card {
                background: white;
                border-radius: 16px;
                padding: 1.5rem;
                border: 1px solid var(--border-color);
                box-shadow: 0 1px 3px rgba(0,0,0,0.02);
                display: flex;
                align-items: center;
                gap: 1rem;
            }
            .stat-icon {
                width: 54px;
                height: 54px;
                border-radius: 12px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 1.5rem;
                flex-shrink: 0;
            }
            .stat-info .stat-label {
                color: var(--text-muted);
                font-size: 0.875rem;
                font-weight: 500;
                margin-bottom: 0.25rem;
            }
            .stat-info .stat-value {
                color: var(--text-dark);
                font-size: 1.5rem;
                font-weight: 700;
                line-height: 1;
            }

            .bg-blue-light {
                background: #eff6ff;
                color: #3b82f6;
            }
            .bg-green-light {
                background: #dcfce7;
                color: #10b981;
            }
            .bg-orange-light {
                background: #fff7ed;
                color: #f97316;
            }
            .bg-purple-light {
                background: #f3e8ff;
                color: #9333ea;
            }

            /* Quick Links */
            .quick-link-card {
                background: white;
                border-radius: 12px;
                padding: 1.5rem;
                border: 1px solid var(--border-color);
                text-align: center;
                text-decoration: none;
                color: var(--text-dark);
                transition: all 0.2s ease;
                display: block;
            }
            .quick-link-card:hover {
                transform: translateY(-3px);
                box-shadow: 0 10px 15px -3px rgba(0,0,0,0.05);
                border-color: #cbd5e1;
                color: var(--bs-primary);
            }
            .quick-link-icon {
                font-size: 2rem;
                margin-bottom: 1rem;
                color: var(--bs-primary);
            }
            .quick-link-title {
                font-weight: 600;
                font-size: 1rem;
                margin: 0;
            }

            /* Responsive */
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
                .main-content {
                    margin-left: 0;
                }
                #mobileToggle {
                    display: block;
                    background: none;
                    border: none;
                    font-size: 1.5rem;
                    color: var(--text-dark);
                    padding: 0;
                    margin-right: 15px;
                }
                .top-header {
                    padding: 0 1.5rem;
                }
                .sidebar-overlay.active {
                    display: block;
                    opacity: 1;
                }
            }
        </style>
    </head>
    <body>

        <jsp:include page="../layout/admin-sidebar.jsp">
            <jsp:param name="activePage" value="adminDashboard" />
        </jsp:include>

        <div class="sidebar-overlay" id="sidebarOverlay"></div>

        <main class="main-content">
            <header class="top-header">
                <div class="d-flex align-items-center w-100">
                    <button id="mobileToggle" aria-label="Toggle menu"><i class="bi bi-list"></i></button>
                    <h1 class="page-title-header">Tổng quan hệ thống</h1>
                </div>
            </header>

            <div class="content-area">

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger shadow-sm border-0"><i class="bi bi-exclamation-triangle-fill me-2"></i>${errorMessage}</div>
                    </c:if>

                <div class="welcome-banner">
                    <h1 class="welcome-title">Xin chào, ${managerName}! 👋</h1>
                    <p class="welcome-subtitle">
                        <c:choose>
                            <c:when test="${not empty mySite}">
                                Bạn đang quản lý trực tiếp bãi xe: <strong>${mySite.siteName}</strong>
                            </c:when>
                            <c:otherwise>
                                Hiện tại bạn chưa được phân công quản lý bãi xe nào.
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <div class="row g-4 mb-5">
                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="stat-card">
                            <div class="stat-icon bg-blue-light"><i class="bi bi-car-front"></i></div>
                            <div class="stat-info">
                                <div class="stat-label">Đang đỗ hiện tại</div>
                                <div class="stat-value">${totalParked != null ? totalParked : 0}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="stat-card">
                            <div class="stat-icon bg-green-light"><i class="bi bi-cash-stack"></i></div>
                            <div class="stat-info">
                                <div class="stat-label">Doanh thu hôm nay</div>
                                <div class="stat-value">
                                    <fmt:formatNumber value="${todayRevenue != null ? todayRevenue : 0}" pattern="#,###" />đ
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="stat-card">
                            <div class="stat-icon bg-orange-light"><i class="bi bi-ticket-detailed"></i></div>
                            <div class="stat-info">
                                <div class="stat-label">Vé tháng hiệu lực</div>
                                <div class="stat-value">${activeSubs != null ? activeSubs : 0}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-sm-6 col-xl-3">
                        <div class="stat-card">
                            <div class="stat-icon bg-purple-light"><i class="bi bi-calendar-check"></i></div>
                            <div class="stat-info">
                                <div class="stat-label">Đặt trước hôm nay</div>
                                <div class="stat-value">${todayBookings != null ? todayBookings : 0}</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row mb-5">
                    <div class="col-12">
                        <div class="stat-card" style="display: block;">
                            <h5 class="fw-bold mb-4 text-dark"><i class="bi bi-graph-up-arrow text-primary me-2"></i>Doanh thu 7 ngày gần nhất</h5>
                            <div style="height: 350px; width: 100%;">
                                <canvas id="revenueChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>

                <h4 class="fw-bold mb-3 text-dark">Truy cập nhanh</h4>
                <div class="row g-4">
                    <div class="col-6 col-md-3">
                        <a href="${ctx}/site" class="quick-link-card">
                            <i class="bi bi-building quick-link-icon"></i>
                            <h3 class="quick-link-title">Bãi xe của tôi</h3>
                        </a>
                    </div>
                    <div class="col-6 col-md-3">
                        <a href="${ctx}/employee" class="quick-link-card">
                            <i class="bi bi-person-workspace quick-link-icon"></i>
                            <h3 class="quick-link-title">Nhân viên</h3>
                        </a>
                    </div>
                    <div class="col-6 col-md-3">
                        <a href="${ctx}/subscription" class="quick-link-card">
                            <i class="bi bi-person-vcard quick-link-icon"></i>
                            <h3 class="quick-link-title">Quản lý vé tháng</h3>
                        </a>
                    </div>
                    <div class="col-6 col-md-3">
                        <a href="${ctx}/transaction-history" class="quick-link-card">
                            <i class="bi bi-clock-history quick-link-icon"></i>
                            <h3 class="quick-link-title">Lịch sử giao dịch</h3>
                        </a>
                    </div>
                </div>

            </div>
        </main>

        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script>
            const mobileToggle = document.getElementById('mobileToggle');
            const sidebar = document.querySelector('.sidebar');
            const overlay = document.getElementById('sidebarOverlay');

            function toggleMenu() {
                if (sidebar)
                    sidebar.classList.toggle('active');
                if (overlay)
                    overlay.classList.toggle('active');
                document.body.style.overflow = sidebar.classList.contains('active') ? 'hidden' : '';
            }

            if (mobileToggle)
                mobileToggle.addEventListener('click', toggleMenu);
            if (overlay)
                overlay.addEventListener('click', toggleMenu);

            // VẼ BIỂU ĐỒ
            document.addEventListener("DOMContentLoaded", function () {
                const ctx = document.getElementById('revenueChart').getContext('2d');

                // Dữ liệu được JSTL bắn từ Controller xuống
                const labels = ${chartLabels != null ? chartLabels : '[]'};
                const data = ${chartData != null ? chartData : '[]'};

                new Chart(ctx, {
                    type: 'line', // Biểu đồ đường (Line Chart)
                    data: {
                        labels: labels,
                        datasets: [{
                                label: 'Doanh thu (VNĐ)',
                                data: data,
                                borderColor: '#3b82f6', // Màu xanh lam
                                backgroundColor: 'rgba(59, 130, 246, 0.1)',
                                borderWidth: 3,
                                pointBackgroundColor: '#ffffff',
                                pointBorderColor: '#3b82f6',
                                pointBorderWidth: 2,
                                pointRadius: 4,
                                pointHoverRadius: 6,
                                fill: true,
                                tension: 0.4 // Làm cong đường nối cho mượt
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {display: false},
                            tooltip: {
                                callbacks: {
                                    label: function (context) {
                                        let value = context.raw || 0;
                                        return value.toLocaleString('vi-VN') + ' đ';
                                    }
                                }
                            }
                        },
                        scales: {
                            y: {
                                beginAtZero: true,
                                border: {display: false},
                                ticks: {
                                    callback: function (value) {
                                        if (value >= 1000000)
                                            return (value / 1000000) + 'M';
                                        if (value >= 1000)
                                            return (value / 1000) + 'k';
                                        return value;
                                    }
                                },
                                grid: {color: '#f1f5f9'}
                            },
                            x: {
                                border: {display: false},
                                grid: {display: false}
                            }
                        }
                    }
                });
            });
        </script>
    </body>
</html>