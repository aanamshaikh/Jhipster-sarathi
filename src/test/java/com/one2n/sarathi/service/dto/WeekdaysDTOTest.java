package com.one2n.sarathi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WeekdaysDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeekdaysDTO.class);
        WeekdaysDTO weekdaysDTO1 = new WeekdaysDTO();
        weekdaysDTO1.setId(1L);
        WeekdaysDTO weekdaysDTO2 = new WeekdaysDTO();
        assertThat(weekdaysDTO1).isNotEqualTo(weekdaysDTO2);
        weekdaysDTO2.setId(weekdaysDTO1.getId());
        assertThat(weekdaysDTO1).isEqualTo(weekdaysDTO2);
        weekdaysDTO2.setId(2L);
        assertThat(weekdaysDTO1).isNotEqualTo(weekdaysDTO2);
        weekdaysDTO1.setId(null);
        assertThat(weekdaysDTO1).isNotEqualTo(weekdaysDTO2);
    }
}
