package _CT1_NGUYEN_TRAN_DINH_HIEU.service;

import _CT1_NGUYEN_TRAN_DINH_HIEU.config.VietQRConfig;
import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Service xử lý tạo mã QR thanh toán động qua chuẩn VietQR (Napas 247).
 * Sử dụng API QuickLink từ dịch vụ VietQR (img.vietqr.io).
 */
@Service
public class QRPaymentService {

    // Cố định thông tin tài khoản thụ hưởng MBBank chuẩn hóa cho toàn dự án
    public static final String DEFAULT_BANK_ID = "mbbank";               // MBBank (Mã BIN: 970422)
    public static final String DEFAULT_ACCOUNT_NO = "3211230102005";      // Số tài khoản MBBank
    public static final String DEFAULT_ACCOUNT_NAME = "NGUYEN TRAN DINH HIEU"; // Chủ tài khoản
    public static final String DEFAULT_TEMPLATE = "compact2";

    private final VietQRConfig vietQRConfig;

    @Autowired
    public QRPaymentService(VietQRConfig vietQRConfig) {
        this.vietQRConfig = vietQRConfig;
    }

    /**
     * Tạo chuỗi URL hình ảnh mã VietQR động dựa trên thông tin đơn hàng.
     * Cú pháp chuẩn VietQR QuickLink API:
     * https://img.vietqr.io/image/<BANK_ID>-<ACCOUNT_NO>-<TEMPLATE>.png?amount=<AMOUNT>&addInfo=<DESCRIPTION>&accountName=<ACCOUNT_NAME>
     *
     * @param order Đối tượng đơn hàng cần thanh toán
     * @return Chuỗi URL trực tiếp đến ảnh mã QR động
     */
    public String generateQRCodeUrl(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Thông tin đơn hàng không được để trống!");
        }

        // 1. Lấy cấu hình tài khoản nhận tiền (có fallback an toàn về hằng số MBBank)
        String bankId = (vietQRConfig != null && vietQRConfig.getBankId() != null && !vietQRConfig.getBankId().isBlank()) 
                ? vietQRConfig.getBankId() : DEFAULT_BANK_ID;

        String accountNo = (vietQRConfig != null && vietQRConfig.getAccountNo() != null && !vietQRConfig.getAccountNo().isBlank()) 
                ? vietQRConfig.getAccountNo() : DEFAULT_ACCOUNT_NO;

        String template = (vietQRConfig != null && vietQRConfig.getTemplate() != null && !vietQRConfig.getTemplate().isBlank()) 
                ? vietQRConfig.getTemplate() : DEFAULT_TEMPLATE;

        String accountName = (vietQRConfig != null && vietQRConfig.getAccountName() != null && !vietQRConfig.getAccountName().isBlank()) 
                ? vietQRConfig.getAccountName() : DEFAULT_ACCOUNT_NAME;

        // 2. Số tiền thanh toán chính xác từ đơn hàng
        Long amount = order.getFinalPrice();
        if (amount == null || amount <= 0) {
            amount = 0L;
        }

        // 3. Tạo nội dung chuyển khoản định danh duy nhất (Ví dụ: "LTA PAY ORDER 12345")
        String addInfo = buildTransferContent(order);

        // 4. Mã hóa URL các chuỗi ký tự có dấu cách hoặc ký tự đặc biệt
        // Lưu ý: Thay thế dấu '+' thành '%20' để tương thích tốt nhất với tất cả app ngân hàng
        String encodedAddInfo = URLEncoder.encode(addInfo, StandardCharsets.UTF_8).replace("+", "%20");
        String encodedAccountName = URLEncoder.encode(accountName, StandardCharsets.UTF_8).replace("+", "%20");

        // 5. Ráp chuỗi URL hình ảnh VietQR hoàn chỉnh
        // Ví dụ kết quả:
        // https://img.vietqr.io/image/MB-0987654321-compact2.png?amount=52500000&addInfo=LTA%20PAY%20ORDER%2012&accountName=NGUYEN%20TRAN%20DINH%20HIEU
        String qrImageUrl = String.format(
                "https://img.vietqr.io/image/%s-%s-%s.png?amount=%d&addInfo=%s&accountName=%s",
                bankId,
                accountNo,
                template,
                amount,
                encodedAddInfo,
                encodedAccountName
        );

        return qrImageUrl;
    }

    /**
     * Phương thức tạo nội dung chuyển khoản định danh (Memo) cho đơn hàng.
     * Ví dụ: "LTA PAY ORDER 1001"
     *
     * @param order Đơn hàng cần thanh toán
     * @return Chuỗi nội dung định danh không dấu
     */
    public String buildTransferContent(Order order) {
        if (order == null || order.getId() == null) {
            return vietQRConfig.getMemoPrefix() + "DEMO";
        }
        return vietQRConfig.getMemoPrefix() + order.getId();
    }
}
