package _CT1_NGUYEN_TRAN_DINH_HIEU.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// --- Thư viện ZXing để tạo QR ---
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.util.Base64;


@Controller
public class PaymentController {

    @GetMapping("/checkout/{productId}")
    public String showPaymentPage(@PathVariable("productId") Long productId, Model model) {
        
        // 1. Dữ liệu tĩnh (Hoặc lấy từ Database)
        Long amountToPay = 21000000L;
        String productName = "Card màn hình NVIDIA RTX 4070 Ti";
        String bankAccount = "3211230102005 (Ngân hàng MB Bank)";
        String accountName = "NGUYEN TRAN DINH HIEU";
        
        // 2. Nội dung sẽ được nhúng vào mã QR (Ví dụ chuẩn QR Code cơ bản)
        String qrContent = "Thanh toan don hang " + productId + " - So tien: " + amountToPay + " VNĐ. Nguoi nhan: " + accountName;

        // 3. Tự sinh ảnh QR Code dưới dạng Base64
        String qrCodeBase64 = "";
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 250, 250);
            
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray(); 
            
            qrCodeBase64 = Base64.getEncoder().encodeToString(pngData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4. Đẩy dữ liệu ra HTML
        model.addAttribute("productName", productName);
        model.addAttribute("amount", amountToPay);
        model.addAttribute("bankAccount", bankAccount);
        model.addAttribute("accountName", accountName);
        model.addAttribute("qrCodeImage", "data:image/png;base64," + qrCodeBase64); // Ghép tiền tố để HTML hiểu
        
        return "checkout";
    }
}