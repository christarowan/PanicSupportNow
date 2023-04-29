package com.zipcode.panic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.zipcode.panic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoundtrackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Soundtrack.class);
        Soundtrack soundtrack1 = new Soundtrack();
        soundtrack1.setId(1L);
        Soundtrack soundtrack2 = new Soundtrack();
        soundtrack2.setId(soundtrack1.getId());
        assertThat(soundtrack1).isEqualTo(soundtrack2);
        soundtrack2.setId(2L);
        assertThat(soundtrack1).isNotEqualTo(soundtrack2);
        soundtrack1.setId(null);
        assertThat(soundtrack1).isNotEqualTo(soundtrack2);
    }
}
