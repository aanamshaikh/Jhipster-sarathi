package com.one2n.sarathi.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.one2n.sarathi.domain.Consultations} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsultationsDTO implements Serializable {

    private Long id;

    private Instant inTime;

    private Instant outTime;

    private AppointmentsDTO appointment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInTime() {
        return inTime;
    }

    public void setInTime(Instant inTime) {
        this.inTime = inTime;
    }

    public Instant getOutTime() {
        return outTime;
    }

    public void setOutTime(Instant outTime) {
        this.outTime = outTime;
    }

    public AppointmentsDTO getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentsDTO appointment) {
        this.appointment = appointment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsultationsDTO)) {
            return false;
        }

        ConsultationsDTO consultationsDTO = (ConsultationsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consultationsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsultationsDTO{" +
            "id=" + getId() +
            ", inTime='" + getInTime() + "'" +
            ", outTime='" + getOutTime() + "'" +
            ", appointment=" + getAppointment() +
            "}";
    }
}
