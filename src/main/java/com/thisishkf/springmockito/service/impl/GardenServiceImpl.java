package com.thisishkf.springmockito.service.impl;

import com.thisishkf.springmockito.exception.BunnyAlreadyExistException;
import com.thisishkf.springmockito.exception.BunnyNotFoundException;
import com.thisishkf.springmockito.exception.BunnyNotHealthyException;
import com.thisishkf.springmockito.model.Bunny;
import com.thisishkf.springmockito.service.DoctorService;
import com.thisishkf.springmockito.service.GardenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Service
public class GardenServiceImpl implements GardenService {
    @Autowired
    private DoctorService doctorService;

    private Map<String, Bunny> bunnyGarden = new HashMap<>();

    @Override
    public synchronized boolean addBunny(@NotNull Bunny bunny) throws BunnyAlreadyExistException, BunnyNotHealthyException {
        if (bunnyGarden.containsKey(bunny.getName())) {
            throw new BunnyAlreadyExistException();
        }
        doctorService.check(bunny);
        bunnyGarden.put(bunny.getName(), bunny);
        return true;
    }

    @Override
    public synchronized boolean removeBunny(@NotNull Bunny bunny) throws BunnyNotFoundException {
        if (!bunnyGarden.containsKey(bunny.getName())) {
            throw new BunnyNotFoundException();
        }
        bunnyGarden.remove(bunny.getName());
        return true;
    }

    @Override
    public synchronized int countBunny() {
        return bunnyGarden.size();
    }
}
