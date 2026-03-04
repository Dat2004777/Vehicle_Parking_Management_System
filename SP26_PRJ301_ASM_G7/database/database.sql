/*
 * SCRIPT KHỞI TẠO DATABASE QUẢN LÝ BÃI ĐỖ XE (UPDATED)
 * Platform: SQL Server
 * Updates: 
 * 1. Thêm cột status (active/inactive) cho TẤT CẢ các bảng để hỗ trợ xóa mềm (Soft Delete).
 * 2. Đổi tên các cột status nghiệp vụ cũ để tránh trùng lặp với status xóa mềm.
 * 3. Loại bỏ phụ thuộc (Foreign Key) config_id từ bảng Subscriptions, thay bằng applied_price.
 */

USE master;
GO

-- 1. KHỞI TẠO DATABASE (Nếu đã tồn tại thì xóa đi tạo lại để sạch data)
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'ParkingManagementDB')
BEGIN
    ALTER DATABASE ParkingManagementDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE ParkingManagementDB;
END
GO

CREATE DATABASE ParkingManagementDB;
GO

USE ParkingManagementDB;
GO

-- =============================================
-- 1. CỤM NGƯỜI DÙNG (USERS) & HẠ TẦNG CƠ BẢN
-- =============================================

-- Table: Accounts
CREATE TABLE Accounts (
    account_id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('admin', 'staff', 'customer')),
    created_at DATETIME DEFAULT GETDATE(),
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')) -- Xóa mềm
);
GO

-- Table: VehicleTypes
CREATE TABLE VehicleTypes (
    vehicle_type_id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(20) NOT NULL CHECK (name IN ('car', 'motorbike')),
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')) -- Xóa mềm
);
GO

-- Table: ParkingSites
CREATE TABLE ParkingSites (
    site_id INT IDENTITY(1,1) PRIMARY KEY,
    site_name NVARCHAR(100) NOT NULL,
    address NVARCHAR(255) NOT NULL,
    region VARCHAR(10) NOT NULL CHECK (region IN ('north', 'middle', 'south')),
    manager_id INT, 
    operating_state VARCHAR(20) DEFAULT 'operating' CHECK (operating_state IN ('maintenance', 'operating', 'closed')), -- Đổi tên từ status
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')) -- Xóa mềm
);
GO

-- Table: Employees
CREATE TABLE Employees (
    employee_id INT IDENTITY(1,1) PRIMARY KEY,
    account_id INT NOT NULL,
    firstname NVARCHAR(50) NOT NULL,
    lastname NVARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    site_id INT, 
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')), -- Xóa mềm
    FOREIGN KEY (account_id) REFERENCES Accounts(account_id) ON DELETE CASCADE,
    FOREIGN KEY (site_id) REFERENCES ParkingSites(site_id)
);
GO

-- Cập nhật ràng buộc: ParkingSites trỏ về Manager
ALTER TABLE ParkingSites
ADD CONSTRAINT FK_ParkingSites_Manager
FOREIGN KEY (manager_id) REFERENCES Employees(employee_id);
GO

-- Table: Customers
CREATE TABLE Customers (
    customer_id INT IDENTITY(1,1) PRIMARY KEY,
    first_name NVARCHAR(50) NOT NULL,
    last_name NVARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    wallet_amount BIGINT DEFAULT 0,
    account_id INT NOT NULL UNIQUE, 
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')), -- Xóa mềm
    FOREIGN KEY (account_id) REFERENCES Accounts(account_id) ON DELETE CASCADE
);
GO

-- =============================================
-- 2. NHÓM HẠ TẦNG CHI TIẾT & VẬT LÝ
-- =============================================

-- Table: ParkingAreas
CREATE TABLE ParkingAreas (
    area_id INT IDENTITY(1,1) PRIMARY KEY,
    site_id INT NOT NULL,
    area_name NVARCHAR(50) NOT NULL,
    vehicle_type_id INT NOT NULL,
    totalSlots INT DEFAULT 0,
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')), -- Xóa mềm
    FOREIGN KEY (site_id) REFERENCES ParkingSites(site_id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_type_id) REFERENCES VehicleTypes(vehicle_type_id)
);
GO

-- Table: ParkingCards
CREATE TABLE ParkingCards (
    card_id VARCHAR(50) PRIMARY KEY,
    site_id INT NOT NULL,
    card_state VARCHAR(20) DEFAULT 'available' CHECK (card_state IN ('available', 'using')), -- Đổi tên từ status
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')), -- Xóa mềm
    FOREIGN KEY (site_id) REFERENCES ParkingSites(site_id) ON DELETE CASCADE
);
GO

-- =============================================
-- 3. NHÓM NGHIỆP VỤ (CORE BUSINESS)
-- =============================================

-- Table: PriceConfigs
CREATE TABLE PriceConfigs (
    config_id INT IDENTITY(1,1) PRIMARY KEY,
    site_id INT NOT NULL,
    vehicle_type_id INT NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('hourly', 'monthly', 'yearly')),
    base_price BIGINT NOT NULL,
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')), -- Xóa mềm
    FOREIGN KEY (site_id) REFERENCES ParkingSites(site_id),
    FOREIGN KEY (vehicle_type_id) REFERENCES VehicleTypes(vehicle_type_id)
);
GO

-- Table: Subscriptions (Vé tháng/năm)
CREATE TABLE Subscriptions (
    subscription_id INT IDENTITY(1,1) PRIMARY KEY,
    customer_id INT NOT NULL,
    card_id VARCHAR(50) NOT NULL,
    license_plate VARCHAR(20) NOT NULL,
    vehicle_type_id INT NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    sub_state VARCHAR(20) CHECK (sub_state IN ('active', 'expired')), -- Đổi tên từ status
    applied_price BIGINT NOT NULL, -- ĐÃ BỎ FK TỚI PriceConfigs, lưu cứng giá tiền tại thời điểm mua
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')), -- Xóa mềm
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
    FOREIGN KEY (card_id) REFERENCES ParkingCards(card_id),
    FOREIGN KEY (vehicle_type_id) REFERENCES VehicleTypes(vehicle_type_id)
);
GO

-- =============================================
-- 4. NHÓM VẬN HÀNH & THANH TOÁN
-- =============================================

-- Table: ParkingSessions
CREATE TABLE ParkingSessions (
    session_id INT IDENTITY(1,1) PRIMARY KEY,
    card_id VARCHAR(50) NOT NULL,
    vehicle_type_id INT NOT NULL,
    license_plate VARCHAR(20),
    entry_time DATETIME DEFAULT GETDATE(),
    exit_time DATETIME,
    session_type VARCHAR(20) CHECK (session_type IN ('noncasual', 'casual')),
    fee_amount BIGINT DEFAULT 0,
    session_state VARCHAR(20) CHECK (session_state IN ('parked', 'completed')), -- Đổi tên từ status
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')), -- Xóa mềm
    FOREIGN KEY (card_id) REFERENCES ParkingCards(card_id),
    FOREIGN KEY (vehicle_type_id) REFERENCES VehicleTypes(vehicle_type_id)
);
GO

-- Table: Bookings
CREATE TABLE Bookings (
    booking_id INT IDENTITY(1,1) PRIMARY KEY,
    customer_id INT NOT NULL,
    card_id VARCHAR(50), 
    vehicle_type_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    booking_state VARCHAR(20) CHECK (booking_state IN ('accepted', 'completed', 'cancelled')), -- Đổi tên từ status
    booking_amount BIGINT DEFAULT 0,
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')), -- Xóa mềm
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
    FOREIGN KEY (card_id) REFERENCES ParkingCards(card_id),
    FOREIGN KEY (vehicle_type_id) REFERENCES VehicleTypes(vehicle_type_id)
);
GO

-- Table: PaymentTransactions
CREATE TABLE PaymentTransactions (
    transaction_id INT IDENTITY(1,1) PRIMARY KEY,
    booking_id INT,
    subscription_id INT,
    session_id INT,
    total_amount BIGINT NOT NULL,
    payment_date DATETIME DEFAULT GETDATE(),
    payment_status VARCHAR(20) CHECK (payment_status IN ('accepted', 'completed', 'failed')),
    status VARCHAR(10) DEFAULT 'active' CHECK (status IN ('active', 'inactive')), -- Xóa mềm
    FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id),
    FOREIGN KEY (subscription_id) REFERENCES Subscriptions(subscription_id),
    FOREIGN KEY (session_id) REFERENCES ParkingSessions(session_id)
);
GO

-- =============================================
-- 5. DATA SEEDING (DỮ LIỆU MẪU)
-- =============================================

INSERT INTO VehicleTypes (name) VALUES ('car'), ('motorbike');

INSERT INTO ParkingSites (site_name, address, region, operating_state) VALUES 
(N'FPT Cầu Giấy', N'Số 10 Phạm Văn Bạch, Hà Nội', 'north', 'operating'),
(N'FPT Quận 9', N'Khu Công Nghệ Cao, TP.HCM', 'south', 'operating');

INSERT INTO Accounts (username, password, role) VALUES 
('admin_main', 'pass_hash_1', 'admin'),
('staff_hn', 'pass_hash_2', 'staff'),
('staff_hcm', 'pass_hash_3', 'staff'),
('customer_a', 'pass_hash_4', 'customer'),
('customer_b', 'pass_hash_5', 'customer');

INSERT INTO Employees (account_id, firstname, lastname, phone, site_id) VALUES 
(1, N'Quản', N'Nguyễn', '0901111111', NULL),
(2, N'Bảo', N'Lê', '0902222222', 1),
(3, N'Vy', N'Trần', '0903333333', 2);

UPDATE ParkingSites SET manager_id = 1 WHERE site_id = 1; 
UPDATE ParkingSites SET manager_id = 3 WHERE site_id = 2; 

INSERT INTO Customers (first_name, last_name, phone, email, wallet_amount, account_id) VALUES 
(N'An', N'Phạm', '0988888888', 'an@gmail.com', 500000, 4),
(N'Bình', N'Đỗ', '0977777777', 'binh@gmail.com', 100000, 5);

INSERT INTO ParkingAreas (site_id, area_name, vehicle_type_id, totalSlots) VALUES 
(1, N'Khu A - Hầm Ô tô', 1, 50),
(1, N'Khu B - Sân xe máy', 2, 200),
(2, N'Khu C - Ngoài trời', 1, 100);

INSERT INTO ParkingCards (card_id, site_id, card_state) VALUES 
('CARD-HN-001', 1, 'using'), 
('CARD-HN-002', 1, 'using'), 
('CARD-HN-003', 1, 'available'),
('CARD-HCM-001', 2, 'available');

INSERT INTO PriceConfigs (site_id, vehicle_type_id, type, base_price, unit) VALUES 
(1, 1, 'hourly', 20000, '1 hour'), 
(1, 1, 'monthly', 1500000, '1 month'), 
(1, 2, 'hourly', 5000, '4 hour'); 

-- Đã thay đổi config_id thành applied_price (1500000)
INSERT INTO Subscriptions (customer_id, card_id, license_plate, vehicle_type_id, start_date, end_date, sub_state, applied_price)
VALUES (1, 'CARD-HN-001', '30A-12345', 1, GETDATE(), DATEADD(day, 30, GETDATE()), 'active', 1500000);

INSERT INTO ParkingSessions (card_id, vehicle_type_id, license_plate, entry_time, session_type, fee_amount, session_state)
VALUES ('CARD-HN-001', 1, '30A-12345', DATEADD(hour, -2, GETDATE()), 'nonCasual', 0, 'parked');

INSERT INTO ParkingSessions (card_id, vehicle_type_id, license_plate, entry_time, exit_time, session_type, fee_amount, session_state)
VALUES ('CARD-HN-002', 1, '29C-99999', DATEADD(hour, -5, GETDATE()), DATEADD(hour, -4, GETDATE()), 'casual', 20000, 'completed');

INSERT INTO Bookings (customer_id, card_id, vehicle_type_id, start_time, end_time, booking_state, booking_amount)
VALUES (2, NULL, 1, DATEADD(day, 1, GETDATE()), DATEADD(day, 1, DATEADD(hour, 2, GETDATE())), 'accepted', 50000);

INSERT INTO PaymentTransactions (subscription_id, total_amount, payment_status)
VALUES (1, 1500000, 'completed');

INSERT INTO PaymentTransactions (session_id, total_amount, payment_status)
VALUES (2, 20000, 'completed');

PRINT N'Khởi tạo Database thành công!';
GO