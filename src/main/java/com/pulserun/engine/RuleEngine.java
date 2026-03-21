package com.pulserun.engine;

import com.pulserun.notification.NotificationService;
import com.pulserun.notification.rule.Rule;
import com.pulserun.notification.rule.RuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RuleEngine {

    private final RuleRepository ruleRepository;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public void evaluate(String symbol, Double currentPrice) {
        List<Rule> activeRules = ruleRepository.findAllBySymbolAndIsActiveTrue(symbol);
        log.info("[RuleEngine] Evaluating {} rules for symbol: {}, Current Price: {}", activeRules.size(), symbol,
                currentPrice);

        if (activeRules.isEmpty()) {
            return;
        }

        for (Rule rule : activeRules) {
            try {
                if (rule.isSatisfied(currentPrice)) {
                    executeNotification(rule, currentPrice);
                }
            } catch (Exception e) {
                log.error("[RuleEngine] Failed to evaluate rule ID: {}. Error: {}", rule.getId(), e.getMessage());
            }
        }
    }

    private void executeNotification(Rule rule, Double currentPrice) {
        String message = String.format("[%s] 알림: 목표가 도달 (현재가: %,.0f)", rule.getSymbol(), currentPrice);

        notificationService.createNotification(
                rule.getUser().getId(),
                message
        );

        log.info("[RuleEngine] Notification triggered for User: {}, Rule ID: {}", rule.getUser().getId(), rule.getId());
    }
}