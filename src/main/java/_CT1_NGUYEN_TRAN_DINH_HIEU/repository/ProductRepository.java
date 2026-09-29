package _CT1_NGUYEN_TRAN_DINH_HIEU.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Tìm kiếm theo từ khóa (Có sẵn)
    List<Product> findByProductNameContainingIgnoreCase(String keyword);

    // 🌟 HÀM MỚI: Tự động tìm sản phẩm dựa trên biến 'slug' của bảng Category
    List<Product> findByCategory_Slug(String slug);
}