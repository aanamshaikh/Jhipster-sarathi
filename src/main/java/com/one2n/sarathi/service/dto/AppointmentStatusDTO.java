package com.one2n.sarathi.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.one2n.sarathi.domain.AppointmentStatus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppointmentStatusDTO implements Serializable {

    private Long id;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentStatusDTO)) {
            return false;
        }

        AppointmentStatusDTO appointmentStatusDTO = (AppointmentStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appointmentStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentStatusDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
