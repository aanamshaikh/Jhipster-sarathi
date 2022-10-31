package com.one2n.sarathi.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.one2n.sarathi.domain.Cancelled} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CancelledDTO implements Serializable {

    private Long id;

    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CancelledDTO)) {
            return false;
        }

        CancelledDTO cancelledDTO = (CancelledDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cancelledDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CancelledDTO{" +
            "id=" + getId() +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
