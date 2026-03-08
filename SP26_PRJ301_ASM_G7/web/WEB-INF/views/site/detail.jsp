<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="defaultBg" value="https://images.unsplash.com/photo-1506521781263-d8422e82f27a?q=80&w=1920&auto=format&fit=crop" />
<c:set var="heroBg" value="defaultBg" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Bãi xe: ${site.siteName}</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">

    <style>
        body { background-color: #f0f2f5; font-family: 'Segoe UI', system-ui, sans-serif; }

        /* HERO SECTION */
        .site-hero {
            position: relative;
            height: 350px;
            /* Dùng biến đã xử lý ở trên */
            background-image: url('${heroBg}');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
        }

        /* Lớp phủ đen mờ để làm nổi bật chữ */
        .site-hero::before {
            content: "";
            position: absolute; top: 0; left: 0; right: 0; bottom: 0;
            background: linear-gradient(to bottom, rgba(0,0,0,0.7), rgba(0,0,0,0.2));
        }

        /* CONTENT CONTAINER (Trồi lên trên Hero) */
        .content-overlay {
            position: relative;
            margin-top: -80px; /* Kéo nội dung lên */
            z-index: 10;
        }

        /* Sticky Sidebar */
        .sticky-col { position: sticky; top: 20px; }

        /* Card Effects */
        .hover-card { transition: transform 0.2s, box-shadow 0.2s; }
        .hover-card:hover { transform: translateY(-3px); box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15)!important; }

        /* Mobile Responsive Tweaks */
        @media (max-width: 768px) {
            .site-hero { height: 220px; }
            .content-overlay { margin-top: -40px; }
            .sticky-col { position: static; } /* Tắt sticky trên mobile */
        }
    </style>
</head>

<body>

    <div class="site-hero d-flex align-items-center justify-content-center">
        <div class="text-center text-white position-relative px-3" style="z-index: 2; margin-bottom: 40px;">
            <span class="badge rounded-pill bg-primary bg-opacity-75 mb-2 px-3">
                <i class="bi bi-geo-alt-fill me-1"></i> ${site.region}
            </span>
            <h1 class="fw-bold display-5 text-shadow">${site.siteName}</h1>
            <p class="opacity-75"><i class="bi bi-map me-1"></i>${site.address}</p>
        </div>
    </div>

    <div class="container content-overlay pb-5">
        <div class="row g-4">
            
            <div class="col-lg-4">
                <div class="card shadow border-0 rounded-4 sticky-col">
                    <div class="card-body p-4">
                        
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h5 class="fw-bold text-secondary m-0">Thông tin vận hành</h5>
                            <c:choose>
                                <c:when test="${site.siteStatus == 'ACTIVE'}">
                                    <span class="badge bg-success-subtle text-success border border-success px-2">${site.siteStatus.label}</span>
                                </c:when>
                                <c:when test="${site.siteStatus == 'MAINTENANCE'}">
                                    <span class="badge bg-warning-subtle text-warning border border-warning px-2">${site.siteStatus.label}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger-subtle text-danger border border-danger px-2">${site.siteStatus.label}</span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <ul class="list-group list-group-flush mb-4 rounded-3">
                            <li class="list-group-item bg-light d-flex justify-content-between">
                                <span class="text-muted small fw-bold">Mã ID</span>
                                <span class="fw-bold">#${site.siteId}</span>
                            </li>
                            <li class="list-group-item bg-light d-flex justify-content-between">
                                <span class="text-muted small fw-bold">Quản lý</span>
                                <span class="fw-bold text-primary">
                                    ${site.managerId != null ? site.managerId : '<span class="text-muted fst-italic">Chưa có</span>'}
                                </span>
                            </li>
                        </ul>

                        <div class="row g-2 mb-4">
                            <div class="col-6">
                                <div class="p-3 bg-primary-subtle rounded-3 text-center">
                                    <h3 class="fw-bold text-primary mb-0">${totalCap}</h3>
                                    <small class="text-primary fw-bold" style="font-size: 0.7rem;">TỔNG SLOT</small>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="p-3 bg-success-subtle rounded-3 text-center">
                                    <h3 class="fw-bold text-success mb-0">${totalCap - totalOcc}</h3>
                                    <small class="text-success fw-bold" style="font-size: 0.7rem;">CÒN TRỐNG</small>
                                </div>
                            </div>
                        </div>

                        <div class="d-grid gap-2">
                            <a href="update-site?id=${site.siteId}" class="btn btn-warning text-white fw-bold shadow-sm">
                                <i class="bi bi-pencil-square me-2"></i>Cập nhật
                            </a>
                            <a href="list-site" class="btn btn-outline-secondary fw-bold">
                                <i class="bi bi-arrow-left me-2"></i>Quay lại
                            </a>
                            <a href="delete-site?id=${site.siteId}" onclick="return confirm('CẢNH BÁO: Xóa bãi xe này sẽ xóa toàn bộ khu vực liên quan?')" class="btn btn-link text-danger text-decoration-none btn-sm">
                                Xóa bãi xe này
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-8">
                
                <div class="card border-0 shadow-sm rounded-4 mb-4">
                    <div class="card-header bg-white p-2 border-0">
                        <ul class="nav nav-pills nav-fill gap-2 p-1" id="siteTabs" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active fw-bold rounded-3 py-2" id="area-tab" data-bs-toggle="tab" data-bs-target="#area-content" type="button" role="tab">
                                    <i class="bi bi-grid-fill me-2"></i>Khu vực & Sức chứa
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link fw-bold rounded-3 py-2" id="staff-tab" data-bs-toggle="tab" data-bs-target="#staff-content" type="button" role="tab">
                                    <i class="bi bi-people-fill me-2"></i>Nhân viên phụ trách
                                </button>
                            </li>
                        </ul>
                    </div>
                </div>

                <div class="tab-content" id="siteTabsContent">
                    
                    <div class="tab-pane fade show active" id="area-content" role="tabpanel">
                        
                        <div class="d-flex justify-content-between align-items-center mb-3 px-2">
                            <h5 class="fw-bold text-secondary m-0">Danh sách Khu vực</h5>
                            <a href="create-area?siteId=${site.siteId}" class="btn btn-primary btn-sm rounded-pill px-3 fw-bold shadow-sm">
                                <i class="bi bi-plus-lg"></i> Thêm khu vực
                            </a>
                        </div>

                        <c:if test="${empty areaList}">
                            <div class="text-center py-5 bg-white rounded-4 shadow-sm border border-dashed">
                                <i class="bi bi-cone-striped display-3 text-muted opacity-25"></i>
                                <h5 class="text-muted mt-3">Chưa có khu vực nào</h5>
                            </div>
                        </c:if>

                        <div class="row g-3">
                            <c:forEach var="area" items="${areaList}">
                                <c:set var="percent" value="${area.totalSlots == 0 ? 0 : (area.totalSlots * 100 / area.totalSlots)}" />
                                <c:set var="bgClass" value="bg-success" />
                                <c:if test="${percent > 70}"><c:set var="bgClass" value="bg-warning" /></c:if>
                                <c:if test="${percent >= 100}"><c:set var="bgClass" value="bg-danger" /></c:if>

                                <div class="col-md-6">
                                    <div class="card h-100 border-0 shadow-sm rounded-3 hover-card bg-white">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <div>
                                                    <h6 class="fw-bold mb-1 text-dark fs-5">${area.areaName}</h6>
                                                    <span class="badge ${area.vehicleTypeId == 1 ? 'bg-primary-subtle text-primary' : 'bg-success-subtle text-success'} border border-opacity-10 rounded-pill">
                                                        ${area.vehicleTypeId == 1 ? 'Ô TÔ' : 'XE MÁY'}
                                                    </span>
                                                </div>
                                                <div class="dropdown">
                                                    <button class="btn btn-light btn-sm rounded-circle" data-bs-toggle="dropdown"><i class="bi bi-three-dots-vertical"></i></button>
                                                    <ul class="dropdown-menu dropdown-menu-end shadow border-0">
                                                        <li><a class="dropdown-item small" href="update-area?id=${area.areaId}">Cấu hình</a></li>
                                                        <li><a class="dropdown-item small text-danger" href="delete-area?id=${area.areaId}" onclick="return confirm('Xóa?')">Xóa</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="d-flex justify-content-between small mb-1">
                                                <span class="fw-bold">${area.totalSlots} xe</span>
                                                <span class="text-muted">/ ${area.totalSlots} chỗ</span>
                                            </div>
                                            <div class="progress" style="height: 6px;">
                                                <div class="progress-bar ${bgClass}" style="width: ${percent}%"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="tab-pane fade" id="staff-content" role="tabpanel">
                        
                        <div class="d-flex justify-content-between align-items-center mb-3 px-2">
                            <h5 class="fw-bold text-secondary m-0">Danh sách Nhân sự</h5>
                            <a href="create-employee?siteId=${site.siteId}" class="btn btn-success btn-sm rounded-pill px-3 fw-bold shadow-sm">
                                <i class="bi bi-person-plus-fill"></i> Thêm nhân viên
                            </a>
                        </div>

                        <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                            <c:if test="${empty employeeList}">
                                <div class="text-center py-5">
                                    <i class="bi bi-people display-4 text-muted opacity-25"></i>
                                    <p class="text-muted mt-3 mb-0">Chưa có nhân viên nào được gán vào bãi này.</p>
                                </div>
                            </c:if>

                            <c:if test="${not empty employeeList}">
                                <div class="list-group list-group-flush">
                                    <c:forEach var="emp" items="${employeeList}">
                                        <div class="list-group-item p-3 d-flex align-items-center justify-content-between hover-bg-light">
                                            <div class="d-flex align-items-center">
                                                <img src="https://ui-avatars.com/api/?name=${emp.lastName}+${emp.firstName}&background=random&size=48" class="rounded-circle me-3 shadow-sm" width="48" height="48">
                                                <div>
                                                    <h6 class="fw-bold text-dark mb-0">
                                                        <a href="detail-employee?id=${emp.employeeId}" class="text-decoration-none text-dark stretched-link">
                                                            ${emp.fullName}
                                                        </a>
                                                    </h6>
                                                    <div class="small text-muted">
                                                        <span class="me-2"><i class="bi bi-telephone-fill me-1" style="font-size: 0.8rem"></i>${emp.phone}</span>
                                                        <span class="badge bg-light text-secondary border">Nhân viên</span>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="position-relative" style="z-index: 2;">
                                                <div class="dropdown">
                                                    <button class="btn btn-light btn-sm rounded-circle text-muted" data-bs-toggle="dropdown">
                                                        <i class="bi bi-three-dots"></i>
                                                    </button>
                                                    <ul class="dropdown-menu dropdown-menu-end border-0 shadow">
                                                        <li><a class="dropdown-item" href="detail-employee?id=${emp.employeeId}"><i class="bi bi-person-badge me-2"></i>Xem hồ sơ</a></li>
                                                        <li><a class="dropdown-item" href="update-employee?id=${emp.employeeId}"><i class="bi bi-pencil me-2"></i>Chỉnh sửa</a></li>
                                                        <li><hr class="dropdown-divider"></li>
                                                        <li>
                                                            <a class="dropdown-item text-warning" href="remove-emp-from-site?empId=${emp.employeeId}&siteId=${site.siteId}" onclick="return confirm('Gỡ nhân viên này khỏi bãi xe (về văn phòng)?')">
                                                                <i class="bi bi-box-arrow-right me-2"></i>Rút khỏi bãi
                                                            </a>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </div>
                    </div>
                    </div>
            </div> </div> </div> <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>