package com.zipcode.panic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.zipcode.panic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhoneLinkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhoneLink.class);
        PhoneLink phoneLink1 = new PhoneLink();
        phoneLink1.setId(1L);
        PhoneLink phoneLink2 = new PhoneLink();
        phoneLink2.setId(phoneLink1.getId());
        assertThat(phoneLink1).isEqualTo(phoneLink2);
        phoneLink2.setId(2L);
        assertThat(phoneLink1).isNotEqualTo(phoneLink2);
        phoneLink1.setId(null);
        assertThat(phoneLink1).isNotEqualTo(phoneLink2);
    }
}
