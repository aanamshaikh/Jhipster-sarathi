package com.one2n.sarathi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SarathiUsersDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SarathiUsersDTO.class);
        SarathiUsersDTO sarathiUsersDTO1 = new SarathiUsersDTO();
        sarathiUsersDTO1.setId(1L);
        SarathiUsersDTO sarathiUsersDTO2 = new SarathiUsersDTO();
        assertThat(sarathiUsersDTO1).isNotEqualTo(sarathiUsersDTO2);
        sarathiUsersDTO2.setId(sarathiUsersDTO1.getId());
        assertThat(sarathiUsersDTO1).isEqualTo(sarathiUsersDTO2);
        sarathiUsersDTO2.setId(2L);
        assertThat(sarathiUsersDTO1).isNotEqualTo(sarathiUsersDTO2);
        sarathiUsersDTO1.setId(null);
        assertThat(sarathiUsersDTO1).isNotEqualTo(sarathiUsersDTO2);
    }
}
