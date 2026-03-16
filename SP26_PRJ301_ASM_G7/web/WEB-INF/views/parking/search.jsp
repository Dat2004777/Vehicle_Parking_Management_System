<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Tra Cứu Thẻ Và Xe - ParkStaff</title>
        <jsp:include page="/WEB-INF/views/layout/layout.jsp"/>
        <style>
            body {
                padding-top: var(--navbar-height);
            }
            
            .card-custom {
                border: none;
                border-radius: 12px;
                box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
                background: #fff;
                margin-bottom: 1.5rem;
            }
            .nav-tabs-custom {
                border-bottom: 1px solid #e2e8f0;
                margin-bottom: 1.5rem;
                /* Tối ưu mobile: Cho phép vuốt ngang thay vì rớt dòng */
                flex-wrap: nowrap;
                overflow-x: auto;
                overflow-y: hidden;
                -webkit-overflow-scrolling: touch;
                scrollbar-width: none; /* Ẩn thanh cuộn Firefox */
            }
            .nav-tabs-custom::-webkit-scrollbar {
                display: none; /* Ẩn thanh cuộn Chrome/Safari */
            }
            .nav-tabs-custom .nav-link {
                border: none;
                color: var(--text-muted);
                font-weight: 500;
                padding: 1rem 1.5rem;
                background: transparent;
                white-space: nowrap; /* Không cho rớt chữ trên mobile */
            }
            .nav-tabs-custom .nav-link:hover {
                color: var(--bs-primary);
            }
            .nav-tabs-custom .nav-link.active {
                color: var(--bs-primary);
                border-bottom: 2px solid var(--bs-primary);
                font-weight: 600;
            }
            .search-container {
                background: #fff;
                padding: 1.5rem;
                border-radius: 12px;
                margin-bottom: 2rem;
                box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
            }
            .search-input-group {
                background: #f8fafc;
                border-radius: 8px;
                border: 1px solid #e2e8f0;
                padding: 0.25rem;
            }
            .search-input-group input {
                background: transparent;
                border: none;
                box-shadow: none;
                padding-left: 1rem;
                outline: none;
                width: 100%;
            }
            .btn-search {
                border-radius: 6px;
                font-weight: 500;
                padding: 0.6rem 2rem;
            }
            .info-label {
                font-size: 0.75rem;
                text-transform: uppercase;
                color: var(--text-muted);
                letter-spacing: 0.5px;
                margin-bottom: 0.25rem;
                font-weight: 500;
            }
            .info-value {
                font-size: 1.1rem;
                font-weight: 600;
                color: var(--text-main);
            }
            .value-large {
                font-size: 1.4rem;
            }

            .table-custom th {
                color: var(--text-muted);
                font-weight: 500;
                font-size: 0.85rem;
                border-bottom-width: 1px;
                padding: 1rem;
            }
            .table-custom td {
                padding: 1rem;
                vertical-align: middle;
                color: var(--text-main);
                border-bottom-color: #f1f5f9;
            }
            .footer-status {
                position: fixed;
                bottom: 0;
                left: 0;
                right: 0;
                background: #fff;
                border-top: 1px solid #e2e8f0;
                padding: 0.5rem 2rem;
                font-size: 0.8rem;
                display: flex;
                justify-content: space-between;
                color: var(--text-muted);
                z-index: 1000;
            }
            .empty-state {
                text-align: center;
                padding: 3rem 1rem;
                color: var(--text-muted);
            }
            .empty-state i {
                font-size: 3rem;
                color: #cbd5e1;
                margin-bottom: 1rem;
                display: block;
            }

            /* Tối ưu padding cho màn hình nhỏ (Mobile) */
            @media (max-width: 575.98px) {
                .search-container {
                    padding: 1rem;
                    margin-bottom: 1.5rem;
                }
                .card-custom {
                    padding: 1rem !important;
                }
                .empty-state {
                    padding: 2rem 1rem !important;
                }
                .value-large {
                    font-size: 1.2rem;
                }
                body {
                    padding-bottom: 60px; /* Nhường chỗ cho footer không đè lên content */
                }
            }
        </style>
    </head>
    <body>

        <jsp:include page="/WEB-INF/views/layout/staff-header.jsp" />

        <jsp:include page="/WEB-INF/views/layout/sidebar.jsp">
                <jsp:param name="activepage" value="search" />
            </jsp:include>

        <main class="container-fluid py-3 py-md-4" style="max-width: 1200px;">
            
            <div class="d-flex justify-content-between align-items-end mb-3 mb-md-4">
                    <div>
                        <h4 class="fw-bold text-dark mb-1"><i class="bi bi-search me-2 text-primary"></i>Tra cứu thông tin</h4>
                    </div>
                    <a href="${ctx}/dashboard" class="btn btn-outline-secondary fw-bold">Quay lại</a>
                </div>
            
            <c:set var="activeTab" value="${empty param.type ? 'vehicle' : param.type}" />

            <ul class="nav nav-tabs nav-tabs-custom" id="searchTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${activeTab == 'vehicle' ? 'active' : ''}" id="vehicle-tab" data-bs-toggle="tab" data-bs-target="#vehicle-pane" type="button" role="tab">Tra cứu xe trong bãi</button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${activeTab == 'card' ? 'active' : ''}" id="card-tab" data-bs-toggle="tab" data-bs-target="#card-pane" type="button" role="tab">Tra cứu thẻ xe</button>
                </li>
            </ul>

            <div class="tab-content" id="searchTabsContent">

                <div class="tab-pane fade ${activeTab == 'vehicle' ? 'show active' : ''}" id="vehicle-pane" role="tabpanel">

                    <form action="${ctx}/search" method="GET" class="search-container d-flex flex-column flex-sm-row gap-2 p-3 p-md-4">
                        <input type="hidden" name="type" value="vehicle">
                        <div class="input-group search-input-group flex-grow-1">
                            <span class="input-group-text bg-transparent border-0 text-muted"><i class="bi bi-search"></i></span>
                            <input type="text" name="query" value="${activeTab == 'vehicle' ? param.query : ''}" class="form-control border-0" placeholder="Nhập biển số xe (VD: 30A-123.45)..." required>
                        </div>
                        <button type="submit" class="btn btn-primary btn-search flex-shrink-0 w-100 text-nowrap" style="max-width: max-content;"><i class="bi bi-search me-1"></i> Tìm kiếm</button>
                    </form>

                    <c:choose>
                        <c:when test="${empty param.query || activeTab != 'vehicle'}">
                            <div class="card card-custom p-4 p-md-5 empty-state">
                                <i class="bi bi-car-front"></i>
                                <h5>Nhập biển số xe để tra cứu thông tin</h5>
                                <p class="text-muted mb-0" style="font-size: 0.9rem;">Thông tin bao gồm: Thời gian vào bãi, vị trí đỗ và lịch sử giao dịch.</p>
                            </div>
                        </c:when>

                        <c:when test="${empty vehicleResult}">
                            <div class="card card-custom p-4 p-md-5 empty-state">
                                <i class="bi bi-search" style="color: #ef4444;"></i>
                                <h5 class="text-danger">Không tìm thấy xe mang biển số "${param.query}"</h5>
                                <p class="text-muted mb-0" style="font-size: 0.9rem;">Xe này có thể chưa vào bãi hoặc hệ thống nhận diện bị mờ. Vui lòng thử lại.</p>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <h5 class="fw-bold mb-3 text-primary d-flex align-items-center">
                                <i class="bi bi-bar-chart-fill me-2"></i> Kết quả tra cứu
                            </h5>

                            <div class="row g-3 g-md-4">
                                <div class="col-12">
                                    <div class="card card-custom p-3 p-md-4 mb-3 mb-md-4">
                                        <div class="row g-3 g-md-4">
                                            <div class="col-6 col-md-4">
                                                <div class="info-label">Biển số xe</div>
                                                <div class="info-value value-large text-uppercase">${vehicleResult.licensePlate}</div>
                                            </div>
                                            <div class="col-6 col-md-4">
                                                <div class="info-label">Mã thẻ RFID</div>
                                                <div class="info-value value-large">${not empty vehicleResult.rfid ? vehicleResult.rfid : 'Không có thẻ'}</div>
                                            </div>
                                            <div class="col-12 col-sm-6 col-md-4">
                                                <div class="info-label">Thời gian vào</div>
                                                <div class="info-value">${vehicleResult.entryTime}</div>
                                            </div>
                                            <div class="col-6 col-md-4">
                                                <div class="info-label">Khu vực đỗ</div>
                                                <div class="info-value text-success"><i class="bi bi-circle-fill" style="font-size: 0.5rem; vertical-align: middle;"></i> ${vehicleResult.areaName}</div>
                                            </div>
                                            <div class="col-6 col-md-4">
                                                <div class="info-label">Loại xe</div>
                                                <div class="info-value">${vehicleResult.vehicleType}</div>
                                            </div>
                                            <div class="col-12 col-sm-6 col-md-4">
                                                <div class="info-label">Trạng thái phí</div>
                                                <div>
                                                    <c:if test="${vehicleResult.isMonthlyTicket}">
                                                        <span class="badge badge-soft-success">Vé tháng (Còn hạn)</span>
                                                    </c:if>
                                                    <c:if test="${!vehicleResult.isMonthlyTicket}">
                                                        <span class="badge badge-soft-warning">Vé lượt (Thu khi ra)</span>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="card card-custom p-0 overflow-hidden">
                                        <div class="d-flex justify-content-between align-items-center p-3 p-md-4 border-bottom">
                                            <h6 class="fw-bold mb-0 text-muted"><i class="bi bi-clock-history me-2"></i> Lịch sử ra vào gần đây</h6>
                                        </div>
                                        <div class="table-responsive">
                                            <table class="table table-custom table-hover mb-0 text-nowrap">
                                                <thead>
                                                    <tr>
                                                        <th class="ps-3 ps-md-4">Thời gian</th>
                                                        <th>Hành động</th>
                                                        <th class="pe-3 pe-md-4">Nhân viên trực</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${vehicleTransactions}" var="txn">
                                                        <tr>
                                                            <td class="ps-3 ps-md-4">${txn.time}</td>
                                                            <td>
                                                                <c:if test="${txn.action == 'IN'}">
                                                                    <span class="badge badge-soft-success"><i class="bi bi-box-arrow-in-right"></i> Vào</span>
                                                                </c:if>
                                                                <c:if test="${txn.action == 'OUT'}">
                                                                    <span class="badge badge-soft-warning"><i class="bi bi-box-arrow-right"></i> Ra</span>
                                                                </c:if>
                                                            </td>
                                                            <td class="pe-3 pe-md-4">${txn.staffName}</td>
                                                        </tr>
                                                    </c:forEach>
                                                    <c:if test="${empty vehicleTransactions}">
                                                        <tr><td colspan="3" class="text-center text-muted py-4">Chưa có lịch sử giao dịch</td></tr>
                                                    </c:if>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="tab-pane fade ${activeTab == 'card' ? 'show active' : ''}" id="card-pane" role="tabpanel">

                    <form action="${pageContext.request.contextPath}/staff/search" method="GET" class="search-container d-flex flex-column flex-sm-row gap-2 p-3 p-md-4">
                        <input type="hidden" name="type" value="card">
                        <div class="input-group search-input-group flex-grow-1">
                            <span class="input-group-text bg-transparent border-0 text-muted"><i class="bi bi-search"></i></span>
                            <input type="text" name="query" value="${activeTab == 'card' ? param.query : ''}" class="form-control border-0" placeholder="Nhập mã số thẻ RFID..." required autofocus>
                        </div>
                        <button type="submit" class="btn btn-primary btn-search flex-shrink-0 w-100 text-nowrap" style="max-width: max-content;"><i class="bi bi-search me-1"></i> Tìm kiếm</button>
                    </form>

                    <c:choose>
                        <c:when test="${empty param.query || activeTab != 'card'}">
                            <div class="card card-custom p-4 p-md-5 empty-state">
                                <i class="bi bi-credit-card-2-front"></i>
                                <h5>Nhập mã thẻ RFID để tra cứu</h5>
                                <p class="text-muted mb-0" style="font-size: 0.9rem;">Xem trạng thái thẻ, thông tin chủ sở hữu và lịch sử quẹt thẻ.</p>
                            </div>
                        </c:when>

                        <c:when test="${empty cardResult}">
                            <div class="card card-custom p-4 p-md-5 empty-state">
                                <i class="bi bi-search" style="color: #ef4444;"></i>
                                <h5 class="text-danger">Không tìm thấy mã RFID "${param.query}"</h5>
                                <p class="text-muted mb-0" style="font-size: 0.9rem;">Thẻ này có thể chưa được đăng ký trong hệ thống.</p>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <h5 class="fw-bold mb-3 text-primary d-flex align-items-center">
                                <i class="bi bi-credit-card-2-front me-2"></i> Kết quả tra cứu
                            </h5>

                            <c:if test="${cardResult.currentlyParked}">
                                <div class="alert alert-warning d-flex flex-column flex-sm-row align-items-start align-items-sm-center mb-3 mb-md-4 shadow-sm p-3" role="alert" style="border-radius: 12px; border-left: 5px solid #ffc107; background-color: #fffbeb; border-color: #fef3c7;">
                                    <i class="bi bi-exclamation-triangle-fill fs-3 me-3 mb-2 mb-sm-0 text-warning"></i>
                                    <div>
                                        <h6 class="fw-bold mb-1 text-dark" style="font-size: 0.95rem;">Lưu ý: Thẻ này đang trong bãi!</h6>
                                        <div class="text-muted" style="font-size: 0.85rem;">
                                            Biển số: <strong class="text-dark text-uppercase">${cardResult.currentParkedPlate}</strong> 
                                            <span class="mx-1 mx-sm-2">|</span> 
                                            Giờ vào: <strong class="text-dark">${cardResult.currentEntryTime}</strong>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <div class="card card-custom p-3 p-md-4 mb-3 mb-md-4">
                                <div class="row g-3 g-md-4">
                                    <div class="col-6 col-md-4">
                                        <div class="info-label">Mã thẻ RFID</div>
                                        <div class="info-value value-large">${cardResult.rfid}</div>
                                    </div>
                                    <div class="col-6 col-md-4">
                                        <div class="info-label">Trạng thái</div>
                                        <div>
                                            <c:choose>
                                                <c:when test="${cardResult.status == 'ACTIVE'}">
                                                    <span class="badge badge-soft-success">Hoạt động</span>
                                                </c:when>
                                                <c:when test="${cardResult.status == 'LOST'}">
                                                    <span class="badge badge-soft-danger">Báo mất</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-soft-warning">Đang khóa</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    <div class="col-12 col-sm-6 col-md-4">
                                        <div class="info-label">Loại thẻ</div>
                                        <div class="info-value">${cardResult.cardType}</div>
                                    </div>

                                    <c:choose>
                                        <%-- TRƯỜNG HỢP 1: THẺ VÉ THÁNG --%>
                                        <c:when test="${cardResult.cardType == 'Vé tháng'}">
                                            <div class="col-12 col-sm-6 col-md-4">
                                                <div class="info-label">Chủ sở hữu</div>
                                                <div class="info-value d-flex align-items-center gap-2">
                                                    <span class="badge-outline"><i class="bi bi-person"></i></span> ${cardResult.ownerName}
                                                </div>
                                            </div>
                                            <div class="col-6 col-md-4">
                                                <div class="info-label">Biển số đăng ký</div>
                                                <div class="info-value text-uppercase">${cardResult.registeredPlate}</div>
                                            </div>
                                            <div class="col-6 col-md-4">
                                                <div class="info-label">Ngày hết hạn</div>
                                                <div class="info-value text-danger">${cardResult.expiryDate}</div>
                                            </div>
                                        </c:when>

                                        <%-- TRƯỜNG HỢP 2: THẺ BOOKING ĐẶT TRƯỚC --%>
                                        <c:when test="${cardResult.cardType == 'Vé đặt trước'}">
                                            <div class="col-12 col-sm-6 col-md-4">
                                                <div class="info-label">Người đặt chỗ</div>
                                                <div class="info-value d-flex align-items-center gap-2">
                                                    <span class="badge-outline"><i class="bi bi-person-check"></i></span> ${cardResult.ownerName}
                                                </div>
                                            </div>
                                            <div class="col-6 col-md-4">
                                                <div class="info-label">Giờ bắt đầu đỗ</div>
                                                <div class="info-value text-success">${cardResult.bookingStart}</div>
                                            </div>
                                            <div class="col-6 col-md-4">
                                                <div class="info-label">Giờ kết thúc đỗ</div>
                                                <div class="info-value text-danger">${cardResult.bookingEnd}</div>
                                            </div>
                                        </c:when>

                                        <%-- TRƯỜNG HỢP 3: VÉ LƯỢT (Không in thêm gì cả) --%>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="card card-custom p-0 overflow-hidden">
                                <div class="d-flex justify-content-between align-items-center p-3 p-md-4 border-bottom">
                                    <h6 class="fw-bold mb-0 text-muted"><i class="bi bi-clock-history me-2"></i> Giao dịch gần nhất</h6>
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-custom table-hover mb-0 text-nowrap">
                                        <thead>
                                            <tr>
                                                <th class="ps-3 ps-md-4">Thời gian</th>
                                                <th>Hành động</th>
                                                <th>Biển số đi kèm</th>
                                                <th class="pe-3 pe-md-4">Nhân viên trực</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${cardTransactions}" var="txn">
                                                <tr>
                                                    <td class="ps-3 ps-md-4">${txn.time}</td>
                                                    <td>
                                                        <c:if test="${txn.action == 'IN'}">
                                                            <span class="badge badge-soft-success"><i class="bi bi-box-arrow-in-right"></i> Vào</span>
                                                        </c:if>
                                                        <c:if test="${txn.action == 'OUT'}">
                                                            <span class="badge badge-soft-warning"><i class="bi bi-box-arrow-right"></i> Ra</span>
                                                        </c:if>
                                                    </td>
                                                    <td class="text-uppercase fw-bold">${txn.licensePlate}</td>
                                                    <td class="pe-3 pe-md-4">${txn.staffName}</td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty cardTransactions}">
                                                <tr><td colspan="4" class="text-center text-muted py-4">Thẻ này chưa có lịch sử giao dịch.</td></tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>

    </body>
</html>