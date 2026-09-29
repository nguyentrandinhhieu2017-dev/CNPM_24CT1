package _CT1_NGUYEN_TRAN_DINH_HIEU.repository;

import _CT1_NGUYEN_TRAN_DINH_HIEU.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {
}