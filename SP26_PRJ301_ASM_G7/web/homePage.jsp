<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>ParkEasy - Quản lý bãi đỗ xe thông minh</title>

        <!-- Google Fonts: Inter -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

        <!-- Bootstrap 5 CSS -->
        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <style>
            :root {
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
            }

            /* --- Hero Section --- */
            .hero-section {
                background: linear-gradient(rgba(17, 24, 39, 0.7), rgba(17, 24, 39, 0.7)), url('https://images.unsplash.com/photo-1449844908441-8829872d2607?auto=format&fit=crop&q=80&w=1920') no-repeat center center/cover;
                padding: 100px 0;
                color: white;
            }
            .hero-title {
                font-size: 3.5rem;
                font-weight: 800;
                line-height: 1.2;
                margin-bottom: 1rem;
            }
            .hero-title span {
                color: #3b82f6; /* Lighter blue for highlight */
            }
            .hero-subtitle {
                font-size: 1.1rem;
                color: #d1d5db;
                max-width: 600px;
                margin-bottom: 2.5rem;
                line-height: 1.6;
            }

            /* Search Bar styling inside Hero */
            .search-bar-wrapper {
                background-color: #fff;
                border-radius: 12px;
                padding: 8px;
                box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            }
            .search-bar-wrapper .form-control,
            .search-bar-wrapper .form-select {
                border: none;
                background-color: transparent;
                box-shadow: none;
                color: var(--text-main);
                font-size: 0.95rem;
            }
            .search-bar-wrapper .form-control::placeholder {
                color: #9ca3af;
            }
            .search-bar-wrapper .input-group-text {
                background-color: transparent;
                border: none;
                color: #6b7280;
            }
            /* Dividers for desktop */
            @media (min-width: 992px) {
                .border-end-lg {
                    border-right: 1px solid #e5e7eb !important;
                }
            }
            .btn-search {
                font-weight: 600;
                border-radius: 8px;
                padding: 12px 24px;
            }

            /* --- Featured Locations Section --- */
            .section-title {
                font-size: 1.75rem;
                font-weight: 700;
                color: #111827;
            }
            .view-all-link {
                font-size: 0.95rem;
                font-weight: 600;
                text-decoration: none;
                color: var(--bs-primary);
            }
            .view-all-link:hover {
                text-decoration: underline;
            }

            .location-card {
                border: 1px solid #f3f4f6;
                border-radius: 16px;
                overflow: hidden;
                transition: all 0.3s ease;
                box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
                background: #fff;
            }
            .location-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
            }
            .location-card img {
                height: 200px;
                object-fit: cover;
                width: 100%;
            }
            .card-body {
                padding: 1.25rem;
            }
            .location-title {
                font-size: 1.1rem;
                font-weight: 700;
                color: #111827;
                margin-bottom: 0.25rem;
            }
            .location-address {
                font-size: 0.875rem;
                color: var(--text-muted);
                margin-bottom: 1.5rem;
            }

            /* Badges */
            .status-badge {
                font-size: 0.75rem;
                font-weight: 500;
                padding: 0.25rem 0.6rem;
                border-radius: 20px;
            }
            .badge-available {
                background-color: #dcfce7;
                color: #166534;
            }
            .badge-filling {
                background-color: #fef08a;
                color: #854d0e;
            }
            .badge-last {
                background-color: #fee2e2;
                color: #991b1b;
            }

            /* Pricing in card */
            .price-label {
                font-size: 0.75rem;
                color: var(--text-muted);
                margin-bottom: 0;
            }
            .price-amount {
                font-size: 1.25rem;
                font-weight: 700;
                color: #111827;
            }
            .price-unit {
                font-size: 0.875rem;
                color: var(--text-muted);
                font-weight: 400;
            }
            .btn-book {
                background-color: #f3f4f6;
                color: #111827;
                font-weight: 600;
                border: none;
                padding: 0.5rem 1rem;
                border-radius: 8px;
                transition: background-color 0.2s;
            }
            .btn-book:hover {
                background-color: #e5e7eb;
            }

            /* --- Features Section --- */
            .features-section {
                background-color: #fff;
                padding-top: 5rem;
                padding-bottom: 5rem;
            }
            .feature-icon {
                width: 64px;
                height: 64px;
                background-color: #eff6ff;
                color: var(--bs-primary);
                border-radius: 50%;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                font-size: 1.5rem;
                margin-bottom: 1.5rem;
            }
            .feature-title {
                font-size: 1.15rem;
                font-weight: 700;
                color: #111827;
                margin-bottom: 0.75rem;
            }
            .feature-desc {
                font-size: 0.95rem;
                color: var(--text-muted);
                line-height: 1.5;
            }

            /* --- CTA Section --- */
            .cta-section {
                background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
                color: white;
                padding: 5rem 0;
                text-align: center;
            }
            .cta-title {
                font-size: 2.5rem;
                font-weight: 700;
                margin-bottom: 1rem;
            }
            .cta-desc {
                font-size: 1.1rem;
                color: #bfdbfe;
                max-width: 600px;
                margin: 0 auto 2.5rem auto;
                line-height: 1.6;
            }
            .btn-cta-white {
                background-color: white;
                color: var(--bs-primary);
                font-weight: 600;
                padding: 0.75rem 1.5rem;
                border-radius: 8px;
                text-decoration: none;
                display: inline-block;
            }
            .btn-cta-link {
                color: white;
                font-weight: 500;
                text-decoration: none;
                margin-left: 1rem;
            }
            .btn-cta-link:hover {
                color: #e5e7eb;
            }

        </style>
    </head>
    <body>

        <jsp:include page="/WEB-INF/views/layout/header.jsp">
            <jsp:param name="activePage" value="homePage" />
        </jsp:include>

        <!-- Hero Section -->
        <header class="hero-section">
            <div class="container">
                <div class="row">
                    <div class="col-lg-8">
                        <h1 class="hero-title">Tìm Chỗ Đỗ Xe<br><span>Hoàn Hảo</span></h1>
                        <p class="hero-subtitle">Nhanh chóng, an toàn và tiện lợi ngay tại trung tâm thành phố. Tham gia cùng hàng ngàn người dùng tiết kiệm thời gian và tiền bạc bằng cách đặt chỗ trước.</p>
                    </div>
                </div>

                <!-- Search Bar -->
                <div class="search-bar-wrapper mt-4">
                    <div class="row g-0 align-items-center">
                        <!-- Location -->
                        <div class="col-12 col-lg-5 p-2 border-end-lg">
                            <div class="input-group">
                                <span class="input-group-text"><i class="bi bi-search"></i></span>
                                <input type="text" class="form-control" placeholder="Bạn muốn đỗ xe ở đâu?">
                            </div>
                        </div>
                        <!-- Date & Time -->
                        <div class="col-12 col-lg-3 p-2 border-end-lg">
                            <div class="input-group">
                                <span class="input-group-text"><i class="bi bi-calendar3"></i></span>
                                <input type="text" class="form-control" placeholder="mm/dd/yyyy, --:-- --">
                                <span class="input-group-text bg-transparent border-0 ms-auto"><i class="bi bi-calendar2-event text-muted"></i></span>
                            </div>
                        </div>
                        <!-- Duration -->
                        <div class="col-12 col-lg-2 p-2">
                            <div class="input-group">
                                <span class="input-group-text"><i class="bi bi-clock"></i></span>
                                <select class="form-select">
                                    <option selected>1 Giờ</option>
                                    <option value="2">2 Giờ</option>
                                    <option value="3">3 Giờ</option>
                                    <option value="day">Cả ngày</option>
                                </select>
                            </div>
                        </div>
                        <!-- Button -->
                        <div class="col-12 col-lg-2 p-2 mt-2 mt-lg-0">
                            <button class="btn btn-primary w-100 btn-search">Tìm kiếm</button>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <!-- Featured Locations -->
        <section class="py-5" style="background-color: #fafafa;">
            <div class="container py-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2 class="section-title mb-0">Các địa điểm đỗ xe nổi bật</h2>
                    <a href="#" class="view-all-link d-none d-sm-block">Xem tất cả <i class="bi bi-arrow-right"></i></a>
                </div>

                <div class="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">
                    <!-- Location 1 -->
                    <div class="col">
                        <div class="location-card h-100 d-flex flex-column">
                            <img src="https://images.unsplash.com/photo-1590674899484-131297d02cb1?auto=format&fit=crop&q=80&w=600" alt="Downtown Plaza">
                            <div class="card-body d-flex flex-column flex-grow-1">
                                <div class="d-flex justify-content-between align-items-start mb-1">
                                    <h3 class="location-title">Downtown Plaza</h3>
                                    <span class="status-badge badge-available">Còn chỗ</span>
                                </div>
                                <p class="location-address">123 Market St, Downtown</p>

                                <div class="mt-auto d-flex justify-content-between align-items-end">
                                    <div>
                                        <p class="price-label">Giá vé</p>
                                        <div><span class="price-amount">$5.00</span><span class="price-unit">/giờ</span></div>
                                    </div>
                                    <button class="btn-book">Đặt ngay</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Location 2 -->
                    <div class="col">
                        <div class="location-card h-100 d-flex flex-column">
                            <img src="https://images.unsplash.com/photo-1573348722427-f1d6819fdf98?auto=format&fit=crop&q=80&w=600" alt="Bãi Xe Nhà Ga">
                            <div class="card-body d-flex flex-column flex-grow-1">
                                <div class="d-flex justify-content-between align-items-start mb-1">
                                    <h3 class="location-title">Bãi Xe Nhà Ga Trung Tâm</h3>
                                    <span class="status-badge badge-filling">Sắp đầy</span>
                                </div>
                                <p class="location-address">45 Station Ave, Central</p>

                                <div class="mt-auto d-flex justify-content-between align-items-end">
                                    <div>
                                        <p class="price-label">Giá vé</p>
                                        <div><span class="price-amount">$3.50</span><span class="price-unit">/giờ</span></div>
                                    </div>
                                    <button class="btn-book">Đặt ngay</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Location 3 -->
                    <div class="col">
                        <div class="location-card h-100 d-flex flex-column">
                            <img src="https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&q=80&w=600" alt="TTTM Westside">
                            <div class="card-body d-flex flex-column flex-grow-1">
                                <div class="d-flex justify-content-between align-items-start mb-1">
                                    <h3 class="location-title">TTTM Westside</h3>
                                    <span class="status-badge badge-available">Còn chỗ</span>
                                </div>
                                <p class="location-address">880 West Blvd, Westside</p>

                                <div class="mt-auto d-flex justify-content-between align-items-end">
                                    <div>
                                        <p class="price-label">Giá vé</p>
                                        <div><span class="price-amount">$4.00</span><span class="price-unit">/giờ</span></div>
                                    </div>
                                    <button class="btn-book">Đặt ngay</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Location 4 -->
                    <div class="col">
                        <div class="location-card h-100 d-flex flex-column">
                            <img src="https://images.unsplash.com/photo-1506521781263-d8422e82f27a?auto=format&fit=crop&q=80&w=600" alt="Bãi Đỗ Sân Bay">
                            <div class="card-body d-flex flex-column flex-grow-1">
                                <div class="d-flex justify-content-between align-items-start mb-1">
                                    <h3 class="location-title">Bãi Đỗ Sân Bay</h3>
                                    <span class="status-badge badge-last">Chỗ cuối cùng</span>
                                </div>
                                <p class="location-address">International Airport Terminal 1</p>

                                <div class="mt-auto d-flex justify-content-between align-items-end">
                                    <div>
                                        <p class="price-label">Giá vé</p>
                                        <div><span class="price-amount">$12.00</span><span class="price-unit">/ngày</span></div>
                                    </div>
                                    <button class="btn-book">Đặt ngay</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="text-center mt-4 d-sm-none">
                    <a href="#" class="view-all-link">Xem tất cả địa điểm <i class="bi bi-arrow-right"></i></a>
                </div>
            </div>
        </section>

        <!-- Why Choose Us -->
        <section class="features-section">
            <div class="container text-center">
                <h2 class="section-title mb-3">Tại sao nên chọn chúng tôi?</h2>
                <p class="text-muted mb-5 mx-auto" style="max-width: 600px;">Trải nghiệm hệ thống quản lý bãi đỗ xe đáng tin cậy nhất được thiết kế vì sự thuận tiện của bạn.</p>

                <div class="row g-4 mt-2">
                    <!-- Feature 1 -->
                    <div class="col-md-4 px-4">
                        <div class="feature-icon">
                            <i class="bi bi-phone"></i>
                        </div>
                        <h4 class="feature-title">Đặt chỗ không tiếp xúc</h4>
                        <p class="feature-desc">Đặt và thanh toán chỗ đỗ xe trực tuyến ngay lập tức mà không cần tiền mặt hay vé giấy.</p>
                    </div>

                    <!-- Feature 2 -->
                    <div class="col-md-4 px-4">
                        <div class="feature-icon">
                            <i class="bi bi-shield-check"></i>
                        </div>
                        <h4 class="feature-title">An ninh 24/7</h4>
                        <p class="feature-desc">Tất cả các địa điểm đối tác của chúng tôi đều được giám sát 24/7 để đảm bảo an toàn cho xe của bạn.</p>
                    </div>

                    <!-- Feature 3 -->
                    <div class="col-md-4 px-4">
                        <div class="feature-icon">
                            <i class="bi bi-piggy-bank"></i>
                        </div>
                        <h4 class="feature-title">Giá tốt nhất</h4>
                        <p class="feature-desc">Chúng tôi so sánh giá trên toàn thành phố để đảm bảo bạn nhận được mức giá đỗ xe thấp nhất.</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- CTA Section -->
        <section class="cta-section">
            <div class="container">
                <h2 class="cta-title">Sẵn sàng đỗ xe thông minh hơn?</h2>
                <p class="cta-desc">Tham gia cùng hàng ngàn tài xế đang tiết kiệm thời gian và tiền bạc với ParkEasy. Tải ứng dụng hoặc đăng ký trực tuyến ngay hôm nay.</p>
                <div>
                    <a href="#" class="btn-cta-white">Bắt đầu ngay</a>
                    <a href="#" class="btn-cta-link">Tìm hiểu thêm <i class="bi bi-arrow-right ms-1"></i></a>
                </div>
            </div>
        </section>

        <%@include file="WEB-INF/views/layout/footer.jsp" %>

        <!-- Bootstrap JS Bundle -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>