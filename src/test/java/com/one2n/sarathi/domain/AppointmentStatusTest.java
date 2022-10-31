package com.one2n.sarathi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentStatus.class);
        AppointmentStatus appointmentStatus1 = new AppointmentStatus();
        appointmentStatus1.setId(1L);
        AppointmentStatus appointmentStatus2 = new AppointmentStatus();
        appointmentStatus2.setId(appointmentStatus1.getId());
        assertThat(appointmentStatus1).isEqualTo(appointmentStatus2);
        appointmentStatus2.setId(2L);
        assertThat(appointmentStatus1).isNotEqualTo(appointmentStatus2);
        appointmentStatus1.setId(null);
        assertThat(appointmentStatus1).isNotEqualTo(appointmentStatus2);
    }
}
