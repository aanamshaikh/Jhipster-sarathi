package com.one2n.sarathi.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.one2n.sarathi.domain.SarathiUsers} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SarathiUsersDTO implements Serializable {

    private Long id;

    @NotNull
    private String username;

    private String userType;

    private Integer userTypeId;

    @NotNull
    private String emailId;

    @NotNull
    private String password;

    private Boolean isDisabled;

    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SarathiUsersDTO)) {
            return false;
        }

        SarathiUsersDTO sarathiUsersDTO = (SarathiUsersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sarathiUsersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SarathiUsersDTO{" +
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
