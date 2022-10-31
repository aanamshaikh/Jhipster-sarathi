package com.one2n.sarathi.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.one2n.sarathi.domain.Weekdays} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WeekdaysDTO implements Serializable {

    private Long id;

    private String day;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeekdaysDTO)) {
            return false;
        }

        WeekdaysDTO weekdaysDTO = (WeekdaysDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, weekdaysDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeekdaysDTO{" +
            "id=" + getId() +
            ", day='" + getDay() + "'" +
            "}";
    }
}
