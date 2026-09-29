package _CT1_NGUYEN_TRAN_DINH_HIEU.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Product;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.ProductRepository;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    // 1. Lấy API Key từ application.properties
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    private ProductRepository productRepository;

    // 2. HÀM XỬ LÝ CHÍNH
    @PostMapping
    public Map<String, String> chatWithAI(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        
        try {
            // Quét toàn bộ sản phẩm từ Database để "nhồi" vào não AI
            List<Product> products = productRepository.findAll();
            StringBuilder khoHang = new StringBuilder();
            for (Product p : products) {
                khoHang.append("- Tên máy: ").append(p.getProductName())
                       .append(" (Giá khởi điểm: ").append(String.format("%,d", p.getStartPrice())).append(" VNĐ). ")
                       .append("Cấu hình: ").append(p.getSpecsDescription()).append("\n");
            }

            // Gọi API 
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.7-flash:generateContent?key=" + geminiApiKey;

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Gắn kho hàng vào "Nhân thân" của AI
            String systemContext = "Bạn là chuyên gia tư vấn công nghệ của sàn đấu giá 'LiveTech Auction'.\n" 
                    + "LUẬT ĐẤU GIÁ (Phải nhớ kỹ):\n"
                    + "- Khách hàng phải Đăng nhập và nạp tiền POINT vào tài khoản mới được tham gia.\n"
                    + "- Lần đầu đặt giá phải lớn hơn hoặc bằng Giá khởi điểm.\n\n"
                    + "KHO HÀNG HIỆN TẠI:\n" 
                    + khoHang.toString() 
                    + "\nNhiệm vụ: Tư vấn sản phẩm trong kho. Trả lời ngắn gọn, thân thiện, dùng biểu tượng cảm xúc. Nếu khách hỏi máy chơi các game như GTA V hay PUBG Mobile, hãy phân tích RAM và Card màn hình trong mô tả. Luôn chèn câu kêu gọi chốt đơn vào cuối.";
            
            String fullPrompt = systemContext + "\nKhách hàng hỏi: " + userMessage;

            // Đóng gói dữ liệu gửi đi chuẩn định dạng Gemini
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(
                Map.of("parts", List.of(Map.of("text", fullPrompt)))
            ));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Gửi request lên Google
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            // Nhận và bóc tách câu trả lời JSON
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    String aiReply = (String) parts.get(0).get("text");
                    
                    return Map.of("reply", aiReply);
                }
            }
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            // Lỗi do Google trả về (Sai key, sai model, v.v.)
            System.out.println("❌ Lỗi HTTP từ Google: " + e.getStatusCode());
            System.out.println("❌ Chi tiết lỗi: " + e.getResponseBodyAsString());
            return Map.of("reply", "Hệ thống AI đang bận một chút, bạn thử lại sau nhé!");
        } catch (Exception e) {
            // Lỗi do code Java của mình
            System.out.println("❌ Lỗi hệ thống: " + e.getMessage());
            e.printStackTrace();
            return Map.of("reply", "Hệ thống AI đang bận một chút, bạn thử lại sau nhé!");
        }

        return Map.of("reply", "Xin lỗi, mình chưa nhận được phản hồi từ vệ tinh.");
    }
}