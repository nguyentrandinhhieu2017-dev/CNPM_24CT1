package _CT1_NGUYEN_TRAN_DINH_HIEU.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Tên hiển thị
    private String slug; // Chuỗi URL định danh (VD: laptops)

    // Liên kết 1-N (Một danh mục có nhiều sản phẩm)
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

    // --- Bấm Alt + Shift + S để tự sinh Getters và Setters ở đây ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
}