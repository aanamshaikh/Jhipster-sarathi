package com.one2n.sarathi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentStatusDTO.class);
        AppointmentStatusDTO appointmentStatusDTO1 = new AppointmentStatusDTO();
        appointmentStatusDTO1.setId(1L);
        AppointmentStatusDTO appointmentStatusDTO2 = new AppointmentStatusDTO();
        assertThat(appointmentStatusDTO1).isNotEqualTo(appointmentStatusDTO2);
        appointmentStatusDTO2.setId(appointmentStatusDTO1.getId());
        assertThat(appointmentStatusDTO1).isEqualTo(appointmentStatusDTO2);
        appointmentStatusDTO2.setId(2L);
        assertThat(appointmentStatusDTO1).isNotEqualTo(appointmentStatusDTO2);
        appointmentStatusDTO1.setId(null);
        assertThat(appointmentStatusDTO1).isNotEqualTo(appointmentStatusDTO2);
    }
}
