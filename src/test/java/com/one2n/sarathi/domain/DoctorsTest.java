package com.one2n.sarathi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DoctorsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doctors.class);
        Doctors doctors1 = new Doctors();
        doctors1.setId(1L);
        Doctors doctors2 = new Doctors();
        doctors2.setId(doctors1.getId());
        assertThat(doctors1).isEqualTo(doctors2);
        doctors2.setId(2L);
        assertThat(doctors1).isNotEqualTo(doctors2);
        doctors1.setId(null);
        assertThat(doctors1).isNotEqualTo(doctors2);
    }
}
