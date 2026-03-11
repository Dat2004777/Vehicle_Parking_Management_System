<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý nhân viên - Manager</title>

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
            .main-content {
                margin-left: var(--sidebar-width);
                min-height: 100vh;
                display: flex;
                flex-direction: column;
                transition: margin-left 0.3s ease;
            }
            .top-header {
                height: 70px;
                background-color: #ffffff;
                border-bottom: 1px solid var(--border-color);
                display: flex;
                align-items: center;
                padding: 0 2rem;
                position: sticky;
                top: 0;
                z-index: 1030;
            }
            .page-title {
                font-size: 1.5rem;
                font-weight: 700;
                color: var(--text-dark);
                margin-bottom: 0.25rem;
            }
            .content-area {
                padding: 2.5rem 2rem;
                flex-grow: 1;
                max-width: 1400px;
                margin: 0 auto;
                width: 100%;
            }

            /* Filter Bar */
            .filter-bar {
                background-color: #ffffff;
                border-radius: 12px;
                padding: 1rem 1.5rem;
                border: 1px solid var(--border-color);
                margin-bottom: 1.5rem;
                display: flex;
                gap: 1rem;
                align-items: center;
            }
            .search-wrapper {
                position: relative;
                flex-grow: 1;
                max-width: 400px;
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
                background: #f8fafc;
                border: 1px solid #cbd5e1;
                border-radius: 8px;
                height: 42px;
                font-size: 0.95rem;
                width: 100%;
                outline: none;
            }
            .search-wrapper input:focus {
                border-color: var(--bs-primary);
            }

            /* Table Styles */
            .table-container {
                background: #ffffff;
                border-radius: 12px;
                border: 1px solid var(--border-color);
                overflow: hidden;
            }
            .table {
                margin-bottom: 0;
            }
            .table thead th {
                background-color: #ffffff;
                color: #64748b;
                font-weight: 600;
                font-size: 0.75rem;
                text-transform: uppercase;
                padding: 1.25rem 1.5rem;
                border-bottom: 1px solid var(--border-color);
            }
            .table tbody td {
                padding: 1.25rem 1.5rem;
                vertical-align: middle;
                font-size: 0.9rem;
                border-bottom: 1px solid #f1f5f9;
            }

            .emp-info {
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
                background-color: #eff6ff;
                color: #3b82f6;
            }
            .emp-name {
                font-weight: 600;
                color: var(--text-dark);
                display: block;
            }
            .emp-username {
                font-size: 0.75rem;
                color: #64748b;
            }

            .status-badge {
                padding: 6px 14px;
                border-radius: 50rem;
                font-size: 0.75rem;
                font-weight: 600;
            }
            .status-active {
                background-color: #dcfce7;
                color: #166534;
            }
            .status-inactive {
                background-color: #fee2e2;
                color: #991b1b;
            }

            #mobileToggle {
                display: none;
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
                    margin-right: 15px;
                    padding: 0;
                }
                .table-container {
                    overflow-x: auto;
                }
            }
        </style>
    </head>
    <body>

        <jsp:include page="../layout/admin-sidebar.jsp">
            <jsp:param name="activePage" value="adminEmployee" />
        </jsp:include>

        <main class="main-content">
            <header class="top-header">
                <div class="d-flex align-items-center w-100">
                    <button id="mobileToggle"><i class="bi bi-list"></i></button>
                    <span class="fw-bold ms-2 fs-5">Nhân viên bảo vệ</span>
                </div>
            </header>

            <div class="content-area">
                <h1 class="page-title">Đội ngũ bảo vệ</h1>
                <p class="text-muted mb-4">Danh sách các nhân viên đang làm việc tại bãi đỗ xe: <strong class="text-primary">${siteName}</strong></p>

                <form action="${pageContext.request.contextPath}/manager/employee" method="GET">
                    <div class="filter-bar">
                        <div class="search-wrapper">
                            <i class="bi bi-search"></i>
                            <input type="text" name="search" placeholder="Tìm tên, số điện thoại, tài khoản..." value="${param.search}">
                        </div>
                        <button type="submit" class="btn btn-primary d-none d-md-block">Tìm kiếm</button>
                    </div>
                </form>

                <div class="table-container">
                    <table class="table table-hover align-middle">
                        <thead>
                            <tr>
                                <th>HỌ VÀ TÊN</th>
                                <th>SỐ ĐIỆN THOẠI</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${listEmployees}" var="emp">
                            <tr>
                                <td>
                                    <div class="emp-info">
                                        <div class="avatar-circle">${emp.lastName.substring(0,1)}${emp.firstName.substring(0,1)}</div>
                                        <div>
                                            <span class="emp-name">${emp.lastName} ${emp.firstName}</span>
                                        </div>
                                    </div>
                                </td>
                                <td class="fw-medium">${emp.phone}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listEmployees}">
                            <tr>
                                <td colspan="4" class="text-center py-5 text-muted">
                                    <i class="bi bi-people fs-2 d-block mb-2"></i>
                                    Không tìm thấy nhân viên nào phù hợp.
                                </td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>

        <script>
            const mobileToggle = document.getElementById('mobileToggle');
            const sidebar = document.querySelector('.sidebar');
            mobileToggle.addEventListener('click', () => {
                sidebar.classList.toggle('active');
            });
        </script>
    </body>
</html>