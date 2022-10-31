package com.one2n.sarathi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialisationsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialisationsDTO.class);
        SpecialisationsDTO specialisationsDTO1 = new SpecialisationsDTO();
        specialisationsDTO1.setId(1L);
        SpecialisationsDTO specialisationsDTO2 = new SpecialisationsDTO();
        assertThat(specialisationsDTO1).isNotEqualTo(specialisationsDTO2);
        specialisationsDTO2.setId(specialisationsDTO1.getId());
        assertThat(specialisationsDTO1).isEqualTo(specialisationsDTO2);
        specialisationsDTO2.setId(2L);
        assertThat(specialisationsDTO1).isNotEqualTo(specialisationsDTO2);
        specialisationsDTO1.setId(null);
        assertThat(specialisationsDTO1).isNotEqualTo(specialisationsDTO2);
    }
}
