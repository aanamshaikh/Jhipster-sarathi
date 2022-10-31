package com.one2n.sarathi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CancelledTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cancelled.class);
        Cancelled cancelled1 = new Cancelled();
        cancelled1.setId(1L);
        Cancelled cancelled2 = new Cancelled();
        cancelled2.setId(cancelled1.getId());
        assertThat(cancelled1).isEqualTo(cancelled2);
        cancelled2.setId(2L);
        assertThat(cancelled1).isNotEqualTo(cancelled2);
        cancelled1.setId(null);
        assertThat(cancelled1).isNotEqualTo(cancelled2);
    }
}
