package com.one2n.sarathi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReasonsMapperTest {

    private ReasonsMapper reasonsMapper;

    @BeforeEach
    public void setUp() {
        reasonsMapper = new ReasonsMapperImpl();
    }
}
