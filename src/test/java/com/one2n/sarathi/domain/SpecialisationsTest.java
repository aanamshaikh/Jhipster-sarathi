package com.one2n.sarathi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialisationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Specialisations.class);
        Specialisations specialisations1 = new Specialisations();
        specialisations1.setId(1L);
        Specialisations specialisations2 = new Specialisations();
        specialisations2.setId(specialisations1.getId());
        assertThat(specialisations1).isEqualTo(specialisations2);
        specialisations2.setId(2L);
        assertThat(specialisations1).isNotEqualTo(specialisations2);
        specialisations1.setId(null);
        assertThat(specialisations1).isNotEqualTo(specialisations2);
    }
}
