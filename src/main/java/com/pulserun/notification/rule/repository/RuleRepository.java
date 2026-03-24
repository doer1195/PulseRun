package com.pulserun.notification.rule.repository;

import com.pulserun.notification.rule.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

    List<Rule> findAllByUserId(Long userId);

    List<Rule> findAllBySymbolAndIsActiveTrue(String symbol);
}