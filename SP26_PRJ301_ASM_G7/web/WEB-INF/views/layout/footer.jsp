<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
    /* --- Footer --- */
    footer {
        background-color: #fff;
        padding-top: 4rem;
        padding-bottom: 2rem;
        border-top: 1px solid #f3f4f6;
    }
    .footer-heading {
        font-size: 0.875rem;
        font-weight: 700;
        color: #111827;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        margin-bottom: 1.5rem;
    }
    .footer-links {
        list-style: none;
        padding: 0;
        margin: 0;
    }
    .footer-links li {
        margin-bottom: 0.75rem;
    }
    .footer-links a {
        color: var(--text-muted);
        text-decoration: none;
        font-size: 0.95rem;
        transition: color 0.2s;
    }
    .footer-links a:hover {
        color: var(--bs-primary);
    }
    .footer-bottom {
        margin-top: 3rem;
        padding-top: 1.5rem;
        border-top: 1px solid #f3f4f6;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        align-items: center;
    }
    .copyright {
        color: var(--text-muted);
        font-size: 0.875rem;
        margin-bottom: 0;
    }
    .social-links a {
        color: #9ca3af;
        font-size: 1.25rem;
        margin-left: 1rem;
        transition: color 0.2s;
    }
    .social-links a:hover {
        color: var(--text-main);
    }
</style>

<!-- Footer -->
<footer>
    <div class="container">
        <div class="row g-4">
            <div class="col-6 col-md-3">
                <h5 class="footer-heading">CÔNG TY</h5>
                <ul class="footer-links">
                    <li><a href="#">Về chúng tôi</a></li>
                    <li><a href="#">Tuyển dụng</a></li>
                    <li><a href="#">Tin tức</a></li>
                </ul>
            </div>
            <div class="col-6 col-md-3">
                <h5 class="footer-heading">HỖ TRỢ</h5>
                <ul class="footer-links">
                    <li><a href="#">Trung tâm trợ giúp</a></li>
                    <li><a href="#">Điều khoản dịch vụ</a></li>
                    <li><a href="#">Chính sách bảo mật</a></li>
                </ul>
            </div>
            <div class="col-6 col-md-3">
                <h5 class="footer-heading">ĐỐI TÁC</h5>
                <ul class="footer-links">
                    <li><a href="#">Đăng tin cho thuê</a></li>
                    <li><a href="#">Cổng quản trị</a></li>
                    <li><a href="#">Cổng nhân viên</a></li>
                </ul>
            </div>
            <div class="col-6 col-md-3">
                <h5 class="footer-heading">LIÊN HỆ</h5>
                <ul class="footer-links">
                    <li><a href="mailto:support@parkeasy.com">support@parkeasy.com</a></li>
                    <li><a href="tel:+15551234567">+1 (555) 123-4567</a></li>
                </ul>
            </div>
        </div>

        <div class="footer-bottom">
            <p class="copyright">© 2024 ParkEasy Inc. Bảo lưu mọi quyền.</p>
            <div class="social-links">
                <a href="#" aria-label="Facebook"><i class="bi bi-facebook"></i></a>
                <a href="#" aria-label="Twitter"><i class="bi bi-twitter"></i></a>
            </div>
        </div>
    </div>
</footer>