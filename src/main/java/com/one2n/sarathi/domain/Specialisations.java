package com.one2n.sarathi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Specialisations.
 */
@Entity
@Table(name = "specialisations")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Specialisations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "specialisation")
    @JsonIgnoreProperties(value = { "specialisation" }, allowSetters = true)
    private Set<Doctors> doctors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Specialisations id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Specialisations name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Specialisations description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Doctors> getDoctors() {
        return this.doctors;
    }

    public void setDoctors(Set<Doctors> doctors) {
        if (this.doctors != null) {
            this.doctors.forEach(i -> i.setSpecialisation(null));
        }
        if (doctors != null) {
            doctors.forEach(i -> i.setSpecialisation(this));
        }
        this.doctors = doctors;
    }

    public Specialisations doctors(Set<Doctors> doctors) {
        this.setDoctors(doctors);
        return this;
    }

    public Specialisations addDoctors(Doctors doctors) {
        this.doctors.add(doctors);
        doctors.setSpecialisation(this);
        return this;
    }

    public Specialisations removeDoctors(Doctors doctors) {
        this.doctors.remove(doctors);
        doctors.setSpecialisation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Specialisations)) {
            return false;
        }
        return id != null && id.equals(((Specialisations) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Specialisations{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
