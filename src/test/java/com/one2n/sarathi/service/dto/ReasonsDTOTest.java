package com.one2n.sarathi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReasonsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReasonsDTO.class);
        ReasonsDTO reasonsDTO1 = new ReasonsDTO();
        reasonsDTO1.setId(1L);
        ReasonsDTO reasonsDTO2 = new ReasonsDTO();
        assertThat(reasonsDTO1).isNotEqualTo(reasonsDTO2);
        reasonsDTO2.setId(reasonsDTO1.getId());
        assertThat(reasonsDTO1).isEqualTo(reasonsDTO2);
        reasonsDTO2.setId(2L);
        assertThat(reasonsDTO1).isNotEqualTo(reasonsDTO2);
        reasonsDTO1.setId(null);
        assertThat(reasonsDTO1).isNotEqualTo(reasonsDTO2);
    }
}
