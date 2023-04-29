package com.zipcode.panic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.zipcode.panic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActionPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActionPlan.class);
        ActionPlan actionPlan1 = new ActionPlan();
        actionPlan1.setId(1L);
        ActionPlan actionPlan2 = new ActionPlan();
        actionPlan2.setId(actionPlan1.getId());
        assertThat(actionPlan1).isEqualTo(actionPlan2);
        actionPlan2.setId(2L);
        assertThat(actionPlan1).isNotEqualTo(actionPlan2);
        actionPlan1.setId(null);
        assertThat(actionPlan1).isNotEqualTo(actionPlan2);
    }
}
