package com.pulserun.notification.rule.controller;

import com.pulserun.notification.rule.dto.RuleCreateRequest;
import com.pulserun.notification.rule.dto.RuleResponse;
import com.pulserun.notification.rule.service.RuleService;
import com.pulserun.notification.rule.dto.SimpleRuleDto;
import com.pulserun.global.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    @GetMapping
    public ResponseEntity<List<RuleResponse>> getMyRules(@LoginUser Long userId) {
        List<RuleResponse> response = ruleService.getMyRules(userId)
                                                 .stream()
                                                 .map(RuleResponse::from)
                                                 .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> createRule(
            @LoginUser Long userId,
            @RequestBody RuleCreateRequest request
    ) {
        SimpleRuleDto simpleRuleDto = new SimpleRuleDto(
                userId,
                request.symbol(),
                request.indicatorType(),
                request.targetValue(),
                request.conditionType()
        );

        ruleService.createSimpleRule(simpleRuleDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteRule(
            @LoginUser Long userId,
            @RequestParam Long notificationId
    ) {
        ruleService.deleteRule(userId, notificationId);

        return ResponseEntity.ok().build();
    }
}
