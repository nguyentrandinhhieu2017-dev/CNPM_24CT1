package _CT1_NGUYEN_TRAN_DINH_HIEU.controller;

import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Auction;
import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Bid;
import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Product;
import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.User;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.AuctionRepository;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.BidRepository;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.ProductRepository;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// --- Thư viện WebSocket ---
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// -------------------------

import java.time.LocalDateTime;

@Controller
public class BidController {

    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private AuctionRepository auctionRepository;
    @Autowired private BidRepository bidRepository;

    @Autowired 
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/place-bid")
    public String placeBid(@RequestParam("productId") Long productId,
                           @RequestParam("bidAmount") Long bidAmount,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        User user = userRepository.findById(loggedInUser.getId()).get();

        // 1. Tìm hoặc tạo mới Phiên đấu giá cho sản phẩm này
        Product product = productRepository.findById(productId).get();
        Auction auction = auctionRepository.findAll().stream()
            .filter(a -> a.getProduct().getId().equals(productId))
            .findFirst()
            .orElseGet(() -> {
                Auction newAuction = new Auction();
                newAuction.setProduct(product);
                // Lấy giá khởi điểm (Giá sàn) từ sản phẩm do Admin thiết lập
                Long defaultPrice = product.getStartPrice() != null ? product.getStartPrice() : 0L;
                newAuction.setStartPrice(defaultPrice);
                newAuction.setCurrentPrice(defaultPrice);
                newAuction.setStartTime(LocalDateTime.now());
                newAuction.setEndTime(LocalDateTime.now().plusDays(7));
                newAuction.setStepPrice(100000L); // Bước giá mặc định 100k
                return auctionRepository.save(newAuction);
            });
        
        // =========================================================================
        // 2. KIỂM TRA LUẬT ĐẶT GIÁ (GIÁ SÀN & BƯỚC GIÁ)
        // =========================================================================
        List<Bid> existingBids = bidRepository.findByAuction_IdOrderByBidAmountDesc(auction.getId());
        boolean isFirstBid = existingBids.isEmpty();
        
        Long minRequiredPrice;
        if (isFirstBid) {
            // Nếu chưa có ai đặt: Bắt buộc phải đặt tối thiểu bằng Giá sàn (Khởi điểm)
            minRequiredPrice = auction.getStartPrice();
        } else {
            // Nếu đã có người đặt: Mức giá tối thiểu = Giá hiện tại cao nhất + Bước giá
            Long stepPrice = auction.getStepPrice() != null ? auction.getStepPrice() : 0L;
            minRequiredPrice = auction.getCurrentPrice() + stepPrice;
        }

        // Nếu khách cố tình đặt thấp hơn mức yêu cầu -> Chặn lại và báo lỗi
        if (bidAmount < minRequiredPrice) {
            String errorMsg = isFirstBid ? 
                "❌ Sản phẩm chưa có người đặt. Mức giá tối thiểu phải bằng Giá sàn là: " + String.format("%,d", minRequiredPrice) + " VNĐ" : 
                "❌ Mức giá tối thiểu tiếp theo phải là: " + String.format("%,d", minRequiredPrice) + " VNĐ";
                
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/product/" + productId;
        }
        // =========================================================================

        // Đã xóa phần "Trừ tiền tài khoản (Bước 4 cũ)" vì không còn sử dụng ví

        // 3. Lưu lịch sử ra giá (Bid)
        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setUser(user);
        bid.setBidAmount(bidAmount);
        bid.setBidTime(LocalDateTime.now());
        bidRepository.save(bid);

        // 4. Cập nhật lại giá cao nhất cho phiên đấu giá
        if (bidAmount > auction.getCurrentPrice()) {
            auction.setCurrentPrice(bidAmount);
            auctionRepository.save(auction);
        }

        // 5. PHÁT SÓNG REAL-TIME (WEB_SOCKET) CHO TẤT CẢ NGƯỜI ĐANG XEM
        Map<String, Object> message = new HashMap<>();
        message.put("newPrice", auction.getCurrentPrice()); 
        message.put("bidderName", user.getFullName());

        messagingTemplate.convertAndSend("/topic/bids/" + productId, message);

        redirectAttributes.addFlashAttribute("success", "🎉 Đặt giá thành công: " + String.format("%,d", bidAmount) + " VNĐ");
        return "redirect:/product/" + productId;
    }
}