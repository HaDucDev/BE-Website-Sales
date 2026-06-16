-- Seed data for hdshopdb
-- Run this file AFTER the Spring Boot backend has started at least once,
-- because Hibernate creates the tables from JPA entities.
--
-- MySQL CLI example:
-- docker exec -i mysqldb mysql -uroot -p123456 hdshopdb < seed-hdshopdb.sql
--
-- MySQL Workbench: open this file, select schema hdshopdb, then execute.

USE hdshopdb;

START TRANSACTION;

-- =========================================================
-- 1. Roles
-- =========================================================
INSERT IGNORE INTO role (name)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_CUSTOMER'),
    ('ROLE_SHIPPER');

-- =========================================================
-- 2. Categories
-- =========================================================
INSERT INTO category (category_name, is_delete)
SELECT 'Laptop', 0
WHERE NOT EXISTS (
    SELECT 1 FROM category WHERE category_name = 'Laptop' AND is_delete = 0
);

INSERT INTO category (category_name, is_delete)
SELECT 'Chuột', 0
WHERE NOT EXISTS (
    SELECT 1 FROM category WHERE category_name = 'Chuột' AND is_delete = 0
);

INSERT INTO category (category_name, is_delete)
SELECT 'Bàn phím', 0
WHERE NOT EXISTS (
    SELECT 1 FROM category WHERE category_name = 'Bàn phím' AND is_delete = 0
);

INSERT INTO category (category_name, is_delete)
SELECT 'Tai nghe', 0
WHERE NOT EXISTS (
    SELECT 1 FROM category WHERE category_name = 'Tai nghe' AND is_delete = 0
);

INSERT INTO category (category_name, is_delete)
SELECT 'Màn hình', 0
WHERE NOT EXISTS (
    SELECT 1 FROM category WHERE category_name = 'Màn hình' AND is_delete = 0
);

-- =========================================================
-- 3. Suppliers
-- =========================================================
INSERT INTO supplier (supplier_name, supplier_image, is_delete)
SELECT 'Dell', 'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg', 0
WHERE NOT EXISTS (
    SELECT 1 FROM supplier WHERE supplier_name = 'Dell' AND is_delete = 0
);

INSERT INTO supplier (supplier_name, supplier_image, is_delete)
SELECT 'Logitech', 'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg', 0
WHERE NOT EXISTS (
    SELECT 1 FROM supplier WHERE supplier_name = 'Logitech' AND is_delete = 0
);

INSERT INTO supplier (supplier_name, supplier_image, is_delete)
SELECT 'Asus', 'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg', 0
WHERE NOT EXISTS (
    SELECT 1 FROM supplier WHERE supplier_name = 'Asus' AND is_delete = 0
);

INSERT INTO supplier (supplier_name, supplier_image, is_delete)
SELECT 'Razer', 'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg', 0
WHERE NOT EXISTS (
    SELECT 1 FROM supplier WHERE supplier_name = 'Razer' AND is_delete = 0
);

INSERT INTO supplier (supplier_name, supplier_image, is_delete)
SELECT 'Samsung', 'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg', 0
WHERE NOT EXISTS (
    SELECT 1 FROM supplier WHERE supplier_name = 'Samsung' AND is_delete = 0
);

-- =========================================================
-- 4. Sample users
-- Password note:
-- The application uses BCryptPasswordEncoder.
-- The bcrypt hash below is for password: password
-- You can login these accounts with password: password
-- =========================================================
INSERT INTO `user` (
    avatar,
    username,
    address,
    full_name,
    email,
    password,
    phone,
    is_delete,
    assignment,
    role_id
)
SELECT
    'https://res.cloudinary.com/dkdyl2pcy/image/upload/v1676872862/avatar-default-9_rv6k1c.png',
    'admin',
    'Hà Nội',
    'Quản trị viên',
    'admin@hdshop.local',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '0900000000',
    0,
    0,
    r.role_id
FROM role r
WHERE r.name = 'ROLE_ADMIN'
  AND NOT EXISTS (SELECT 1 FROM `user` u WHERE u.username = 'admin');

INSERT INTO `user` (
    avatar,
    username,
    address,
    full_name,
    email,
    password,
    phone,
    is_delete,
    assignment,
    role_id
)
SELECT
    'https://res.cloudinary.com/dkdyl2pcy/image/upload/v1676872862/avatar-default-9_rv6k1c.png',
    'customer1',
    'Đà Nẵng',
    'Khách hàng 1',
    'customer1@hdshop.local',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '0911111111',
    0,
    0,
    r.role_id
FROM role r
WHERE r.name = 'ROLE_CUSTOMER'
  AND NOT EXISTS (SELECT 1 FROM `user` u WHERE u.username = 'customer1');

INSERT INTO `user` (
    avatar,
    username,
    address,
    full_name,
    email,
    password,
    phone,
    is_delete,
    assignment,
    role_id
)
SELECT
    'https://res.cloudinary.com/dkdyl2pcy/image/upload/v1676872862/avatar-default-9_rv6k1c.png',
    'shipper1',
    'TP. Hồ Chí Minh',
    'Nhân viên giao hàng 1',
    'shipper1@hdshop.local',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '0922222222',
    0,
    0,
    r.role_id
FROM role r
WHERE r.name = 'ROLE_SHIPPER'
  AND NOT EXISTS (SELECT 1 FROM `user` u WHERE u.username = 'shipper1');

-- =========================================================
-- 5. Products
-- =========================================================
INSERT INTO product (
    product_name,
    quantity,
    product_image,
    discount,
    unit_price,
    description_product,
    is_delete,
    rating,
    category_id,
    supplier_id
)
SELECT
    'Dell Inspiron 15',
    20,
    'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg',
    10,
    15000000,
    'Laptop Dell Inspiron 15 phù hợp học tập, văn phòng và làm việc hằng ngày.',
    0,
    0.0,
    c.category_id,
    s.supplier_id
FROM category c
JOIN supplier s ON s.supplier_name = 'Dell' AND s.is_delete = 0
WHERE c.category_name = 'Laptop'
  AND c.is_delete = 0
  AND NOT EXISTS (SELECT 1 FROM product p WHERE p.product_name = 'Dell Inspiron 15' AND p.is_delete = 0);

INSERT INTO product (
    product_name,
    quantity,
    product_image,
    discount,
    unit_price,
    description_product,
    is_delete,
    rating,
    category_id,
    supplier_id
)
SELECT
    'Asus Vivobook 14',
    15,
    'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg',
    8,
    13500000,
    'Laptop Asus Vivobook 14 mỏng nhẹ, hiệu năng ổn định cho sinh viên và nhân viên văn phòng.',
    0,
    0.0,
    c.category_id,
    s.supplier_id
FROM category c
JOIN supplier s ON s.supplier_name = 'Asus' AND s.is_delete = 0
WHERE c.category_name = 'Laptop'
  AND c.is_delete = 0
  AND NOT EXISTS (SELECT 1 FROM product p WHERE p.product_name = 'Asus Vivobook 14' AND p.is_delete = 0);

INSERT INTO product (
    product_name,
    quantity,
    product_image,
    discount,
    unit_price,
    description_product,
    is_delete,
    rating,
    category_id,
    supplier_id
)
SELECT
    'Chuột Logitech M331',
    100,
    'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg',
    5,
    350000,
    'Chuột không dây Logitech M331 Silent Plus, hạn chế tiếng click, pin bền.',
    0,
    0.0,
    c.category_id,
    s.supplier_id
FROM category c
JOIN supplier s ON s.supplier_name = 'Logitech' AND s.is_delete = 0
WHERE c.category_name = 'Chuột'
  AND c.is_delete = 0
  AND NOT EXISTS (SELECT 1 FROM product p WHERE p.product_name = 'Chuột Logitech M331' AND p.is_delete = 0);

INSERT INTO product (
    product_name,
    quantity,
    product_image,
    discount,
    unit_price,
    description_product,
    is_delete,
    rating,
    category_id,
    supplier_id
)
SELECT
    'Chuột Razer DeathAdder Essential',
    60,
    'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg',
    12,
    690000,
    'Chuột gaming Razer DeathAdder Essential thiết kế công thái học, cảm biến chính xác.',
    0,
    0.0,
    c.category_id,
    s.supplier_id
FROM category c
JOIN supplier s ON s.supplier_name = 'Razer' AND s.is_delete = 0
WHERE c.category_name = 'Chuột'
  AND c.is_delete = 0
  AND NOT EXISTS (SELECT 1 FROM product p WHERE p.product_name = 'Chuột Razer DeathAdder Essential' AND p.is_delete = 0);

INSERT INTO product (
    product_name,
    quantity,
    product_image,
    discount,
    unit_price,
    description_product,
    is_delete,
    rating,
    category_id,
    supplier_id
)
SELECT
    'Bàn phím Asus Gaming',
    50,
    'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg',
    15,
    1200000,
    'Bàn phím gaming Asus độ bền cao, phù hợp chơi game và làm việc.',
    0,
    0.0,
    c.category_id,
    s.supplier_id
FROM category c
JOIN supplier s ON s.supplier_name = 'Asus' AND s.is_delete = 0
WHERE c.category_name = 'Bàn phím'
  AND c.is_delete = 0
  AND NOT EXISTS (SELECT 1 FROM product p WHERE p.product_name = 'Bàn phím Asus Gaming' AND p.is_delete = 0);

INSERT INTO product (
    product_name,
    quantity,
    product_image,
    discount,
    unit_price,
    description_product,
    is_delete,
    rating,
    category_id,
    supplier_id
)
SELECT
    'Tai nghe Razer Kraken X',
    40,
    'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg',
    10,
    990000,
    'Tai nghe gaming Razer Kraken X âm thanh rõ, micro tích hợp, đeo thoải mái.',
    0,
    0.0,
    c.category_id,
    s.supplier_id
FROM category c
JOIN supplier s ON s.supplier_name = 'Razer' AND s.is_delete = 0
WHERE c.category_name = 'Tai nghe'
  AND c.is_delete = 0
  AND NOT EXISTS (SELECT 1 FROM product p WHERE p.product_name = 'Tai nghe Razer Kraken X' AND p.is_delete = 0);

INSERT INTO product (
    product_name,
    quantity,
    product_image,
    discount,
    unit_price,
    description_product,
    is_delete,
    rating,
    category_id,
    supplier_id
)
SELECT
    'Màn hình Samsung 24 inch',
    30,
    'https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg',
    7,
    3200000,
    'Màn hình Samsung 24 inch Full HD, phù hợp học tập, làm việc và giải trí.',
    0,
    0.0,
    c.category_id,
    s.supplier_id
FROM category c
JOIN supplier s ON s.supplier_name = 'Samsung' AND s.is_delete = 0
WHERE c.category_name = 'Màn hình'
  AND c.is_delete = 0
  AND NOT EXISTS (SELECT 1 FROM product p WHERE p.product_name = 'Màn hình Samsung 24 inch' AND p.is_delete = 0);

-- =========================================================
-- 6. Sample cart for customer1
-- =========================================================
INSERT IGNORE INTO cart (user_id, product_id, quantity, is_delete)
SELECT u.user_id, p.product_id, 1, 0
FROM `user` u
JOIN product p ON p.product_name = 'Dell Inspiron 15' AND p.is_delete = 0
WHERE u.username = 'customer1'
  AND u.is_delete = 0;

INSERT IGNORE INTO cart (user_id, product_id, quantity, is_delete)
SELECT u.user_id, p.product_id, 2, 0
FROM `user` u
JOIN product p ON p.product_name = 'Chuột Logitech M331' AND p.is_delete = 0
WHERE u.username = 'customer1'
  AND u.is_delete = 0;

COMMIT;

-- Quick checks
SELECT role_id, name FROM role ORDER BY role_id;
SELECT category_id, category_name FROM category WHERE is_delete = 0 ORDER BY category_id;
SELECT supplier_id, supplier_name FROM supplier WHERE is_delete = 0 ORDER BY supplier_id;
SELECT product_id, product_name, quantity, unit_price, discount, category_id, supplier_id FROM product WHERE is_delete = 0 ORDER BY product_id;
SELECT user_id, username, email, role_id FROM `user` WHERE is_delete = 0 ORDER BY user_id;

