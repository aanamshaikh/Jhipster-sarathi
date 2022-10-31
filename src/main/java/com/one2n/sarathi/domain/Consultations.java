package com.one2n.sarathi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Consultations.
 */
@Entity
@Table(name = "consultations")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Consultations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "in_time")
    private Instant inTime;

    @Column(name = "out_time")
    private Instant outTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "patient", "doctor", "reason", "cancelReason", "status" }, allowSetters = true)
    private Appointments appointment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Consultations id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInTime() {
        return this.inTime;
    }

    public Consultations inTime(Instant inTime) {
        this.setInTime(inTime);
        return this;
    }

    public void setInTime(Instant inTime) {
        this.inTime = inTime;
    }

    public Instant getOutTime() {
        return this.outTime;
    }

    public Consultations outTime(Instant outTime) {
        this.setOutTime(outTime);
        return this;
    }

    public void setOutTime(Instant outTime) {
        this.outTime = outTime;
    }

    public Appointments getAppointment() {
        return this.appointment;
    }

    public void setAppointment(Appointments appointments) {
        this.appointment = appointments;
    }

    public Consultations appointment(Appointments appointments) {
        this.setAppointment(appointments);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consultations)) {
            return false;
        }
        return id != null && id.equals(((Consultations) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consultations{" +
            "id=" + getId() +
            ", inTime='" + getInTime() + "'" +
            ", outTime='" + getOutTime() + "'" +
            "}";
    }
}
