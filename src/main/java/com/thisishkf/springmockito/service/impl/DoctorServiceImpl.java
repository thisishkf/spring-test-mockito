package com.thisishkf.springmockito.service.impl;

import com.thisishkf.springmockito.exception.BunnyNotHealthyException;
import com.thisishkf.springmockito.model.Bunny;
import com.thisishkf.springmockito.service.DoctorService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Override
    public boolean check(@NotNull Bunny bunny) throws BunnyNotHealthyException {
        if (!bunny.isHealthy()) {
            throw new BunnyNotHealthyException();
        }
        return true;
    }

    @Override
    public Bunny heal(Bunny bunny) {
        if (!bunny.isHealthy()) {
            bunny.setHealthy(true);
        }
        return bunny;
    }
}
