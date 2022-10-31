package com.one2n.sarathi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CancelledMapperTest {

    private CancelledMapper cancelledMapper;

    @BeforeEach
    public void setUp() {
        cancelledMapper = new CancelledMapperImpl();
    }
}
