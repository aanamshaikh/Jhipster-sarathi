package com.one2n.sarathi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DoctorsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorsDTO.class);
        DoctorsDTO doctorsDTO1 = new DoctorsDTO();
        doctorsDTO1.setId(1L);
        DoctorsDTO doctorsDTO2 = new DoctorsDTO();
        assertThat(doctorsDTO1).isNotEqualTo(doctorsDTO2);
        doctorsDTO2.setId(doctorsDTO1.getId());
        assertThat(doctorsDTO1).isEqualTo(doctorsDTO2);
        doctorsDTO2.setId(2L);
        assertThat(doctorsDTO1).isNotEqualTo(doctorsDTO2);
        doctorsDTO1.setId(null);
        assertThat(doctorsDTO1).isNotEqualTo(doctorsDTO2);
    }
}
