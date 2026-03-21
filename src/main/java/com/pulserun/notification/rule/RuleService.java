package com.pulserun.notification.rule;

import com.pulserun.user.User;
import com.pulserun.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RuleService {

    private final RuleRepository ruleRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createSimpleRule(Long userId, String symbol, String indicatorType, Double targetValue, String condition) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Map<String, Object> conditions = Map.of(
                "targetValue", targetValue,
                "condition", condition
        );

        Rule rule = Rule.builder()
                .user(user)
                .symbol(symbol)
                .indicatorType(indicatorType)
                .conditions(conditions)
                .build();

        ruleRepository.save(rule);
    }

    @Transactional(readOnly = true)
    public List<Rule> getMyRules(Long userId) {
        return ruleRepository.findAllByUserId(userId);
    }

    @Transactional
    public void deleteRule(Long ruleId, Long userId) {
        Rule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림 규칙입니다."));

        if (!rule.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 알림 규칙에 대한 접근 권한이 없습니다.");
        }

        ruleRepository.delete(rule);
    }
}