package _CT1_NGUYEN_TRAN_DINH_HIEU.controller;

import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Product;
import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.User;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    // 🛡️ HÀM KIỂM TRA QUYỀN: Chỉ cho phép tài khoản có role là ADMIN đi qua
    private boolean isAdmin(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        return loggedInUser != null && "ADMIN".equals(loggedInUser.getRole());
    }

    // 1. Xem danh sách sản phẩm
    @GetMapping
    public String listProducts(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("error", "⛔ Truy cập bị từ chối: Bạn không phải là Quản trị viên!");
            return "redirect:/login"; // Đuổi về trang đăng nhập
        }
        
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "admin-products";
    }

    // 2. Hiển thị Form thêm sản phẩm mới
    @GetMapping("/new")
    public String showCreateForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Thêm Sản Phẩm Mới");
        return "admin-product-form";
    }

    // 3. Hiển thị Form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        
        try {
            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Sửa Sản Phẩm (ID: " + id + ")");
            return "admin-product-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
            return "redirect:/admin/products";
        }
    }

    // 4. Lưu sản phẩm 
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("success", "Đã lưu sản phẩm thành công!");
        return "redirect:/admin/products";
    }

    // 5. Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        
        try {
            productRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa sản phẩm này vì đã có lịch sử đấu giá!");
        }
        return "redirect:/admin/products";
    }
}