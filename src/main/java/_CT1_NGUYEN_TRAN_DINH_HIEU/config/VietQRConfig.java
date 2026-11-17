package _CT1_NGUYEN_TRAN_DINH_HIEU.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class cấu hình thông tin thanh toán VietQR.
 * Đọc tự động các giá trị cấu hình từ application.properties với tiền tố "vietqr.*"
 */
@Configuration
@ConfigurationProperties(prefix = "vietqr")
public class VietQRConfig {

    /**
     * Mã định danh ngân hàng nhận tiền.
     * Mặc định: mbbank (Ngân hàng TMCP Quân đội - MB)
     */
    private String bankId = "mbbank";

    /**
     * Số tài khoản ngân hàng nhận tiền
     */
    private String accountNo = "3211230102005";

    /**
     * Tên chủ tài khoản ngân hàng (Viết hoa không dấu chuẩn ngân hàng)
     */
    private String accountName = "NGUYEN TRAN DINH HIEU";

    /**
     * Kiểu mẫu giao diện mã QR sinh bởi VietQR:
     * - "compact2": Mẫu thẻ chứa QR, logo Napas, logo ngân hàng, số tiền (Đẹp nhất cho Web)
     * - "compact": Mẫu thu gọn
     * - "qr_only": Chỉ mã QR thuần
     * - "print": Mẫu in ấn kèm khung
     */
    private String template = "compact2";

    /**
     * Tiền tố nội dung chuyển khoản
     */
    private String memoPrefix = "LTA PAY ORDER ";

    // ==========================================
    // GETTERS & SETTERS
    // ==========================================
    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getMemoPrefix() {
        return memoPrefix;
    }

    public void setMemoPrefix(String memoPrefix) {
        this.memoPrefix = memoPrefix;
    }
}
