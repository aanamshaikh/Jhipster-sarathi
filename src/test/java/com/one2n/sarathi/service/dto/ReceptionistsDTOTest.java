package com.one2n.sarathi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReceptionistsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReceptionistsDTO.class);
        ReceptionistsDTO receptionistsDTO1 = new ReceptionistsDTO();
        receptionistsDTO1.setId(1L);
        ReceptionistsDTO receptionistsDTO2 = new ReceptionistsDTO();
        assertThat(receptionistsDTO1).isNotEqualTo(receptionistsDTO2);
        receptionistsDTO2.setId(receptionistsDTO1.getId());
        assertThat(receptionistsDTO1).isEqualTo(receptionistsDTO2);
        receptionistsDTO2.setId(2L);
        assertThat(receptionistsDTO1).isNotEqualTo(receptionistsDTO2);
        receptionistsDTO1.setId(null);
        assertThat(receptionistsDTO1).isNotEqualTo(receptionistsDTO2);
    }
}
