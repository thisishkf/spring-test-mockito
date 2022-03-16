package com.thisishkf.springmockito.service;

import com.thisishkf.springmockito.exception.BunnyNotHealthyException;
import com.thisishkf.springmockito.model.Bunny;
import com.thisishkf.springmockito.service.impl.DoctorServiceImpl;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DoctorServiceTest {

    @InjectMocks
    private DoctorServiceImpl service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @SneakyThrows
    @Test(expected = BunnyNotHealthyException.class)
    public void checkBunny_shouldThrowException_whenBunnyIsSick() {
        Bunny bunny = new Bunny("bunny1", false);
        service.check(bunny);
    }

    @SneakyThrows
    @Test
    public void checkBunny_shouldPass_whenBunnyIsHealthy() {
        Bunny bunny = new Bunny("bunny1", true);
        service.check(bunny);
    }

}
