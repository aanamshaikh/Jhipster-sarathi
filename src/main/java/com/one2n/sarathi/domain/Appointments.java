package com.one2n.sarathi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Appointments.
 */
@Entity
@Table(name = "appointments")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Appointments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "slot_time")
    private Instant slotTime;

    @Column(name = "book_day")
    private LocalDate bookDay;

    @Column(name = "is_cancelled")
    private Boolean isCancelled;

    @Column(name = "requested_at")
    private Instant requestedAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private Patients patient;

    @ManyToOne
    @JsonIgnoreProperties(value = { "specialisation" }, allowSetters = true)
    private Doctors doctor;

    @ManyToOne
    private Reasons reason;

    @ManyToOne
    private Cancelled cancelReason;

    @ManyToOne
    private AppointmentStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Appointments id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSlotTime() {
        return this.slotTime;
    }

    public Appointments slotTime(Instant slotTime) {
        this.setSlotTime(slotTime);
        return this;
    }

    public void setSlotTime(Instant slotTime) {
        this.slotTime = slotTime;
    }

    public LocalDate getBookDay() {
        return this.bookDay;
    }

    public Appointments bookDay(LocalDate bookDay) {
        this.setBookDay(bookDay);
        return this;
    }

    public void setBookDay(LocalDate bookDay) {
        this.bookDay = bookDay;
    }

    public Boolean getIsCancelled() {
        return this.isCancelled;
    }

    public Appointments isCancelled(Boolean isCancelled) {
        this.setIsCancelled(isCancelled);
        return this;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Instant getRequestedAt() {
        return this.requestedAt;
    }

    public Appointments requestedAt(Instant requestedAt) {
        this.setRequestedAt(requestedAt);
        return this;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Appointments updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Patients getPatient() {
        return this.patient;
    }

    public void setPatient(Patients patients) {
        this.patient = patients;
    }

    public Appointments patient(Patients patients) {
        this.setPatient(patients);
        return this;
    }

    public Doctors getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctors doctors) {
        this.doctor = doctors;
    }

    public Appointments doctor(Doctors doctors) {
        this.setDoctor(doctors);
        return this;
    }

    public Reasons getReason() {
        return this.reason;
    }

    public void setReason(Reasons reasons) {
        this.reason = reasons;
    }

    public Appointments reason(Reasons reasons) {
        this.setReason(reasons);
        return this;
    }

    public Cancelled getCancelReason() {
        return this.cancelReason;
    }

    public void setCancelReason(Cancelled cancelled) {
        this.cancelReason = cancelled;
    }

    public Appointments cancelReason(Cancelled cancelled) {
        this.setCancelReason(cancelled);
        return this;
    }

    public AppointmentStatus getStatus() {
        return this.status;
    }

    public void setStatus(AppointmentStatus appointmentStatus) {
        this.status = appointmentStatus;
    }

    public Appointments status(AppointmentStatus appointmentStatus) {
        this.setStatus(appointmentStatus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appointments)) {
            return false;
        }
        return id != null && id.equals(((Appointments) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appointments{" +
            "id=" + getId() +
            ", slotTime='" + getSlotTime() + "'" +
            ", bookDay='" + getBookDay() + "'" +
            ", isCancelled='" + getIsCancelled() + "'" +
            ", requestedAt='" + getRequestedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
