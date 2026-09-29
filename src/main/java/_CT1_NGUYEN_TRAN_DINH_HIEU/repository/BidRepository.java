package _CT1_NGUYEN_TRAN_DINH_HIEU.repository;

import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> { // hoặc Long tùy ID của bạn
    
    // Thêm dòng này: Lấy danh sách bid của 1 phiên đấu giá, sắp xếp theo giá giảm dần
    List<Bid> findByAuction_IdOrderByBidAmountDesc(Integer auctionId);
}