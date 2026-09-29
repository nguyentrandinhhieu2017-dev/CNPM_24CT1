package _CT1_NGUYEN_TRAN_DINH_HIEU.controller;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Product;
import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Auction;
import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Bid;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.ProductRepository;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.AuctionRepository;
import _CT1_NGUYEN_TRAN_DINH_HIEU.repository.BidRepository;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuctionRepository auctionRepository;
    
    @Autowired
    private BidRepository bidRepository;

    // Chỉ giữ lại DUY NHẤT một hàm cho đường dẫn trang chủ "/"
    @GetMapping("/")
    public String viewHomePage(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Product> listProducts;
        
        // Xử lý tìm kiếm theo tên hoặc hashtag chính xác
        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.toLowerCase().trim();
            
            listProducts = productRepository.findAll().stream()
                .filter(p -> (p.getProductName() != null && p.getProductName().toLowerCase().contains(kw)) ||
                             (p.getHashtags() != null && p.getHashtags().toLowerCase().contains(kw)))
                .toList();
                
            model.addAttribute("keyword", keyword);
        } else {
            listProducts = productRepository.findAll();
            model.addAttribute("keyword", "");
        }
        
        model.addAttribute("listProducts", listProducts);
        return "index";
    }

    @GetMapping("/category/{slug}")
    public String viewProductsByCategory(@PathVariable("slug") String slug, Model model) {
        List<Product> products = productRepository.findByCategory_Slug(slug);
        
        model.addAttribute("listProducts", products);
        model.addAttribute("keyword", "");
        
        return "index";
    }

    @GetMapping("/product/{id}")
    public String viewProductDetail(@PathVariable("id") Integer id, Model model) {
        Optional<Product> optionalProduct = productRepository.findById(Long.valueOf(id));
        
        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
            
            Auction auction = auctionRepository.findAll().stream()
                .filter(a -> a.getProduct().getId().equals(id))
                .findFirst().orElse(null);
            
            if (auction != null) {
                List<Bid> listBids = bidRepository.findAll().stream()
                    .filter(b -> b.getAuction().getId().equals(auction.getId()))
                    .sorted((b1, b2) -> b2.getBidAmount().compareTo(b1.getBidAmount()))
                    .toList();
                
                model.addAttribute("listBids", listBids);
                model.addAttribute("currentPrice", auction.getCurrentPrice());
                
                long participantCount = listBids.stream()
                    .map(bid -> bid.getUser().getId())
                    .distinct()
                    .count();

                model.addAttribute("endTime", auction.getEndTime().toString()); 
                model.addAttribute("participantCount", participantCount);
            } else {
                Long price = optionalProduct.get().getStartPrice() != null ? optionalProduct.get().getStartPrice() : 0L;
                model.addAttribute("currentPrice", price); 
            }
            
            return "product-detail";
        } else {
            return "redirect:/"; 
        }
    }
}