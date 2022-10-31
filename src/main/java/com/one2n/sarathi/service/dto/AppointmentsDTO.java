package com.one2n.sarathi.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.one2n.sarathi.domain.Appointments} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppointmentsDTO implements Serializable {

    private Long id;

    private Instant slotTime;

    private LocalDate bookDay;

    private Boolean isCancelled;

    private Instant requestedAt;

    private Instant updatedAt;

    private PatientsDTO patient;

    private DoctorsDTO doctor;

    private ReasonsDTO reason;

    private CancelledDTO cancelReason;

    private AppointmentStatusDTO status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(Instant slotTime) {
        this.slotTime = slotTime;
    }

    public LocalDate getBookDay() {
        return bookDay;
    }

    public void setBookDay(LocalDate bookDay) {
        this.bookDay = bookDay;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PatientsDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientsDTO patient) {
        this.patient = patient;
    }

    public DoctorsDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorsDTO doctor) {
        this.doctor = doctor;
    }

    public ReasonsDTO getReason() {
        return reason;
    }

    public void setReason(ReasonsDTO reason) {
        this.reason = reason;
    }

    public CancelledDTO getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(CancelledDTO cancelReason) {
        this.cancelReason = cancelReason;
    }

    public AppointmentStatusDTO getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatusDTO status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentsDTO)) {
            return false;
        }

        AppointmentsDTO appointmentsDTO = (AppointmentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appointmentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentsDTO{" +
            "id=" + getId() +
            ", slotTime='" + getSlotTime() + "'" +
            ", bookDay='" + getBookDay() + "'" +
            ", isCancelled='" + getIsCancelled() + "'" +
            ", requestedAt='" + getRequestedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", patient=" + getPatient() +
            ", doctor=" + getDoctor() +
            ", reason=" + getReason() +
            ", cancelReason=" + getCancelReason() +
            ", status=" + getStatus() +
            "}";
    }
}
