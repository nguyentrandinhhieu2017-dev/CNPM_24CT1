package _CT1_NGUYEN_TRAN_DINH_HIEU.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products") // Móc nối với bảng 'products' trong MySQL
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "specs_description", columnDefinition = "TEXT")
    private String specsDescription;

    @Column(name = "image_url")
    private String imageUrl;
    
 // Liên kết N-1 (Nhiều sản phẩm thuộc 1 danh mục)
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // NHỚ THÊM GETTER / SETTER CHO category NHÉ!
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    // --- CÁC HÀM GETTER VÀ SETTER ---
 // Thêm dòng này vào dưới các thuộc tính hiện có 
    private Long startPrice;

    // Thêm bộ Getter/Setter để Spring Boot có thể đọc/ghi dữ liệu
    public Long getStartPrice() {
        return startPrice;
    }
    
    private String hashtags; // Ví dụ lưu: "#laptop #gaming #asus"

    public String getHashtags() { return hashtags; }
    public void setHashtags(String hashtags) { this.hashtags = hashtags; }

    public void setStartPrice(Long startPrice) {
        this.startPrice = startPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpecsDescription() {
        return specsDescription;
    }

    public void setSpecsDescription(String specsDescription) {
        this.specsDescription = specsDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}