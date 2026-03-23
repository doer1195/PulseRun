package com.pulserun.market.repository;

import com.pulserun.market.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {

    boolean existsByCode(String code);
}
