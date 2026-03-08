<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.ParkingSite"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Danh sách bãi xe</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>
            body {
                background-color: #f8f9fa; /* Màu nền chuẩn Bootstrap */
                min-height: 100vh;
            }
            .hover-card {
                transition: transform 0.2s, box-shadow 0.2s;
                cursor: pointer;
            }
            .hover-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important;
            }
            /* Style riêng cho ảnh card */
            .card-img-top {
                height: 200px;
                object-fit: cover;
            }
        </style>
    </head>

    <body>

        <div class="d-lg-flex align-items-stretch">

            <div class="container-fluid">
                <div class="container py-4">

                    <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-4">
                        <h2 class="h3 text-dark m-0">Quản lý bãi xe</h2>

                        <a href="add-site" class="btn btn-success d-flex align-items-center gap-2 shadow-sm">
                            <span class="fw-bold fs-5 lh-1">+</span> 
                            <span>Thêm bãi xe mới</span>
                        </a>
                    </div>

                    <div class="card border-0 shadow-sm mb-4">
                        <div class="card-body p-3">
                            <div class="row g-3 align-items-end">
                                <div class="col-md-4">
                                    <label class="form-label fw-bold text-secondary small">Lọc theo khu vực</label>
                                    <select class="form-select">
                                        <option value="">-- Tất cả khu vực --</option>
                                        <option value="North">Miền Bắc</option>
                                        <option value="Central">Miền Trung</option>
                                        <option value="South">Miền Nam</option>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-bold text-secondary small">Tìm kiếm</label>
                                    <input type="text" class="form-control" placeholder="Nhập tên bãi hoặc địa chỉ...">
                                </div>
                                <div class="col-md-2">
                                    <button class="btn btn-primary w-100">Tìm kiếm</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row row-cols-1 row-cols-md-2 g-4 mb-4">

                        <c:forEach items="${siteList}" var="site">
                            <div class="col">
                                <div class="card h-100 border-0 shadow-sm hover-card" 
                                     onclick="location.href = '${ctx}/site/detail?id=${site.siteId}'">

                                    <img src="https://via.placeholder.com/600x300" 
                                         alt="${site.siteName}" class="card-img-top">

                                    <div class="card-body">
                                        <div class="d-flex justify-content-between align-items-start mb-2">
                                            <h5 class="card-title fw-bold text-dark m-0">${site.siteName}</h5>
                                            <span class="badge bg-info text-dark">${site.region.label}</span>
                                        </div>

                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <small class="text-muted text-truncate" style="max-width: 60%;">
                                                <i class="bi bi-geo-alt"></i> ${site.address}
                                            </small>

                                            <c:choose>
                                                <c:when test="${site.siteStatus.name() == 'ACTIVE'}">
                                                    <span class="badge rounded-pill text-bg-success">Hoạt động</span>
                                                </c:when>
                                                <c:when test="${site.siteStatus.name() == 'MAINTENANCE'}">
                                                    <span class="badge rounded-pill text-bg-warning">Bảo trì</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge rounded-pill text-bg-danger">Đóng cửa</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                                            <span class="text-secondary fw-bold small">
                                                📦 150 Slots
                                            </span>

<!--                                            <a href="booking?action=form&siteId=${site.siteId}" 
                                               class="btn btn-sm btn-primary px-3 rounded-pill"
                                               onclick="event.stopPropagation()">
                                                Đặt chỗ ngay
                                            </a>-->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <nav aria-label="Page navigation">
                        <ul class="pagination justify-content-center">
                            <li class="page-item disabled">
                                <a class="page-link" href="#" tabindex="-1">Trước</a>
                            </li>
                            <li class="page-item active"><a class="page-link" href="#">1</a></li>
                            <li class="page-item"><a class="page-link" href="#">2</a></li>
                            <li class="page-item"><a class="page-link" href="#">3</a></li>
                            <li class="page-item">
                                <a class="page-link" href="#">Sau</a>
                            </li>
                        </ul>
                    </nav>

                </div>
            </div>
        </div>

        <script src="../../../assests/js/bootstrap.bundle.min.js"></script>
    </body>
</html>