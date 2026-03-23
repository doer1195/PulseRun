package com.pulserun.notification.rule.service;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.error.exception.PulserunException;
import com.pulserun.notification.rule.repository.RuleRepository;
import com.pulserun.notification.rule.dto.SimpleRuleDto;
import com.pulserun.notification.rule.entity.Rule;
import com.pulserun.user.entity.User;
import com.pulserun.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RuleService {

    private final RuleRepository ruleRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Rule> getMyRules(Long userId) {
        return ruleRepository.findAllByUserId(userId);
    }

    @Transactional
    public void createSimpleRule(SimpleRuleDto simpleRuleDto) {
        User user = userRepository.findById(simpleRuleDto.userId())
                                  .orElseThrow(() -> new PulserunException(ErrorCode.INVALID_INPUT_VALUE));

        Rule rule = simpleRuleDto.toEntity(user);

        ruleRepository.save(rule);
    }

    @Transactional
    public void deleteRule(Long ruleId, Long userId) {
        Rule rule = ruleRepository.findById(ruleId)
                                  .orElseThrow(() -> new PulserunException(ErrorCode.INVALID_INPUT_VALUE));

        if (!rule.getUser().getId().equals(userId)) {
            throw new PulserunException(ErrorCode.INVALID_INPUT_VALUE);
        }

        ruleRepository.delete(rule);
    }
}