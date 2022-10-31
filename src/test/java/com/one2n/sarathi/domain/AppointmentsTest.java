package com.one2n.sarathi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appointments.class);
        Appointments appointments1 = new Appointments();
        appointments1.setId(1L);
        Appointments appointments2 = new Appointments();
        appointments2.setId(appointments1.getId());
        assertThat(appointments1).isEqualTo(appointments2);
        appointments2.setId(2L);
        assertThat(appointments1).isNotEqualTo(appointments2);
        appointments1.setId(null);
        assertThat(appointments1).isNotEqualTo(appointments2);
    }
}
