<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="vi">
    <jsp:include page="/WEB-INF/views/layout/layout.jsp"/>



    <style>
        body {
            padding-top: var(--navbar-height);
        }
        /* Tối ưu hóa card để chứa nội dung cuộn */
        .history-card {
            border-radius: 16px;
            display: flex;
            flex-direction: column;
            /* Đặt chiều cao tĩnh để ép phần body phải cuộn */
            height: calc(100vh - 180px); /* Lấy tổng chiều cao màn hình trừ đi header và padding */
            min-height: 400px;
        }

        .history-card-header {
            border-top-left-radius: 16px;
            border-top-right-radius: 16px;
            background-color: #fff;
            border-bottom: 1px solid #e2e8f0;
            z-index: 10;
        }

        /* Vùng chứa bảng có thể cuộn */
        .history-table-container {
            flex-grow: 1; /* Tự động phình to chiếm hết khoảng trống */
            overflow-y: auto; /* Cho phép cuộn dọc */
            background-color: #fff;
        }

        /* Giữ cố định tiêu đề cột của bảng khi cuộn */
        .history-table-container thead th {
            position: sticky;
            top: 0;
            background-color: #f8fafc;
            z-index: 5;
            box-shadow: 0 1px 0 #e2e8f0; /* Kẻ vạch dưới cho tiêu đề */
            border-bottom: none; /* Bỏ viền mặc định của bootstrap để tránh lỗi khi sticky */
        }

        .history-card-footer {
            border-bottom-left-radius: 16px;
            border-bottom-right-radius: 16px;
            background-color: #f8fafc;
            border-top: 1px solid #e2e8f0;
        }

        /* Custom Scrollbar cho bảng */
        .history-table-container::-webkit-scrollbar {
            width: 8px;
            height: 8px;
        }
        .history-table-container::-webkit-scrollbar-track {
            background: #f1f5f9;
        }
        .history-table-container::-webkit-scrollbar-thumb {
            background: #cbd5e1;
            border-radius: 4px;
        }
        .history-table-container::-webkit-scrollbar-thumb:hover {
            background: #94a3b8;
        }

        /* Tối ưu cho Mobile */
        @media (max-width: 768px) {
            .history-card {
                height: calc(100vh - 130px);
            }
        }
    </style>

    <body>
        <jsp:include page="/WEB-INF/views/layout/staff-header.jsp">
            <jsp:param name="pageTitle" value="Lịch sử ra vào | Smart Parking" />
        </jsp:include>

        <main class="container-fluid d-flex justify-content-center py-3 py-md-4" style="max-width: 1400px;">

            <jsp:include page="/WEB-INF/views/layout/sidebar.jsp">
                <jsp:param name="activepage" value="history" />
            </jsp:include>

            <div class="w-100">
                <div class="d-flex justify-content-between align-items-end mb-3 mb-md-4">
                    <div>
                        <h4 class="fw-bold text-dark mb-1"><i class="bi bi-clock-history me-2 text-primary"></i>Lịch sử ra vào bãi</h4>
                    </div>
                    <a href="${ctx}/dashboard" class="btn btn-outline-secondary fw-bold">Quay lại</a>
                </div>



                <div class="card shadow-sm border-0 history-card">

                    <div class="history-card-header py-3 px-3 px-md-4">
                        <form action="${ctx}/parking/history" method="GET" class="row g-2 align-items-center">
                            <div class="col-12 col-md-4 col-lg-3 mt-2 mt-md-0">
                                <select name="state" class="form-select form-select-sm rounded-pill" onchange="this.form.submit()">
                                    <option value="">Tất cả trạng thái</option>
                                    <option value="parked" ${param.state == 'parked' ? 'selected' : ''}>Đang đỗ</option>
                                    <option value="completed" ${param.state == 'completed' ? 'selected' : ''}>Đã ra</option>
                                </select>
                            </div>
                        </form>
                    </div>

                    <div class="history-table-container p-0">
                        <table class="table table-hover align-middle mb-0 text-nowrap">
                            <thead class="text-muted" style="font-size: 0.85rem;">
                                <tr>
                                    <th class="ps-3 ps-md-4 py-3">Biển số xe</th>
                                    <th class="py-3">Mã thẻ</th>
                                    <th class="py-3">Hành động</th>
                                    <th class="py-3">Thời gian ghi nhận</th>
                                    <th class="py-3">Trạng thái</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <%-- TRƯỜNG HỢP CÓ DỮ LIỆU --%>
                                    <c:when test="${not empty recentLogs}">
                                        <c:forEach var="log" items="${recentLogs}">
                                            <c:choose>
                                                <c:when test="${log.sessionState == 'parked' || log.sessionState == 'PARKED' || log.sessionState == 'ACTIVE'}">
                                                    <c:set var="actionName" value="Xe vào bãi" />
                                                    <c:set var="actionBadge" value="badge-soft-success" /> 
                                                    <c:set var="icon" value="bi-arrow-down-circle" />
                                                    <c:set var="statusText" value="Đang đỗ" />
                                                    <c:set var="statusBadge" value="badge-soft-warning" /> 
                                                </c:when>

                                                <c:when test="${log.sessionState == 'completed' || log.sessionState == 'COMPLETED'}">
                                                    <c:set var="actionName" value="Xe ra bãi" />
                                                    <c:set var="actionBadge" value="badge-soft-warning" /> 
                                                    <c:set var="icon" value="bi-arrow-up-circle" />
                                                    <c:set var="statusText" value="Đã hoàn tất" />
                                                    <c:set var="statusBadge" value="badge-soft-success" /> 
                                                </c:when>

                                                <c:otherwise>
                                                    <c:set var="actionName" value="Lỗi Dữ liệu" />
                                                    <c:set var="actionBadge" value="badge-soft-danger" /> 
                                                    <c:set var="icon" value="bi-exclamation-octagon" />
                                                    <c:set var="statusText" value="Lỗi hệ thống" />
                                                    <c:set var="statusBadge" value="badge-soft-danger" />
                                                </c:otherwise>
                                            </c:choose>

                                            <tr>
                                                <td class="ps-3 ps-md-4">
                                                    <span class="fw-bold text-dark fs-6 text-uppercase">${log.licensePlate}</span>
                                                </td>
                                                <td>
                                                    <span class="badge bg-primary-subtle text-primary font-monospace fw-medium" style="font-size: 0.8rem; padding: 5px 8px;">
                                                        <i class="bi bi-credit-card me-1"></i>${log.cardId}
                                                    </span>
                                                </td>
                                                <td>
                                                    <span class="${actionBadge} d-inline-block">
                                                        <i class="bi ${icon} me-1"></i> ${actionName}
                                                    </span>
                                                </td>
                                                <td>
                                                    <span class="text-muted"><i class="bi bi-clock me-1"></i> ${log.formattedTime}</span>
                                                </td>
                                                <td>
                                                    <span class="${statusBadge} d-inline-block">${statusText}</span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>

                                    <%-- TRƯỜNG HỢP KHÔNG CÓ DỮ LIỆU --%>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="5" class="text-center py-5 text-muted" style="height: 300px; vertical-align: middle;">
                                                <i class="bi bi-inbox fs-1 d-block mb-2 text-light"></i>
                                                <span>Không tìm thấy lịch sử ra vào nào.</span>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>

                    <%-- KHU VỰC CHỨA PHÂN TRANG (PAGINATION) --%>
                    <%-- Chỉ hiện thanh phân trang nếu có nhiều hơn 1 trang --%>
                    <c:if test="${totalPages > 1}">
                        <div class="d-flex justify-content-center bg-white border-top py-3">
                            <nav aria-label="Page navigation">
                                <ul class="pagination pagination-sm mb-0">

                                    <%-- Nút "Trang trước" --%>
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link" href="?page=${currentPage - 1}&state=${param.state}" tabindex="-1">
                                            <i class="bi bi-chevron-left"></i> Trước
                                        </a>
                                    </li>

                                    <%-- Danh sách các số trang --%>
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}&state=${param.state}">${i}</a>
                                        </li>
                                    </c:forEach>

                                    <%-- Nút "Trang sau" --%>
                                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                        <a class="page-link" href="?page=${currentPage + 1}&state=${param.state}">
                                            Sau <i class="bi bi-chevron-right"></i>
                                        </a>
                                    </li>

                                </ul>
                            </nav>
                        </div>
                    </c:if>

                    <div class="history-card-footer py-2 px-3 px-md-4 d-flex justify-content-between align-items-center">
                        <span class="text-muted fw-medium" style="font-size: 0.85rem;">
                            Trang <strong class="text-dark">${currentPage}</strong> / ${totalPages == 0 ? 1 : totalPages}
                        </span>
                        <span class="text-muted" style="font-size: 0.8rem;">
                            <i class="bi bi-info-circle me-1"></i> Hiển thị tối đa 10 bản ghi mỗi trang
                        </span>
                    </div>

                </div>
            </div>
        </main>
    </body>
</html>