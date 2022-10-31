package com.one2n.sarathi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReceptionistsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Receptionists.class);
        Receptionists receptionists1 = new Receptionists();
        receptionists1.setId(1L);
        Receptionists receptionists2 = new Receptionists();
        receptionists2.setId(receptionists1.getId());
        assertThat(receptionists1).isEqualTo(receptionists2);
        receptionists2.setId(2L);
        assertThat(receptionists1).isNotEqualTo(receptionists2);
        receptionists1.setId(null);
        assertThat(receptionists1).isNotEqualTo(receptionists2);
    }
}
