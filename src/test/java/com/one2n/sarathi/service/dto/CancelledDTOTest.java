package com.one2n.sarathi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CancelledDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CancelledDTO.class);
        CancelledDTO cancelledDTO1 = new CancelledDTO();
        cancelledDTO1.setId(1L);
        CancelledDTO cancelledDTO2 = new CancelledDTO();
        assertThat(cancelledDTO1).isNotEqualTo(cancelledDTO2);
        cancelledDTO2.setId(cancelledDTO1.getId());
        assertThat(cancelledDTO1).isEqualTo(cancelledDTO2);
        cancelledDTO2.setId(2L);
        assertThat(cancelledDTO1).isNotEqualTo(cancelledDTO2);
        cancelledDTO1.setId(null);
        assertThat(cancelledDTO1).isNotEqualTo(cancelledDTO2);
    }
}
