package com.one2n.sarathi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A SarathiUsers.
 */
@Entity
@Table(name = "sarathi_users")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SarathiUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "user_type_id")
    private Integer userTypeId;

    @NotNull
    @Column(name = "email_id", nullable = false)
    private String emailId;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_disabled")
    private Boolean isDisabled;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "parent")
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private Set<Patients> patients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SarathiUsers id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public SarathiUsers username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return this.userType;
    }

    public SarathiUsers userType(String userType) {
        this.setUserType(userType);
        return this;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getUserTypeId() {
        return this.userTypeId;
    }

    public SarathiUsers userTypeId(Integer userTypeId) {
        this.setUserTypeId(userTypeId);
        return this;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public SarathiUsers emailId(String emailId) {
        this.setEmailId(emailId);
        return this;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return this.password;
    }

    public SarathiUsers password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsDisabled() {
        return this.isDisabled;
    }

    public SarathiUsers isDisabled(Boolean isDisabled) {
        this.setIsDisabled(isDisabled);
        return this;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public SarathiUsers createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public SarathiUsers updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Patients> getPatients() {
        return this.patients;
    }

    public void setPatients(Set<Patients> patients) {
        if (this.patients != null) {
            this.patients.forEach(i -> i.setParent(null));
        }
        if (patients != null) {
            patients.forEach(i -> i.setParent(this));
        }
        this.patients = patients;
    }

    public SarathiUsers patients(Set<Patients> patients) {
        this.setPatients(patients);
        return this;
    }

    public SarathiUsers addPatients(Patients patients) {
        this.patients.add(patients);
        patients.setParent(this);
        return this;
    }

    public SarathiUsers removePatients(Patients patients) {
        this.patients.remove(patients);
        patients.setParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SarathiUsers)) {
            return false;
        }
        return id != null && id.equals(((SarathiUsers) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SarathiUsers{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", userType='" + getUserType() + "'" +
            ", userTypeId=" + getUserTypeId() +
            ", emailId='" + getEmailId() + "'" +
            ", password='" + getPassword() + "'" +
            ", isDisabled='" + getIsDisabled() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
