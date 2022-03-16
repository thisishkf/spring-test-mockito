package com.thisishkf.springmockito.service;

import com.thisishkf.springmockito.exception.BunnyAlreadyExistException;
import com.thisishkf.springmockito.exception.BunnyNotFoundException;
import com.thisishkf.springmockito.exception.BunnyNotHealthyException;
import com.thisishkf.springmockito.model.Bunny;

public interface GardenService {
    boolean addBunny(Bunny bunny) throws BunnyAlreadyExistException, BunnyNotHealthyException;

    boolean removeBunny(Bunny bunny) throws BunnyNotFoundException;

    int countBunny();
}
