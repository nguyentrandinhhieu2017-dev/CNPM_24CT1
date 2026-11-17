# LiveTech Auction - Bộ Giao Diện Figma Ready (Static HTML Templates)

Thư mục này chứa đầy đủ **6 tệp giao diện HTML tĩnh** hoàn chỉnh theo tiêu chuẩn Bootstrap 5.3 và Font Awesome 6, tối ưu riêng để nhập vào Figma qua plugin **html.to.design** mà không phát sinh lỗi biên dịch.

---

## 📁 Danh Sách 6 Tệp HTML

| STT | Tên Tệp | Mô Tả Chi Tiết Giao Diện |
| :---: | :--- | :--- |
| **1** | [`login.html`](login.html) | **Đăng nhập:** Form căn giữa màn hình, ô Username, Password, nút Đăng nhập màu xanh lá (`btn-success`), link Quên mật khẩu và link Đăng ký. |
| **2** | [`register.html`](register.html) | **Đăng ký:** Form căn giữa màn hình, các trường Họ tên, Email, Username, Password kèm dòng text note quy chuẩn bảo mật bên dưới, nút Đăng ký ngay màu xanh dương (`btn-primary`). |
| **3** | [`profile.html`](profile.html) | **Hồ sơ cá nhân:** Card căn giữa màn hình, form thông tin user (Username, Email disable; Họ tên, Số điện thoại, Textarea Địa chỉ cho phép sửa), nút "Lưu thay đổi". |
| **4** | [`product-detail.html`](product-detail.html) | **Chi tiết sản phẩm:** Bố cục chia 2 cột (`col-md-6`). Cột trái là ảnh sản phẩm + thumbnails + bảng thông số. Cột phải là thông tin (Tên SP, Giá nổi bật màu đỏ, bộ đếm ngược thời gian, ô nhập giá đấu, nút "Đặt giá ngay", và bảng lịch sử đấu giá bên dưới). |
| **5** | [`index.html`](index.html) | **Trang chủ:** Navbar có ô tìm kiếm, Hero section gradient công nghệ, Danh mục sản phẩm dạng cuộn ngang (eBay style), lưới danh sách thẻ sản phẩm (Card grid) hiển thị thông số, countdown và nút "Xem chi tiết". |
| **6** | [`checkout.html`](checkout.html) | **Thanh toán:** Form căn giữa hiển thị Tên sản phẩm, Tổng tiền định dạng VNĐ nổi bật, Thông tin người nhận và ảnh vuông chứa Mã QR thanh toán để quét. |

---

## 🛠️ Yêu Cầu Kỹ Thuật Đã Áp Dụng
- [x] Thẻ viewport: `<meta name="viewport" content="width=device-width, initial-scale=1.0">`.
- [x] Nhúng Bootstrap 5.3 CDN: `<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">`.
- [x] Nhúng Font Awesome 6 CDN: `https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css`.
- [x] Tích hợp font Google Fonts **Inter** chuẩn UI hiện đại.
- [x] Sử dụng dummy data thực tế (MacBook Pro 16 inch M3 Max, 52.500.000 VNĐ, Nguyễn Văn A, MB Bank, v.v.).
- [x] Loại bỏ 100% cú pháp Thymeleaf (`th:field`, `th:action`, `th:if`, `th:object`, `th:text`).
