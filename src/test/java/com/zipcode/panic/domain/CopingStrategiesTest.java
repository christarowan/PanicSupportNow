package com.zipcode.panic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.zipcode.panic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CopingStrategiesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CopingStrategies.class);
        CopingStrategies copingStrategies1 = new CopingStrategies();
        copingStrategies1.setId(1L);
        CopingStrategies copingStrategies2 = new CopingStrategies();
        copingStrategies2.setId(copingStrategies1.getId());
        assertThat(copingStrategies1).isEqualTo(copingStrategies2);
        copingStrategies2.setId(2L);
        assertThat(copingStrategies1).isNotEqualTo(copingStrategies2);
        copingStrategies1.setId(null);
        assertThat(copingStrategies1).isNotEqualTo(copingStrategies2);
    }
}
