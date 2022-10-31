package com.one2n.sarathi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.one2n.sarathi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WeekdaysTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Weekdays.class);
        Weekdays weekdays1 = new Weekdays();
        weekdays1.setId(1L);
        Weekdays weekdays2 = new Weekdays();
        weekdays2.setId(weekdays1.getId());
        assertThat(weekdays1).isEqualTo(weekdays2);
        weekdays2.setId(2L);
        assertThat(weekdays1).isNotEqualTo(weekdays2);
        weekdays1.setId(null);
        assertThat(weekdays1).isNotEqualTo(weekdays2);
    }
}
