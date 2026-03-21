package com.pulserun.notification.rule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

    List<Rule> findAllByUserId(Long userId);

    List<Rule> findAllBySymbolAndIsActiveTrue(String symbol);

    long countByUserId(Long userId);
}