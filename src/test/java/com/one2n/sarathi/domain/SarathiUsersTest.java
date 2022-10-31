package com.one2n.sarathi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SarathiUsersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SarathiUsers.class);
        SarathiUsers sarathiUsers1 = new SarathiUsers();
        sarathiUsers1.setId(1L);
        SarathiUsers sarathiUsers2 = new SarathiUsers();
        sarathiUsers2.setId(sarathiUsers1.getId());
        assertThat(sarathiUsers1).isEqualTo(sarathiUsers2);
        sarathiUsers2.setId(2L);
        assertThat(sarathiUsers1).isNotEqualTo(sarathiUsers2);
        sarathiUsers1.setId(null);
        assertThat(sarathiUsers1).isNotEqualTo(sarathiUsers2);
    }
}
