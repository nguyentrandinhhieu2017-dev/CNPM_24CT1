# Bộ Giao Diện HTML Thuần Cho Figma (HTML-to-Design)

Thư mục này chứa các file giao diện HTML chuẩn được chuyển đổi từ các template Thymeleaf trong dự án Spring Boot, phục vụ cho việc nhập (import) trực tiếp vào Figma thông qua plugin **html.to.design** hoặc bất kỳ công cụ chuyển đổi HTML sang thiết kế nào.

---

## 📁 Danh sách các file trong thư mục:
- `login.html`: Giao diện **Đăng nhập** chuẩn HTML5.
- `register.html`: Giao diện **Đăng ký tài khoản** chuẩn HTML5.
- `forgot-password.html`: Giao diện **Khôi phục mật khẩu** (bổ trợ liên kết với trang đăng nhập).
- `index.html`: Trang tổng quan điều hướng giúp xem trước toàn bộ các trang trên trình duyệt.

---

## 🔄 Bảng đối chiếu chuyển đổi Thymeleaf ➔ HTML thuần:

| Thuộc tính Thymeleaf | Chuyển đổi sang HTML thuần | Mục đích & Lưu ý |
| :--- | :--- | :--- |
| `xmlns:th="http://www.thymeleaf.org"` | Đã loại bỏ | Chỉ giữ lại `<html lang="vi">` chuẩn HTML5 |
| `th:action="@{/login}"` | `action="/login"` | Đường dẫn form action chuẩn, không còn cú pháp Spring Expression `@{} ` |
| `th:object="${user}"` | Đã loại bỏ | HTML thuần không cần liên kết bean backend |
| `th:field="*{username}"` | `name="username" id="username"` | Tách thành thuộc tính `name` và `id` riêng biệt cho thẻ `<input>` |
| `th:if="${error}"` | Thẻ `alert` thuần với class `d-none` (hoặc mẫu hiển thị) | Giữ nguyên cấu trúc CSS Alert của Bootstrap để Figma nhận diện layer, tránh lỗi biểu thức `${}` |
| `th:text="${error}"` | Chứa text mẫu cụ thể | Thay biến động backend bằng văn bản tĩnh mô phỏng |

---

## 🎨 Điểm tối ưu cho Figma HTML-to-Design:
1. **Liên kết CDN trực tuyến đầy đủ:**
   - Bootstrap 5.3 CSS & JS.
   - Bootstrap Icons (SVG/Webfonts) để biểu tượng hiển thị sắc nét.
   - Font chữ Google Fonts (`Inter`) giúp Figma load font tự nhiên mà không bị lỗi thiếu font.
2. **Auto-Layout & Semantic:**
   - Ghép đôi chính xác `<label for="id">` và `<input id="...">`.
   - Căn giữa giao diện bằng `min-vh-100 d-flex align-items-center` giúp Frame khi import vào Figma luôn nằm cân đối ở giữa artboard.
3. **Không ảnh hưởng dự án gốc:**
   - Các file Thymeleaf gốc tại `src/main/resources/templates/` được giữ nguyên 100%.
