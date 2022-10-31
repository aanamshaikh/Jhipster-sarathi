package com.one2n.sarathi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientsDTO.class);
        PatientsDTO patientsDTO1 = new PatientsDTO();
        patientsDTO1.setId(1L);
        PatientsDTO patientsDTO2 = new PatientsDTO();
        assertThat(patientsDTO1).isNotEqualTo(patientsDTO2);
        patientsDTO2.setId(patientsDTO1.getId());
        assertThat(patientsDTO1).isEqualTo(patientsDTO2);
        patientsDTO2.setId(2L);
        assertThat(patientsDTO1).isNotEqualTo(patientsDTO2);
        patientsDTO1.setId(null);
        assertThat(patientsDTO1).isNotEqualTo(patientsDTO2);
    }
}
