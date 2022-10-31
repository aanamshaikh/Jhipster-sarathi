package com.one2n.sarathi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsultationsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsultationsDTO.class);
        ConsultationsDTO consultationsDTO1 = new ConsultationsDTO();
        consultationsDTO1.setId(1L);
        ConsultationsDTO consultationsDTO2 = new ConsultationsDTO();
        assertThat(consultationsDTO1).isNotEqualTo(consultationsDTO2);
        consultationsDTO2.setId(consultationsDTO1.getId());
        assertThat(consultationsDTO1).isEqualTo(consultationsDTO2);
        consultationsDTO2.setId(2L);
        assertThat(consultationsDTO1).isNotEqualTo(consultationsDTO2);
        consultationsDTO1.setId(null);
        assertThat(consultationsDTO1).isNotEqualTo(consultationsDTO2);
    }
}
