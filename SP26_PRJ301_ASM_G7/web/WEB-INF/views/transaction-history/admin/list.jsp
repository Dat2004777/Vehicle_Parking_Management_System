<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lịch sử giao dịch</title>

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
                --border-color: #e2e8f0;
            }

            body {
                font-family: 'Inter', sans-serif;
                background-color: var(--bg-body);
                color: var(--text-main);
                overflow-x: hidden;
            }

            /* Main Layout */
            .main-content {
                margin-left: var(--sidebar-width);
                min-height: 100vh;
                display: flex;
                flex-direction: column;
                transition: margin-left 0.3s ease;
            }

            /* Header */
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
                color: #0f172a;
                margin: 0;
            }

            .content-area {
                padding: 2.5rem 2rem;
                flex-grow: 1;
                max-width: 1400px;
                margin: 0 auto;
                width: 100%;
            }

            .page-subtitle {
                color: #64748b;
                font-size: 0.95rem;
                margin-bottom: 2rem;
            }

            /* Filter Bar */
            .filter-bar {
                background-color: #ffffff;
                border-radius: 12px;
                padding: 1rem 1.5rem;
                box-shadow: 0 1px 2px rgba(0,0,0,0.02);
                border: 1px solid var(--border-color);
                margin-bottom: 1.5rem;
                display: flex;
                gap: 1rem;
                align-items: center;
                flex-wrap: wrap;
            }

            .search-wrapper {
                position: relative;
                flex-grow: 1;
                min-width: 250px;
            }
            .search-wrapper i {
                position: absolute;
                left: 1rem;
                top: 50%;
                transform: translateY(-50%);
                color: #94a3b8;
            }
            .search-wrapper input {
                padding-left: 2.5rem;
                border: none;
                background: #f8fafc;
                border-radius: 8px;
                height: 42px;
                font-size: 0.95rem;
                width: 100%;
                outline: none;
            }
            .search-wrapper input:focus {
                box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
            }

            .filter-select {
                border: 1px solid #cbd5e1;
                border-radius: 8px;
                height: 42px;
                padding: 0 1rem;
                font-size: 0.95rem;
                color: #334155;
                min-width: 150px;
                background-color: #fff;
                cursor: pointer;
                outline: none;
            }
            .filter-select:focus {
                border-color: var(--bs-primary);
            }

            /* Card */
            .custom-card {
                background-color: #ffffff;
                border-radius: 16px;
                border: 1px solid var(--border-color);
                box-shadow: 0 1px 3px rgba(0,0,0,0.02);
                padding: 1.5rem;
            }

            /* Table */
            .table-scroll-container {
                max-height: 400px;
                overflow-y: auto;
                position: relative;
                border-radius: 8px;
            }

            .table-scroll-container::-webkit-scrollbar {
                width: 6px;
            }
            .table-scroll-container::-webkit-scrollbar-track {
                background: #f1f5f9;
            }
            .table-scroll-container::-webkit-scrollbar-thumb {
                background: #cbd5e1;
                border-radius: 10px;
            }
            .table-scroll-container::-webkit-scrollbar-thumb:hover {
                background: #94a3b8;
            }

            .table {
                margin-bottom: 0;
            }

            .table thead {
                position: sticky;
                top: 0;
                z-index: 10;
                background-color: #ffffff;
                box-shadow: 0 1px 0 var(--border-color);
            }
            .table th {
                font-size: 0.75rem;
                font-weight: 600;
                color: #94a3b8;
                text-transform: uppercase;
                border-bottom: none;
                padding: 1rem 0.75rem;
                background-color: transparent;
            }
            .table td {
                padding: 1.25rem 0.75rem;
                vertical-align: middle;
                border-bottom: 1px solid #f1f5f9;
                color: var(--text-main);
                font-size: 0.875rem;
            }
            .table tbody tr:last-child td {
                border-bottom: none;
            }

            .clickable-row {
                cursor: pointer;
                transition: all 0.2s ease-in-out;
            }
            .clickable-row:hover {
                background-color: #f8fafc !important;
                transform: translateY(-1px);
                box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
            }
            .clickable-row td {
                border-bottom: 1px solid transparent;
            }

            /* Table content style */
            .tx-code {
                font-weight: 600;
                color: #475569;
            }
            .price-text {
                font-weight: 700;
                color: var(--text-dark);
            }
            .customer-info {
                display: flex;
                align-items: center;
                gap: 12px;
            }
            .avatar-circle {
                width: 38px;
                height: 38px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: 600;
                font-size: 0.85rem;
                flex-shrink: 0;
                background-color: #f1f5f9;
                color: #475569;
            }
            .customer-name {
                font-weight: 600;
                color: var(--text-dark);
            }

            .status-badge {
                display: inline-flex;
                align-items: center;
                gap: 6px;
                padding: 4px 12px;
                border-radius: 20px;
                font-size: 0.75rem;
                font-weight: 600;
            }
            .status-success {
                background-color: #dcfce7;
                color: #16a34a;
            }
            .status-warning {
                background-color: #fef3c7;
                color: #d97706;
            }
            .status-danger {
                background-color: #fee2e2;
                color: #dc2626;
            }

            .table-footer {
                padding-top: 1.5rem;
                margin-top: 0.5rem;
                font-size: 0.9rem;
                color: #64748b;
                border-top: 1px solid var(--border-color);
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
                    color: var(--text-dark);
                    padding: 0;
                }
                .top-header {
                    padding: 0 1.5rem;
                    justify-content: flex-start;
                    gap: 1rem;
                }
                .content-area {
                    padding: 1.5rem;
                }
                .sidebar-overlay.active {
                    display: block;
                    opacity: 1;
                }
                .filter-bar {
                    flex-direction: column;
                    align-items: stretch;
                }
                .table-container {
                    overflow-x: auto;
                }
            }
        </style>
    </head>
    <body>

        <jsp:include page="../../layout/admin-sidebar.jsp">
            <jsp:param name="activePage" value="adminTransactionHistory" />
        </jsp:include>

        <div class="sidebar-overlay" id="sidebarOverlay"></div>

        <main class="main-content">
            <header class="top-header">
                <div class="d-flex align-items-center gap-3 w-100">
                    <button id="mobileToggle" aria-label="Toggle menu">
                        <i class="bi bi-list"></i>
                    </button>
                    <h1 class="page-title-header">Quản lý lịch sử giao dịch</h1>
                </div>
            </header>

            <div class="content-area">
                <p class="page-subtitle">Theo dõi chi tiết các giao dịch thanh toán trong hệ thống.</p>

                <form action="${pageContext.request.contextPath}/admin/transaction-history" method="GET">
                    <div class="filter-bar">
                        <div class="search-wrapper">
                            <i class="bi bi-search"></i>
                            <input type="text" name="search" placeholder="Tìm kiếm theo mã GD hoặc tên khách hàng" value="${param.search}">
                        </div>

                        <select name="serviceType" class="filter-select" onchange="this.form.submit()">
                            <option value="">Loại dịch vụ</option>
                            <option value="subscription" ${param.serviceType == 'subscription' ? 'selected' : ''}>Vé tháng</option>
                            <option value="casual" ${param.serviceType == 'casual' ? 'selected' : ''}>Gửi theo giờ</option>
                        </select>

                        <select name="status" class="filter-select" onchange="this.form.submit()">
                            <option value="">Trạng thái</option>
                            <option value="completed" ${param.status == 'completed' ? 'selected' : ''}>Thành công</option>
                            <option value="accepted" ${param.status == 'accepted' ? 'selected' : ''}>Chờ xử lý</option>
                            <option value="failed" ${param.status == 'failed' ? 'selected' : ''}>Thất bại</option>
                        </select>
                    </div>
                </form>

                <div class="custom-card">
                    <div class="table-responsive">
                        <div class="table-scroll-container">
                            <table class="table table-borderless align-middle text-nowrap">
                                <thead>
                                    <tr>
                                        <th>MÃ GD</th>
                                        <th>KHÁCH HÀNG</th>
                                        <th>LOẠI DỊCH VỤ</th>
                                        <th>SỐ TIỀN</th>
                                        <th>PHƯƠNG THỨC</th>
                                        <th>THỜI GIAN</th>
                                        <th>TRẠNG THÁI</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${listTransactions}" var="tx">
                                        <tr class="clickable-row">
                                            <td class="tx-code">${tx.transactionCode}</td>
                                            <td>
                                                <div class="customer-info">
                                                    <div class="avatar-circle">${tx.avatarInitials}</div>
                                                    <span class="customer-name">${tx.customerName}</span>
                                                </div>
                                            </td>
                                            <td>${tx.serviceType}</td>
                                            <td class="price-text"><fmt:formatNumber value="${tx.amount}" pattern="#,###"/>đ</td>
                                            <td>${tx.paymentMethod}</td>
                                            <td><fmt:formatDate value="${tx.paymentTime}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${tx.status == 'completed'}">
                                                        <span class="status-badge status-success">Thành công</span>
                                                    </c:when>
                                                    <c:when test="${tx.status == 'accepted'}">
                                                        <span class="status-badge status-warning">Chờ xử lý</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge status-danger">Thất bại</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty listTransactions}">
                                        <tr>
                                            <td colspan="7" class="text-center py-5 text-muted">
                                                <i class="bi bi-inbox fs-2 d-block mb-2"></i>
                                                Không có dữ liệu giao dịch.
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>

            </div>
        </main>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <script>
                            const mobileToggle = document.getElementById('mobileToggle');
                            const sidebar = document.querySelector('.sidebar');
                            const overlay = document.getElementById('sidebarOverlay');

                            function toggleMenu() {
                                sidebar.classList.toggle('active');
                                overlay.classList.toggle('active');

                                // Khóa không cho cuộn nền ở dưới khi mở menu
                                if (sidebar.classList.contains('active')) {
                                    document.body.style.overflow = 'hidden';
                                } else {
                                    document.body.style.overflow = '';
                                }
                            }

                            if (mobileToggle)
                                mobileToggle.addEventListener('click', toggleMenu);
                            if (overlay)
                                overlay.addEventListener('click', toggleMenu);
        </script>
    </body>
</html>