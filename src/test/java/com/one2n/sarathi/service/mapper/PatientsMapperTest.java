package com.one2n.sarathi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientsMapperTest {

    private PatientsMapper patientsMapper;

    @BeforeEach
    public void setUp() {
        patientsMapper = new PatientsMapperImpl();
    }
}
