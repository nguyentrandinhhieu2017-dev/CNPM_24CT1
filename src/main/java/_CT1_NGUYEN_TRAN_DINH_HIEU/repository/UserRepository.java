package _CT1_NGUYEN_TRAN_DINH_HIEU.repository;

import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    
    // Các hàm phục vụ việc Kiểm thử (Validation)
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}