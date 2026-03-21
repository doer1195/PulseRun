package com.pulserun.notification.rule;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RuleTest {



    @Test
    @DisplayName("isActive의 값이 메서드를 통해 토글되는지 확인")
    void toggleActive() {
        Rule ruleOne = new Rule();
        Rule ruleTwo = new Rule();

        ruleOne.toggleActive();

        Assertions.assertThat(ruleOne.isActive()).isNotEqualTo(ruleTwo.isActive());
    }

}