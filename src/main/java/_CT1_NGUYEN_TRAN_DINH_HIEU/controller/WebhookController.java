package _CT1_NGUYEN_TRAN_DINH_HIEU.controller;

import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Order;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller xử lý Webhook / IPN (Instant Payment Notification).
 * Tự động nhận tín hiệu báo có từ Ngân hàng (hoặc qua các cổng trung gian như Casso, SePay, VietQR API)
 * để tự động chuyển trạng thái đơn hàng sang 'PAID' (Đã thanh toán) tức thì theo thời gian thực.
 */
@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final OrderRepository orderRepository;

    @Autowired
    public WebhookController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Endpoint nhận thông báo biến động số dư khi khách hàng chuyển khoản thành công.
     * URL: POST /api/webhook/payment
     *
     * Cấu trúc JSON nhận vào thường có dạng (chuẩn SePay / Casso / Bank API):
     * {
     *   "gateway": "MBBank",
     *   "transactionDate": "2026-11-17 14:00:00",
     *   "accountNumber": "0987654321",
     *   "transferAmount": 52500000,
     *   "content": "LTA PAY ORDER 10",
     *   "referenceCode": "FT243229988123"
     * }
     */
    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> handlePaymentWebhook(
            @RequestBody(required = false) Map<String, Object> payload,
            @RequestHeader(value = "Authorization", required = false) String apiKeyHeader) {

        Map<String, Object> response = new HashMap<>();
        logger.info(">>> [WEBHOOK PAYMENT] Nhận tín hiệu biến động số dư: {}", payload);

        // Trường hợp payload rỗng
        if (payload == null || payload.isEmpty()) {
            response.put("success", false);
            response.put("message", "Payload rỗng!");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 1. Trích xuất nội dung chuyển khoản (có thể nằm ở field "content", "description", hoặc "transactionContent")
            String content = "";
            if (payload.containsKey("content")) {
                content = String.valueOf(payload.get("content"));
            } else if (payload.containsKey("description")) {
                content = String.valueOf(payload.get("description"));
            } else if (payload.containsKey("transactionContent")) {
                content = String.valueOf(payload.get("transactionContent"));
            }

            // 2. Trích xuất Order ID từ chuỗi nội dung (Ví dụ: "LTA PAY ORDER 10" -> 10)
            Long orderId = extractOrderIdFromContent(content);

            if (orderId == null) {
                logger.warn(">>> [WEBHOOK] Không tìm thấy mã đơn hàng hợp lệ trong nội dung: '{}'", content);
                response.put("success", false);
                response.put("message", "Không tìm thấy mã đơn hàng trong nội dung chuyển khoản!");
                return ResponseEntity.ok(response);
            }

            // 3. Tìm kiếm đơn hàng trong cơ sở dữ liệu
            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isEmpty()) {
                logger.error(">>> [WEBHOOK] Không tìm thấy Order ID #{} trong database", orderId);
                response.put("success", false);
                response.put("message", "Đơn hàng #" + orderId + " không tồn tại!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Order order = orderOpt.get();

            // 4. Kiểm tra trạng thái hiện tại của đơn hàng
            if ("PAID".equalsIgnoreCase(order.getStatus())) {
                logger.info(">>> [WEBHOOK] Đơn hàng #{} đã ở trạng thái PAID từ trước đó.", orderId);
                response.put("success", true);
                response.put("message", "Đơn hàng đã được thanh toán trước đó!");
                return ResponseEntity.ok(response);
            }

            // 5. Cập nhật trạng thái đơn hàng sang 'PAID'
            order.setStatus("PAID");
            order.setPaidAt(LocalDateTime.now());

            // Lưu mã giao dịch tham chiếu của ngân hàng (nếu có)
            if (payload.containsKey("referenceCode")) {
                order.setTransactionNo(String.valueOf(payload.get("referenceCode")));
            } else if (payload.containsKey("referenceNumber")) {
                order.setTransactionNo(String.valueOf(payload.get("referenceNumber")));
            }

            orderRepository.save(order);
            logger.info(">>> [WEBHOOK SUCCESS] Đã cập nhật thành công Đơn hàng #{} sang trạng thái 'PAID'!", orderId);

            response.put("success", true);
            response.put("orderId", orderId);
            response.put("status", "PAID");
            response.put("message", "Xác nhận thanh toán thành công!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error(">>> [WEBHOOK ERROR] Lỗi trong quá trình xử lý Webhook: ", e);
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * API Polling / Ajax dành cho Frontend tự động kiểm tra xem đơn hàng đã được thanh toán hay chưa.
     * URL: GET /api/webhook/order/{id}/status
     *
     * Giúp giao diện checkout.html tự động phát hiện khi khách chuyển khoản xong mà không cần tải lại trang!
     */
    @GetMapping("/order/{id}/status")
    public ResponseEntity<Map<String, Object>> checkOrderStatus(@PathVariable("id") Long id) {
        Map<String, Object> result = new HashMap<>();
        Optional<Order> orderOpt = orderRepository.findById(id);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            result.put("orderId", order.getId());
            result.put("status", order.getStatus());
            result.put("isPaid", "PAID".equalsIgnoreCase(order.getStatus()));
            return ResponseEntity.ok(result);
        }

        result.put("error", "Order not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    /**
     * Thuật toán Regex trích xuất Order ID từ nội dung chuyển khoản.
     * Bắt các trường hợp: "LTA PAY ORDER 123", "LTA ORDER 123", "ORDER 123", "ORDER123"...
     */
    private Long extractOrderIdFromContent(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        // Regex tìm cụm từ ORDER kèm chữ số đằng sau
        Pattern pattern = Pattern.compile("ORDER\\s*([0-9]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            try {
                return Long.parseLong(matcher.group(1));
            } catch (NumberFormatException ignored) {}
        }

        return null;
    }
}
