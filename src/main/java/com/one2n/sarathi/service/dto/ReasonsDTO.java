package com.one2n.sarathi.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.one2n.sarathi.domain.Reasons} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReasonsDTO implements Serializable {

    private Long id;

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReasonsDTO)) {
            return false;
        }

        ReasonsDTO reasonsDTO = (ReasonsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reasonsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReasonsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
