package _CT1_NGUYEN_TRAN_DINH_HIEU.controller;

import _CT1_NGUYEN_TRAN_DINH_HIEU.config.VietQRConfig;
import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Order;
import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Product;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.OrderRepository;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.ProductRepository;
import _CT1_NGUYEN_TRAN_DINH_HIEU.service.QRPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controller xử lý luồng đơn hàng và thanh toán.
 */
@Controller
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final QRPaymentService qrPaymentService;
    private final VietQRConfig vietQRConfig;

    @Autowired
    public OrderController(OrderRepository orderRepository,
                           ProductRepository productRepository,
                           QRPaymentService qrPaymentService,
                           VietQRConfig vietQRConfig) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.qrPaymentService = qrPaymentService;
        this.vietQRConfig = vietQRConfig;
    }

    /**
     * API hiển thị giao diện thanh toán đơn hàng bằng mã QR động.
     * URL: GET /order/{id}/checkout
     *
     * @param id ID của đơn hàng cần thanh toán
     * @param model Đối tượng Model truyền dữ liệu sang Thymeleaf
     * @param redirectAttributes Chuyển tiếp thông báo lỗi nếu có
     * @return Tên file template giao diện (user/checkout.html)
     */
    @GetMapping("/order/{id}/checkout")
    public String showCheckoutPage(@PathVariable("id") Long id,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        // 1. Tìm Order theo ID trong Cơ sở dữ liệu
        Optional<Order> orderOptional = orderRepository.findById(id);

        Order order;
        if (orderOptional.isEmpty()) {
            // [TÍNH NĂNG TIỆN LỢI ĐỒ ÁN]: Nếu database chưa có đơn hàng với ID này,
            // hệ thống tự động khởi tạo 1 đơn hàng mẫu 'PENDING' để bạn test ngay lập tức trên trình duyệt!
            order = createDemoOrder(id);
        } else {
            order = orderOptional.get();
        }

        // 2. Kiểm tra trạng thái đơn hàng (Phải là 'PENDING')
        if ("PAID".equalsIgnoreCase(order.getStatus())) {
            // Đơn hàng đã thanh toán trước đó
            model.addAttribute("alreadyPaid", true);
        } else if (!"PENDING".equalsIgnoreCase(order.getStatus())) {
            // Đơn hàng bị hủy hoặc trạng thái không hợp lệ
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "Đơn hàng #" + id + " hiện ở trạng thái: " + order.getStatus() + ". Không thể tiếp tục thanh toán!");
            return "redirect:/";
        }

        // 3. Gọi QRPaymentService lấy đường link ảnh QR động chuẩn VietQR
        String qrCodeUrl = qrPaymentService.generateQRCodeUrl(order);
        String transferContent = qrPaymentService.buildTransferContent(order);

        // 4. Truyền URL QR và toàn bộ thông tin Order xuống giao diện (Model)
        model.addAttribute("order", order);
        model.addAttribute("qrCodeUrl", qrCodeUrl);
        model.addAttribute("transferContent", transferContent);
        
        // Truyền thêm thông tin ngân hàng để hiển thị hướng dẫn chi tiết
        model.addAttribute("bankId", vietQRConfig.getBankId());
        model.addAttribute("bankName", "MBBank (Ngân hàng TMCP Quân đội)");
        model.addAttribute("accountNo", vietQRConfig.getAccountNo());
        model.addAttribute("accountName", vietQRConfig.getAccountName());

        // Trả về view: templates/user/checkout.html
        return "user/checkout";
    }

    /**
     * Phương thức phụ trợ tự sinh Đơn hàng mẫu PENDING phục vụ demo đồ án khi DB chưa có dữ liệu.
     */
    private Order createDemoOrder(Long id) {
        Order demoOrder = new Order();
        demoOrder.setId(id);
        demoOrder.setOrderCode("LTA-" + id);
        demoOrder.setFinalPrice(52500000L); // 52.500.000 VNĐ
        demoOrder.setStatus("PENDING");
        demoOrder.setReceiverName("Nguyễn Văn A");
        demoOrder.setReceiverPhone("0987 654 321");
        demoOrder.setShippingAddress("Số 123 Đường Lê Lợi, Phường Bến Nghé, Quận 1, TP. Hồ Chí Minh");
        demoOrder.setNote("Đơn hàng trúng đấu giá MacBook Pro 16 inch M3 Max");

        // Tìm một sản phẩm đầu tiên trong DB để gán (nếu có)
        try {
            productRepository.findAll().stream().findFirst().ifPresent(demoOrder::setProduct);
        } catch (Exception ignored) {}

        // Nếu sản phẩm vẫn null, tạo đối tượng hiển thị tạm
        if (demoOrder.getProduct() == null) {
            Product p = new Product();
            p.setProductName("Apple MacBook Pro 16 inch (M3 Max / 64GB / 1TB SSD) - Space Black");
            p.setImageUrl("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?auto=format&fit=crop&w=600&q=80");
            p.setSpecsDescription("Chip M3 Max 16-Core CPU, 40-Core GPU, 64GB Unified Memory, 1TB SSD, Mới 99% Fullbox.");
            demoOrder.setProduct(p);
        }

        // Lưu vào DB nếu muốn hoặc giữ nguyên đối tượng
        try {
            return orderRepository.save(demoOrder);
        } catch (Exception e) {
            return demoOrder;
        }
    }
}
