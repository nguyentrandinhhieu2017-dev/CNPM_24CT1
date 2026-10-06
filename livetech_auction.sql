-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 06, 2026 lúc 09:40 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `livetech_auction`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `auctions`
--

CREATE TABLE `auctions` (
  `id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `start_price` bigint(20) NOT NULL,
  `step_price` bigint(20) NOT NULL,
  `current_price` bigint(20) DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `status` varchar(20) DEFAULT 'PENDING'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `auctions`
--

INSERT INTO `auctions` (`id`, `product_id`, `start_price`, `step_price`, `current_price`, `start_time`, `end_time`, `status`) VALUES
(2, 1, 0, 100000, 3600000, '2026-08-29 03:03:18', '2026-09-05 03:03:18', 'PENDING'),
(3, 14, 2500000, 100000, 3000000, '2026-09-08 06:41:04', '2026-09-15 06:41:04', 'PENDING'),
(4, 14, 2500000, 100000, 4000000, '2026-09-08 07:04:04', '2026-09-15 07:04:04', 'PENDING');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bids`
--

CREATE TABLE `bids` (
  `id` int(11) NOT NULL,
  `auction_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `bid_amount` bigint(20) NOT NULL,
  `bid_time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `categories`
--

CREATE TABLE `categories` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `slug` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `categories`
--

INSERT INTO `categories` (`id`, `name`, `slug`) VALUES
(1, 'Laptops (Máy tính xách tay)', 'laptops'),
(2, 'Linh kiện máy tính', 'components'),
(3, 'Điện thoại thông minh', 'smartphones'),
(4, 'Máy tính bảng', 'tablets'),
(5, 'Thiết bị Gaming', 'gaming-gear'),
(6, 'Màn hình PC', 'monitors');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `specs_description` text DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `start_price` bigint(20) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `hashtags` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `products`
--

INSERT INTO `products` (`id`, `product_name`, `specs_description`, `image_url`, `created_at`, `start_price`, `category_id`, `hashtags`) VALUES
(1, 'Laptop Lenovo LOQ 2026', ' NVIDIA GeForce RTX 3050 6GB GDDR6, Boost Clock 990MHz, TGP 65W/ AMD Ryzen 5 7535HS (6 lõi / 12 luồng, 3.3 / 4.55GHz, 3MB L2 / 16MB L3) / RAM 16GB /ROM 512GB ', 'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/t/e/text_d_i_9_2.png', '2026-08-25 09:34:16', 20000000, NULL, NULL),
(2, 'Samsung Galaxy Z Fold8 Ultra 5G 12GB 256GB', 'Snapdragon® 8 Elite Gen 5', 'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/s/a/samsung-galaxy-z-fold-8-violet-01.jpg', '2026-08-25 09:34:16', 20000000, NULL, NULL),
(3, 'iPhone 17 ProMax', 'Chip A19 Pro/ 1TB ROM / ', 'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-17-pro-cam_4.jpg', '2026-08-26 02:41:35', 20000000, NULL, NULL),
(4, 'Lenovo LOQ 2024', 'Cỗ máy chiến game hoàn hảo với hệ thống tản nhiệt cực mát. Trang bị RTX 4050, dư sức cân mượt GTA V và các tựa game AAA ở mức Max Setting.', 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 18000000, 1, NULL),
(5, 'MacBook Pro 14 M3', 'Chip Apple M3 siêu tốc độ, RAM 16GB, SSD 512GB. Màn hình Liquid Retina XDR hiển thị màu sắc chuẩn xác cho dân thiết kế.', 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 38000000, 1, NULL),
(6, 'Dell XPS 15', 'Laptop doanh nhân cao cấp, thiết kế nhôm nguyên khối, viền màn hình siêu mỏng. Intel Core i7 thế hệ 13.', 'https://laptops.vn/san-pham/dell-xps-15-9500/?srsltid=AfmBOopEu-OWaquCkIrsrv6R5GOXyQ9Jn_7sNnLBzmM5iq2H2DZO25aQ', '2026-09-07 08:18:12', 32000000, NULL, NULL),
(7, 'Card màn hình NVIDIA RTX 4070 Ti', 'Kiến trúc Ada Lovelace mạnh mẽ, hỗ trợ DLSS 3.0, chơi game 4K không độ trễ, render video siêu tốc.', 'https://images.unsplash.com/photo-1591488320449-011701bb6704?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 21000000, 2, NULL),
(8, 'CPU Intel Core i9-14900K', '24 nhân 32 luồng, xung nhịp lên tới 6.0 GHz. Trái tim quyền lực cho mọi dàn PC Hi-end.', 'https://images.unsplash.com/photo-1591799264318-7e6ef8ddb7ea?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 15000000, 2, NULL),
(9, 'Redmi Turbo 4 Pro', 'Quái vật hiệu năng tầm trung, màn hình 120Hz mượt mà. Tối ưu cực tốt để leo rank PUBG Mobile liên tục nhiều giờ mà không lo rớt FPS hay quá nhiệt.', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 7500000, 3, NULL),
(10, 'iPhone 16 Pro Max 256GB', 'Siêu phẩm mới nhất từ Apple. Khung Titanium siêu nhẹ, camera viền siêu mỏng, chip A18 Pro mạnh vô đối.', 'https://images.unsplash.com/photo-1603791440384-56cd371ee9a7?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 29000000, 3, NULL),
(11, 'Samsung Galaxy S24 Ultra', 'Tích hợp Galaxy AI thông minh, bút S-Pen đa năng, camera zoom quang học 100x chụp trăng cực đỉnh.', 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 25000000, 3, NULL),
(12, 'iPad Pro 13-inch M4', 'Thiết kế siêu mỏng nhẹ chỉ 5.1mm, sức mạnh chip M4 vượt trội, màn hình OLED Tandem tuyệt đẹp.', 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 28000000, 4, NULL),
(13, 'Samsung Galaxy Tab S9', 'Máy tính bảng Android tốt nhất hiện nay, chống nước IP68, đi kèm bút S-Pen nhạy bén.', 'https://images.unsplash.com/photo-1585790050230-5dd28404ccb9?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 16000000, 4, NULL),
(14, 'Bàn phím cơ Logitech G Pro X', 'Switch GX Blue gõ cực nảy, thiết kế TKL nhỏ gọn, LED RGB đồng bộ LIGHTSYNC.', 'https://images.unsplash.com/photo-1595225476474-87563907a212?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 2500000, 5, NULL),
(15, 'Chuột Razer DeathAdder V3 Pro', 'Chuột gaming không dây siêu nhẹ (63g), cảm biến quang học Focus Pro 30K siêu chính xác.', 'https://images.unsplash.com/photo-1615663245857-ac93bb7c3c9c?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 3200000, 5, NULL),
(16, 'Màn hình LG UltraGear 27\" 2K', 'Độ phân giải 2K sắc nét, tần số quét 165Hz, tấm nền Nano IPS cho tốc độ phản hồi 1ms.', 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 8500000, 6, NULL),
(17, 'Màn hình cong Samsung Odyssey G9', 'Kích thước siêu khổng lồ 49 inch, tỷ lệ 32:9, mang lại trải nghiệm đắm chìm tuyệt đối khi đua xe hoặc chơi game thế giới mở.', 'https://images.unsplash.com/photo-1616035134706-e0e6490c2f35?q=80&w=600&auto=format&fit=crop', '2026-09-07 08:18:12', 35000000, 6, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `role` varchar(20) DEFAULT 'USER',
  `balance` bigint(20) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `email` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `role`, `balance`, `created_at`, `email`, `address`, `phone`) VALUES
(12, 'dieu3010', 'LiveTech@5209', 'Nguyễn Trần Đình Hiếu', 'ADMIN', 0, '2026-09-08 08:37:05', 'nguyentrandinhhieu2017@gmail.com', NULL, NULL);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `auctions`
--
ALTER TABLE `auctions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_id` (`product_id`);

--
-- Chỉ mục cho bảng `bids`
--
ALTER TABLE `bids`
  ADD PRIMARY KEY (`id`),
  ADD KEY `auction_id` (`auction_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Chỉ mục cho bảng `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `slug` (`slug`);

--
-- Chỉ mục cho bảng `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_product_category` (`category_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `auctions`
--
ALTER TABLE `auctions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `bids`
--
ALTER TABLE `bids`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `categories`
--
ALTER TABLE `categories`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `auctions`
--
ALTER TABLE `auctions`
  ADD CONSTRAINT `auctions_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `bids`
--
ALTER TABLE `bids`
  ADD CONSTRAINT `bids_ibfk_1` FOREIGN KEY (`auction_id`) REFERENCES `auctions` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `bids_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
