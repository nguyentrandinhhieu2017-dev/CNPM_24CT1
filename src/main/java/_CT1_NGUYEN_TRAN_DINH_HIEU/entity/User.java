package _CT1_NGUYEN_TRAN_DINH_HIEU.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Đặt tên bảng trong MySQL là "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;
    
    private String address;
    private String phone;

    // Thêm Getter và Setter cho address và phone
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // 🌟 Trường email mới thêm để phục vụ Đăng ký & Quên mật khẩu
    @Column(unique = true, nullable = false)
    private String email;

    private String role; // Phân quyền: "USER" hoặc "ADMIN"

    // ==========================================
    // GETTER VÀ SETTER
    // (Bắt buộc phải có để Spring Boot đọc/ghi dữ liệu)
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}