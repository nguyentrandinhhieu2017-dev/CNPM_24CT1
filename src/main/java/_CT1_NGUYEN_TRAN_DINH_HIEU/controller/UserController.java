package _CT1_NGUYEN_TRAN_DINH_HIEU.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.User;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.UserRepository;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ==========================================
    // 1. ĐĂNG KÝ (Có Validation kỹ lưỡng)
    // ==========================================
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam("username") String username,
                                  @RequestParam("password") String password,
                                  @RequestParam("fullName") String fullName,
                                  @RequestParam("email") String email,
                                  RedirectAttributes redirectAttributes) {
        
        // KIỂM THỬ 1: Tên đăng nhập phải từ 5 ký tự và không chứa khoảng trắng
        if (username.length() < 5 || username.contains(" ")) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập phải từ 5 ký tự và không có khoảng trắng!");
            return "redirect:/register";
        }

        // KIỂM THỬ 2: Email phải đúng định dạng
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            redirectAttributes.addFlashAttribute("error", "Định dạng Email không hợp lệ!");
            return "redirect:/register";
        }

        // KIỂM THỬ 3: Chống trùng lặp dữ liệu
        if (userRepository.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập này đã có người sử dụng!");
            return "redirect:/register";
        }
        if (userRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email này đã được đăng ký cho một tài khoản khác!");
            return "redirect:/register";
        }
        // KIỂM THỬ 4: Mật khẩu bảo mật cao (Hoa, thường, đặc biệt)
        // (?=.*[a-z]) : Phải có ít nhất 1 chữ cái viết thường
        // (?=.*[A-Z]) : Phải có ít nhất 1 chữ cái viết hoa
        // (?=.*[!@#$%^&*()_+~`|}{#\\[\\]:;?><,./-]) : Phải có ít nhất 1 ký tự đặc biệt
        // .{8 }
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+~`|}{#\\[\\]:;?><,./-]).{8,}$";
        if (!password.matches(passwordRegex)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu phải dài ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và ký tự đặc biệt!");
            return "redirect:/register";
        }

        // Vượt qua kiểm thử -> Lưu tài khoản
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); 
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setRole("USER"); 
        
        userRepository.save(newUser);
        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/login";
    }

    // ==========================================
    // 2. ĐĂNG NHẬP
    // ==========================================
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/"; // Về trang chủ
        }
        
        redirectAttributes.addFlashAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

 // Hiển thị trang Profile
    @GetMapping("/profile")
    public String showProfilePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";
        
        // Lấy thông tin mới nhất từ DB
        User user = userRepository.findById(loggedInUser.getId()).orElse(null);
        model.addAttribute("user", user);
        return "profile";
    }

    // Xử lý cập nhật thông tin
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("fullName") String fullName,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        User user = userRepository.findById(loggedInUser.getId()).get();
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);
        
        userRepository.save(user);
        session.setAttribute("loggedInUser", user); // Cập nhật lại session
        
        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        return "redirect:/profile";
    }
    
    // 3. QUÊN MẬT KHẨU
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản nào liên kết với Email này!");
            return "redirect:/forgot-password";
        }

        // Tạo mật khẩu mới tạm thời
        String newTempPassword = "LiveTech@" + (int)(Math.random() * 10000);
        user.setPassword(newTempPassword);
        userRepository.save(user);

        // 🌟 Biến success này chứa mật khẩu mới để đẩy ra trang Login
        redirectAttributes.addFlashAttribute("success", "Mật khẩu mới của bạn là: " + newTempPassword);
        return "redirect:/login";
    }
}