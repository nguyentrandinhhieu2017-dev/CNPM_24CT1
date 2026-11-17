package _CT1_NGUYEN_TRAN_DINH_HIEU.repository;

import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository thao tác với bảng 'orders' trong MySQL.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Tìm đơn hàng theo mã đơn
    Optional<Order> findByOrderCode(String orderCode);

    // Lấy danh sách đơn hàng theo trạng thái (PENDING, PAID, CANCELLED)
    List<Order> findByStatus(String status);

    // Lấy danh sách đơn hàng của một người dùng
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
