package _CT1_NGUYEN_TRAN_DINH_HIEU.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity đại diện cho Đơn hàng sau khi đấu giá thành công hoặc mua hàng.
 * Bảng 'orders' trong MySQL.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã đơn hàng định danh hiển thị cho khách (Ví dụ: ORD-1001)
    @Column(name = "order_code", unique = true)
    private String orderCode;

    // Người đặt hàng / Người thắng đấu giá
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // Sản phẩm trúng đấu giá
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    // Số tiền cuối cùng cần thanh toán (giá chốt phiên đấu giá)
    @Column(name = "final_price", nullable = false)
    private Long finalPrice;

    // Trạng thái đơn hàng: PENDING (Chờ thanh toán), PAID (Đã thanh toán), CANCELLED (Đã hủy)
    @Column(nullable = false)
    private String status = "PENDING";

    // Thông tin giao nhận hàng
    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_phone")
    private String receiverPhone;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    // Thời gian tạo đơn
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Thời gian thanh toán thành công
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    // Mã tham chiếu giao dịch ngân hàng (lưu lại khi nhận IPN/Webhook)
    @Column(name = "transaction_no")
    private String transactionNo;

    // Ghi chú đơn hàng
    private String note;

    // ==========================================
    // CONSTRUCTORS
    // ==========================================
    public Order() {
    }

    public Order(Product product, User user, Long finalPrice, String receiverName, String receiverPhone, String shippingAddress) {
        this.product = product;
        this.user = user;
        this.finalPrice = finalPrice;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.shippingAddress = shippingAddress;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    // ==========================================
    // GETTERS & SETTERS
    // ==========================================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode != null ? orderCode : ("LTA-" + id);
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Long finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
