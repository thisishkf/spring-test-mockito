package com.thisishkf.springmockito.service;

import com.thisishkf.springmockito.exception.BunnyAlreadyExistException;
import com.thisishkf.springmockito.exception.BunnyNotFoundException;
import com.thisishkf.springmockito.model.Bunny;
import com.thisishkf.springmockito.service.impl.GardenServiceImpl;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GardenServiceTest {

    @InjectMocks
    private GardenServiceImpl service;
    @Mock
    private DoctorService doctorService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @SneakyThrows
    @Test
    public void addBunny_shouldAddBunnyToGarden() {
        int originalCount = service.countBunny();
        Bunny bunny = new Bunny("bunny1", true);
        service.addBunny(bunny);
        assertEquals(1, service.countBunny() - originalCount);
    }

    @SneakyThrows
    @Test
    public void addBunny_shouldCheckBunnyHealthiness() {
        Bunny bunny = new Bunny("bunny1", true);
        service.addBunny(bunny);
        verify(doctorService, times(1)).check(bunny);
    }

    @SneakyThrows
    @Test(expected = BunnyAlreadyExistException.class)
    public void addBunny_shouldThrowException_whenBunnyIsAlreadyInGarden() {
        Bunny bunny = new Bunny("bunny1", true);
        service.addBunny(bunny);
        service.addBunny(bunny);
    }

    @SneakyThrows
    @Test(expected = BunnyNotFoundException.class)
    public void removeBunny_shouldThrowException_whenBunnyNotExist() {
        service.removeBunny(new Bunny("newBunny", true));
    }

    @SneakyThrows
    @Test
    public void removeBunny_shouldRemoveBunnyFromGarden() {
        Bunny bunny = new Bunny("bunny1", true);
        service.addBunny(bunny);
        int originalCount = service.countBunny();
        System.out.println(originalCount);
        service.removeBunny(bunny);
        assertEquals(1, originalCount - service.countBunny());
    }

}
