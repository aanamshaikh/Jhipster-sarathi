package com.one2n.sarathi.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.one2n.sarathi.domain.Patients} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientsDTO implements Serializable {

    private Long id;

    private String name;

    private String gender;

    private LocalDate dob;

    private String mobileNumber;

    private String address;

    private SarathiUsersDTO parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SarathiUsersDTO getParent() {
        return parent;
    }

    public void setParent(SarathiUsersDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientsDTO)) {
            return false;
        }

        PatientsDTO patientsDTO = (PatientsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patientsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gender='" + getGender() + "'" +
            ", dob='" + getDob() + "'" +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", parent=" + getParent() +
            "}";
    }
}
