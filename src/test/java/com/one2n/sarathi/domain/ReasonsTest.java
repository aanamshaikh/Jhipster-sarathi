package com.one2n.sarathi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReasonsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reasons.class);
        Reasons reasons1 = new Reasons();
        reasons1.setId(1L);
        Reasons reasons2 = new Reasons();
        reasons2.setId(reasons1.getId());
        assertThat(reasons1).isEqualTo(reasons2);
        reasons2.setId(2L);
        assertThat(reasons1).isNotEqualTo(reasons2);
        reasons1.setId(null);
        assertThat(reasons1).isNotEqualTo(reasons2);
    }
}
