package com.one2n.sarathi.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.one2n.sarathi.domain.Receptionists} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReceptionistsDTO implements Serializable {

    private Long id;

    private String name;

    private String gender;

    private LocalDate dob;

    private String mobileNumber;

    private String address;

    private String qualification;

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

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceptionistsDTO)) {
            return false;
        }

        ReceptionistsDTO receptionistsDTO = (ReceptionistsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, receptionistsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceptionistsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gender='" + getGender() + "'" +
            ", dob='" + getDob() + "'" +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", qualification='" + getQualification() + "'" +
            "}";
    }
}
